# Modelo entidad-relación — Motostock

*Tienda web de motocicletas, accesorios y refacciones*

## 1. Universo del discurso

Motostock es una tienda web dedicada a ofrecer motocicletas, accesorios y refacciones. Los visitantes pueden consultar el catálogo, buscar productos y revisar sus características, precios y disponibilidad. Para agregar artículos al carrito y realizar una compra simulada, deben registrarse e iniciar sesión.

Cada producto pertenece a una marca y se clasifica como motocicleta, accesorio o refacción. Todos comparten información general, como nombre y descripción, pero cada categoría cuenta con una ficha que almacena sus características particulares. Los productos pueden tener varias imágenes y una o más variantes de venta (SKU), cada una con su propio precio y existencia.

Los clientes pueden mantener un carrito de compras que se conserva entre sesiones. Al confirmar una compra simulada, el sistema verifica las existencias, registra el pedido y sus artículos, descuenta el inventario y vacía el carrito. La operación debe evitar compras duplicadas y conservar los datos históricos de cada pedido, aunque el catálogo cambie después.

El sistema no incluye pagos bancarios reales, facturación, envíos gestionados por paqueterías ni un módulo administrativo web. La administración técnica de PostgreSQL se aborda por separado como parte del proyecto académico.

## 2. Objetivo del modelo

Definir la estructura de datos que permitirá gestionar el catálogo, las variantes de productos, el inventario, los clientes, sus carritos y las compras simuladas. El modelo busca que las entidades estén relacionadas de forma clara, evitando duplicar información innecesariamente y conservando la integridad de los pedidos.

## 3. Entidades y atributos

**PK:** clave primaria. **FK:** clave foránea. **PK/FK:** atributo que funciona como clave primaria y foránea.

### 3.1 Marca

Registra las marcas de los productos, por ejemplo Honda, Yamaha o LS2. Una marca puede estar asociada con varios productos.

| Atributo | Función |
|---|---|
| `id_marca` (PK) | Identificador de la marca. |
| `nombre` | Nombre de la marca. |

### 3.2 Producto

Contiene la información común de las motocicletas, accesorios y refacciones. El campo `nombre` identifica comercialmente el producto o modelo, por ejemplo «CB190R»; no se necesita un campo `modelo` adicional.

| Atributo | Función |
|---|---|
| `id_producto` (PK) | Identificador del producto. |
| `id_marca` (FK) | Marca a la que pertenece. |
| `nombre` | Nombre comercial del producto. |
| `descripcion` | Información general del producto. |
| `tipo_producto` | Indica si es moto, accesorio o refacción. |
| `activo` | Indica si se muestra como disponible en el catálogo. |

Cada producto se relaciona con su ficha específica, sus imágenes y sus SKU.

### 3.3 Ficha_Moto

Guarda las características técnicas de los productos clasificados como motocicletas.

| Atributo | Función |
|---|---|
| `id_producto` (PK/FK) | Motocicleta a la que pertenece la ficha. |
| `anio` | Año de la motocicleta. |
| `cilindrada_cc` | Cilindrada en centímetros cúbicos. |
| `motor` | Descripción del motor. |
| `frenos` | Características de los frenos. |
| `tanque_litros` | Capacidad del tanque. |

### 3.4 Ficha_Accesorio

Describe la función principal de los accesorios, como protección, carga o seguridad. Las variantes de talla y color se manejan en SKU.

| Atributo | Función |
|---|---|
| `id_producto` (PK/FK) | Accesorio al que pertenece la ficha. |
| `tipo_uso` | Tipo de uso del accesorio. |

### 3.5 Ficha_Refaccion

Contiene la información de compatibilidad de una refacción. La compatibilidad se presenta al usuario como texto informativo.

| Atributo | Función |
|---|---|
| `id_producto` (PK/FK) | Refacción a la que pertenece la ficha. |
| `compatibilidad` | Motocicletas o modelos con los que es compatible. |

### 3.6 Producto_Imagen

Permite asociar varias imágenes con un mismo producto. La base de datos almacena la ruta o URL, no necesariamente el archivo de imagen.

| Atributo | Función |
|---|---|
| `id_imagen` (PK) | Identificador de la imagen. |
| `id_producto` (FK) | Producto al que pertenece. |
| `imagen_url` | Ruta o dirección de la imagen. |
| `orden` | Posición en que se mostrará en la galería. |

### 3.7 SKU

Representa cada variante que puede comprarse. Por ejemplo, un casco talla M y otro talla L son SKU distintos, aunque correspondan al mismo producto.

| Atributo | Función |
|---|---|
| `id_sku` (PK) | Identificador de la variante. |
| `id_producto` (FK) | Producto al que pertenece. |
| `codigo_sku` | Código único de la variante. |
| `talla` | Talla, cuando corresponda. |
| `color` | Color, cuando corresponda. |
| `precio_mxn` | Precio de venta de esa variante. |
| `stock` | Unidades disponibles. |
| `activo` | Indica si la variante puede ofrecerse. |

### 3.8 Cliente

Almacena la información necesaria para registrar e identificar a los usuarios de la tienda.

| Atributo | Función |
|---|---|
| `id_cliente` (PK) | Identificador del cliente. |
| `nombre` | Nombre del cliente. |
| `correo` | Correo electrónico único. |
| `password_hash` | Contraseña almacenada de forma protegida. |

### 3.9 Carrito_Item

Representa los artículos que un cliente mantiene en su carrito. No repite nombres ni precios: estos se obtienen desde SKU y Producto.

| Atributo | Función |
|---|---|
| `id_cliente` (PK/FK) | Cliente propietario del carrito. |
| `id_sku` (PK/FK) | Variante agregada. |
| `cantidad` | Unidades que desea comprar. |

La clave primaria es compuesta: (`id_cliente`, `id_sku`). De esta manera, un mismo SKU aparece una sola vez en el carrito de cada cliente.

### 3.10 Intento_Confirmacion

Permite identificar una solicitud de compra y evitar que, por ejemplo, un doble clic genere dos pedidos.

| Atributo | Función |
|---|---|
| `id_intento` (PK) | Identificador del intento. |
| `id_cliente` (FK) | Cliente que intenta comprar. |
| `referencia_confirmacion` | Referencia para reconocer solicitudes repetidas. |
| `resumen_hash` | Huella del resumen de compra confirmado. |
| `resultado` | Resultado de la confirmación. |
| `fecha_intento` | Fecha y hora del intento. |

### 3.11 Pedido

Guarda los datos generales de una compra simulada confirmada y la información de entrega capturada por el cliente.

| Atributo | Función |
|---|---|
| `id_pedido` (PK) | Identificador del pedido. |
| `id_cliente` (FK) | Cliente que realizó la compra. |
| `id_intento` (FK, único) | Intento de confirmación que generó el pedido. |
| `folio` | Folio único de compra. |
| `fecha` | Fecha y hora de compra. |
| `estado` | Estado del pedido. |
| `total_mxn` | Importe total de la compra. |
| `nombre_destinatario` | Persona que recibiría el pedido. |
| `telefono` | Teléfono de contacto. |
| `direccion` | Calle, número y demás datos de dirección. |
| `localidad` | Localidad de entrega. |
| `estado_entrega` | Estado o entidad federativa de entrega. |
| `codigo_postal` | Código postal. |

### 3.12 Pedido_Detalle

Guarda cada artículo adquirido. Conserva los datos del momento de compra para que el historial no cambie si después se modifica el catálogo.

| Atributo | Función |
|---|---|
| `id_detalle` (PK) | Identificador del detalle. |
| `id_pedido` (FK) | Pedido al que pertenece. |
| `id_sku` (FK) | Variante comprada. |
| `nombre_congelado` | Nombre al momento de la compra. |
| `marca_congelada` | Marca al momento de la compra. |
| `descripcion_congelada` | Descripción al momento de la compra. |
| `codigo_sku_congelado` | Código SKU al momento de la compra. |
| `talla_congelada` | Talla seleccionada, si aplica. |
| `color_congelado` | Color seleccionado, si aplica. |
| `precio_unitario_mxn` | Precio unitario pagado. |
| `cantidad` | Unidades compradas. |
| `subtotal_mxn` | Importe de esa línea. |

## 4. Relaciones entre entidades

| Relación | Cardinalidad | Explicación |
|---|---|---|
| Marca — Producto | 1:N | Una marca tiene muchos productos; cada producto tiene una marca. |
| Producto — Ficha_Moto | 1:1 condicional | Un producto de tipo moto tiene su ficha de motocicleta. |
| Producto — Ficha_Accesorio | 1:1 condicional | Un producto de tipo accesorio tiene su ficha de accesorio. |
| Producto — Ficha_Refaccion | 1:1 condicional | Un producto de tipo refacción tiene su ficha de refacción. |
| Producto — Producto_Imagen | 1:N | Un producto puede tener varias imágenes. |
| Producto — SKU | 1:N | Un producto puede ofrecerse en varias variantes. |
| Cliente — Carrito_Item | 1:N | Un cliente puede agregar varios artículos. |
| SKU — Carrito_Item | 1:N | Un SKU puede estar en carritos de diferentes clientes. |
| Cliente — Intento_Confirmacion | 1:N | Un cliente puede realizar varios intentos de compra. |
| Cliente — Pedido | 1:N | Un cliente puede realizar varios pedidos. |
| Intento_Confirmacion — Pedido | 1:0..1 | Un intento puede generar un pedido o ninguno si no se confirma. |
| Pedido — Pedido_Detalle | 1:N | Un pedido confirmado contiene uno o varios artículos. |
| SKU — Pedido_Detalle | 1:N | Un SKU puede aparecer en distintos pedidos. |

**Relaciones muchos a muchos:** Cliente y SKU se relacionan mediante `Carrito_Item`; Pedido y SKU se relacionan mediante `Pedido_Detalle`.

**Especialización de Producto:** cada producto debe tener únicamente la ficha que corresponda a su `tipo_producto`. Esta regla se detallará mediante restricciones al implementar la base de datos.

## 5. Funcionamiento general de la estructura

1. **Consulta del catálogo:** la web obtiene los datos generales desde Producto y Marca; las características específicas desde la ficha correspondiente; las imágenes desde Producto_Imagen; y los precios y existencias desde SKU.
2. **Carrito:** cuando un cliente agrega una variante, se guarda su SKU y la cantidad en Carrito_Item. El carrito permanece asociado al cliente entre sesiones.
3. **Confirmación:** Intento_Confirmacion identifica la solicitud para evitar procesarla más de una vez. El sistema verifica precios y existencias antes de aceptar la compra simulada.
4. **Compra:** al confirmarse, se crea un Pedido con sus filas de Pedido_Detalle, se descuentan las existencias y se vacía el carrito como parte de una misma transacción.
5. **Historial:** el cliente consulta sus pedidos y sus detalles. Los datos históricos se mantienen aunque cambien los productos actuales.

## 6. Alcance de esta entrega

Este documento presenta **únicamente la estructura inicial del modelo de datos**: universo del discurso, entidades, atributos y relaciones. El diagrama entidad-relación, los tipos de datos definitivos, las restricciones SQL, los scripts DDL/DML y las pruebas de integridad se desarrollarán o ajustarán en las siguientes etapas.

La propuesta contiene **seis entidades operativas principales** (`Cliente`, `Producto`, `SKU`, `Carrito_Item`, `Pedido` y `Pedido_Detalle`) y **seis entidades complementarias** (`Marca`, `Ficha_Moto`, `Ficha_Accesorio`, `Ficha_Refaccion`, `Producto_Imagen` e `Intento_Confirmacion`). La distinción entre principales y complementarias es organizativa; el modelo completo tiene doce tablas propuestas.
