# Endpoints REST — Motostock

## Descripción

La API REST de Motostock permite gestionar la autenticación de clientes, consulta de productos, carrito de compras y pedidos.

**URL base:** `/api/v1`

## 1. Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/auth/register` | Registrar un nuevo cliente. |
| POST | `/auth/login` | Autenticar al cliente e iniciar sesión. |
| POST | `/auth/logout` | Cerrar e invalidar la sesión activa. |

## 2. Catálogo de productos

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/productos` | Consultar el catálogo con búsqueda, filtros y paginación. |
| GET | `/productos/{id}` | Obtener información detallada de un producto. |

**Parámetros de consulta:** `busqueda`, `categoria`, `precioMin`, `precioMax`, `page`, `size`.

## 3. Carrito de compras

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/carrito` | Consultar los productos del carrito. |
| POST | `/carrito/items` | Agregar un SKU al carrito. |
| PUT | `/carrito/items/{skuId}` | Actualizar la cantidad de un producto. |
| DELETE | `/carrito/items/{skuId}` | Eliminar un producto del carrito. |
| DELETE | `/carrito` | Vaciar el carrito completamente. |

## 4. Proceso de compra

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/checkout/resumen` | Validar existencias, precios y datos de entrega. |
| POST | `/pedidos` | Confirmar la compra simulada y generar el pedido. |

La confirmación utiliza una clave de idempotencia para evitar pedidos duplicados. Cuando el pago simulado es aprobado, se registra el pedido, se actualiza el inventario y se vacía el carrito mediante una transacción.

## 5. Historial de pedidos

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/pedidos` | Consultar el historial de compras del cliente. |
| GET | `/pedidos/{id}` | Consultar los detalles de un pedido específico. |

## Consideraciones generales

- **GET:** Consultar información.
- **POST:** Crear recursos o procesar solicitudes.
- **PUT:** Actualizar la cantidad de un artículo.
- **DELETE:** Eliminar artículos del carrito.
- Los endpoints de autenticación y catálogo son públicos, excepto el cierre de sesión, que requiere una sesión activa.
- Los endpoints de carrito y pedidos requieren autenticación.
- El backend valida los datos, precios, existencias y permisos.
- Los datos se almacenan mediante Spring Data JPA/Hibernate y Postgresql.

**Total: 14 endpoints REST**, distribuidos en 5 módulos funcionales y orientados a cubrir los requisitos RF01–RF14 de la app.