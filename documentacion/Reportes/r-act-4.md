# Reporte de Avance del Proyecto

**Equipo:** 5  
**Fecha:** 20/11/2026  
**Número de reporte:** 04  
**Repositorio GitHub:** [Kevin-2-3/desarrollo-web](https://github.com/Kevin-2-3/desarrollo-web)

---

# 1. Actividades realizadas

Se completó la interfaz de Motostock . La tienda usa páginas HTML separadas, CSS y JavaScript. Spring Boot sirve los archivos estáticos y las páginas consultan la API REST con fetch.

Se incorporaron el registro y la sesión, el catálogo con búsqueda y filtros, la ficha de producto con selector de variantes, el carrito, la revisión de compra, el pago simulado y el historial de pedidos. Se preparó la carga inicial de PostgreSQL con 37 motos, 8 accesorios y 8 refacciones, junto con las fotos y fichas del catálogo.

---

# 2. Evidencias de trabajo

| Actividad                                                                            | Integrante responsable          | Enlace a commit                                                                                                                                                                                                                                                                                                                                                                                                                |
| ------------------------------------------------------------------------------------ | ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Portada y estilos iniciales, registro e inicio de sesión                             | Kevin Pérez                     | [1943cf0](https://github.com/Kevin-2-3/desarrollo-web/commit/1943cf0a91f7481b83815e8f0475e4ffd92f27b6), [daaf97c](https://github.com/Kevin-2-3/desarrollo-web/commit/daaf97c95ee33e02718a05cf2a2be22280317517)                                                                                                                                                                                                                 |
| Carga del catálogo en PostgreSQL, listado con filtros, ficha y selector de variantes | Kevin Cravioto                  | [320660c](https://github.com/Kevin-2-3/desarrollo-web/commit/320660cb3b6f153058ff9d649b86607f312dcbcf), [90db08d](https://github.com/Kevin-2-3/desarrollo-web/commit/90db08d7668006848d2ad20410761744356c8139), [6a06cf4](https://github.com/Kevin-2-3/desarrollo-web/commit/6a06cf4874b68ffa2984bb709b007e29489fd88f)                                                                                                         |
| Gestión del carrito y envío del SKU elegido desde la ficha                           | Erik                            | [1b99707](https://github.com/Kevin-2-3/desarrollo-web/commit/1b99707a96b8617e016c08783dda8b5554b3d274), [34a4b2e](https://github.com/Kevin-2-3/desarrollo-web/commit/34a4b2ef674dfc220a3af38cfb785bd633a5919d)                                                                                                                                                                                                                 |
| Datos de entrega, revisión de compra, pago simulado, historial y detalle             | Emiliano Garcia                 | [d05c19e](https://github.com/Kevin-2-3/desarrollo-web/commit/d05c19e536f6932cb2cd1dd8e447eaf06055bb62), [59edcd0](https://github.com/Kevin-2-3/desarrollo-web/commit/59edcd0092a30a559a87ee15f3eb54ab838d2995)                                                                                                                                                                                                                 |


---

# 3. Problemas encontrados

- Al abrir el acceso desde una ficha se perdía el producto que estaba viendo el cliente. Se pasó la ruta de origen con el parámetro `volver` para recuperar la ficha después de iniciar sesión.
- Los datos de prueba mezclaban productos de muestra con los del catálogo. Se preparó `database/catalogo.sql` y se movió la carga anterior a `pruebas/datos-historicos/`. Las variantes comparten la foto y la ficha del producto y tienen su propio SKU, precio y existencias.
- Si el navegador pierde la respuesta al confirmar un pago, el cliente puede necesitar reenviarlo. La página guarda la referencia del intento y la reutiliza para que la API pueda devolver el resultado de esa misma confirmación.

---

# 4. Avance funcional

La carga inicial define 53 productos con foto y 97 SKU. Quedaron integradas las pantallas de acceso, listado, detalle de producto, carrito, compra e historial. Una instalación nueva carga el catálogo en PostgreSQL. Las cuentas se crean desde el formulario de registro.


---

# 5. Uso de IA generativa

- **Herramienta utilizada:** Codex, Gemini, Claude
- **Actividad para la que se utilizó:** resolver dudas puntuales del frontend
- **Código o contenido generado:** fragmentos para validar el registro, actualizar la variante, enviarla al carrito y abrir el detalle del pedido
- **Modificaciones realizadas por el equipo:** los cambios publicados adaptaron los formularios, el catálogo y la compra a las rutas y respuestas de la API de Motostock.
- **Forma en que se verificó el resultado:** Las pruebas de la API se documentaron con CURL desde WSL y las pruebas desde la pagina de capturaron para cada intento de actividad

---

# 6. Participación individual

- **Integrante 1:** Kevin Pérez. estilos iniciales y los formularios de registro e inicio de sesión.
- **Integrante 2:** Emiliano Garcia . Integró el formulario de entrega, el resumen, el pago simulado y las vistas de historial y detalle del pedido.
- **Integrante 3:** Erik. Integró el envío de la variante seleccionada y la actualización, eliminación y vaciado de artículos en el carrito.
- **Integrante 4:** Kevin Cravioto. Preparó la carga del catálogo en PostgreSQL e integró la búsqueda, los filtros, la paginación y la ficha de producto.

---

# 7. Próximas actividades

- Anotar las versiones de los navegadores usados en la comprobación final.
- Registrar el enlace del cuarto release cuando se publique.
- Anexar casos de prueba 
- Corregir últimos problemas

---

## Observaciones adicionales

Como ultima instancia, parece que ya quedo realizado el sistema, se necesitan hacer pruebas continuas para verificar y en caso, comprobar el funcionamiento y anexar evidencias
