package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="intento_confirmacion")
public class IntentoConfirmacion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_intento") public Long id;
    @ManyToOne @JoinColumn(name="id_cliente") public Cliente cliente;
    public String referenciaConfirmacion;
    public String resumenHash;
    public String resultado;
    public java.time.Instant fechaIntento;
}
