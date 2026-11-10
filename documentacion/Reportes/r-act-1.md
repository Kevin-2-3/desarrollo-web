# Reporte de Avance del Proyecto

**Equipo:** 5
**Fecha:** 9 de noviembre de 2026  
**Número de reporte:** 01  
**Repositorio GitHub:** https://github.com/Kevin-2-3/desarrollo-web

---

# 1. Actividades realizadas

Durante la etapa inicial se realizo  el análisis y diseño de Motostock, una tienda web de motocicletas, accesorios y refacciones enfocada exclusivamente en la experiencia de compra del cliente.

- Se definieron el propósito, la problemática, el alcance y los actores del sistema.
- Se elaboró la especificación de requerimientos  requisitos funcionales, requisitos no funcionales y reglas de negocio.
- Se propuso el modelo entidad-relación .
- Se definieron  endpoints REST.
- Se realizo la distribución de responsabilidades.
- Se delimitó el flujo principal de el usuario en el sistema.

---

# 2. Evidencias de trabajo

| Actividad                                               | Integrante responsable                         | Enlace a commit                                                                             |
| ------------------------------------------------------- | ---------------------------------------------- | ------------------------------------------------------------------------------------------- |
| Especificación de requerimientos y revisión del alcance | Equipo completo                                | https://github.com/Kevin-2-3/desarrollo-web/commit/937cea29a067ec26388b553487612343cf647f95 |
| Modelo entidad-relación                                 | Erik y Kevin Cravioto, con revisión del equipo | https://github.com/Kevin-2-3/desarrollo-web/commit/726806e1ca5b29c72978f793c080c1f879802268 |
| Definición de endpoints REST                            | Kevin Perez, con revision de equipo.           | https://github.com/Kevin-2-3/desarrollo-web/commit/97c49a229c8616f3a278ec12e53a3168f79628f7 |
| Distribución de responsabilidades                       | Emiliano,con revision del equipo.              | https://github.com/Kevin-2-3/desarrollo-web/commit/96ee8cc177ff937d14c64c89067703b39f963c63 |
| Preparación de la documentación                         | Kevin Cravioto revisado por el equipo          |                                                                                             |

---

# 3. Problemas encontrados

- Productos con variantes se definió utilizar SKU independientes para manejar precio, talla, color y existencias, según corresponda.
- Consistencia de compras: se contemplaron transacciones para crear el pedido, descontar existencias y vaciar el carrito de forma conjunta, además de una referencia de confirmación para prevenir compras duplicadas.
- Protección de información: se estableció el almacenamiento de contraseñas con BCrypt y la validación de sesiones y acceso a recursos propios.

---

# 4. Avance funcional

Por ahora, el avance corresponde a la carpeta de análisis y diseño. No se ha realizado una implementación a código funcional aun.

---

# 5. Uso de IA generativa

- **Herramienta utilizada:** ChatGPT.
- **Actividad para la que se utilizó:** apoyo en los endpoints REST
- **Código o contenido generado:** propuestas y contenido en formato de graficos.
- **Modificaciones realizadas por el equipo:** Se planteo por el momento tomar los endpoints planteados
- **Forma en que se verificó el resultado:** queda pendiente la validación  mediante implementación y pruebas

---

# 6. Participación individual

- **Kevin Pérez:** Definición de endpoints REST y documentación de SRS
- **Emiliano:** Distribución de responsabilidades
- **Erik:** Diseño inicial de la base de datos.
- **Kevin Cravioto:** Diseño inicial de la base de datos.

---

# 7. Próximas actividades

- Preparar el proyecto Spring Boot y el entorno de ejecución en WSL
- Configurar la conexión a PostgreSQL.
- Implementar las entidades, relaciones, repositorios y restricciones iniciales de la base de datos.
- Desarrollar y probar los endpoints de autenticación, catálogo, carrito y compras, conforme a los responsables asignados.
- Iniciar las interfaces HTML, CSS y JavaScript e integrar las peticiones con Fetch API.
- Registrar pruebas, capturas, enlaces a commits y consultas a IA .

---

## Observaciones adicionales

- El proyecto seguirá una arquitectura de tres capas: frontend, backend Spring Boot y base de datos PostgreSQL, con acceso mediante Hibernate.
- El proyecto se limita a clientes y compras simuladas; no incluye un panel administrativo ni transacciones bancarias reales.
- Este reporte documenta no una entrega funcional.
