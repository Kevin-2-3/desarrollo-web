package mx.uv.motostock.pedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.servlet.http.HttpServletRequest;
import mx.uv.motostock.seguridad.Sesiones;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1") public class PedidoController {
    public record Entrega(@NotBlank @Size(max=150) String nombreDestinatario,@NotBlank @Pattern(regexp="[0-9+ ()-]{7,20}") String telefono,@NotBlank @Size(max=500) String direccion,@NotBlank @Size(max=150) String localidad,@NotBlank @Size(max=100) String estadoEntrega,@NotBlank @Pattern(regexp="[0-9]{5}") String codigoPostal){
    }
    public record Confirmacion(@NotNull @Valid Entrega entrega,@NotBlank @Pattern(regexp="[a-f0-9]{64}") String resumenHash,@NotBlank @Size(max=100) String referenciaConfirmacion,@NotNull Boolean aprobado){
    }
    private final PedidoService s;
    public PedidoController(PedidoService s){
        this.s=s;
    }
    @PostMapping("/checkout/resumen") public Object resumen(@Valid @RequestBody Entrega e,HttpServletRequest r){
        return s.resumen(Sesiones.cliente(r),e);
    }
    @PostMapping("/pedidos") public Object confirmar(@Valid @RequestBody Confirmacion c,HttpServletRequest r){
        return s.confirmar(Sesiones.cliente(r),c);
    }
    @GetMapping("/pedidos") public Object historial(HttpServletRequest r){
        return s.historial(Sesiones.cliente(r));
    }
    @GetMapping("/pedidos/{id}") public Object detalle(@PathVariable Long id,HttpServletRequest r){
        return s.detalle(Sesiones.cliente(r),id);
    }
}
