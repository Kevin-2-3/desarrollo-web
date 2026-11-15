# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 14/11/2026  
**Número de reporte:** 02  

---

# 1. Breve descripción de uso

Se utilizo Codex con una copia local del repositorio para tener mas informacion y recursos del proyecto. Se planteó resolver el problema al agregar múltiples veces el mismo producto (mismo SKU) al carrito de compras, para asegurar que las cantidades se acumularan en una sola línea y que la validación respetara el stock disponible en la base de datos.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
Herramienta | Codex (OpenAI)
Modelo/temperatura | Sin registro en el intercambio original.
Prompt original | [Formulación del caso documentado](#prompt-original)
Respuesta de IA | [Cita textual del módulo](#respuesta-de-ia)
Código anterior | [8eb799d](https://github.com/Kevin-2-3/desarrollo-web/commit/7a1d4afcdbff863e469a49d84a6fd364004795a9)
Código posterior | [d2ff933](https://github.com/Kevin-2-3/desarrollo-web/commit/d2ff933910063986d6c1d35da9c58133d0cddd9b)
Diferencias entre versiones | `git diff --numstat 8eb799d d2ff933`<br>`32	0	backend/src/main/java/mx/uv/motostock/carrito/CarritoController.java`<br>`68	0	backend/src/main/java/mx/uv/motostock/carrito/CarritoService.java`
Prueba de funcionamiento | Se enviaron dos peticiones POST consecutivas a `/api/v1/carrito/items` con `{"skuId":2,"cantidad":1}`, seguidas de una petición GET para comprobar el estado final.<br>`{"items":[{"skuId":2,"nombre":"Casco Stream","cantidad":1,"precioUnitarioMxn":1500.00,"subtotalMxn":1500.00}],"totalMxn":1500.00} HTTP 200`<br>`{"items":[{"skuId":2,"nombre":"Casco Stream","cantidad":2,"precioUnitarioMxn":1500.00,"subtotalMxn":3000.00}],"totalMxn":3000.00} HTTP 200`
Código de uso | Generación / Documentación / Pruebas
Reflexión del estudiante | Se comprendió que al gestionar el carrito no basta con procesar únicamente los datos de la solicitud actual, sino que es obligatorio consultar los registros previos en la base de datos para realizar la suma acumulada y contrastarla adecuadamente contra el stock.

### Prompt original

Si agrego un casco al carrito y después vuelvo a agregar la misma talla, ¿cómo hago para que quede en una sola fila con cantidad 2? También quiero evitar que la suma pase de lo que hay en stock.

### Respuesta de IA

CarritoId `identifica la línea por cliente y SKU, y ` CarritoService suma la nueva cantidad y valida que haya existencias suficientes.

Para agregar una unidad, identifica primero la línea por el cliente de la sesión y el SKU elegido. Dos clientes pueden comprar la misma talla y cada uno necesita su propio carrito. También importa la variante: si un cliente agrega un casco M y otro L, tendrá dos líneas, porque son SKU distintos. La segunda unidad del mismo casco M sí pertenece a la línea existente.

Fragmento de `CarritoService.java`:

```java
        var key=new CarritoId(id,skuId);
        var previo=items.findById(key);
        if(reemplazar&&previo.isEmpty())throw ApiError.noExiste();
        long nueva=reemplazar?cantidad:(long)previo.map(i->i.cantidad).orElse(0)+cantidad;
        validar(s,nueva);
        var item=previo.orElseGet(CarritoItem::new);
        item.id=key;
        item.cliente=c;
        item.sku=s;
        item.cantidad=(int)nueva;
        items.save(item);
```

En un POST, si ya había una unidad y llega otra, la cantidad que debes comparar con las existencias es dos. Esta distinción se aprecia cuando queda una sola unidad en inventario: cada solicitud individual pide una cantidad válida, pero aceptar las dos dejaría un carrito imposible de surtir. El método calcula la cantidad final, la valida y después guarda la línea. También admite PUT, donde la cantidad enviada reemplaza la anterior.

La prueba envió dos POST con el SKU 2. La primera respuesta mostró una unidad a 1500 MXN. La segunda mostró la misma línea con cantidad 2 y subtotal de 3000 MXN. El GET posterior confirmó el contenido del carrito. Agregar esas unidades deja el descuento de inventario para la confirmación de compra.

---

## Observaciones adicionales

El cambio del carrito lo subí en el commit "Se implemento el carrito y el calculo de totales". La prueba la corrí en la versión ya integrada, en WSL con Java 21 y PostgreSQL 18.6. Después de agregar dos veces el SKU 2, la consulta dio una sola línea con cantidad 2 y un total de 3000 MXN. Me faltaría checar si el carrito se sigue conservando bien después de reiniciar el backend.