package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="cliente")
public class Cliente {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_cliente") public Long id;
    public String nombre;
    public String correo;
    @com.fasterxml.jackson.annotation.JsonIgnore public String passwordHash;
}
