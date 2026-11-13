package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="ficha_accesorio") public class FichaAccesorio {
    @Id @Column(name="id_producto") public Long id;
    @OneToOne @MapsId @JoinColumn(name="id_producto") public Producto producto;
    public String tipoUso;
}
