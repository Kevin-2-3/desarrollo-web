package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="producto_imagen")
public class ProductoImagen {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_imagen") public Long id;
    public String imagenUrl;
    public int orden;
    @ManyToOne @JoinColumn(name="id_producto") public Producto producto;
}
