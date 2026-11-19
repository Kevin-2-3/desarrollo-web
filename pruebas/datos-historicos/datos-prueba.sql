-- Solo para desarrollo; ejecutar una vez después de esquema.sql.
BEGIN;
INSERT INTO marca(nombre) VALUES ('Honda'),('LS2');
INSERT INTO producto(id_marca,nombre,descripcion,tipo_producto) VALUES
 ((SELECT id_marca FROM marca WHERE nombre='Honda'),'CB190R','Motocicleta de prueba','MOTO'),
 ((SELECT id_marca FROM marca WHERE nombre='LS2'),'Casco Stream','Casco integral de prueba','ACCESORIO'),
 ((SELECT id_marca FROM marca WHERE nombre='Honda'),'Filtro de aceite','Refacción de prueba','REFACCION');
INSERT INTO ficha_moto SELECT id_producto,2026,190,'Monocilíndrico','Disco',12.00 FROM producto WHERE nombre='CB190R';
INSERT INTO ficha_accesorio SELECT id_producto,'Protección' FROM producto WHERE nombre='Casco Stream';
INSERT INTO ficha_refaccion SELECT id_producto,'Consultar compatibilidad con CB190R' FROM producto WHERE nombre='Filtro de aceite';
INSERT INTO sku(id_producto,codigo_sku,talla,color,precio_mxn,stock) VALUES
 ((SELECT id_producto FROM producto WHERE nombre='CB190R'),'HONDA-CB190R-ROJO',NULL,'Rojo',65000.00,2),
 ((SELECT id_producto FROM producto WHERE nombre='Casco Stream'),'LS2-STREAM-M','M','Negro',1500.00,5),
 ((SELECT id_producto FROM producto WHERE nombre='Casco Stream'),'LS2-STREAM-L','L','Negro',1550.00,1),
 ((SELECT id_producto FROM producto WHERE nombre='Filtro de aceite'),'HONDA-FILTRO',NULL,NULL,180.00,10);
COMMIT;
