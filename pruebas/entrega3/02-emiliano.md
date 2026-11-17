# Pruebas de Emiliano

Antes de empezar hay que hacer lo de 00-preparacion.md. Para el catálogo no hace falta iniciar sesión porque es público.

## 1. Búsqueda y filtros

```bash
curl -i "$API/productos?busqueda=cb190r&categoria=MOTO&precioMin=60000&precioMax=70000"
```

Esperado: 200 y que salga la CB190R que viene en los datos iniciales.

```bash
curl -i "$API/productos?busqueda=HONDA"
```

Esperado: 200 y que salgan los productos Honda aunque se escriba en mayúsculas.

```bash
curl -i "$API/productos?busqueda=%25"
```

Esperado: 200 pero sin resultados (ningún producto tiene % en el nombre). No debe regresar todo el catálogo.

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

## 2. Rango de precio en una misma variante

Primero revisamos los precios del casco en la base:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e3 -W -c "SELECT codigo_sku,precio_mxn FROM sku WHERE codigo_sku LIKE 'LS2-STREAM-%' ORDER BY id_sku;"
```

Si los precios son 1500 y 1550:

```bash
curl -i "$API/productos?busqueda=LS2&precioMin=1510&precioMax=1540"
```

Esperado: 200 con totalElements=0.

```bash
curl -i "$API/productos?busqueda=LS2&precioMin=1510&precioMax=1550"
```

Esperado: 200 y que salga el casco, porque la talla de 1550 sí entra en el rango.

## 3. Paginación, detalle y productos inactivos

Para probar dos páginas necesitamos mínimo 13 productos activos. Entramos a psql:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e3 -W
```

Corremos esto una sola vez para meter los datos de prueba:

```sql
BEGIN;
INSERT INTO producto(id_marca,nombre,tipo_producto,activo)
SELECT (SELECT id_marca FROM marca WHERE nombre='LS2'),'PaginacionE3-'||n,'ACCESORIO',true FROM generate_series(1,13) AS n;
INSERT INTO ficha_accesorio SELECT id_producto,'Prueba' FROM producto WHERE nombre LIKE 'PaginacionE3-%';
INSERT INTO sku(id_producto,codigo_sku,precio_mxn,stock,activo)
SELECT id_producto,'PAG-E3-'||id_producto,100.00,10,true FROM producto WHERE nombre LIKE 'PaginacionE3-%';
COMMIT;
```

Salimos con `\q` y pedimos las dos páginas:

```bash
curl -i "$API/productos?busqueda=PaginacionE3-&page=0"
```

```bash
curl -i "$API/productos?busqueda=PaginacionE3-&page=1"
```

Esperado: la primera trae 12 y la segunda 1, con totalElements=13 y sin IDs repetidos. Tomamos uno de los IDs que salieron (el 4 es solo ejemplo):

```bash
PRODUCTO_ID=4
```

```bash
curl -i "$API/productos/$PRODUCTO_ID"
```

Esperado: 200 con sus variantes y la imagen por defecto.

Ahora lo desactivamos:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e3 -W -c "UPDATE producto SET activo=false WHERE id_producto=$PRODUCTO_ID;"
```

```bash
curl -i "$API/productos/$PRODUCTO_ID"
```

Esperado: 404. Luego lo volvemos a activar:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e3 -W -c "UPDATE producto SET activo=true WHERE id_producto=$PRODUCTO_ID;"
```

```bash
curl -i "$API/productos/99999999"
```

Esperado: 404 porque ese ID no existe. Anotar lo que salió en cada prueba.
