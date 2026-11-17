# Reporte de Uso Documental en el Proyecto

**Fecha:** 15/11/2026  
**Número de reporte:** 03

## 1. Breve descripción de uso

Utilicé Codex para resolver una duda sobre cómo se manejan las cantidades de los productos en el carrito de compras, para ver la diferencia entre agregar productos y modificar la cantidad de los que ya estaban guardados.

## 2. Evidencias de uso

| Fecha | Herramienta | Enlace a prompt | Enlace a respuesta |
|---|---|---|---|
| 15/11/2026 | Codex (OpenAI) | [Consulta](#prompt) | [Explicación](#respuesta) |

### Prompt

¿PUT suma la cantidad como POST o debe dejar exactamente la que mando?

### Respuesta

POST sirve para agregar productos al carrito. Si ya existe el mismo SKU, la cantidad se suma a la que tenía anteriormente.

En cambio, PUT reemplaza la cantidad que ya estaba guardada por la nueva cantidad indicada. Si el producto no está en el carrito, devuelve un error 404.

En ambos casos se debe comprobar que la cantidad no supere las existencias disponibles. Además, cada producto se identifica mediante su SKU, lo que permite distinguir diferentes tallas y colores.

---

## Observaciones adicionales

Revisé la explicación con el código de `CarritoService.java` y los cambios del [commit a46277f](https://github.com/Kevin-2-3/desarrollo-web/commit/a46277f56f62eb86789d59b15e8d994aa72aae35), donde se agregaron validaciones para las cantidades y los identificadores de SKU, además de información sobre las variantes de los productos.