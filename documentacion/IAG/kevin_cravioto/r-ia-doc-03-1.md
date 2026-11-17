# Reporte de Uso Documental en el Proyecto

**Fecha:** 15/11/2026  
**Número de reporte:** 03  


# 1. Breve descripción de uso

Usé Gemini para consultar sobre una duda de confirmación e integridad de pedidos en el proceso de armar la tercera entrega. 

# 2. Evidencias de uso

| Fecha | Herramienta | Enlace a prompt | Enlace a respuesta |
|---|---|---|---|
| 15/11/2026 | Gemini (Google) | [Consulta](#prompt) | [Explicación](#respuesta) |

### Prompt

¿Por qué cuando confirmo dos veces la misma compra aparece una diferencia en la fecha de pedido?, es que se supone que es la misma compra y quiero hacer que las dos fechas coincidan

### Respuesta

Esto sucede porque Java y PostgreSQL manejan las fechas con diferente precisión. Java puede trabajar con nanosegundos, mientras que PostgreSQL normalmente almacena microsegundos.

Para solucionarlo, puedes utilizar `em.flush()` y `em.refresh(p)` en `PedidoService.java`.

```java
em.flush();
em.refresh(p);
```

`flush()` sincroniza los cambios pendientes con la base de datos y `refresh()` vuelve a cargar el pedido con la fecha almacenada en PostgreSQL.

Así, cuando el cliente repita la confirmación, ambas respuestas deberían devolver el mismo ID, folio y fecha del pedido.


## Observaciones adicionales

Fue de mucha ayuda la IA porque pude entender un poco más por qué las fechas de un mismo pedido podían ser diferentes y quedó claro que no solo basta con verificar el ID y el folio y que también es importante checar que la fecha sea la misma al repetir una confirmación.