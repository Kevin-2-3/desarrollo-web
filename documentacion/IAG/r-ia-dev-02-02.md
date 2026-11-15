# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 15/11/2026  
**Entrega:** 02  

---

# 1. Breve descripción de uso

Se utilizó Codex para generar la estructura inicial y las guías de pruebas manuales a partir del código de la segunda entrega. Las instrucciones se organizaron por integrante e incluyeron comandos CURL para ejecutar uno por uno desde WSL, consultas a PostgreSQL y el resultado esperado en cada caso. Esto permitió agilizar la documentación de pruebas que el equipo necesitaba.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|---|---|
| Herramienta | Codex (OpenAI). |
| Modelo/temperatura | GPT-6 |
| Prompt original | [Consulta](#prompt-original). |
| Respuesta de IA | [Guías preparadas](#respuesta-de-ia). |
| Código anterior | c64bea1 |
| Código posterior | Pendiente. |
| Diferencias entre versiones | Pendiente git diff --numstat |
| Prueba de funcionamiento | Se ejecutaron de forma manual los comandos CURL y se registraron las respuestas, consultas SQL y evidencias correspondientes. |
| Código de uso | Generación / Documentación / Pruebas. |
| Reflexión del estudiante | El apoyo de la IA en la generación de escenarios de prueba facilito la planeación, permitiendo que cada integrante ejecute y valide su módulo paso a paso. |

### Prompt original

En base al codigo realizado ya en general, crea guias para cada una de las pruebas necesarias para asi poder replicaralas de manera manual por cada integrante del equipo 

### Respuesta de IA

Se preparó una guía de entorno y cuatro guías por módulo:

- **Preparación:** obtener una copia de la versión de la entrega 2 sin alterar los cambios actuales, preparar una base de pruebas y arrancar Spring Boot desde WSL.
- **Kevin Pérez:** registro, correo único, contraseña con BCrypt, inicio y cierre de sesión, CSRF y validación de entradas.
- **Emiliano:** catálogo, búsqueda, filtros, precios por variante, paginación y detalle del producto.
- **Erik:** agregar y acumular cantidades, actualizar, eliminar, consultar y conservar el carrito de cada cliente.
- **Kevin Cravioto:** resumen de compra, aprobación o rechazo simulado, reenvío de confirmaciones, inventario, historial y comprobaciones de integridad y persistencia.

Las guías se encuentran en `pruebas/entrega2/00-preparacion.md`, `01-kevin-perez.md`, `02-emiliano.md`, `03-erik.md` y `04-kevin-cravioto.md`.

Cada caso indica qué comando ejecutar y qué respuesta se espera.

---

## Observaciones adicionales

No se modificó el código del backend para este material. Las pruebas se ejecutaron de forma manual y los resultados sirvieron para comprobar el funcionamiento del sistema. Tambien, se creó una carpeta exclusiva para guardar los archivos .md de las pruebas en lugar de un script de Bash para facilitar que el equipo pueda entender y seguir cada caso sin tener problemas.