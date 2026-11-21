# Reporte de Uso Documental en el Proyecto

**Fecha:** 19/11/2026  
**Número de reporte:** 04  
# 1. Breve descripción de uso

Tuve una duda para saber que tenia que hacer si se cortaba la conexion cuando estaba 
# 2. Evidencias de uso

| Fecha | Herramienta | Prompt | Respuesta |
|------------|------------------------|-----------------|-------|
| 19/11/2026 | Chat gpt | Aqui abajo| Aqui abajo |

### Prompt

Que pasa si doy a confirmar y se corta la conexión y nunca veo el folio. Si vuelvo a pulsar el boton, como puede saber la app si es la misma compra?

### Respuesta

Antes de enviar la confirmación, la página guarda una referencia para ese intento. Si se pierde la respuesta, el siguiente envío usa la referencia guardada. El servidor puede reconocer la operación y devolver el pedido que ya registró. Si responde que el pago fue rechazado, la página permite iniciar otro intento y el carrito se conserva.

---

## Observaciones adicionales


Lo mejor es distinguir el mensaje de pago rechazado del de conexión interrumpida.
