package mx.uv.motostock.catalogo;
import mx.uv.motostock.modelo.*;
import mx.uv.motostock.repositorio.*;
import mx.uv.motostock.comun.ApiError;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
@Service @Transactional(readOnly=true) public class CatalogoService {
    private final ProductoRepository productos;
    private final SkuRepository skus;
    private final ProductoImagenRepository imagenes;
    private final FichaMotoRepository motos;
    private final FichaAccesorioRepository accesorios;
    private final FichaRefaccionRepository refacciones;
    public CatalogoService(ProductoRepository p,SkuRepository s,ProductoImagenRepository i,FichaMotoRepository m,FichaAccesorioRepository a,FichaRefaccionRepository r){
        productos=p;
        skus=s;
        imagenes=i;
        motos=m;
        accesorios=a;
        refacciones=r;
    }
    public Page<Map<String,Object>> listar(String busqueda,String categoria,BigDecimal min,BigDecimal max,int page,int size){
        if(page<0||size<1||size>100||min!=null&&min.signum()<0||max!=null&&max.signum()<0||min!=null&&max!=null&&min.compareTo(max)>0)throw ApiError.invalido("Paginación o rango inválido");
        if(categoria!=null&&!Set.of("MOTO","ACCESORIO","REFACCION").contains(categoria))throw ApiError.invalido("Categoría inválida");
        Specification<Producto> spec=(root,q,cb)->{
            List<Predicate> filtros=new ArrayList<>();
            filtros.add(cb.isTrue(root.get("activo")));
            if(busqueda!=null&&!busqueda.isBlank()){
                String b="%"+busqueda.toLowerCase(Locale.ROOT)+"%";
                filtros.add(cb.or(cb.like(cb.lower(root.get("nombre")),b),cb.like(cb.lower(root.get("marca").get("nombre")),b)));
            }
            if(categoria!=null)filtros.add(cb.equal(root.get("tipoProducto"),categoria));
            var sub=q.subquery(Long.class);
            var sku=sub.from(Sku.class);
            List<Predicate> variantes=new ArrayList<>();
            variantes.add(cb.equal(sku.get("producto"),root));
            variantes.add(cb.isTrue(sku.get("activo")));
            if(min!=null)variantes.add(cb.greaterThanOrEqualTo(sku.get("precioMxn"),min));
            if(max!=null)variantes.add(cb.lessThanOrEqualTo(sku.get("precioMxn"),max));
            sub.select(sku.get("id")).where(variantes.toArray(Predicate[]::new));
            filtros.add(cb.exists(sub));
            return cb.and(filtros.toArray(Predicate[]::new));
        }
        ;
        return productos.findAll(spec,PageRequest.of(page,size,Sort.by("id"))).map(this::vista);
    }
    public Map<String,Object> detalle(Long id){
        return vista(productos.findById(id).filter(p->p.activo).orElseThrow(ApiError::noExiste));
    }
    private Map<String,Object> vista(Producto p){
        Map<String,Object> v=new LinkedHashMap<>();
        v.put("id",p.id);
        v.put("nombre",p.nombre);
        v.put("descripcion",p.descripcion);
        v.put("marca",p.marca.nombre);
        v.put("categoria",p.tipoProducto);
        var variantes=skus.findByProductoIdAndActivoTrueOrderByIdAsc(p.id).stream().map(s->{
            Map<String,Object> x=new LinkedHashMap<>();x.put("id",s.id);x.put("codigoSku",s.codigoSku);x.put("talla",s.talla);x.put("color",s.color);x.put("precioMxn",s.precioMxn);x.put("stock",s.stock);return x;
        }
        ).toList();
        v.put("variantes",variantes);
        var imgs=imagenes.findByProductoIdOrderByOrdenAsc(p.id).stream().map(i->i.imagenUrl).toList();
        v.put("imagenes",imgs.isEmpty()?List.of("/sin-imagen.svg"):imgs);
        switch(p.tipoProducto){
            case "MOTO"->motos.findById(p.id).ifPresent(f->{
                Map<String,Object> m=new LinkedHashMap<>();m.put("anio",f.anio);m.put("cilindradaCc",f.cilindradaCc);m.put("motor",f.motor);m.put("frenos",f.frenos);m.put("tanqueLitros",f.tanqueLitros);v.put("ficha",m);
            }
            );
            case "ACCESORIO"->accesorios.findById(p.id).ifPresent(f->v.put("ficha",Map.of("tipoUso",f.tipoUso)));
            case "REFACCION"->refacciones.findById(p.id).ifPresent(f->v.put("ficha",Map.of("compatibilidad",f.compatibilidad)));
            default->{
            }
        }
        return v;
    }
}
