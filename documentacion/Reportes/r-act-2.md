# Reporte de Avance del Proyecto

**Equipo:** 5  
**Fecha:** 13 de noviembre de 2026  
**Número de reporte:** 02  
**Repositorio GitHub:** [Kevin-2-3/desarrollo-web](https://github.com/Kevin-2-3/desarrollo-web)

---

# 1. Actividades realizadas

En la segunda etapa de Motostock se llevó el diseño de la tienda a un backend en Spring Boot. El código cubre el recorrido de un cliente desde la consulta del catálogo hasta la confirmación de una compra simulada.

- Se configuró el proyecto Maven con Java 21 y la conexión a PostgreSQL mediante Spring Data JPA e Hibernate.
- Se implementaron las entidades y los repositorios del modelo de datos. Se prepararon el esquema de doce tablas y datos de prueba con tres productos y cuatro variantes de venta (SKU).
- Se implementaron los controladores y servicios de autenticación, catálogo, carrito y pedidos.
- Se documentaron la instalación desde WSL y las pruebas manuales con CURL. Los archivos se organizaron por etapa para que cada integrante pueda revisar su módulo y preparar el commit correspondiente.

---

# 2. Evidencias de trabajo

Los enlaces de la primera columna permiten revisar los archivos de la entrega. Los enlaces a commits de esta etapa quedan pendientes de confirmar.

| Actividad                                                                                                                 | Integrante responsable                                               | Enlace a commit                                                                             |
| ------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| [Configuración de Spring Boot](../../backend/pom.xml) y [esquema PostgreSQL](../../database/esquema.sql)                  | Kevin Pérez en configuración, Erik y Kevin Cravioto en base de datos | https://github.com/Kevin-2-3/desarrollo-web/commit/7a1d4afcdbff863e469a49d84a6fd364004795a9 |
| [Registro, inicio de sesión y cierre de sesión](../../backend/src/main/java/mx/uv/motostock/usuario/AuthController.java)  | Kevin Pérez                                                          | https://github.com/Kevin-2-3/desarrollo-web/commit/8f8528282086bee94f9f524cd27828b8886922d8 |
| [Consulta del catálogo, filtros y variantes](../../backend/src/main/java/mx/uv/motostock/catalogo/CatalogoService.java)   | Emiliano                                                             | https://github.com/Kevin-2-3/desarrollo-web/commit/8eb799d1dd2b0059062280f77ea88cc8e0e6e967 |
| [Persistencia del carrito y cálculo de importes](../../backend/src/main/java/mx/uv/motostock/carrito/CarritoService.java) | Erik                                                                 | https://github.com/Kevin-2-3/desarrollo-web/commit/d2ff933910063986d6c1d35da9c58133d0cddd9b |
| [Confirmación de compras e historial](../../backend/src/main/java/mx/uv/motostock/pedido/PedidoService.java)              | Kevin Cravioto                                                       | https://github.com/Kevin-2-3/desarrollo-web/commit/e9ecf8f12019501965d14307e566acdb5f4a78af |
| [Instalación en WSL](../../README.md) y [registro de pruebas por módulo](../Entrega%202/05-evidencias.md)                 | Equipo completo                                                      | https://github.com/Kevin-2-3/desarrollo-web/commit/ee667567a887ddefb735f54478354b3a46607ea3 |

---

# 3. Problemas encontrados

- La preparación inicial de PostgreSQL requería escribir por separado las instrucciones de creación del usuario y de la base. Se incorporó [preparar.sql](../../database/preparar.sql) para ejecutar ambas operaciones desde WSL y solicitar la contraseña de forma interactiva.
- El precio y las existencias dependen de la variante elegida. El carrito identifica cada línea por cliente y SKU, lo que permite acumular dos unidades de la misma talla y mantener por separado las de otra talla.
- Reenviar una confirmación podía producir un segundo pedido o descuento de inventario. El servicio consulta la referencia de confirmación antes de procesar el carrito y agrupa los cambios de una compra aprobada en una transacción. La comprobación con dos solicitudes queda pendiente.

---

# 4. Avance funcional

 El catálogo admite consultas públicas. Para agregar una variante al carrito, confirmar una compra o consultar un pedido, el servidor identifica al cliente mediante su sesión.

---

# 5. Uso de IA generativa

- **Herramienta utilizada:** ChatGPT y Gemini.
- **Actividad para la que se utilizó:** Generación de README y clases Java
- **Código o contenido generado:** propuestas de codigo, documentación
- **Modificaciones realizadas por el equipo:** Se modificarón secciones de codigo o correciones conforme las inidicaciones de la actividad a realizar.
- **Forma en que se verificó el resultado:** queda pendiente la validación  mediante implementación y pruebas

---

# 6. Participación individual

La distribución de responsabilidades para esta entrega fue la siguiente.

- **Kevin Pérez** : configuración de Spring Boot y módulo de usuarios. Su revisión abarca el almacenamiento de contraseñas con BCrypt, la sesión y la protección CSRF.
- **Emiliano** :catálogo y detalle de productos. Su revisión abarca variantes SKU, búsqueda, filtros de precio y paginación.
- **Erik** : carrito persistente y revisión del esquema. Le corresponde comprobar la acumulación de cantidades, los subtotales y la recuperación del carrito después de iniciar sesión nuevamente.
- **Kevin Cravioto** : compras y pedidos, con revisión del esquema. Le corresponde comprobar el historial, la confirmación repetida y la consistencia de pedido, inventario y carrito.

---

# 7. Próximas actividades

1. Reproducir la instalación en WSL, compilar con Maven y comprobar que el catálogo responde con los productos de ejemplo.
2. Ejecutar con CURL el recorrido de registro, inicio de sesión, carrito y compra. Contrastar las respuestas HTTP con los datos guardados en PostgreSQL.
3. Probar una confirmación repetida, dos compradores que solicitan la última unidad y un fallo durante la compra. Registrar el efecto sobre pedidos, carritos y existencias.
4. Incorporar las evidencias y los enlaces a los commits revisados. Continuar con la integración del frontend mediante Fetch API.

---

## Observaciones adicionales

Motostock utiliza PostgreSQL como motor acordado por el equipo.
Las compras son simuladas. 
