# Preparación manual en WSL — entrega 3

Ejecutar cada comando por separado desde Ubuntu/WSL, leyendo el resultado antes de continuar. Los comandos suponen el usuario PostgreSQL motostock_app configurado según el README.

## 1. Base exclusiva para las comprobaciones

Desde la raíz del repositorio, iniciar PostgreSQL:

```bash
sudo service postgresql start
```

Crear una base nueva de pruebas una sola vez. Si ya existe, revisar su contenido antes de reutilizarla; no repetir el esquema sobre tablas existentes.

```bash
sudo -u postgres createdb -O motostock_app motostock_pruebas_e3
```

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e3 -W -v ON_ERROR_STOP=1 -f database/esquema.sql
```

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e3 -W -v ON_ERROR_STOP=1 -f database/datos-prueba.sql
```

Esperado: ambos archivos terminan sin errores. No usar una base con información real para los casos de fallos e inventario.

## 2. Arrancar el backend en una primera terminal

```bash
export DB_URL='jdbc:postgresql://localhost:5432/motostock_pruebas_e3'
```

```bash
export DB_USER='motostock_app'
```

```bash
read -rsp 'Contraseña PostgreSQL: ' DB_PASSWORD
```

```bash
export DB_PASSWORD
```

```bash
mvn -f backend/pom.xml clean package
```

Esperado: BUILD SUCCESS. Solo después:

```bash
mvn -f backend/pom.xml spring-boot:run
```

Esperado: arranque en el puerto 8080. Dejar esta terminal abierta.

## 3. Preparar una segunda terminal

Entrar a la raíz del mismo repositorio y definir:

```bash
export API='http://localhost:8080/api/v1'
```

```bash
umask 077
```

```bash
mkdir -p .cookies
```

```bash
curl -i "$API/productos"
```

Esperado: HTTP 200 con JSON. Si falla, revisar el arranque antes de seguir.

Para consultas SQL, cada comando de las guías pide la contraseña de PostgreSQL mediante -W. No guardarla en un reporte. Tampoco publicar cookies ni tokens CSRF.

## 4. Trabajo por integrante

Seguir la guía del módulo, comando por comando. Al abrir otra terminal hay que volver a definir API y las variables de cookie/token. Los ejemplos utilizan los SKU 2, 3 y 4 del conjunto inicial; verificar sus valores antes de cada caso:

```bash
psql -h localhost -U motostock_app -d motostock_pruebas_e3 -W -c 'SELECT id_sku,codigo_sku,precio_mxn,stock FROM sku ORDER BY id_sku;'
```

Si los identificadores o existencias cambiaron, ajustar el comando con datos comprobados. No asumir los resultados esperados como si ya hubieran ocurrido. Registrar fecha, comando, estado esperado, estado observado, comprobación SQL cuando corresponda y captura en documentacion/Entrega 3/REGISTRO-EVIDENCIAS.md.
