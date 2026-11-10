# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 09/11/2026  
**Número de reporte:** 01

---

# 1. Breve descripción de uso

Se utilizó ChatGPT para apoyar el **diseño inicial de la API REST** de Motostock, a partir de la especificación de requerimientos del proyecto. La consulta buscó identificar los endpoints necesarios, elegir los métodos HTTP correspondientes y conocer porque se usa cada uno.

---

# 2. Evidencias de uso

| Evidencia                       | Descripción                                                                                                                                                                                                                                                                                                                                                                                                            |
| ------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Herramienta**                 | ChatGPT (OpenAI)                                                                                                                                                                                                                                                                                                                                                                                                       |
| **Modelo/temperatura**          | GPT-6 / Temperatura no disponible                                                                                                                                                                                                                                                                                                                                                                                      |
| **Prompt original**             | Ver sección 2.1                                                                                                                                                                                                                                                                                                                                                                                                        |
| **Respuesta de IA**             | Ver sección 2.2                                                                                                                                                                                                                                                                                                                                                                                                        |
| **Código anterior**             | No aplica generación de codígo. Commit: -                                                                                                                                                                                                                                                                                                                                                                              |
| **Código posterior**            | No aplica generación de codígo. Commit: 937cea2                                                                                                                                                                                                                                                                                                                                                                        |
| **Diferencias entre versiones** | -                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **Prueba de funcionamiento**    | No realizada: los endpoints fueron propuestos, pero aun no han sido implementados.                                                                                                                                                                                                                                                                                                                                     |
| **Código de uso**               | Diseño / Explicación / Documentación                                                                                                                                                                                                                                                                                                                                                                                   |
| **Reflexión del estudiante**    | La consulta permitió identificar cómo dividir la API por responsabilidades y relacionar cada endpoint con los requisitos funcionales. También ayudó a distinguir cuándo usar GET, POST, PUT y DELETE y a considerar aspectos como autenticación, persistencia del carrito y prevención de compras duplicadas. Se analizo y verifico la prouesta, aun en claro que es posible su modificación a lo largo del desarollo. |

## 2.1. Prompt original

Ahora, tomando encuentra la especificación de requerimientos define los endpoints REST necesarios para el proyecto de la tienda de motocicletas, ademas de una breve explicacion de porque se elige cada uno de ellos.

## 2.2. Respuesta de IA (síntesis de la primera respuesta)

# Propuesta de endpoints REST — Motostock

Tomando como base la Especificación de Requerimientos de Motostock (RF01–RF14), propongo una API REST que permita implementar todas las funcionalidades definidas sin agregar complejidad innecesaria.

La propuesta está pensada para utilizar Java con Spring Boot, Spring Data JPA/Hibernate y MySQL, mientras que el frontend consumirá los endpoints mediante `fetch()` de JavaScript, siguiendo la arquitectura de tres capas solicitada en el proyecto final.

proyecto-final.pdf

## 1. Resumen general

Endpoints propuestos

# 16

Módulos

# 5

Requisitos cubiertos

# 14/14

Distribución por método HTTP

GET 6

POST 6

PUT 2

DELETE 2

Todos los endpoints utilizarán el prefijo:

`/api/v1`

La versión `v1` es una decisión de diseño propuesta, no una exigencia de la especificación. Permite organizar la API y facilitar futuras modificaciones.

## 2. Autenticación y cuentas (RF01–RF03)

Este módulo se encarga del registro de clientes, inicio y cierre de sesión.

|Método|Endpoint|Propósito|
|---|---|---|
|POST|`/auth/register`|Registrar cliente|
|POST|`/auth/login`|Iniciar sesión|
|POST|`/auth/logout`|Cerrar sesión|

### Justificación

POST `/auth/register`: Se utiliza POST porque se crea un nuevo recurso, en este caso una cuenta de cliente. Se reciben nombre, correo y contraseña. El backend valida que el correo sea único y almacena únicamente el hash de la contraseña.

POST `/auth/login`: Se utiliza POST porque se envían credenciales para autenticación y se establece una nueva sesión. No sería adecuado GET porque los datos sensibles podrían quedar expuestos en la URL.

POST `/auth/logout`: Se utiliza POST porque cerrar sesión modifica el estado de autenticación en el servidor, invalidando la sesión activa.

Ejemplo de registro:

```
POST /api/v1/auth/register
Content-Type: application/json

{
  "nombre": "Carlos Pérez",
  "correo": "carlos@gmail.com",
  "password": "ClaveSegura123"
}
```

Para este proyecto recomiendo sesiones del servidor con cookies HttpOnly, en lugar de implementar JWT, ya que la especificación exige invalidar sesiones en el servidor y conservar el carrito en la base de datos. Spring Security puede gestionar estas sesiones.

## 3. Catálogo de productos (RF04–RF06)

Este módulo permite consultar motocicletas, accesorios y refacciones, sin necesidad de autenticarse.

|Método|Endpoint|Propósito|
|---|---|---|
|GET|`/productos`|Consultar catálogo, buscar y filtrar|
|GET|`/productos/{id}`|Consultar detalle del producto|

### Justificación

GET `/productos`: Se utiliza GET porque únicamente recupera información. La búsqueda, los filtros y la paginación se integran mediante parámetros de consulta (`query parameters`), evitando crear endpoints separados para cada filtro.

Ejemplo:

```
GET /api/v1/productos?busqueda=honda&categoria=motocicletas&precioMin=20000&precioMax=80000&page=0&size=12
```

Este único endpoint cubre consulta de catálogo, búsqueda por nombre, marca o modelo, filtros de categoría y precios y paginación.

GET `/productos/{id}`: Permite recuperar un producto específico, incluyendo descripción, imágenes, marca, modelo, existencias y atributos según su categoría. Se usa GET porque no modifica el producto.

Ejemplo:

```
GET /api/v1/productos/15
```

El backend únicamente mostrará productos activos. Las variantes de talla o color se identificarán mediante su SKU, como establece el RF06.

Especificacion de requerimientos.md

## 4. Carrito de compras (RF07–RF09)

El carrito pertenece al cliente autenticado y debe conservarse aunque cierre sesión.

|Método|Endpoint|Propósito|
|---|---|---|
|GET|`/carrito`|Consultar carrito|
|POST|`/carrito/items`|Agregar producto|
|PUT|`/carrito/items/{skuId}`|Actualizar cantidad|
|DELETE|`/carrito/items/{skuId}`|Eliminar producto|
|DELETE|`/carrito`|Vaciar carrito|

### Justificación

GET `/carrito`: Recupera las líneas guardadas, cantidades, precios vigentes, subtotales y total del cliente autenticado.

POST `/carrito/items`: Agrega un SKU al carrito. POST se utiliza porque modifica la colección de artículos; si el SKU ya está incluido, el servidor acumula la cantidad solicitada.

PUT `/carrito/items/{skuId}`: Reemplaza la cantidad del artículo por una nueva cantidad. Se utiliza PUT porque establece el estado deseado de ese recurso.

DELETE `/carrito/items/{skuId}`: Elimina exclusivamente el artículo indicado, sin afectar los demás.

DELETE `/carrito`: Vacía el carrito completo. No elimina la cuenta ni el carrito persistente; únicamente sus artículos.

Ejemplo de agregar un artículo:

```
POST /api/v1/carrito/items
Content-Type: application/json

{
  "skuId": 12,
  "cantidad": 2
}
```

Ejemplo de modificación:

```
PUT /api/v1/carrito/items/12
Content-Type: application/json

{
  "cantidad": 3
}
```

Decisión importante: No se utiliza `/carrito/{usuarioId}` porque el cliente no debe poder elegir de quién es el carrito. El backend identifica al propietario a partir de la sesión.

Además, agregar artículos al carrito no reserva inventario, de acuerdo con RN04.

## 5. Proceso de compra (RF10–RF12)

Este módulo permite revisar el pedido, confirmar la compra y ejecutar el pago simulado.

|Método|Endpoint|Propósito|
|---|---|---|
|POST|`/checkout/resumen`|Validar y revisar compra|
|POST|`/pedidos`|Confirmar compra simulada|

### Justificación

POST `/checkout/resumen`: Recibe los datos de entrega y valida que estén completos, comprueba precios y existencias y devuelve un resumen actualizado.

Aunque este endpoint no guarda un pedido, se propone POST porque recibe una estructura de datos que debe validarse y procesarse. Es una operación de cálculo y validación sin efectos persistentes.

POST `/pedidos`: Se utiliza POST porque, cuando la operación es aprobada, crea un nuevo pedido. También realiza la simulación del pago y coordina la actualización del inventario.

Ejemplo:

```
POST /api/v1/pedidos
Content-Type: application/json
Idempotency-Key: compra-a82d91

{
  "destinatario": "Carlos Pérez",
  "telefono": "2281234567",
  "direccion": {
    "calle": "Av. Principal 100",
    "localidad": "Xalapa",
    "estado": "Veracruz",
    "codigoPostal": "91000"
  },
  "pagoSimulado": "APROBADO"
}
```

La cabecera `Idempotency-Key` es una propuesta técnica para implementar RF12. Si el usuario hace doble clic en confirmar, las solicitudes con la misma clave deben producir como máximo un pedido y devolver el mismo resultado.

Para cumplir RF11, la operación de confirmación deberá ejecutarse dentro de una transacción de base de datos:

Flujo de confirmación

Recibir confirmación

Validar sesión, precios y stock

Simular pago

Aprobado

Crear pedido, descontar stock y vaciar carrito

Rechazado o error

No confirmar pedido ni cambiar inventario o carrito

La validación del stock debe hacerse nuevamente al confirmar, incluso si ya se revisó el resumen. El backend debe controlar la concurrencia para evitar que dos clientes compren la última unidad.

## 6. Historial de pedidos (RF13–RF14)

|Método|Endpoint|Propósito|
|---|---|---|
|GET|`/pedidos`|Consultar historial|
|GET|`/pedidos/{id}`|Consultar detalle de pedido|

### Justificación

GET `/pedidos`: Recupera los pedidos del cliente autenticado, ordenados del más reciente al más antiguo. Se utiliza GET porque es una consulta y no modifica ningún recurso.

GET `/pedidos/{id}`: Recupera la información histórica de un pedido específico, incluyendo artículos, precios al momento de comprar, total y dirección de entrega.

Por ejemplo:

```
GET /api/v1/pedidos/25
```

No se propone un endpoint para cancelar pedidos ni actualizar su estado, porque esas operaciones están fuera del alcance actual. La especificación únicamente contempla el estado `Confirmado`.

## 7. Correspondencia con los requisitos funcionales

|Requisito|Endpoint que lo implementa|
|---|---|
|RF01 Registro|`POST /auth/register`|
|RF02 Inicio de sesión|`POST /auth/login`|
|RF03 Cierre de sesión|`POST /auth/logout`|
|RF04 Catálogo|`GET /productos`|
|RF05 Búsqueda y filtros|`GET /productos`|
|RF06 Detalle producto|`GET /productos/{id}`|
|RF07 Agregar carrito|`POST /carrito/items`|
|RF08 Modificar carrito|`GET`, `PUT`, `DELETE /carrito...`|
|RF09 Persistencia carrito|Módulo carrito + base de datos|
|RF10 Revisar compra|`POST /checkout/resumen`|
|RF11 Confirmar compra|`POST /pedidos`|
|RF12 Evitar duplicados|`POST /pedidos` + clave de idempotencia|
|RF13 Historial|`GET /pedidos`|
|RF14 Detalle pedido|`GET /pedidos/{id}`|

Todos los requisitos funcionales quedan cubiertos. Sin embargo, algunos requieren lógica adicional en los servicios y repositorios, no solamente definir una ruta HTTP.

## 8. Distribución recomendada en Spring Boot

Para mantener una arquitectura organizada, los 16 endpoints pueden distribuirse en cinco controladores:

|Controlador|Endpoints|Responsabilidad|
|---|---|---|
|`AuthController`|3|Registro, login y logout|
|`ProductoController`|2|Catálogo, búsqueda y detalle|
|`CarritoController`|5|Gestión del carrito persistente|
|`CheckoutController`|1|Revisión y validación previa|
|`PedidoController`|3|Confirmación, historial y detalle|
|Total|14||

Corrección del conteo: la propuesta detallada contiene 14 endpoints, no 16: seis GET, cinco POST, un PUT y dos DELETE. Son suficientes para cubrir los 14 requisitos funcionales.

Cada controlador debería delegar la lógica a su correspondiente servicio (`AuthService`, `ProductoService`, `CarritoService`, `CheckoutService` y `PedidoService`). Los servicios utilizarán repositorios JPA para acceder a MySQL.

## 9. Recomendaciones para la implementación

Es importante definir desde el principio algunos comportamientos comunes:

- Autorización: los endpoints de autenticación y catálogo serán públicos. Carrito, checkout y pedidos requerirán una sesión válida.
    
- Respuestas HTTP: usar `200 OK` para consultas, `201 Created` para nuevos recursos, `400 Bad Request` para entradas inválidas, `401 Unauthorized` para ausencia de sesión, `404 Not Found` para recursos inexistentes y `409 Conflict` para conflictos como correo duplicado o stock insuficiente.
    
- Validación: utilizar `@Valid` y anotaciones de Jakarta Validation en los DTO de Spring Boot.
    
- Integridad: utilizar `@Transactional` durante la confirmación y mecanismos de bloqueo o actualización condicional para evitar ventas por encima de las existencias.
    
- Seguridad: usar BCrypt para contraseñas, cookies de sesión seguras y protección CSRF cuando se autentique mediante cookies.
    
- Persistencia: almacenar usuarios, productos, variantes SKU, carritos, pedidos y detalles de pedidos en MySQL.
    

## 10. Conclusión

Recomiendo comenzar con estos 14 endpoints REST, distribuidos en los cinco módulos anteriores. La propuesta mantiene el alcance original de Motostock: una tienda orientada exclusivamente a clientes, sin funciones administrativas, cobros reales o gestión de envíos.

La elección de cada método HTTP responde a la operación que realiza: GET para consultar, POST para crear o procesar, PUT para reemplazar cantidades y DELETE para eliminar artículos. Esto permitirá implementar una API coherente, fácil de consumir mediante JavaScript y razonablemente sencilla de explicar en la defensa técnica del proyecto.

Como siguiente paso, convendría definir los DTO de entrada y salida de cada endpoint, así como las entidades de MySQL y sus relaciones. Esto permitirá pasar del diseño de la API a una implementación concreta en Spring Boot.

---

## Observaciones adicionales

- Se tomó como referencia la especificación de requerimientos de Motostock, que contempla únicamente las funciones de compra para clientes y excluye el módulo administrativo.
- La respuesta fue utilizada para definir una propuesta de API, no se ha implementado codigo aun en el aplicativo.