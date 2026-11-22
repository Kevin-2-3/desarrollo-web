# Comprobación de Motostock en WSL — Entrega 5

Pruebas realizadas el  sobre el código de la version V4 con una base PostgreSQL , Java 21 y Spring Boot  La aplicación se ejecutó en WSL Ubuntu.

En la tienda se usaron cuentas de ejemplo con nombres y datos de entrega distintos. Las solicitudes a la API se hicieron con CURL desde WSL, comando por comando. La tienda se comprobó en  Edge y Chrome.

| Caso de uso                         | Pasos                                                                                                                                                       | Resultado observado                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ----------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Instalación limpia                  | Cargar el esquema y el catálogo en una base vacía, empaquetar y arrancar Spring Boot desde WSL.                                                             | Los dos SQL terminaron en `COMMIT`, Maven informó `BUILD SUCCESS` y respondieron la portada y el catálogo. Las capturas muestran la instalación en WSL con [Chrome](capturas/Instalaci%C3%B3n%20WSL%20Chome.png) y [Edge](capturas/Instalaci%C3%B3n%20WSL%20Edge.png).                                                                                                                                                                                                                                                          |
| Registro e inicio de sesión         | Registrar una cuenta. Intentar entrar con una contraseña incorrecta y luego con la correcta.                                                                | CURL devolvió 201, 401 y 200. La tienda mostró [Cuenta creada](capturas/Confirmaci%C3%B3n%20del%20registro.png) y [Credenciales inválidas](capturas/Inicio%20de%20sesi%C3%B3n%20con%20contrase%C3%B1a%20incorrecta.png), y permitió entrar con la contraseña correcta.                                                                                                                                                                                                                                                          |
| Catálogo y variantes                | Buscar cb190r, filtrar por motocicletas y abrir la ficha para elegir una variante.                                                                          | CURL devolvió un producto con dos variantes. En la pagina se obtuvo el [catálogo filtrado](capturas/Cat%C3%A1logo%20filtrado%20por%20modelo%20y%20categor%C3%ADa.png) y la [variante roja](capturas/Ficha%20de%20la%20CB190R%20roja.png) mostró $63,500.00 MXN y 5 unidades disponibles.                                                                                                                                                                                                                                        |
| Gestionar el carrito                | Agregar una variante, actualizar a 2, intentar guardar 6 cuando hay 5 y quitar el artículo.                                                                 | CURL devolvió 409 por existencias insuficientes, conservó la cantidad anterior y devolvió 204 al quitar el artículo. En la pagina mostró [dos unidades por $127,000.00 MXN](capturas/Carrito%20con%20dos%20motocicletas.png), bloqueó la cantidad fuera del máximo y dejó el carrito vacío al quitar el artículo.                                                                                                                                                                                                               |
| Pago simulado rechazado             | Revisar la entrega y confirmar seleccionando Rechazado.                                                                                                     | CURL devolvió RECHAZADO, con el historial vacío y las mismas cantidades y existencias. La interfaz mostró [el rechazo y el aviso de que se conserva el carrito](capturas/Resumen%20de%20compra%20con%20pago%20rechazado.png).                                                                                                                                                                                                                                                                                                   |
| Compra simulada aprobada            | Confirmar el pago, consultar el historial y volver al carrito y a la ficha.                                                                                 | CURL confirmó dos unidades negras y el stock bajó de 5 a 3. En web, el [pedido confirmado](capturas/Detalle%20del%20pedido%20confirmado.png) por $63,500.00 MXN quedó como único pedido en el [historial](capturas/Pedido%20en%20el%20historial.png). El [carrito quedó vacío](capturas/Carrito%20vac%C3%ADo%20despu%C3%A9s%20de%20la%20compra.png) y el stock de la variante roja [bajó de 5 a 4](capturas/Existencias%20de%20la%20CB190R%20roja%20despu%C3%A9s%20de%20la%20compra.png).                                       |
| Cierre de sesión y carrito guardado | Agregar una CB190R negra, cerrar sesión e intentar abrir el carrito y el historial. Volver a iniciar sesión con la misma cuenta.                            | La tienda pidió iniciar sesión al abrir el [carrito](capturas/Carrito%20bloqueado%20despu%C3%A9s%20de%20cerrar%20sesi%C3%B3n.png) y el [historial](capturas/Historial%20bloqueado%20despu%C3%A9s%20de%20cerrar%20sesi%C3%B3n.png). Al volver a entrar, el [carrito conservó una CB190R negra](capturas/Carrito%20recuperado%20al%20iniciar%20sesi%C3%B3n.png).                                                                                                                                                                  |
| Confirmación sin duplicados         | Agregar una unidad, seleccionar el pago aprobado y dar doble clic en Confirmar compra. Revisar el historial y las existencias.                              | La tienda envió una sola solicitud. En el historial apareció [un solo pedido](capturas/Un%20solo%20pedido%20despu%C3%A9s%20del%20doble%20clic.png) y las [existencias bajaron de 5 a 4](capturas/Existencias%20despu%C3%A9s%20de%20una%20sola%20confirmaci%C3%B3n.png).                                                                                                                                                                                                                                                         |
| Validaciones de formularios         | Intentar registrarse con un correo ya usado y con contraseñas distintas. Intentar revisar la compra sin dirección y con un código postal de cuatro dígitos. | La tienda mostró [Correo ya registrado](capturas/Registro%20con%20correo%20ya%20registrado.png) y  la interfaz el registro con [contraseñas distintas](capturas/Registro%20con%20contrase%C3%B1as%20distintas.png). En la compra, Edge mosto "Rellena este campo" para la [dirección vacía](capturas/Formulario%20de%20entrega%20sin%20direcci%C3%B3n.png) y "Hazlo coincidir con el formato solicitado" para el [código postal inválido](capturas/Formulario%20de%20entrega%20con%20c%C3%B3digo%20postal%20inv%C3%A1lido.png). |
| Catálogo sin coincidencias          | Buscar un resultado inexistente y limpiar los filtros.                                                                                                      | El catálogo mostró [Sin resultados](capturas/Cat%C3%A1logo%20sin%20coincidencias.png) y al limpiar los filtros volvió a mostrar los productos.                                                                                                                                                                                                                                                                                                                                                                                  |

## Capturas de la interfaz:

### Instalación limpia en WSL

![Instalación en WSL con Chrome](capturas/Instalaci%C3%B3n%20WSL%20Chome.png)

![Instalación en WSL con Edge](capturas/Instalaci%C3%B3n%20WSL%20Edge.png)

### Catálogo filtrado por modelo y categoría

![Catálogo filtrado por modelo y categoría](capturas/Cat%C3%A1logo%20filtrado%20por%20modelo%20y%20categor%C3%ADa.png)

### Confirmación del registro

![Confirmación del registro](capturas/Confirmaci%C3%B3n%20del%20registro.png)

### Inicio de sesión con contraseña incorrecta

![Inicio de sesión con contraseña incorrecta](capturas/Inicio%20de%20sesi%C3%B3n%20con%20contrase%C3%B1a%20incorrecta.png)

### Ficha de la CB190R roja

![Ficha de la CB190R roja](capturas/Ficha%20de%20la%20CB190R%20roja.png)

### Carrito con dos motocicletas

![Carrito con dos motocicletas](capturas/Carrito%20con%20dos%20motocicletas.png)

### Resumen de compra con pago rechazado

![Resumen de compra con pago rechazado](capturas/Resumen%20de%20compra%20con%20pago%20rechazado.png)

### Detalle del pedido confirmado

![Detalle del pedido confirmado](capturas/Detalle%20del%20pedido%20confirmado.png)

### Pedido en el historial

![Pedido en el historial](capturas/Pedido%20en%20el%20historial.png)

### Carrito vacío después de la compra

![Carrito vacío después de la compra](capturas/Carrito%20vac%C3%ADo%20despu%C3%A9s%20de%20la%20compra.png)

### Existencias de la CB190R roja después de la compra

![Existencias de la CB190R roja después de la compra](capturas/Existencias%20de%20la%20CB190R%20roja%20despu%C3%A9s%20de%20la%20compra.png)

### Carrito bloqueado después de cerrar sesión

![Carrito bloqueado después de cerrar sesión](capturas/Carrito%20bloqueado%20despu%C3%A9s%20de%20cerrar%20sesi%C3%B3n.png)

### Historial bloqueado después de cerrar sesión

![Historial bloqueado después de cerrar sesión](capturas/Historial%20bloqueado%20despu%C3%A9s%20de%20cerrar%20sesi%C3%B3n.png)

### Carrito recuperado al iniciar sesión

![Carrito recuperado al iniciar sesión](capturas/Carrito%20recuperado%20al%20iniciar%20sesi%C3%B3n.png)

### Un solo pedido después del doble clic

![Un solo pedido después del doble clic](capturas/Un%20solo%20pedido%20despu%C3%A9s%20del%20doble%20clic.png)

### Existencias después de una sola confirmación

![Existencias después de una sola confirmación](capturas/Existencias%20despu%C3%A9s%20de%20una%20sola%20confirmaci%C3%B3n.png)

### Registro con correo ya registrado

![Registro con correo ya registrado](capturas/Registro%20con%20correo%20ya%20registrado.png)

### Registro con contraseñas distintas

![Registro con contraseñas distintas](capturas/Registro%20con%20contrase%C3%B1as%20distintas.png)

### Formulario de entrega sin dirección

![Formulario de entrega sin dirección](capturas/Formulario%20de%20entrega%20sin%20direcci%C3%B3n.png)

### Formulario de entrega con código postal inválido

![Formulario de entrega con código postal inválido](capturas/Formulario%20de%20entrega%20con%20c%C3%B3digo%20postal%20inv%C3%A1lido.png)

### Catálogo sin coincidencias

![Catálogo sin coincidencias](capturas/Cat%C3%A1logo%20sin%20coincidencias.png)
