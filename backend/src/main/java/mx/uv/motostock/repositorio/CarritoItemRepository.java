package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface CarritoItemRepository extends JpaRepository<CarritoItem,CarritoId> {
    java.util.List<CarritoItem> findByClienteIdOrderByIdSkuIdAsc(Long id);
    void deleteByClienteId(Long id);
}
