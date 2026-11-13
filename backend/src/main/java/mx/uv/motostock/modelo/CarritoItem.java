package mx.uv.motostock.modelo;
import jakarta.persistence.*;
@Entity @Table(name="carrito_item") public class CarritoItem {
    @EmbeddedId public CarritoId id;
    @ManyToOne @MapsId("clienteId") @JoinColumn(name="id_cliente") public Cliente cliente;
    @ManyToOne @MapsId("skuId") @JoinColumn(name="id_sku") public Sku sku;
    public int cantidad;
}
