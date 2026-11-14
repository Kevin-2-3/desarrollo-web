package mx.uv.motostock.pedido;
import mx.uv.motostock.modelo.*;
import mx.uv.motostock.repositorio.*;
import mx.uv.motostock.carrito.CarritoService;
import mx.uv.motostock.comun.ApiError;
import jakarta.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service public class PedidoService {
    private final CarritoService carrito;
    private final CarritoItemRepository items;
    private final SkuRepository skus;
    private final IntentoConfirmacionRepository intentos;
    private final PedidoRepository pedidos;
    private final PedidoDetalleRepository detalles;
    private final EntityManager em;
    private final ObjectMapper json;
    public PedidoService(CarritoService c,CarritoItemRepository i,SkuRepository s,IntentoConfirmacionRepository in,PedidoRepository p,PedidoDetalleRepository d,EntityManager em,ObjectMapper json){
        carrito=c;
        items=i;
        skus=s;
        intentos=in;
        pedidos=p;
        detalles=d;
        this.em=em;
        this.json=json;
    }
    private String hash(Object o){
        try{
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(json.writeValueAsBytes(o)));
        }
        catch(Exception e){
            throw new IllegalStateException(e);
        }
    }
    private List<CarritoItem> lineas(Long cliente){
        var ls=items.findByClienteIdOrderByIdSkuIdAsc(cliente);
        if(ls.isEmpty())throw ApiError.conflicto("Carrito vacío");
        for(var i:ls){
            Sku s=skus.bloquear(i.id.skuId).orElseThrow(ApiError::noExiste);
            em.refresh(s,LockModeType.PESSIMISTIC_WRITE);
            em.refresh(s.producto);
            i.sku=s;
            carrito.validar(s,i.cantidad);
        }
        return ls;
    }
    private BigDecimal total(List<CarritoItem> ls){
        return ls.stream().map(i->i.sku.precioMxn.multiply(BigDecimal.valueOf(i.cantidad))).reduce(BigDecimal.ZERO.setScale(2),BigDecimal::add);
    }
    private String huella(List<CarritoItem> ls,PedidoController.Entrega e){
        var snapshot=ls.stream().map(i->List.of(i.sku.id,i.cantidad,i.sku.precioMxn,i.sku.producto.nombre,i.sku.producto.marca.nombre,Objects.toString(i.sku.producto.descripcion,""),i.sku.codigoSku,Objects.toString(i.sku.talla,""),Objects.toString(i.sku.color,""))).toList();
        return hash(List.of(snapshot,e));
    }
    @Transactional public Object resumen(Long cliente,PedidoController.Entrega e){
        carrito.bloquear(cliente);
        var ls=lineas(cliente);
        return Map.of("carrito",carrito.vista(cliente),"entrega",e,"resumenHash",huella(ls,e));
    }
    @Transactional public Object confirmar(Long cliente,PedidoController.Confirmacion c){
        Cliente usuario=carrito.bloquear(cliente);
        // Consultar el intento ANTES del carrito: una repetición funciona aunque ya se vació.
        String firma=hash(List.of(c.resumenHash(),c.entrega(),c.aprobado()));
        var anterior=intentos.findByClienteIdAndReferenciaConfirmacion(cliente,c.referenciaConfirmacion());
        if(anterior.isPresent()){
            var a=anterior.get();
            if(!a.resumenHash.equals(firma))throw ApiError.conflicto("Referencia reutilizada con otros datos");
            return resultado(a);
        }
        var ls=lineas(cliente);
        if(!huella(ls,c.entrega()).equals(c.resumenHash()))throw ApiError.conflicto("La compra cambió: consulte y confirme un nuevo resumen");
        IntentoConfirmacion intento=new IntentoConfirmacion();
        intento.cliente=usuario;
        intento.referenciaConfirmacion=c.referenciaConfirmacion();
        intento.resumenHash=firma;
        intento.resultado=c.aprobado()?"APROBADO":"RECHAZADO";
        intento.fechaIntento=Instant.now();
        intentos.save(intento);
        if(!c.aprobado())return resultado(intento);
        Pedido p=new Pedido();
        p.cliente=usuario;
        p.intento=intento;
        p.folio="MOTO-"+UUID.randomUUID();
        p.fecha=Instant.now();
        p.estado="CONFIRMADO";
        p.totalMxn=total(ls);
        var e=c.entrega();
        p.nombreDestinatario=e.nombreDestinatario();
        p.telefono=e.telefono();
        p.direccion=e.direccion();
        p.localidad=e.localidad();
        p.estadoEntrega=e.estadoEntrega();
        p.codigoPostal=e.codigoPostal();
        pedidos.save(p);
        for(var i:ls){
            var s=i.sku;
            var d=new PedidoDetalle();
            d.pedido=p;
            d.sku=s;
            d.nombreCongelado=s.producto.nombre;
            d.marcaCongelada=s.producto.marca.nombre;
            d.descripcionCongelada=s.producto.descripcion;
            d.codigoSkuCongelado=s.codigoSku;
            d.tallaCongelada=s.talla;
            d.colorCongelado=s.color;
            d.precioUnitarioMxn=s.precioMxn;
            d.cantidad=i.cantidad;
            d.subtotalMxn=s.precioMxn.multiply(BigDecimal.valueOf(i.cantidad));
            detalles.save(d);
            s.stock-=i.cantidad;
        }
        items.deleteByClienteId(cliente);
        em.flush();
        return resultado(intento);
    }
    private Object resultado(IntentoConfirmacion i){
        Map<String,Object> r=new LinkedHashMap<>();
        r.put("resultado",i.resultado);
        r.put("referenciaConfirmacion",i.referenciaConfirmacion);
        if(i.resultado.equals("APROBADO")){
            var p=pedidos.findByIntentoId(i.id).orElseThrow();
            r.put("pedido",cabecera(p));
        }
        return r;
    }
    private Map<String,Object> cabecera(Pedido p){
        return Map.of("id",p.id,"folio",p.folio,"fecha",p.fecha,"estado",p.estado,"totalMxn",p.totalMxn);
    }
    @Transactional(readOnly=true) public Object historial(Long cliente){
        return pedidos.findByClienteIdOrderByFechaDescIdDesc(cliente).stream().map(this::cabecera).toList();
    }
    @Transactional(readOnly=true) public Object detalle(Long cliente,Long id){
        var p=pedidos.findByIdAndClienteId(id,cliente).orElseThrow(ApiError::noExiste);
        Map<String,Object> r=new LinkedHashMap<>(cabecera(p));
        r.put("entrega",new PedidoController.Entrega(p.nombreDestinatario,p.telefono,p.direccion,p.localidad,p.estadoEntrega,p.codigoPostal));
        r.put("items",detalles.findByPedidoIdOrderByIdAsc(id).stream().map(d->{
            Map<String,Object> x=new LinkedHashMap<>();x.put("skuId",d.sku.id);x.put("nombre",d.nombreCongelado);x.put("marca",d.marcaCongelada);x.put("descripcion",d.descripcionCongelada);x.put("codigoSku",d.codigoSkuCongelado);x.put("talla",d.tallaCongelada);x.put("color",d.colorCongelado);x.put("precioUnitarioMxn",d.precioUnitarioMxn);x.put("cantidad",d.cantidad);x.put("subtotalMxn",d.subtotalMxn);return x;
        }
        ).toList());
        return r;
    }
}
