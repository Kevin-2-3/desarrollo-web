# Reporte de Uso Documental en el Proyecto

**Fecha:** 19/11/2026
**Número de reporte:** 04

# 1. Breve descripción de uso

Surgió una duda al cargar las tallas del casco Shaft, usé a Gemini en mi caso para consultar la información que se debía compartir y seguí en la ficha qué cambia al elegir otra talla.

# 2. Evidencias de uso

| Fecha | Herramienta | Enlace a prompt | Enlace a respuesta |
|------------|------------------------|-----------------|-------|
| 19/11/2026 | Gemini (Google) | [Consulta](#prompt) | [Explicación](#respuesta) |

### Prompt

Oye tengo una duda, si tengo un casco que se vende en diferentes tallas, ¿tengo que registrar cada talla como un producto diferente o puedo manejarlo como uno solo? Y cómo le haría para controlar las existencias de cada talla?

### Respuesta

No necesitas registrar cada talla como un producto diferente. Lo recomendable es manejar **un solo producto con varias variantes o SKU**.

Por ejemplo, si vendes un casco en tallas S, M y L, puedes conservar la misma descripción e imagen, pero cada talla tendrá su propio identificador, precio y existencias.

De esta manera, cuando el cliente seleccione una talla, el sistema mostrará las unidades disponibles de esa variante sin necesidad de duplicar el producto. Esto también se puede aplicar a las motocicletas, donde un mismo modelo puede tener diferentes colores y existencias independientes.

---

## Observaciones adicionales

Si se agota la talla M, puedo seguir ofreciendo la L desde la misma ficha. [Detalle de producto y variantes](https://github.com/Kevin-2-3/desarrollo-web/blob/6a06cf4/frontend/js/catalogo.js)
