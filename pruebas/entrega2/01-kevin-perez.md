# Kevin Pérez — comprobaciones manuales

## Sesión para el módulo

Completar primero `00-preparacion.md`. En la segunda terminal, ejecutar uno por uno:

```bash
COOKIE='.cookies/kevin_perez.txt'
```

```bash
TOKEN=$(curl -fsS -c "$COOKIE" "$API/auth/csrf" | jq -er '.token')
```

```bash
curl -i -b "$COOKIE" -c "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"nombre":"Cliente kevin_perez","correo":"kevin_perez-e2@example.test","password":"Prueba123!","confirmacionPassword":"Prueba123!"}' "$API/auth/register"
```

Esperado: 201 en el primer registro; 409 si ya existe. El registro no inicia sesión.

```bash
curl -i -b "$COOKIE" -c "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"correo":"kevin_perez-e2@example.test","password":"Prueba123!"}' "$API/auth/login"
```

Esperado: 200. Conservar cookie y token de esta misma sesión.

## 1. Correo único, hash e inicio de sesión

Repetir el registro con el correo en mayúsculas:

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"nombre":"Cliente repetido","correo":"KEVIN_PEREZ-E2@EXAMPLE.TEST","password":"Prueba123!","confirmacionPassword":"Prueba123!"}' "$API/auth/register"
```

Esperado: 409. Consultar la base:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c "SELECT correo,count(*),bool_and(password_hash LIKE '\$2%') AS bcrypt FROM cliente WHERE correo='kevin_perez-e2@example.test' GROUP BY correo;"
```

Esperado: una cuenta con correo en minúsculas y bcrypt=t. El inicio de sesión previo comprueba que la contraseña puede verificarse.

## 2. Sesión, credenciales y CSRF

```bash
curl -i "$API/carrito"
```

Esperado: 401, porque esta solicitud no envía la cookie.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"correo":"kevin_perez-e2@example.test","password":"Incorrecta123!"}' "$API/auth/login"
```

Esperado: 401 y mensaje general. Esto no demuestra que una sesión anterior se cierre: comprueba el rechazo de las credenciales enviadas.

```bash
curl -i -b "$COOKIE" -X DELETE "$API/carrito"
```

Esperado: 403 por falta del token CSRF.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -X POST "$API/auth/logout"
```

Esperado: 204.

```bash
curl -i -b "$COOKIE" "$API/carrito"
```

Esperado: 401. Para seguir, repetir obtención de token e inicio de sesión de la preparación del módulo.

## 3. Validación del registro y JSON

Con una sesión válida, enviar campos inválidos:

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"nombre":"","correo":"mal","password":"x","confirmacionPassword":"x"}' "$API/auth/register"
```

Esperado: 400 con un mensaje general de entrada inválida. Esta versión todavía no devuelve el mapa campos de la entrega 3.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"nombre":"Prueba","correo":"distinto-e2@example.test","password":"Prueba123!","confirmacionPassword":"OtraClave123!"}' "$API/auth/register"
```

Esperado: 400 por contraseñas distintas, sin crear la cuenta.

```bash
curl -i -b "$COOKIE" -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' -d '{"correo":"kevin_perez-e2@example.test","password":"Prueba123!","extra":true}' "$API/auth/login"
```

Esperado: 400 por propiedad no admitida. No se exige aquí el manejo específico de 405 y 415 añadido después. Registrar respuesta real y captura.
