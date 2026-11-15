# Kevin Cravioto — comprobaciones manuales

## Sesión y datos de entrega

Ejecutar uno por uno:

```bash
COOKIE='.cookies/kevin_cravioto.txt'
```

```bash
TOKEN=$(curl -fsS -c "$COOKIE" "$API/auth/csrf" | jq -er '.token')
```

```bash
curl -i -b "$COOKIE" -c "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"nombre":"Kevin Cravioto QA","correo":"kevin_cravioto-e2@example.test","password":"Prueba123!","confirmacionPassword":"Prueba123!"}' "$API/auth/register"
```

Esperado: 201 la primera vez, 409 si ya existe.

```bash
curl -i -b "$COOKIE" -c "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"correo":"kevin_cravioto-e2@example.test","password":"Prueba123!"}' "$API/auth/login"
```

Esperado: 200.

```bash
ENTREGA='{"nombreDestinatario":"Prueba E2","telefono":"5512345678","direccion":"Calle Prueba 1","localidad":"Xalapa","estadoEntrega":"Veracruz","codigoPostal":"91000"}'
```

## 1. Aprobación, reenvío e histórico

Con el carrito propio vacío, agregar dos unidades del SKU 2, si hay existencias:

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"skuId":2,"cantidad":2}' "$API/carrito/items"
```

Esperado: 200 y cantidad 2. Anotar stock y precio antes de comprar:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c 'SELECT id_sku,precio_mxn,stock FROM sku WHERE id_sku=2;'
```

Solicitar el resumen, guardando el JSON para leer su huella:

```bash
curl -sS -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d "$ENTREGA" -o .cookies/resumen.json -w '\nHTTP %{http_code}\n' "$API/checkout/resumen"
```

Esperado: 200. Revisar el contenido:

```bash
cat .cookies/resumen.json
```

```bash
HASH=$(jq -er '.resumenHash' .cookies/resumen.json)
```

```bash
CONFIRMACION=$(jq -nc --argjson entrega "$ENTREGA" --arg hash "$HASH" '{entrega:$entrega,resumenHash:$hash,referenciaConfirmacion:"e2-compra-01",aprobado:true}')
```

Esta referencia identifica una compra. Usar una nueva para otro caso, pero conservar exactamente la misma para comprobar su reenvío.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d "$CONFIRMACION" "$API/pedidos"
```

Esperado: 200, resultado APROBADO y pedido confirmado. Repetir exactamente ese comando. Comparar manualmente ID, folio y total: deben coincidir. En esta versión la fecha puede diferir únicamente en la precisión de fracciones de segundo entre el primer envío y la lectura desde PostgreSQL; no se exige todavía igualdad exacta del JSON. Anotar el ID real para el detalle:

```bash
PEDIDO_ID=1
```

Sustituir 1 si la respuesta mostró otro ID.

```bash
curl -i -b "$COOKIE" "$API/carrito"
```

```bash
curl -i -b "$COOKIE" "$API/pedidos"
```

```bash
curl -i -b "$COOKIE" "$API/pedidos/$PEDIDO_ID"
```

Esperado: carrito vacío, pedido en el historial y detalle propio accesible.

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c "SELECT stock FROM sku WHERE id_sku=2; SELECT count(*) FROM pedido p JOIN intento_confirmacion i USING(id_intento) WHERE i.referencia_confirmacion='e2-compra-01';"
```

Esperado: stock anterior menos dos y un solo pedido. Si el stock inicial era cinco, debe quedar tres.

Para comprobar el histórico, anotar el precio original, cambiar temporalmente el del catálogo y consultar de nuevo el detalle:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c 'UPDATE sku SET precio_mxn=1600 WHERE id_sku=2;'
```

```bash
curl -i -b "$COOKIE" "$API/pedidos/$PEDIDO_ID"
```

Esperado: el detalle conserva el precio al comprar. Restaurar el precio original; si era 1500:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c 'UPDATE sku SET precio_mxn=1500 WHERE id_sku=2;'
```

Con la cookie y el token de otro cliente, consultar ese mismo ID: debe responder 404. No usar la cookie del propietario para ese caso.

## 2. Rechazo y cambio de precio

Agregar una unidad del SKU 4 y obtener un resumen con los comandos anteriores. Leer el nuevo HASH y preparar:

```bash
CONFIRMACION=$(jq -nc --argjson entrega "$ENTREGA" --arg hash "$HASH" '{entrega:$entrega,resumenHash:$hash,referenciaConfirmacion:"e2-rechazo-01",aprobado:false}')
```

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d "$CONFIRMACION" "$API/pedidos"
```

Esperado: 200 con RECHAZADO; sin pedido nuevo, sin cambio de stock y con el carrito conservado. Comparar con SQL y GET /carrito antes y después.

Para cambio de precio, obtener un resumen nuevo, leer su HASH y preparar una confirmación aprobada con referencia e2-cambio-01. Antes de enviarla, cambiar el precio del SKU 4 de 180 a 181 en la base de pruebas:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c 'UPDATE sku SET precio_mxn=181 WHERE id_sku=4;'
```

Enviar la confirmación preparada antes del cambio. Esperado: 409, sin pedido nuevo y con carrito conservado. Para continuar se necesita obtener y revisar otro resumen. Restaurar el precio original (180 si corresponde) y vaciar solo el carrito propio antes del caso de fallo.

## 3. Última unidad desde dos clientes

Consultar el SKU 3: el conjunto inicial tiene una unidad. No aumentar el stock sin revisar primero lo ocurrido en la base de pruebas.

Preparar dos cuentas distintas siguiendo la sección Sesión, con correos y cookies diferentes, por ejemplo compra_a-e2 y compra_b-e2. En cada terminal definir API, COOKIE, TOKEN y ENTREGA. Agregar una unidad del SKU 3 a cada carrito. Cada cuenta debe obtener su propio resumen y preparar una confirmación aprobada con una referencia distinta.

En cada terminal dejar preparado este comando con la confirmación correspondiente:

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d "$CONFIRMACION" "$API/pedidos"
```

Enviar ambos lo más cerca posible en el tiempo, idealmente con dos personas. Esperado: un 200 y un 409; stock cero, un solo detalle de compra y carrito conservado para quien no pudo comprar.

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c 'SELECT stock FROM sku WHERE id_sku=3; SELECT count(*) FROM pedido_detalle WHERE id_sku=3;'
```

Registrar el conteo antes y después si ya había pedidos de ese SKU. Dos envíos consecutivos comprueban el rechazo por falta de stock, pero no demuestran concurrencia. Si no se puede acreditar que las solicitudes se solaparon, dejar esa parte pendiente.

## 4. Misma referencia desde dos sesiones

Abrir dos sesiones de la misma cuenta, con archivos de cookie y tokens distintos. Preparar una compra nueva con existencias y obtener su resumen. Copiar exactamente el mismo JSON CONFIRMACION a ambas terminales, con una referencia nueva compartida, por ejemplo e2-simultanea-01. Cada terminal debe conservar su propia cookie y token.

Enviar manualmente el POST /pedidos anterior desde ambas terminales. Esperado: ambos 200 con el mismo ID y folio y stock descontado una sola vez. La precisión de la fecha puede variar, como se explicó en el primer caso. Comparar respuestas y consultar el conteo por referencia con SQL. Si se ejecuta de forma consecutiva, registrar solo reenvío; la simultaneidad queda pendiente.

## 5. Reversión ante un fallo y persistencia

Volver a la cuenta kevin_cravioto-e2. Con su carrito vacío, agregar una unidad del SKU 4, obtener resumen y preparar una confirmación aprobada con referencia e2-fallo-01. Anotar el stock inicial.

Entrar a psql de la base de pruebas y ejecutar manualmente:

```sql
CREATE FUNCTION fallo_e2() RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  IF OLD.id_sku=4 THEN RAISE EXCEPTION 'Fallo controlado de entrega 2'; END IF;
  RETURN OLD;
END $$;
```

```sql
CREATE TRIGGER fallo_e2 BEFORE DELETE ON carrito_item FOR EACH ROW EXECUTE FUNCTION fallo_e2();
```

Salir con `\q`. Enviar una vez la confirmación e2-fallo-01 con CURL. Esperado: 500 por el fallo provocado. Volver a psql y retirar el fallo antes de continuar, incluso si la respuesta no fue la esperada:

```sql
DROP TRIGGER fallo_e2 ON carrito_item;
```

```sql
DROP FUNCTION fallo_e2();
```

Consultar el stock, el carrito propio y los registros asociados a esa referencia. No debe existir pedido ni intento parcial, el stock debe seguir igual y el carrito debe conservar la unidad:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c "SELECT stock FROM sku WHERE id_sku=4; SELECT count(*) FROM intento_confirmacion WHERE referencia_confirmacion='e2-fallo-01';"
```

```bash
curl -i -b "$COOKIE" "$API/carrito"
```

Reenviar la misma confirmación una vez retirado el fallo. Esperado: 200, un pedido y descuento único.

Finalmente dejar un artículo en el carrito y anotar un pedido propio. Detener Spring Boot con Ctrl+C en la primera terminal y arrancarlo otra vez con el mismo comando y las mismas variables. Obtener un token nuevo, iniciar sesión y consultar carrito, historial y detalle del pedido. Esperado: cuenta, carrito y pedido conservados. No recargar el esquema ni los datos de prueba.
