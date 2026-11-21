# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 19/11/2026  
**Número de reporte:** 04-01  

---

# 1. Breve descripción de uso

Queria que el usuario pudiera ver el folio cuando confirmara la compra y despues volver a consultar el pedido en el historial de compra

---

# 2. Evidencias de uso

| Evidencia | Formato | 
|------------|------------------------|
| Herramienta | Claude |
| Modelo/temperatura | — |
| Prompt original | En la parte inferior esta |
| Respuesta de IA | En la parte inferior esta |
| Código anterior | [34a4b2e](https://github.com/Kevin-2-3/desarrollo-web/commit/34a4b2ef674dfc220a3af38cfb785bd633a5919d) |
| Código posterior | [d05c19e](https://github.com/Kevin-2-3/desarrollo-web/commit/d05c19e536f6932cb2cd1dd8e447eaf06055bb62) aqui fue la revision de compra y en [59edcd0](https://github.com/Kevin-2-3/desarrollo-web/commit/59edcd0092a30a559a87ee15f3eb54ab838d2995) añade lo del historial |
| Diferencias entre versiones | [Salida de git diff --numstat](#diferencias-entre-versiones) |
| Prueba de funcionamiento | En la versión local revise una compra de dos unidades y elegi rechazado. La pagina aviso que el carrito de compra se guardo y mantuvo el mismo resumen de 127,000 |
| Código de uso | Generacion y Revision |
| Reflexión del estudiante | Con el folio ya se puede identificar el pedido y con el id ver sus detalles |

### Prompt

La api ya me da el pedido aprobado. ¿Como le hago para que muestre su folio y el cliente pueda volver a abrir ese pedido desde Mis pedidos?

### Respuesta

Cuando la API apruebe el pago, borra el resumen guardado y abre el detalle con el ID del pedido. Muestra el folio en el mensaje de confirmación.

~~~js
sessionStorage.removeItem(clave);
ctx.ir('pedido.html?id=' + encodeURIComponent(respuesta.pedido.id), 'Compra confirmada. Folio ' + respuesta.pedido.folio);
~~~

En el historial, usa el folio de cada fila como enlace a la misma página de detalle con el ID de ese pedido.

### Diferencias entre versiones

~~~text
git diff --numstat 34a4b2e 59edcd0
49	47	frontend/carrito.html
40	0	frontend/compra.html
133	131	frontend/css/tienda.css
175	175	frontend/js/app.js
252	77	frontend/js/compras.js
53	0	frontend/pedido.html
41	0	frontend/pedidos.html
61	0	frontend/resumen.html
~~~

### Capturas de la prueba

**Resumen tras el rechazo del pago simulado**

![Resumen tras el rechazo del pago simulado](<capturas/Resumen de compra con pago rechazado.jpg>)

---

## Observaciones adicionales

El folio ya es mostrado y el cliente puede ver los detalles la compra
