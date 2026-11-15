# Erik — comprobaciones manuales

## Sesión para el módulo

Completar primero `00-preparacion.md`. En la segunda terminal, ejecutar uno por uno:

```bash
COOKIE='.cookies/erik.txt'
```

```bash
TOKEN=$(curl -fsS -c "$COOKIE" "$API/auth/csrf" | jq -er '.token')
```

```bash
curl -i -b "$COOKIE" -c "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"nombre":"Cliente erik","correo":"erik-e2@example.test","password":"Prueba123!","confirmacionPassword":"Prueba123!"}' "$API/auth/register"
```

Esperado: 201 en el primer registro; 409 si ya existe. El registro no inicia sesión.

```bash
curl -i -b "$COOKIE" -c "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"correo":"erik-e2@example.test","password":"Prueba123!"}' "$API/auth/login"
```

Esperado: 200. Conservar cookie y token de esta misma sesión.

## 1. Agregar, reemplazar y eliminar

Revisar que el SKU 2 tenga cinco unidades y precio 1500. Vaciar únicamente el carrito de esta cuenta:

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -X DELETE "$API/carrito"
```

Esperado: 204.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"skuId":2,"cantidad":1}' "$API/carrito/items"
```

Repetir ese comando una segunda vez. Esperado: una sola línea con cantidad 2 y total 3000.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -X PUT -d '{"cantidad":3}' "$API/carrito/items/2"
```

Esperado: cantidad 3, no 5; total 4500.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -X PUT -d '{"cantidad":6}' "$API/carrito/items/2"
```

Esperado: 409 si el stock sigue en cinco. La cantidad previa permanece.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -X PUT -d '{"cantidad":0}' "$API/carrito/items/2"
```

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -X PUT -d '{"cantidad":1.5}' "$API/carrito/items/2"
```

Esperado: 400 en ambos casos.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"skuId":4,"cantidad":1}' "$API/carrito/items"
```

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -X DELETE "$API/carrito/items/2"
```

Esperado: 204 y la línea del SKU 4 permanece. Repetir DELETE del SKU 2: sigue respondiendo 204.

```bash
curl -i -b "$COOKIE" "$API/carrito"
```

Comprobar que solo queda el SKU 4. Vaciar después el carrito y consultar nuevamente: items vacío y total cero.

## 2. Variantes, propiedad y persistencia

Agregar una unidad del SKU 2 con el comando anterior. Consultar el carrito y revisar skuId, nombre, cantidad, precioUnitarioMxn y subtotalMxn. Abrir otra terminal y seguir la preparación de sesión con otra cuenta, por ejemplo cambiando erik por erik_otro en cookie y correo. No reutilizar el token del primer cliente.

Consultar el carrito de la segunda cuenta: debe estar vacío. Intentar PUT sobre el SKU 2: 404 si no tiene esa línea. Eliminarlo desde la segunda cuenta no debe cambiar el carrito de Erik. Comprobarlo consultando ambos carritos.

En la cuenta de Erik, cerrar sesión:

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -X POST "$API/auth/logout"
```

Obtener otro token, volver a iniciar sesión y consultar el carrito. Esperado: conserva la línea. Para probar persistencia, detener Spring Boot con Ctrl+C en la primera terminal, volver a ejecutar `mvn -f backend/pom.xml spring-boot:run -Dspring-boot.run.arguments=--server.port=8081` en esa misma terminal, obtener un token nuevo e iniciar sesión otra vez. La línea debe seguir presente. No volver a cargar el SQL.

## 3. SKU inexistente y carrito vacío

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"skuId":99999999,"cantidad":1}' "$API/carrito/items"
```

Esperado: 404 si el SKU no existe.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -X DELETE "$API/carrito"
```

```bash
curl -i -b "$COOKIE" "$API/carrito"
```

Esperado: 204 al vaciar y 200 con items vacío y total cero al consultar. Solicitar un resumen con los datos de entrega de la guía de compras debe responder 409 por carrito vacío.

Esta versión no devuelve productoId, codigoSku, talla, color, stock ni disponible en la vista del carrito. Si hace falta comprobar qué variante representa un SKU, consultarlo en PostgreSQL. No se exige la validación directa del identificador no positivo añadida en la entrega 3. Los resultados manuales quedan pendientes.
