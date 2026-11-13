package mx.uv.motostock.repositorio;
import mx.uv.motostock.modelo.*;
import org.springframework.data.jpa.repository.*;
public interface ProductoImagenRepository extends JpaRepository<ProductoImagen,Long> {
    java.util.List<ProductoImagen> findByProductoIdOrderByOrdenAsc(Long id);
}
