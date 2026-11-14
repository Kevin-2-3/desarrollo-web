package mx.uv.motostock.carrito;
import mx.uv.motostock.modelo.*;
import mx.uv.motostock.repositorio.*;
import mx.uv.motostock.comun.ApiError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.math.BigDecimal;
@Service public class CarritoService {
    private final ClienteRepository clientes;
    private final SkuRepository skus;
    private final CarritoItemRepository items;
    public CarritoService(ClienteRepository c,SkuRepository s,CarritoItemRepository i){
        clientes=c;
        skus=s;
        items=i;
    }
    // El mismo bloqueo de cliente serializa cambios de carrito y confirmaciones propias.
    public Cliente bloquear(Long id){
        return clientes.bloquear(id).orElseThrow(ApiError::noExiste);
    }
    public void validar(Sku s,long cantidad){
        if(cantidad<1||cantidad>Integer.MAX_VALUE)throw ApiError.invalido("Cantidad inválida");
        if(!s.activo||!s.producto.activo||s.stock<cantidad)throw ApiError.conflicto("SKU no disponible o existencia insuficiente");
    }
    @Transactional public Object agregar(Long id,Long skuId,int cantidad,boolean reemplazar){
        Cliente c=bloquear(id);
        Sku s=skus.findById(skuId).orElseThrow(ApiError::noExiste);
        var key=new CarritoId(id,skuId);
        var previo=items.findById(key);
        if(reemplazar&&previo.isEmpty())throw ApiError.noExiste();
        long nueva=reemplazar?cantidad:(long)previo.map(i->i.cantidad).orElse(0)+cantidad;
        validar(s,nueva);
        var item=previo.orElseGet(CarritoItem::new);
        item.id=key;
        item.cliente=c;
        item.sku=s;
        item.cantidad=(int)nueva;
        items.save(item);
        return vista(id);
    }
    @Transactional public void eliminar(Long id,Long skuId){
        bloquear(id);
        items.deleteById(new CarritoId(id,skuId));
    }
    @Transactional public void vaciar(Long id){
        bloquear(id);
        items.deleteByClienteId(id);
    }
    @Transactional(readOnly=true) public Map<String,Object> vista(Long id){
        var lineas=items.findByClienteIdOrderByIdSkuIdAsc(id);
        List<Map<String,Object>> resultado=new ArrayList<>();
        BigDecimal total=BigDecimal.ZERO.setScale(2);
        for(var i:lineas){
            var s=i.sku;
            var subtotal=s.precioMxn.multiply(BigDecimal.valueOf(i.cantidad));
            Map<String,Object> l=new LinkedHashMap<>();
            l.put("skuId",s.id);
            l.put("nombre",s.producto.nombre);
            l.put("cantidad",i.cantidad);
            l.put("precioUnitarioMxn",s.precioMxn);
            l.put("subtotalMxn",subtotal);
            resultado.add(l);
            total=total.add(subtotal);
        }
        return Map.of("items",resultado,"totalMxn",total);
    }
}
