package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="marca")
public class Marca {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_marca") public Long id;
    public String nombre;
}
