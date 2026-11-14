package mx.uv.motostock.carrito;
import mx.uv.motostock.seguridad.Sesiones;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
@RestController @RequestMapping("/api/v1/carrito") public class CarritoController {
    public record Agregar(@NotNull @Positive Long skuId,@Min(1) int cantidad){
    }
    public record Cantidad(@Min(1) int cantidad){
    }
    private final CarritoService s;
    public CarritoController(CarritoService s){
        this.s=s;
    }
    @GetMapping public Object ver(HttpServletRequest r){
        return s.vista(Sesiones.cliente(r));
    }
    @PostMapping("/items") public Object agregar(@Valid @RequestBody Agregar a,HttpServletRequest r){
        return s.agregar(Sesiones.cliente(r),a.skuId(),a.cantidad(),false);
    }
    @PutMapping("/items/{skuId}") public Object cambiar(@PathVariable Long skuId,@Valid @RequestBody Cantidad c,HttpServletRequest r){
        return s.agregar(Sesiones.cliente(r),skuId,c.cantidad(),true);
    }
    @DeleteMapping("/items/{skuId}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long skuId,HttpServletRequest r){
        s.eliminar(Sesiones.cliente(r),skuId);
    }
    @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT) public void vaciar(HttpServletRequest r){
        s.vaciar(Sesiones.cliente(r));
    }
}
