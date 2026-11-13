package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle,Long> {
    java.util.List<PedidoDetalle> findByPedidoIdOrderByIdAsc(Long id);
}
