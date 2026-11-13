package mx.uv.motostock.usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.servlet.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import mx.uv.motostock.seguridad.Sesiones;
import java.util.Map;
@RestController @RequestMapping("/api/v1/auth") public class AuthController {
    public record Registro(@NotBlank @Size(max=150) String nombre,@NotBlank @Email @Size(max=254) String correo,@NotBlank @Size(min=8,max=72) String password,@NotBlank String confirmacionPassword){
    }
    public record Login(@NotBlank @Email String correo,@NotBlank @Size(max=72) String password){
    }
    private final AuthService servicio;
    public AuthController(AuthService s){
        servicio=s;
    }
    @GetMapping("/csrf") public Map<String,String> csrf(HttpServletRequest r){
        return Map.of("token",Sesiones.csrf(r));
    }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> registro(@Valid @RequestBody Registro r){
        var c=servicio.registrar(r);
        return Map.of("id",c.id,"nombre",c.nombre,"correo",c.correo);
    }
    @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody Login l,HttpServletRequest r){
        var c=servicio.login(l);
        r.changeSessionId();
        r.getSession().setAttribute("clienteId",c.id);
        return Map.of("id",c.id,"nombre",c.nombre);
    }
    @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT) public void logout(HttpServletRequest r){
        r.getSession(false).invalidate();
    }
}
