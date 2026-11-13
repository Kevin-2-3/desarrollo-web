package mx.uv.motostock.modelo;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@Embeddable public class CarritoId implements Serializable {
    @Column(name="id_cliente") public Long clienteId;
    @Column(name="id_sku") public Long skuId;
    public CarritoId() {
    }
    public CarritoId(Long c,Long s){
        clienteId=c;
        skuId=s;
    }
    @Override public boolean equals(Object o){
        return o instanceof CarritoId x && Objects.equals(clienteId,x.clienteId)&&Objects.equals(skuId,x.skuId);
    }
    @Override public int hashCode(){
        return Objects.hash(clienteId,skuId);
    }
}
