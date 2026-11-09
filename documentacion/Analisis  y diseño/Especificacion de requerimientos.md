# Especificación de requerimientos

*Tienda web de motocicletas accesorios y refacciones: Motostock

## 1 Propósito y contexto

El sistema permitirá consultar productos de una tienda de motocicletas y realizar un proceso de compra desde el navegador. El recorrido principal será registrarse, iniciar sesión, buscar un producto, agregarlo al carrito, confirmar una compra y consultar el historial.

### Problemática y objetivo

Como planteamiento inicial, la información dispersa sobre productos y la consulta manual de precios y disponibilidad dificultan la selección y el seguimiento de las compras.

### Alcance

Se incluyen cuentas de clientes, sesiones, catálogo de motocicletas, accesorios y refacciones, búsqueda y filtros básicos, detalle de productos, carrito, confirmación de compra e historial personal. El inventario se utiliza para validar las compras.

Quedan fuera el módulo administrativo, los roles de vendedor y administrador, los cobros reales, el financiamiento, la facturación fiscal, las devoluciones, la integración con paqueterías, el taller y las cotizaciones.

### Actores y permisos

- **Visitante.** Consultar catálogo, buscar, filtrar, abrir productos, registrarse e iniciar sesión.

- **Cliente autenticado.** Todas las consultas públicas, administrar su carrito, confirmar compras, consultar sus propios pedidos y cerrar sesión.

## 2 Requisitos funcionales

### RF01 Registro de cliente

El sistema deberá registrar nombre, correo electrónico y contraseña. El correo será único, sin distinguir mayúsculas, y la contraseña tendrá al menos 8 caracteres. La confirmación de contraseña se usará solo para validar el formulario.

**Criterio de aceptación:** Un registro válido crea una sola cuenta. Si el correo ya existe, el formato es incorrecto o las contraseñas no coinciden, se informa el error y no se guarda la cuenta.

### RF02 Inicio de sesión

El sistema deberá autenticar mediante correo y contraseña y establecer una sesión asociada al cliente.

**Criterio de aceptación:** Con credenciales válidas se habilitan carrito, compra e historial; con credenciales inválidas se muestra un mensaje general y no se crea una sesión.

### RF03 Cierre de sesión

El sistema deberá ofrecer la opción de cerrar sesión e invalidarla en el servidor.

**Criterio de aceptación:** Después de salir, una nueva solicitud a carrito, compra o historial exige autenticación. El carrito guardado permanece para el siguiente inicio de sesión.

### RF04 Consulta del catálogo

El sistema deberá mostrar productos activos con imagen, nombre, categoría, precio en MXN y disponibilidad. La lista tendrá paginación de 12 productos por página.

**Criterio de aceptación:** Se pueden recorrer todas las páginas sin duplicar productos; un catálogo sin registros muestra un mensaje comprensible.

### RF05 Búsqueda y filtros

El sistema deberá buscar por nombre, marca o modelo, sin distinguir mayúsculas, y permitir combinar categoría y rango de precio.

**Criterio de aceptación:** Los resultados cumplen simultáneamente los filtros seleccionados. Si no hay coincidencias, se informa y se permite limpiar los filtros; un rango inválido se rechaza.

### RF06 Detalle del producto

El sistema deberá mostrar descripción, imágenes, precio, marca, modelo y existencias del producto seleccionado. Para motocicletas mostrará año y cilindrada; para accesorios, talla o color cuando aplique; para refacciones, compatibilidad cuando esté registrada.

**Criterio de aceptación:** Se muestra el producto solicitado; un identificador inexistente o un producto inactivo produce un aviso de no disponible. La falta de imagen utiliza un recurso de sustitución.

Los datos de talla y color se tratarán por SKU: cada variante comprable tendrá su propio precio y existencia. La compatibilidad de refacciones será informativa; no se implementará un recomendador automático.

### RF07 Agregar al carrito

El cliente deberá poder agregar un SKU disponible a su carrito. Si ya existe, se acumulará la cantidad. El visitante será dirigido a iniciar sesión antes de agregar.

**Criterio de aceptación:** Una cantidad entera positiva dentro de la existencia disponible se agrega una sola vez. Los productos inactivos, agotados o las cantidades inválidas se rechazan sin alterar el carrito.

### RF08 Consultar y modificar el carrito

El sistema deberá mostrar producto, precio unitario vigente, cantidad, subtotal por línea y total. Permitirá modificar cantidades, eliminar líneas y vaciar el carrito.

**Criterio de aceptación:** Al cambiar una cantidad válida se actualizan los importes; eliminar una línea no modifica las restantes. El carrito vacío muestra total cero y bloquea la confirmación de compra.

### RF09 Conservar el carrito

El sistema deberá guardar un carrito por cliente en la base de datos y recuperarlo cuando vuelva a iniciar sesión.

**Criterio de aceptación:** El contenido persiste después de cerrar sesión o reiniciar el backend. Un cliente no puede consultar ni modificar el carrito de otro.

### RF10 Revisar la compra

Antes de confirmar, el sistema deberá presentar el resumen final y solicitar nombre de destinatario, teléfono y dirección de entrega, incluidos localidad, estado y código postal. No será necesario guardar una libreta de direcciones.

**Criterio de aceptación:** Los datos obligatorios incompletos impiden continuar. Si el precio o la disponibilidad cambió, se actualiza el resumen y se solicita una nueva confirmación antes de generar el pedido.

### RF11 Confirmar compra simulada

El sistema deberá ofrecer un pago simulado con resultado aprobado o rechazado, sin solicitar datos de tarjetas ni contactar servicios bancarios. Ante aprobación, creará pedido y detalles, descontará existencias y vaciará el carrito en una sola transacción.

**Criterio de aceptación:** La aprobación genera un folio y un pedido confirmado. El rechazo o un fallo al guardar no crea un pedido confirmado, no descuenta existencias y conserva el carrito.

### RF12 Evitar compras duplicadas

El sistema deberá impedir que el doble clic o el reenvío de la misma confirmación genere más de un pedido o descuento de inventario.

**Criterio de aceptación:** Dos solicitudes con la misma referencia de confirmación devuelven el mismo resultado y producen, como máximo, un pedido.

### RF13 Consultar historial de compras

El sistema deberá mostrar solo los pedidos del cliente autenticado, ordenados del más reciente al más antiguo, con folio, fecha, total y estado.

**Criterio de aceptación:** Una compra confirmada aparece en el historial. Cuando no existen compras se muestra un mensaje; no se incluyen pedidos de otros clientes.

### RF14 Consultar detalle del pedido

El sistema deberá mostrar los artículos adquiridos, cantidades, precios al comprar, subtotales, total y los datos de entrega capturados.

**Criterio de aceptación:** Cambiar después el precio o la descripción del catálogo no modifica el comprobante histórico. Solicitar un pedido ajeno no revela su contenido.

## 3 Requisitos no funcionales

### RNF01 Seguridad de credenciales

Guardar contraseñas mediante un algoritmo de hash para contraseñas, como BCrypt. Verificar en base de datos que nunca se almacena la contraseña original ni se devuelve en respuestas.

### RNF02 Control de acceso

Validar autenticación y propiedad de recursos en cada operación privada del servidor. Las pruebas con sesión ausente y con un segundo cliente deben impedir acceso y cambios a información ajena.

### RNF03 Validación y errores

Validar campos tanto en cliente como en servidor. Entradas vacías, cantidades negativas o identificadores inválidos generan mensajes claros y respuestas HTTP coherentes, sin trazas internas ni credenciales.

### RNF05 Usabilidad

Formularios con etiquetas, errores junto al campo, botones identificables y confirmación visible de compra. Las operaciones principales deben completarse usando únicamente teclado.

### RNF08 Integridad y persistencia

Cuentas, productos, carritos y pedidos sobreviven al reinicio del backend. Probar concurrencia sobre la última unidad y provocar un fallo de confirmación para comprobar la reversión completa.

### RNF09 Compatibilidad

Verificar el recorrido principal en las versiones estables de Chrome y Edge disponibles al realizar la prueba, registrando sus versiones.

### RNF10 Mantenibilidad y reproducción

Separar presentación, lógica de negocio y acceso a datos. El README debe permitir que otro integrante clone, configure y ejecute el sistema en un entorno WSL limpio sin pasos implícitos.

## 4 Reglas de negocio

- **RN01.** Solo los clientes autenticados pueden mantener carrito, comprar o consultar sus pedidos.

- **RN02.** Los importes se expresan en MXN con dos decimales y se calculan en el servidor con tipos decimales; no se confía en precios enviados por el navegador.

- **RN03.** Subtotal = precio unitario × cantidad. Total = suma de subtotales. Para el prototipo, los precios son finales y no se agregan descuentos ni gastos de envío.

- **RN04.** Las cantidades son enteras positivas y las existencias nunca pueden ser negativas. Agregar al carrito no reserva inventario.

- **RN05.** La existencia se verifica al confirmar. Si dos clientes compran la última unidad, solo uno obtiene la compra; el otro recibe un aviso y conserva su carrito.

- **RN06.** Un fallo en cualquier parte de la confirmación revierte pedido, detalles y descuento de existencias. No se permiten compras parciales.

- **RN07.** El pedido conserva una copia de los datos del producto, precio y entrega utilizados al confirmar.

- **RN08.** Cada pedido confirmado tiene un folio único, fecha y cliente. La interfaz de esta versión solo utiliza el estado Confirmado; no simula envíos ni entregas.

- **RN09.** La dirección capturada representa una entrega simulada. Comprar una motocicleta no realiza trámites, registro vehicular ni entrega legal de la unidad.
