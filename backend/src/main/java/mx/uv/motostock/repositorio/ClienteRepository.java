package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    java.util.Optional<Cliente> findByCorreo(String correo);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select c from Cliente c where c.id = :id")
    java.util.Optional<Cliente> bloquear(@org.springframework.data.repository.query.Param("id") Long id);
}
