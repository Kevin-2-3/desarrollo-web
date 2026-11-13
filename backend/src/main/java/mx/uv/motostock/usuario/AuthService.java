package mx.uv.motostock.usuario;
import mx.uv.motostock.modelo.Cliente;
import mx.uv.motostock.repositorio.ClienteRepository;
import mx.uv.motostock.comun.ApiError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;
import java.util.Locale;
@Service public class AuthService {
    private final ClienteRepository clientes;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    private final String dummy=encoder.encode("clave-inexistente");
    public AuthService(ClienteRepository c){
        clientes=c;
    }
    private String correo(String c){
        return c.trim().toLowerCase(Locale.ROOT);
    }
    @Transactional public Cliente registrar(AuthController.Registro r){
        if(!r.password().equals(r.confirmacionPassword()))throw ApiError.invalido("Las contraseñas no coinciden");
        if(r.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)throw ApiError.invalido("Contraseña demasiado larga para BCrypt");
        String email=correo(r.correo());
        if(clientes.findByCorreo(email).isPresent())throw ApiError.conflicto("Correo ya registrado");
        Cliente c=new Cliente();
        c.nombre=r.nombre().trim();
        c.correo=email;
        c.passwordHash=encoder.encode(r.password());
        return clientes.save(c);
    }
    @Transactional(readOnly=true) public Cliente login(AuthController.Login r){
        if(r.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)throw new ApiError(HttpStatus.UNAUTHORIZED,"Credenciales inválidas");
        var c=clientes.findByCorreo(correo(r.correo()));
        boolean ok=encoder.matches(r.password(),c.map(x->x.passwordHash).orElse(dummy));
        if(!ok||c.isEmpty())throw new ApiError(HttpStatus.UNAUTHORIZED,"Credenciales inválidas");
        return c.get();
    }
}
