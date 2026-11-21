# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 19/11/2026
**Número de reporte:** 04

---

# 1. Breve descripción de uso

Al abrir una ficha ya tenía las variantes del producto. Para apoyarme, le pregunté a Gemini y una vez dandole todo el contexto, le pregunte como hacer para que el selector mostrara el precio y las unidades de la talla elegida y después revisé el cambio en la función que atiende al selector.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
| Herramienta | Gemini (Google) |
| Modelo/temperatura | — |
| Prompt original | [Consulta](#prompt) |
| Respuesta de IA | [Respuesta](#respuesta) |
| Código anterior | [90db08d](https://github.com/Kevin-2-3/desarrollo-web/commit/90db08d) |
| Código posterior | [6a06cf4](https://github.com/Kevin-2-3/desarrollo-web/commit/6a06cf4) |
| Diferencias entre versiones | [Salida de git diff --numstat](#diferencias-entre-versiones) |
| Prueba de funcionamiento | En el catálogo local aparecieron 53 productos. Abrí la CB190R, seleccioné Negro y la ficha mostró cinco unidades disponibles. [Ver capturas](#capturas-de-la-prueba) |
| Código de uso | Generación / Revisión |
| Reflexión del estudiante | Cuando paso de una talla a otra tengo que reiniciar también la cantidad. Si conservo las tres unidades que elegí en M, la talla L podría tener solo una. |

### Prompt

Tengo un selector de variantes en mi proyecto con JavaScript, pero quiero que cuando el usuario elija otra talla o color se actualice automáticamente el precio y las existencias disponibles. ¿Cómo puedo hacerlo? Igual quiero que no deje agregar más productos de los que ya estan disponibles.

### Respuesta

Puedes hacerlo utilizando JavaScript para detectar cuándo el usuario cambia la variante seleccionada y actualizar los datos que aparecen en la página.

Primero debes identificar el SKU seleccionado y obtener su precio y existencias. Después, actualizas esos valores en la interfaz.

**Ejemplo de código:**

```javascript
const v = seleccion();

form.querySelector(".precio").textContent =
    v ? ctx.dinero(v.precioMxn) : "Sin precio disponible";

cantidad.max = v?.stock || 0;
cantidad.value = 1;
```

Este código actualiza el precio según la variante elegida, establece el stock como cantidad máxima y reinicia la cantidad a 1 cada vez que se cambia de talla o color.

También es recomendable desactivar el botón de compra cuando no haya existencias y mostrar un mensaje de **«Agotado»**.

Así evitas que el cliente seleccione una cantidad mayor a la disponible.

### Diferencias entre versiones

~~~text
git diff --numstat 90db08d 6a06cf4
18	5	frontend/css/tienda.css
96	2	frontend/js/catalogo.js
53	0	frontend/producto.html
~~~

### Capturas de la prueba

**Ficha de la CB190R con la variante Negra**

![Ficha de la CB190R con la variante Negra](<capturas/Ficha de la CB190R negra.jpg>)

---

## Observaciones adicionales

Carga del catálogo en [320660c](https://github.com/Kevin-2-3/desarrollo-web/commit/320660c), listado en [90db08d](https://github.com/Kevin-2-3/desarrollo-web/commit/90db08d) y detalle de producto y variantes en [6a06cf4](https://github.com/Kevin-2-3/desarrollo-web/commit/6a06cf4).
