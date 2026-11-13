package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="ficha_moto") public class FichaMoto {
    @Id @Column(name="id_producto") public Long id;
    @OneToOne @MapsId @JoinColumn(name="id_producto") public Producto producto;
    public Integer anio;
    public Integer cilindradaCc;
    public String motor;
    public String frenos;
    @Column(precision=6,scale=2) public java.math.BigDecimal tanqueLitros;
}
