package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="ficha_refaccion") public class FichaRefaccion {
    @Id @Column(name="id_producto") public Long id;
    @OneToOne @MapsId @JoinColumn(name="id_producto") public Producto producto;
    public String compatibilidad;
}
