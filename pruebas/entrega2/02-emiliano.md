# Emiliano — comprobaciones manuales

## 1. Nombre, marca y filtros

```bash
curl -i "$API/productos?busqueda=cb190r&categoria=MOTO&precioMin=60000&precioMax=70000"
```

Esperado: 200 y la CB190R de los datos iniciales. El nombre representa también el modelo comercial según las fuentes.

```bash
curl -i "$API/productos?busqueda=HONDA"
```

Esperado: 200 con productos de esa marca, sin distinguir mayúsculas.

```bash
curl -i "$API/productos?precioMin=2&precioMax=1"
```

Esperado: 400.

```bash
curl -i "$API/productos?categoria=OTRO"
```

Esperado: 400.

```bash
curl -i "$API/productos?page=-1"
```

Esperado: 400.

## 2. Rango de una misma variante

Verificar primero los precios del casco en PostgreSQL:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c "SELECT codigo_sku,precio_mxn FROM sku WHERE codigo_sku LIKE 'LS2-STREAM-%' ORDER BY id_sku;"
```

Con precios 1500 y 1550:

```bash
curl -i "$API/productos?busqueda=LS2&precioMin=1510&precioMax=1540"
```

Esperado: 200 y totalElements=0.

```bash
curl -i "$API/productos?busqueda=LS2&precioMin=1510&precioMax=1550"
```

Esperado: 200 con el casco. Una misma talla debe cumplir ambos límites.

## 3. Paginación, detalle e inactividad

Para demostrar dos páginas hacen falta al menos 13 productos activos. En la base de pruebas, entrar a psql:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W
```

Ejecutar este bloque SQL una sola vez; prepara datos y no comprueba respuestas:

```sql
BEGIN;
INSERT INTO producto(id_marca,nombre,tipo_producto,activo)
SELECT (SELECT id_marca FROM marca WHERE nombre='LS2'),'PaginacionE2-'||n,'ACCESORIO',true FROM generate_series(1,13) AS n;
INSERT INTO ficha_accesorio SELECT id_producto,'Prueba' FROM producto WHERE nombre LIKE 'PaginacionE2-%';
INSERT INTO sku(id_producto,codigo_sku,precio_mxn,stock,activo)
SELECT id_producto,'PAG-E2-'||id_producto,100.00,10,true FROM producto WHERE nombre LIKE 'PaginacionE2-%';
COMMIT;
```

Salir con `\q` y consultar cada página:

```bash
curl -i "$API/productos?busqueda=PaginacionE2-&page=0"
```

```bash
curl -i "$API/productos?busqueda=PaginacionE2-&page=1"
```

Esperado: 12 y 1 productos, totalElements=13 y ningún ID repetido. Anotar un ID y asignarlo manualmente, por ejemplo si la respuesta mostró el 4:

```bash
PRODUCTO_ID=4
```

```bash
curl -i "$API/productos/$PRODUCTO_ID"
```

Esperado: 200 con variantes e imagen de sustitución. Usar el ID realmente recibido, no asumir el ejemplo.

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c "UPDATE producto SET activo=false WHERE id_producto=$PRODUCTO_ID;"
```

```bash
curl -i "$API/productos/$PRODUCTO_ID"
```

Esperado: 404. Restaurar solo ese producto de prueba:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e2 -W -c "UPDATE producto SET activo=true WHERE id_producto=$PRODUCTO_ID;"
```

```bash
curl -i "$API/productos/99999999"
```

Esperado: 404 si ese ID no existe. Registrar cada resultado real.

