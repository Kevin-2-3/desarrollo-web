# Distribución de responsabilidades — Motostock

## 1. Asignaciones principales

| Integrante | Módulo asignado | Responsabilidad principal |
|---|---|---|
| **Kevin Pérez** | Autenticación y usuarios | Registro, inicio y cierre de sesión, seguridad y control de acceso. |
| **Emiliano** | Catálogo de productos | Visualización del catálogo, búsqueda, filtros y detalle de productos. |
| **Erik** | Carrito de compras | Agregar, modificar, eliminar y conservar productos en el carrito. |
| **Kevin Cravioto** | Compras y pedidos | Revisión y confirmación de compras, actualización de inventario, historial y detalle de pedidos. |

## 2. Responsabilidades por integrante

### Kevin Pérez — Autenticación y usuarios

**Requisitos:** RF01, RF02, RF03; RNF01 y RNF02.

- Desarrollar los formularios de registro e inicio de sesión.
- Validar el correo electrónico y la contraseña.
- Almacenar las contraseñas de forma segura mediante BCrypt.
- Implementar la creación e invalidación de sesiones.
- Proteger los endpoints privados y verificar el acceso de los clientes.
- Implementar las entidades y repositorios relacionados con los usuarios.

### Emiliano — Catálogo y productos

**Requisitos:** RF04, RF05 y RF06.

- Desarrollar la página principal y el catálogo de motocicletas, accesorios y refacciones.
- Implementar la búsqueda por nombre, marca o modelo.
- Implementar los filtros por categoría y rango de precio.
- Incorporar la paginación de 12 productos por página.
- Desarrollar la vista de detalle con descripción, imágenes y disponibilidad.
- Implementar las entidades y repositorios de productos y sus variantes (SKU).

### Erik — Carrito de compras

**Requisitos:** RF07, RF08 y RF09.

- Desarrollar la interfaz del carrito.
- Implementar la incorporación de artículos y la acumulación de cantidades.
- Permitir modificar cantidades, eliminar artículos y vaciar el carrito.
- Calcular subtotales y total desde el backend.
- Conservar el carrito de cada cliente en la base de datos.
- Validar cantidades y disponibilidad de los productos.

### Kevin Cravioto — Compras e historial

**Requisitos:** RF10, RF11, RF12, RF13 y RF14.

- Desarrollar la pantalla de revisión de compra.
- Implementar la captura y validación de los datos de entrega.
- Incorporar el pago simulado con resultado aprobado o rechazado.
- Generar pedidos y folios únicos.
- Descontar existencias y vaciar el carrito mediante una transacción cuando corresponda.
- Evitar compras duplicadas mediante una clave de idempotencia.
- Implementar el historial de compras y la consulta del detalle de cada pedido.

## 3. Responsabilidades compartidas

| Actividad                                        | Responsable principal  | Participación del equipo                                          |
| ------------------------------------------------ | ---------------------- | ----------------------------------------------------------------- |
| Diseño inicial de la base de datos               | Erik y Kevin Cravioto  | Todos revisan entidades, relaciones y restricciones.              |
| Configuración inicial de Spring Boot y seguridad | Kevin Pérez            | Todos configuran y verifican su entorno WSL.                      |
| Integración entre frontend y backend             | Emiliano               | Cada integrante conecta y prueba su módulo.                       |
| Integración de compras, carrito e inventario     | Kevin Cravioto         | Erik y Emiliano apoyan con sus módulos.                           |
| Organización de GitHub, ramas e integración      | Kevin Pérez y Emiliano | Todos realizan commits, revisiones e integraciones periódicas.    |
| Pruebas funcionales y pruebas con IA             | Cada integrante        | Todos prueban y documentan sus funcionalidades.                   |
| README y documentación final                     | Kevin Pérez coordina   | Todos aportan instrucciones, evidencias y explicaciones técnicas. |
| Video de presentación y defensa técnica          | Todos                  | Cada integrante presenta y explica su módulo.                     |
