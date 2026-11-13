package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="producto")
public class Producto {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_producto") public Long id;
    public String nombre;
    public String descripcion;
    public String tipoProducto;
    public boolean activo;
    @ManyToOne @JoinColumn(name="id_marca") public Marca marca;
}
