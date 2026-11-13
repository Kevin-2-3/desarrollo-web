package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="pedido_detalle")
public class PedidoDetalle {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_detalle") public Long id;
    @ManyToOne @JoinColumn(name="id_pedido") public Pedido pedido;
    @ManyToOne @JoinColumn(name="id_sku") public Sku sku;
    public String nombreCongelado;
    public String marcaCongelada;
    public String descripcionCongelada;
    public String codigoSkuCongelado;
    public String tallaCongelada;
    public String colorCongelado;
    @Column(precision=12,scale=2) public java.math.BigDecimal precioUnitarioMxn;
    public int cantidad;
    @Column(precision=12,scale=2) public java.math.BigDecimal subtotalMxn;
}
