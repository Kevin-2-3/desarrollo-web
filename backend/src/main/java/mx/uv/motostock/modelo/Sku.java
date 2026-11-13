package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="sku")
public class Sku {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_sku") public Long id;
    public String codigoSku;
    public String talla;
    public String color;
    @Column(precision=12,scale=2) public java.math.BigDecimal precioMxn;
    public int stock;
    public boolean activo;
    @ManyToOne @JoinColumn(name="id_producto") public Producto producto;
}
