# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 14/11/2026  
**Número de reporte:** 02  

---

# 1. Breve descripción de uso

En mi caso utilicé ChatGPT como apoyo para entender cómo evitar que una compra se registre dos veces cuando un cliente vuelve a enviar la confirmación. Basicamente quería entender biens cómo funciona la referencia de confirmación y cómo el sistema puede reconocer una compra que ya había sido hecha, evitando así que se genere otro pedido o que se descuenten de más las existencias de tal producto.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
Herramienta | ChatGPT (OpenAI)
Modelo/temperatura | Sin registro en el intercambio original.
Prompt original | [Formulación del caso documentado](#prompt-original)
Respuesta de IA | [Cita textual del módulo](#respuesta-de-ia)
Código anterior | [d2ff933](https://github.com/Kevin-2-3/desarrollo-web/commit/d2ff933910063986d6c1d35da9c58133d0cddd9b)
Código posterior | [e9ecf8f](https://github.com/Kevin-2-3/desarrollo-web/commit/e9ecf8f12019501965d14307e566acdb5f4a78af)
Diferencias entre versiones | `git diff --numstat d2ff933 e9ecf8f`<br>`28	0	backend/src/main/java/mx/uv/motostock/pedido/PedidoController.java`<br>`148	0	backend/src/main/java/mx/uv/motostock/pedido/PedidoService.java`
Prueba de funcionamiento | Consulta del stock del SKU 2, se enviaron dos POST /api/v1/pedidos con la misma referencia y el mismo resumen aprobado de dos unidades, y se consultaron el número de pedidos y el stock final. El ID y el folio coinciden en ambas respuestas. La fecha devuelta en el reenvío muestra la precisión de microsegundos de PostgreSQL.<br>`5`<br>`{"resultado":"APROBADO","referenciaConfirmacion":"qa-duplicado","pedido":{"totalMxn":3000.00,"id":1,"estado":"CONFIRMADO","fecha":"2026-11-14T23:23:40.238988545Z","folio":"MOTO-42892749-fef0-4795-b8ef-dd550f2e6540"}} HTTP 200`<br>`{"resultado":"APROBADO","referenciaConfirmacion":"qa-duplicado","pedido":{"totalMxn":3000.00,"id":1,"estado":"CONFIRMADO","fecha":"2026-11-14T23:23:40.238989Z","folio":"MOTO-42892749-fef0-4795-b8ef-dd550f2e6540"}} HTTP 200`<br>
Código de uso | Generación / Documentación / Pruebas
Reflexión del estudiante | Me quedó mucho más claro por qué se busca la referencia de confirmación antes de revisar el carrito y es porque la primera compra puede haberlo vaciado, pude ver que recibir dos respuestas correctas no basta sino que también hay que revisar que solo exista un pedido y que el stock se haya descontado una vez.

### Prompt original

¿Cómo puedo evitar que se registre una compra dos veces si un cliente vuelve a presionar el botón de confirmar? Quiero que si la compra ya se realizó, el sistema devuelva el mismo pedido y no vuelva a descontar productos del inventario aunque el carrito ya esté vacío.

### Respuesta de IA

Para evitar que una compra se registre dos veces, se puede utilizar una referencia única de confirmación. Esto permite identificar si una solicitud ya fue procesada anteriormente.

En `PedidoService.java`, primero se debe comprobar si existe una compra con la misma referencia, antes de revisar los productos del carrito. Esto es importante porque, después de una compra aprobada, el carrito queda vacío.

**Fragmento de `PedidoService.java`:**

```java
@Transactional
public Object confirmar(Long cliente, PedidoController.Confirmacion c) {
    Cliente usuario = carrito.bloquear(cliente);

    String firma = hash(List.of(
        c.resumenHash(),
        c.entrega(),
        c.aprobado()
    ));

    var anterior = intentos.findByClienteIdAndReferenciaConfirmacion(
        cliente, c.referenciaConfirmacion()
    );

    if (anterior.isPresent()) {
        var a = anterior.get();

        if (!a.resumenHash.equals(firma)) {
            throw ApiError.conflicto("Referencia reutilizada con otros datos");
        }

        return resultado(a);
    }

    var ls = lineas(cliente);
}
```

El código utiliza una firma generada a partir de los datos de confirmación para comprobar si la operación coincide con una anterior.

Si la referencia ya existe y los datos son iguales, el sistema devuelve el pedido registrado con el mismo ID y folio. Si los datos son diferentes, responde con un error `409 Conflict`. Cuando se trata de una referencia nueva, continúa con el procesamiento de la compra dentro de una transacción.

**Para comprobar su funcionamiento**, se pueden enviar dos solicitudes `POST /api/v1/pedidos` con los mismos datos y verificar que ambas devuelvan el mismo pedido.

También es importante consultar PostgreSQL para comprobar que solamente se haya registrado una compra y que las existencias del producto se hayan descontado una sola vez.

## Observaciones adicionales

El código de compras corresponde a mi commit «Implementando compras transaccionales e historial de pedidos». Las pruebas se realizaron en WSL con Java 21 y PostgreSQL 18.6, donde pude comprobar que al repetir una confirmación se devuelve el mismo pedido y que el stock solo se descuenta una vez. Solo queda pendiente probar qué sucede con dos solicitudes simultáneas.