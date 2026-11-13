package mx.uv.motostock.catalogo;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/productos") public class CatalogoController {
    private final CatalogoService s;
    public CatalogoController(CatalogoService s){
        this.s=s;
    }
    @GetMapping public Object listar(@RequestParam(required=false) String busqueda,@RequestParam(required=false) String categoria,@RequestParam(required=false) BigDecimal precioMin,@RequestParam(required=false) BigDecimal precioMax,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="12") int size){
        return s.listar(busqueda,categoria,precioMin,precioMax,page,size);
    }
    @GetMapping("/{id}") public Object detalle(@PathVariable Long id){
        return s.detalle(id);
    }
}
