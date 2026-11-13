package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface ProductoRepository extends JpaRepository<Producto,Long>, JpaSpecificationExecutor<Producto> {
}
