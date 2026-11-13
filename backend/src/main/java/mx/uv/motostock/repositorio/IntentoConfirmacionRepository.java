package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface IntentoConfirmacionRepository extends JpaRepository<IntentoConfirmacion,Long> {
    java.util.Optional<IntentoConfirmacion> findByClienteIdAndReferenciaConfirmacion(Long clienteId,String referencia);
}
