package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface PedidoRepository extends JpaRepository<Pedido,Long> {
    java.util.List<Pedido> findByClienteIdOrderByFechaDescIdDesc(Long id);
    java.util.Optional<Pedido> findByIdAndClienteId(Long id,Long clienteId);
    java.util.Optional<Pedido> findByIntentoId(Long id);
}
