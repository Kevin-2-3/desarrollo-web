package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface SkuRepository extends JpaRepository<Sku,Long> {
    java.util.List<Sku> findByProductoIdAndActivoTrueOrderByIdAsc(Long id);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select s from Sku s where s.id = :id")
    java.util.Optional<Sku> bloquear(@org.springframework.data.repository.query.Param("id") Long id);
}
