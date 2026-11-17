# Reporte de Avance del Proyecto

**Equipo:** 5 
**Fecha:** 16/11/2026  
**Número de reporte:** 03  
**Repositorio GitHub:** [Kevin-2-3/desarrollo-web](https://github.com/Kevin-2-3/desarrollo-web)

---

# 1. Actividades realizadas

Se integraron los cuatro ajustes del backend de la tercera entrega: errores por campo y estados HTTP, búsqueda literal del catálogo, validaciones e información de variantes en el carrito y lectura de la fecha persistida al confirmar pedidos. 

---

# 2. Evidencias de trabajo

| Actividad | Integrante responsable | Enlace a commit |
|------------|------------------------|-----------------|
| se agregan verificaciones de campo y errores HTTP | Kevin Pérez  | [65dbbb8](https://github.com/Kevin-2-3/desarrollo-web/commit/65dbbb823b642877c9d8665b9743f022edee5e04) |
| Busqueda de catalogo | Emiliano  | [8c2d412](https://github.com/Kevin-2-3/desarrollo-web/commit/8c2d412a2344978f4c4e5e2c893117cc46932e70) |
| Validacion de entradas y variantes del carrito | Erik  | [a46277f](https://github.com/Kevin-2-3/desarrollo-web/commit/a46277f56f62eb86789d59b15e8d994aa72aae35) |
| Actualizacion para devolver la fecha persistida al confirmar pedidos | Kevin Cravioto  | [6705497](https://github.com/Kevin-2-3/desarrollo-web/commit/670549789e70d6e2984880f42c98399b0740a3c3) |
| Guías manuales de preparación y autenticación | Todos   | [d313c94](https://github.com/Kevin-2-3/desarrollo-web/commit/d313c94da23ff92b6a8a74dd7027a7dc22ca2fb4) |

---

# 3. Problemas encontrados

El manejo general de excepciones no distinguía algunos errores de entrada. 
La búsqueda interpretaba los comodines del texto como parte del patrón SQL.
El carrito no mostraba información adicional de la variante. 
La precisión temporal de PostgreSQL podía producir una fecha ligeramente distinta entre la primera confirmación y su reenvío.

---

# 4. Avance funcional

Los cuatro cambios están versionados. Las guías manuales de autenticación también están incorporadas y en teoria ya toda la logica back esta implementada y probada.

---

# 5. Uso de IA generativa

- **Herramienta utilizada:** Codex, Claude y Gemini
- **Actividad para la que se utilizó:** apoyo para revisar el código, explicar su funcionamiento y preparar guías manuales.
- **Código o contenido generado:** cambios por módulo descritos en sus reportes y en los commits indicados.
- **Modificaciones realizadas por el equipo:** se integraron los ajustes de validación, búsqueda, carrito y fecha de pedidos registrados en los commits indicados.
- **Forma en que se verificó el resultado:** se revisaron los cambios de cada commit, ademas de las verificaciones con CURL.

---

# 6. Participación individual

- **Integrante 1:** Kevin Pérez: errores por campo, estados HTTP y guías de autenticación.
- **Integrante 2:** Emiliano: búsqueda del catálogo con caracteres literales.
- **Integrante 3:** Erik: validación de entradas y datos de variantes en el carrito.
- **Integrante 4:** Kevin Cravioto: fecha persistida al confirmar pedidos.

---

# 7. Próximas actividades

- Revisar e incorporar los reportes actualizados.
- Integrar las guías manuales restantes.
- Ejecutar las comprobaciones con CURL desde WSL.
- Revisar la correspondencia del tercer release con el código entregado y continuar con la integración del frontend.

---

## Observaciones adicionales

Por ahora, parece que todo el backend funciona como deberia, en caso de fallar algo no tomando aun, se anexa como actividad pendiente para el siguiente incremento.
