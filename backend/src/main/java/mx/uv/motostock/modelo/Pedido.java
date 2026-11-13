package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="pedido")
public class Pedido {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_pedido") public Long id;
    @ManyToOne @JoinColumn(name="id_cliente") public Cliente cliente;
    @OneToOne @JoinColumn(name="id_intento") public IntentoConfirmacion intento;
    public String folio;
    public java.time.Instant fecha;
    public String estado;
    @Column(precision=12,scale=2) public java.math.BigDecimal totalMxn;
    public String nombreDestinatario;
    public String telefono;
    public String direccion;
    public String localidad;
    public String estadoEntrega;
    public String codigoPostal;
}
