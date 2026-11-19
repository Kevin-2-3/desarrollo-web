-- Ejecutar después de esquema.sql o sobre una base Motostock ya preparada.
-- Los precios y las especificaciones de origen proceden del catálogo suministrado.
-- Los años documentados y los de demostración se distinguen en documentacion/Catalogo-fuentes.md.
-- Solo los SKU nuevos reciben cinco unidades de demostración.
BEGIN;

DO $$
DECLARE
    articulo record;
    producto_id bigint;
    marca_id bigint;
    codigo_base text;
    codigo_anterior text;
    codigo text;
    talla_base text;
    color_base text;
BEGIN
    FOR articulo IN
        SELECT *
        FROM (VALUES
            -- motos
            ('honda_cb190r', 'Honda', 'CB190R', 'MOTO', 63500, 'Deportiva', 184, 'Monocilíndrico, 4 tiempos', 'Disco lobulado', 12, 2025, 'Deportiva ligera de 184 cc para recorridos cotidianos.'),
            ('honda_cbr650r', 'Honda', 'CBR650R', 'MOTO', 224900, 'Deportiva', 649, '4 cilindros en línea', 'Doble disco ABS', 15.4, 2024, 'Deportiva de 649 cc con motor de cuatro cilindros en línea.'),
            ('honda_cbr1000rr', 'Honda', 'CBR1000RR-R', 'MOTO', 465000, 'Deportiva', 999, '4 cilindros, Fireblade', 'Disco doble Brembo ABS', 16.1, 2020, 'Deportiva Fireblade de 999 cc con frenos delanteros de doble disco.'),
            ('honda_cargo150', 'Honda', 'GL150 Cargo', 'MOTO', 32990, 'Trabajo', 149, 'Monocilíndrico, 4 tiempos', 'Tambor', 14.3, 2026, 'Moto de trabajo de 149 cc para trayectos y tareas diarias.'),
            ('honda_twister125', 'Honda', 'CBF125 Twister', 'MOTO', 34990, 'Trabajo', 124, 'Monocilíndrico, 4 tiempos', 'Disco/Tambor', 10.1, 2026, 'Moto de trabajo de 124 cc con motor monocilíndrico de cuatro tiempos.'),
            ('honda_xr150l', 'Honda', 'XR150L', 'MOTO', 49990, 'Trabajo', 149, 'Monocilíndrico, 4 tiempos', 'Disco/Tambor', 12, 2025, 'Moto de doble propósito de 149 cc para ciudad y caminos sin pavimentar.'),
            ('honda_navi', 'Honda', 'NAVi 110', 'MOTO', 31490, 'Urbana', 109, 'Monocilíndrico, Automática', 'Tambor', 3.5, 2026, 'Moto urbana de 109 cc con transmisión automática.'),
            ('honda_xr190l', 'Honda', 'XR190L', 'MOTO', 62990, 'Urbana', 184, 'Monocilíndrico, 4 tiempos', 'Disco/Tambor', 12, 2025, 'Moto de doble propósito de 184 cc para pavimento y caminos sin asfaltar.'),
            ('honda_pcx160', 'Honda', 'PCX 160', 'MOTO', 75990, 'Urbana', 156, 'Monocilíndrico, Smart Power+', 'Disco ABS', 8.1, 2025, 'Scooter urbano de 156 cc con motor monocilíndrico.'),
            ('honda_cb300f', 'Honda', 'CB300F Twister', 'MOTO', 105990, 'Deportiva', 293, 'Monocilíndrico, 4 válvulas', 'Disco ABS', 14.1, 2025, 'Moto de estilo deportivo con motor monocilíndrico de 293 cc.'),
            ('honda_xre300', 'Honda', 'XRE300', 'MOTO', 110900, 'Urbana', 291, 'Monocilíndrico, DOHC', 'Disco doble ABS', 13.8, 2025, 'Moto de doble propósito de 291 cc con motor DOHC.'),
            ('honda_rebel500', 'Honda', 'Rebel 500', 'MOTO', 169900, 'Urbana', 471, 'Bicilíndrico en paralelo', 'Disco sencillo ABS', 11.2, 2025, 'Moto tipo cruiser de 471 cc con motor bicilíndrico en paralelo.'),
            ('honda_africatwin', 'Honda', 'Africa Twin 1100', 'MOTO', 345000, 'Urbana', 1084, 'Bicilíndrico paralelo, 4T', 'Doble disco flotante ABS', 24.8, 2020, 'Moto de aventura de 1,084 cc para viajes por carretera y fuera de ella.'),
            ('yamaha_r3', 'Yamaha', 'YZF-R3', 'MOTO', 164999, 'Deportiva', 321, '2 cilindros, 4 tiempos', 'Disco ABS', 14, 2025, 'Deportiva de 321 cc con motor bicilíndrico de cuatro tiempos.'),
            ('yamaha_r1', 'Yamaha', 'YZF-R1', 'MOTO', 419999, 'Deportiva', 998, '4 cilindros, crossplane', 'Doble disco ABS', 17, 2026, 'Deportiva de 998 cc con motor de cuatro cilindros y cigüeñal crossplane.'),
            ('yamaha_ybr125', 'Yamaha', 'YBR125 ZR', 'MOTO', 41999, 'Trabajo', 124, 'Monocilíndrico, 4 tiempos', 'Disco/Tambor', 13, 2025, 'Moto de trabajo de 124 cc con freno delantero de disco.'),
            ('yamaha_crux', 'Yamaha', 'Crux RE', 'MOTO', 31999, 'Trabajo', 110, 'Monocilíndrico, 4 tiempos', 'Tambor mecánico', 11, 2025, 'Moto de trabajo de 110 cc con frenos de tambor.'),
            ('yamaha_mt15', 'Yamaha', 'MT-15', 'MOTO', 84999, 'Deportiva', 155, 'Monocilíndrico, VVA', 'Disco ABS delantero', 10, 2025, 'Moto urbana de estilo deportivo con motor de 155 cc y tecnología VVA.'),
            ('yamaha_mt09', 'Yamaha', 'MT-09', 'MOTO', 299999, 'Deportiva', 890, '3 cilindros, cp3', 'Doble disco ABS', 14, 2025, 'Moto de calle de 890 cc con motor tricilíndrico CP3.'),
            ('yamaha_nmax', 'Yamaha', 'NMAX 155', 'MOTO', 89999, 'Urbana', 155, 'Monocilíndrico, VVA', 'Disco ABS en ambas', 7.1, 2025, 'Scooter urbano de 155 cc con motor VVA.'),
            ('yamaha_terene700', 'Yamaha', 'Tenere 700', 'MOTO', 299999, 'Urbana', 689, '2 cilindros, cp2 crossplane', 'Doble disco ABS', 16, 2026, 'Moto de aventura de 689 cc con motor bicilíndrico CP2.'),
            ('suzuki_gixxer250sf', 'Suzuki', 'Gixxer 250 SF', 'MOTO', 89990, 'Deportiva', 249, 'Monocilíndrico, SOCS (aceite)', 'Disco ABS canales dobles', 12, 2025, 'Deportiva de 249 cc con motor monocilíndrico refrigerado por aceite.'),
            ('suzuki_gsxr1000', 'Suzuki', 'GSX-R1000', 'MOTO', 344900, 'Deportiva', 999, '4 cilindros en línea', 'Doble disco Brembo ABS', 16, 2025, 'Deportiva de 999 cc con cuatro cilindros en línea.'),
            ('suzuki_hayabusa', 'Suzuki', 'Hayabusa 1300', 'MOTO', 424900, 'Deportiva', 1340, '4 cilindros, 4 tiempos', 'Doble disco Brembo ABS', 20, 2025, 'Deportiva de 1,340 cc con motor de cuatro cilindros.'),
            ('suzuki_ax100', 'Suzuki', 'AX100', 'MOTO', 23990, 'Trabajo', 98, 'Monocilíndrico, 2 tiempos', 'Tambor', 12, 2025, 'Moto de trabajo de 98 cc con motor de dos tiempos.'),
            ('suzuki_gn125', 'Suzuki', 'GN125', 'MOTO', 38990, 'Trabajo', 124, 'Monocilíndrico, 4 tiempos', 'Disco delantero', 10.3, 2025, 'Moto de trabajo de 124 cc con freno delantero de disco.'),
            ('suzuki_vstrom250', 'Suzuki', 'V-Strom 250SX', 'MOTO', 104990, 'Urbana', 249, 'Monocilíndrico, SOCS', 'Disco ABS', 12, 2025, 'Moto de aventura ligera de 249 cc con motor monocilíndrico.'),
            ('suzuki_vstrom650', 'Suzuki', 'V-Strom 650', 'MOTO', 201900, 'Urbana', 645, 'V-Twin a 90 grados', 'Doble disco ABS', 20, 2024, 'Moto de turismo y aventura de 645 cc con motor V-Twin.'),
            ('suzuki_gsx8s', 'Suzuki', 'GSX-8S', 'MOTO', 214900, 'Urbana', 776, 'Bicilíndrico en línea', 'Doble disco radial ABS', 14, 2025, 'Moto de calle de 776 cc con motor bicilíndrico en paralelo.'),
            ('kawasaki_zx6r', 'Kawasaki', 'Ninja ZX-6R', 'MOTO', 299990, 'Deportiva', 636, '4 cilindros en línea', 'Doble disco radial ABS', 17, 2025, 'Deportiva de 636 cc con motor de cuatro cilindros en línea.'),
            ('kawasaki_zx10r', 'Kawasaki', 'Ninja ZX-10R', 'MOTO', 449990, 'Deportiva', 998, '4 cilindros en línea, DOHC', 'Doble disco Brembo ABS', 17, 2025, 'Deportiva de 998 cc con motor de cuatro cilindros en línea.'),
            ('kawasaki_kx250', 'Kawasaki', 'KX250', 'MOTO', 189990, 'Urbana', 249, 'Monocilíndrico, 4 tiempos', 'Disco sencillo', 6.2, 2026, 'Moto de motocross de 249 cc con motor monocilíndrico.'),
            ('kawasaki_zh2', 'Kawasaki', 'Z H2', 'MOTO', 499990, 'Urbana', 998, '4 cilindros con Supercargador', 'Doble disco Brembo Stylema', 19, 2025, 'Moto de calle de 998 cc con motor de cuatro cilindros sobrealimentado.'),
            ('kawasaki_z900', 'Kawasaki', 'Z900', 'MOTO', 269990, 'Urbana', 948, '4 cilindros en línea', 'Doble disco de 4 pistones', 17, 2026, 'Moto de calle de 948 cc con motor de cuatro cilindros en línea.'),
            ('kawasaki_versys650', 'Kawasaki', 'Versys 650', 'MOTO', 224990, 'Urbana', 649, 'Bicilíndrico, 4 tiempos', 'Doble disco ABS', 21, 2025, 'Moto de turismo de 649 cc con motor bicilíndrico.'),
            ('kawasaki_vulcans', 'Kawasaki', 'Vulcan S 650', 'MOTO', 199990, 'Urbana', 649, 'Bicilíndrico en paralelo', 'Disco sencillo ABS', 14, 2026, 'Moto tipo cruiser con motor bicilíndrico de 649 cc.'),
            ('kawasaki_klr650', 'Kawasaki', 'KLR 650', 'MOTO', 194990, 'Urbana', 652, 'Monocilíndrico, 4 tiempos', 'Disco doble ABS desactivable', 23, 2026, 'Moto de doble propósito de 652 cc con motor monocilíndrico.'),
            -- accesorios
            ('casco_shaft', 'Shaft', 'Casco Integral Shaft', 'ACCESORIO', 3500, 'Protección', NULL, NULL, NULL, NULL, NULL, 'Casco de cobertura integral para motociclista.'),
            ('guantes_piel', 'Sin especificar', 'Guantes de Piel', 'ACCESORIO', 690, 'Protección', NULL, NULL, NULL, NULL, NULL, 'Guantes de piel para cubrir las manos durante la conducción.'),
            ('slider', 'Sin especificar', 'Slider de proteccion', 'ACCESORIO', 1200, 'Protección', NULL, NULL, NULL, NULL, NULL, 'Protector lateral para la motocicleta.'),
            ('funda', 'Sin especificar', 'Funda Impermeable para Moto', 'ACCESORIO', 550, 'Accesorios', NULL, NULL, NULL, NULL, NULL, 'Funda impermeable para cubrir la motocicleta cuando está estacionada.'),
            ('celular', 'Sin especificar', 'Soporte para Celular', 'ACCESORIO', 890, 'Tecnología', NULL, NULL, NULL, NULL, NULL, 'Soporte para colocar el celular en la motocicleta.'),
            ('chaqueta', 'Sin especificar', 'Chaqueta De Piel', 'ACCESORIO', 2900, 'Protección', NULL, NULL, NULL, NULL, NULL, 'Chaqueta de piel para motociclista.'),
            ('maleta', 'Sin especificar', 'Maleta Trasera', 'ACCESORIO', 2100, 'Carga', NULL, NULL, NULL, NULL, NULL, 'Maleta para transportar objetos en la parte trasera de la motocicleta.'),
            ('candado', 'Sin especificar', 'Candado De Disco Con Alarma', 'ACCESORIO', 800, 'Seguridad', NULL, NULL, NULL, NULL, NULL, 'Candado para disco de freno con alarma.'),
            -- refacciones
            ('balatas_honda', 'Honda', 'Balatas Delanteras Orgánicas', 'REFACCION', 450, 'CB190R / GL150', NULL, NULL, NULL, NULL, NULL, 'Juego de balatas orgánicas para el freno delantero.'),
            ('filtro_yamaha', 'Yamaha', 'Filtro de Aceite Original', 'REFACCION', 290, 'R3 / MT-03', NULL, NULL, NULL, NULL, NULL, 'Filtro de aceite para el mantenimiento del motor.'),
            ('bujia_ngk', 'Suzuki', 'Bujía de Iridium NGK', 'REFACCION', 320, 'Gixxer 150', NULL, NULL, NULL, NULL, NULL, 'Bujía NGK con electrodo de iridio.'),
            ('kit_arrastre', 'Honda', 'Kit de Arrastre', 'REFACCION', 1850, 'CB190R', NULL, NULL, NULL, NULL, NULL, 'Juego de componentes para renovar la transmisión por cadena.'),
            ('manijas', 'Yamaha', 'Manijas De Freno', 'REFACCION', 420, 'MT-03 / R3', NULL, NULL, NULL, NULL, NULL, 'Manijas de reemplazo para accionar el freno.'),
            ('filtro_aire', 'Kawasaki', 'Filtro de Aire de Alto Flujo', 'REFACCION', 980, 'Ninja 400', NULL, NULL, NULL, NULL, NULL, 'Filtro de alto flujo para la entrada de aire del motor.'),
            ('empaque', 'Suzuki', 'Empaque de Tapa de Punterías', 'REFACCION', 210, 'Gixxer 150', NULL, NULL, NULL, NULL, NULL, 'Empaque para sellar la tapa de punterías.'),
            ('chicote', 'Honda', 'Cable de Embrague Original', 'REFACCION', 380, 'CB190R', NULL, NULL, NULL, NULL, NULL, 'Cable de reemplazo para accionar el embrague.')
        ) AS catalogo(archivo, marca, nombre, categoria, precio, tipo_o_compatibilidad,
                     cilindrada, motor, frenos, tanque, anio_referencia, descripcion)
    LOOP
        INSERT INTO marca (nombre) VALUES (articulo.marca)
        ON CONFLICT (nombre) DO NOTHING;

        SELECT id_marca INTO marca_id FROM marca WHERE nombre = articulo.marca;

        codigo_base := upper(replace(articulo.archivo, '_', '-'));
        codigo_anterior := CASE articulo.archivo
            WHEN 'honda_cb190r' THEN 'HONDA-CB190R-ROJO'
            ELSE 'DEMO-' || codigo_base
        END;
        talla_base := CASE WHEN articulo.archivo IN ('casco_shaft', 'guantes_piel', 'chaqueta', 'funda') THEN 'M' END;
        color_base := CASE
            WHEN articulo.archivo = 'honda_cb190r' THEN 'Rojo'
            WHEN articulo.categoria = 'MOTO' THEN 'Negro'
        END;
        codigo := CASE
            WHEN talla_base IS NOT NULL THEN codigo_base || '-' || talla_base
            WHEN color_base IS NOT NULL THEN codigo_base || '-' || upper(color_base)
            ELSE codigo_base
        END;

        SELECT s.id_producto INTO producto_id FROM sku s
        WHERE s.codigo_sku IN (codigo, codigo_anterior)
        ORDER BY (s.codigo_sku = codigo) DESC LIMIT 1;
        IF producto_id IS NULL THEN
            SELECT p.id_producto INTO producto_id
            FROM producto p
            WHERE p.id_marca = marca_id AND lower(p.nombre) = lower(articulo.nombre)
                AND p.tipo_producto = articulo.categoria
            ORDER BY p.id_producto LIMIT 1;
        END IF;

        IF producto_id IS NULL THEN
            INSERT INTO producto (id_marca, nombre, tipo_producto)
            VALUES (marca_id, articulo.nombre, articulo.categoria)
            RETURNING id_producto INTO producto_id;
        END IF;

        UPDATE producto
        SET id_marca = marca_id, nombre = articulo.nombre,
            descripcion = articulo.descripcion
        WHERE id_producto = producto_id;

        CASE articulo.categoria
            WHEN 'MOTO' THEN
                INSERT INTO ficha_moto (id_producto, anio, cilindrada_cc, motor, frenos, tanque_litros)
                VALUES (producto_id, articulo.anio_referencia, articulo.cilindrada, articulo.motor, articulo.frenos, articulo.tanque)
                ON CONFLICT (id_producto) DO UPDATE
                SET anio = COALESCE(EXCLUDED.anio, ficha_moto.anio),
                    cilindrada_cc = EXCLUDED.cilindrada_cc, motor = EXCLUDED.motor,
                    frenos = EXCLUDED.frenos, tanque_litros = EXCLUDED.tanque_litros;
            WHEN 'ACCESORIO' THEN
                INSERT INTO ficha_accesorio (id_producto, tipo_uso)
                VALUES (producto_id, articulo.tipo_o_compatibilidad)
                ON CONFLICT (id_producto) DO UPDATE SET tipo_uso = EXCLUDED.tipo_uso;
            WHEN 'REFACCION' THEN
                INSERT INTO ficha_refaccion (id_producto, compatibilidad)
                VALUES (producto_id, articulo.tipo_o_compatibilidad)
                ON CONFLICT (id_producto) DO UPDATE SET compatibilidad = EXCLUDED.compatibilidad;
        END CASE;

        -- Renombra el SKU existente para conservar su ID, inventario y referencias.
        UPDATE sku SET codigo_sku = codigo
        WHERE id_producto = producto_id AND codigo_sku = codigo_anterior AND codigo_anterior <> codigo;

        INSERT INTO sku (id_producto, codigo_sku, talla, color, precio_mxn, stock)
        VALUES (producto_id, codigo, talla_base, color_base, articulo.precio, 5)
        ON CONFLICT (codigo_sku) DO UPDATE
        SET talla = COALESCE(sku.talla, EXCLUDED.talla),
            color = COALESCE(sku.color, EXCLUDED.color),
            precio_mxn = EXCLUDED.precio_mxn;

        -- Las tallas y los colores adicionales son datos para la demostración.
        INSERT INTO sku (id_producto, codigo_sku, talla, color, precio_mxn, stock)
        SELECT producto_id, codigo_base || '-' || variante.sufijo,
               variante.talla, variante.color, articulo.precio, 5
        FROM (VALUES
            ('L', 'L', NULL, talla_base IS NOT NULL),
            ('XL', 'XL', NULL, articulo.archivo IN ('casco_shaft', 'guantes_piel', 'chaqueta')),
            ('NEGRO', NULL, 'Negro', articulo.archivo = 'honda_cb190r'),
            ('BLANCO', NULL, 'Blanco', articulo.categoria = 'MOTO' AND articulo.archivo <> 'honda_cb190r')
        ) AS variante(sufijo, talla, color, aplica)
        WHERE variante.aplica
        ON CONFLICT (codigo_sku) DO UPDATE SET precio_mxn = EXCLUDED.precio_mxn;

        INSERT INTO producto_imagen (id_producto, imagen_url, orden)
        SELECT producto_id, '/img/productos/' || articulo.archivo || '.webp', 0
        WHERE NOT EXISTS (SELECT FROM producto_imagen i WHERE i.id_producto = producto_id);
    END LOOP;
END;
$$;

UPDATE producto_imagen
SET imagen_url = '/img/productos/honda_cb190r.webp'
WHERE imagen_url = '/img/cb190r.webp';

DELETE FROM producto_imagen
WHERE imagen_url IN ('/img/casco-stream.webp', '/img/filtro-aceite.webp');

-- Retira los SKU de muestra y conserva las referencias de pedidos y carritos.
UPDATE sku
SET activo = false
WHERE activo AND codigo_sku IN ('LS2-STREAM-M', 'LS2-STREAM-L', 'HONDA-FILTRO');

UPDATE producto p
SET activo = false
WHERE p.activo
  AND EXISTS (SELECT FROM sku s WHERE s.id_producto = p.id_producto
              AND s.codigo_sku IN ('LS2-STREAM-M', 'LS2-STREAM-L', 'HONDA-FILTRO'))
  AND NOT EXISTS (SELECT FROM sku s WHERE s.id_producto = p.id_producto AND s.activo);

-- El filtro de prueba puede salir de la base si nadie usó su SKU.
DO $$
DECLARE
    filtro_id bigint;
BEGIN
    SELECT p.id_producto INTO filtro_id
    FROM producto p JOIN sku s USING (id_producto)
    WHERE s.codigo_sku = 'HONDA-FILTRO' AND NOT p.activo
      AND NOT EXISTS (SELECT FROM sku otra WHERE otra.id_producto = p.id_producto AND otra.id_sku <> s.id_sku)
      AND NOT EXISTS (SELECT FROM producto_imagen i WHERE i.id_producto = p.id_producto)
      AND NOT EXISTS (SELECT FROM pedido_detalle d WHERE d.id_sku = s.id_sku)
      AND NOT EXISTS (SELECT FROM carrito_item c WHERE c.id_sku = s.id_sku);

    IF filtro_id IS NOT NULL THEN
        DELETE FROM sku WHERE id_producto = filtro_id;
        DELETE FROM ficha_refaccion WHERE id_producto = filtro_id;
        DELETE FROM producto WHERE id_producto = filtro_id;
    END IF;
END;
$$;

COMMIT;
