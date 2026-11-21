# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 19/11/2026  
**Número de reporte:** 04-01

# 1. Breve descripción de uso

Utilicé Codex para revisar cómo conectar el botón de agregar al carrito con la variante que selecciona el cliente. La idea era que se enviaran tanto el SKU como la cantidad, para que el carrito pudiera mostrar el artículo correcto y permitir cambiar sus unidades.

# 2. Evidencias de uso

| Evidencia                   | Formato                                                                                                                                                                          |
| --------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Herramienta                 | Codex                                                                                                                                                                            |
| Modelo/temperatura          | —                                                                                                                                                                                |
| Prompt original             | [consulta](#prompt)                                                                                                                                                              |
| Respuesta de IA             | [Respuesta](#respuesta)                                                                                                                                                          |
| Código anterior             | [6a06cf4](https://github.com/Kevin-2-3/desarrollo-web/commit/6a06cf4874b68ffa2984bb709b007e29489fd88f)                                                                           |
| Código posterior            | [34a4b2e](https://github.com/Kevin-2-3/desarrollo-web/commit/34a4b2ef674dfc220a3af38cfb785bd633a5919d)                                                                           |
| Diferencias entre versiones | [Salida de git diff --numstat](#diferencias-entre-versiones)                                                                                                                     |
| Prueba de funcionamiento    | En la versión local integrada agregué una CB190R Negra. Cambié la cantidad de una a dos y el total subió de $63,500.00 a $127,000.00 MXN. [Ver capturas](#capturas-de-la-prueba) |
| Código de uso               | Generación / Revisión                                                                                                                                                            |
| Reflexión del estudiante    | Que el botón conozca el producto no basta para llenar el carrito. Tengo que enviar también la variante exacta que el cliente eligió.                                             |

### Prompt

¿Me ayudas con el botón de agregar? Si elijo talla L y dos unidades, quiero que llegue eso al carrito y que la ficha siga abierta si el servidor rechaza la cantidad.

### Respuesta

Dentro del envío del formulario, toma el ID del SKU seleccionado y la cantidad numérica. Después de validar el formulario y la sesión, manda esos dos datos.

```js
const body = { skuId: seleccion().id, cantidad: cantidad.valueAsNumber };
await ctx.api("/carrito/items", { method: "POST", body });
```

Espera la respuesta antes de abrir `carrito.html`. Si hay un error, muestra el mensaje en la ficha y habilita los controles para que el cliente pueda corregir la cantidad.

### Capturas de la prueba

**Carrito con dos unidades de la variante Negra**

![Carrito con dos unidades de la variante Negra](<capturas/Carrito con dos motocicletas.jpg>)

---

# 3. Observaciones adicionales

Con esta parte entendí que no basta con enviar el producto al carrito, porque también hay que indicar qué variante eligió el cliente. Esto es importante para que no se confundan productos que tienen diferentes tallas o colores.

El [commit 1b99707](https://github.com/Kevin-2-3/desarrollo-web/commit/1b99707a96b8617e016c08783dda8b5554b3d274) incluye la gestión del carrito y después se conectó el botón de la ficha del producto.
