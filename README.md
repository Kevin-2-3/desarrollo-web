# Motostock

Motostock es una aplicación web de venta de motocicletas, accesorios y refacciones. Permite consultar el catálogo, revisar variantes y disponibilidad, mantener un carrito por cliente y realizar compras simuladas con consulta de historial.

El proyecto utiliza un backend Spring Boot con una API HTTP de diseño REST, respuestas JSON y persistencia en PostgreSQL mediante Spring Data JPA e Hibernate. La autenticación se realiza con sesiones del servidor y contraseñas protegidas con BCrypt.
## Tecnologías

| Componente | Tecnología |
|---|---|
| Entorno de referencia | Ubuntu 24.04 dentro de WSL |
| Lenguaje del backend | Java 21 |
| Framework | Spring Boot; versión fijada en backend/pom.xml |
| Dependencias y compilación | Maven |
| Acceso a datos | Spring Data JPA e Hibernate |
| Base de datos | PostgreSQL |
| Contraseñas | BCrypt mediante spring-security-crypto |
| Validaciones | Jakarta Bean Validation |
| Frontend previsto | HTML, CSS y JavaScript |
| Pruebas manuales | CURL, jq y psql |

## Estructura del repositorio

```text
desarrollo-web/
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/mx/uv/motostock/
│       │   │   ├── MotostockApplication.java
│       │   │   ├── modelo/
│       │   │   ├── repositorio/
│       │   │   ├── comun/
│       │   │   ├── seguridad/
│       │   │   ├── usuario/
│       │   │   ├── catalogo/
│       │   │   ├── carrito/
│       │   │   └── pedido/
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── static/sin-imagen.svg
│       │       └── templates/
│       └── test/java/mx/uv/motostock/
├── frontend/
│   ├── index.html
│   ├── css/
│   └── js/
├── database/
│   ├── preparar.sql
│   ├── esquema.sql
│   └── datos-prueba.sql
├── documentacion/
│   ├── Analisis  y diseño/
│   ├── Entrega 2/
│   ├── IAG/
│   └── Reportes/
├── .gitignore
└── README.md
```

- **modelo:** entidades que representan las tablas y sus relaciones.
- **repositorio:** acceso a PostgreSQL mediante Spring Data JPA.
- **usuario, catalogo, carrito y pedido:** controladores HTTP y servicios con reglas de negocio por módulo.
- **seguridad:** comprobación de sesión y token CSRF.
- **comun:** manejo de errores y respuestas HTTP.
- **templates y test:** espacios reservados; esta entrega responde JSON y se valida manualmente.
- **database:** preparación del usuario/base, creación de tablas y ejemplos, en ese orden.
- **documentacion:** requisitos, diseño, responsabilidades, guías y evidencias del equipo.

El frontend y los documentos de contexto se conservan del repositorio original. Las solicitudes siguen el recorrido controlador → servicio → repositorio → PostgreSQL.

## Instalación y primera ejecución en WSL

### 1. Requisitos previos

Se requiere WSL con Ubuntu ya instalado, conexión a internet y un usuario Linux con permisos sudo. Todos los comandos siguientes se ejecutan manualmente en la terminal de Ubuntu dentro de WSL, no en PowerShell, CMD ni Git Bash.

### 2. Instalar herramientas

```bash
sudo apt update
sudo apt install openjdk-21-jdk maven postgresql postgresql-contrib curl git jq
java -version
mvn -version
psql --version
```

Comprobar que Java y la JVM mostrada por Maven correspondan a Java 21.

### 3. Clonar el proyecto

```bash
mkdir -p ~/proyectos
cd ~/proyectos
git clone https://github.com/Kevin-2-3/desarrollo-web.git
cd desarrollo-web
ls backend/pom.xml database/preparar.sql database/esquema.sql database/datos-prueba.sql
```

La rama clonada debe contener los archivos del backend y la base de datos de la entrega 2. Si faltan, obtener la rama correspondiente o integrar primero los paquetes de la entrega; no continuar con una copia incompleta. Si el repositorio ya existe en la computadora, entrar a su raíz en lugar de clonarlo nuevamente.

### 4. Iniciar PostgreSQL y preparar la base

```bash
sudo service postgresql start
sudo -u postgres psql -X -d postgres -f - < database/preparar.sql
```

preparar.sql crea el usuario motostock_app, solicita establecer y confirmar su contraseña y crea la base motostock con ese usuario como propietario. La contraseña no se guarda en el archivo SQL. sudo puede solicitar primero la contraseña del usuario Linux; no confundirla con la que se está definiendo para PostgreSQL.

La redirección < permite leer el archivo con los permisos del usuario de WSL antes de ejecutar psql como postgres. -X evita cargar configuraciones personales de psql.

Ejecutar preparar.sql solo una vez en un entorno donde todavía no existen ese usuario y esa base. Si ya existen, conservarlos y utilizar su configuración. Si el proceso quedó incompleto, revisar qué se creó antes de repetirlo. No usar --single-transaction, porque CREATE DATABASE debe ejecutarse fuera de una transacción.

### 5. Crear las tablas y cargar ejemplos

Desde la raíz del proyecto, ejecutar cada comando e introducir la contraseña de motostock_app:

```bash
psql -h localhost -U motostock_app -d motostock -W -v ON_ERROR_STOP=1 -f database/esquema.sql
psql -h localhost -U motostock_app -d motostock -W -v ON_ERROR_STOP=1 -f database/datos-prueba.sql
```

Estos archivos se cargan una sola vez en una base vacía. Si un paso falla, revisar el error antes de continuar. Los ejemplos incluyen tres productos de diferentes categorías y cuatro SKU. Las cuentas de clientes se crean mediante la API.

### 6. Configurar la conexión

En la misma terminal:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/motostock'
export DB_USER='motostock_app'
read -rsp 'Contraseña de motostock_app: ' DB_PASSWORD
printf '\n'
export DB_PASSWORD
```

| Variable | Descripción |
|---|---|
| DB_URL | Dirección JDBC de PostgreSQL. |
| DB_USER | Usuario de PostgreSQL propietario de la base del proyecto. |
| DB_PASSWORD | Contraseña establecida al ejecutar preparar.sql. |

La aplicación lee las variables mediante application.properties. No escribir la contraseña en el repositorio. Hibernate tiene ddl-auto=validate: valida las tablas existentes y no crea ni modifica el esquema.

### 7. Compilar y ejecutar

```bash
cd backend
mvn clean package
```

Continuar únicamente si Maven indica BUILD SUCCESS:

```bash
mvn spring-boot:run
```

La primera compilación descarga dependencias. Mantener esta terminal abierta mientras se prueba la aplicación. La URL base de la API es http://localhost:8080/api/v1. Para detener el backend, presionar Ctrl+C.

## Comprobación inicial con CURL

Abrir otra terminal de la misma distribución WSL:

```bash
cd ~/proyectos/desarrollo-web
export API='http://localhost:8080/api/v1'
umask 077
mkdir -p .cookies
curl -i "$API/productos"
```

Esperado: HTTP 200 y JSON con el catálogo. API evita repetir la URL; umask 077 restringe los permisos de archivos nuevos; .cookies conserva temporalmente las sesiones de prueba; -i muestra encabezados y cuerpo de la respuesta.

Para registrar un cliente de prueba, obtener primero el token CSRF:

```bash
TOKEN=$(curl -fsS -c .cookies/cliente.txt "$API/auth/csrf" | jq -er '.token')
curl -i -b .cookies/cliente.txt \
  -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' \
  -d '{"nombre":"Cliente de prueba","correo":"prueba@example.test","password":"Prueba123!","confirmacionPassword":"Prueba123!"}' \
  "$API/auth/register"
```

Esperado: 201. Si el correo ya existe, responde 409; no es necesario registrarlo nuevamente.

Iniciar sesión guardando la cookie actualizada:

```bash
curl -i -b .cookies/cliente.txt -c .cookies/cliente.txt \
  -H "X-CSRF-TOKEN: $TOKEN" -H 'Content-Type: application/json' \
  -d '{"correo":"prueba@example.test","password":"Prueba123!"}' \
  "$API/auth/login"
curl -i -b .cookies/cliente.txt "$API/carrito"
```

Esperados: login 200 y carrito 200. No enviar contraseña, cookie ni token CSRF como evidencia pública.

## Endpoints

Todas las rutas siguientes utilizan el prefijo /api/v1.

| Método | Ruta | Función |
|---|---|---|
| GET | /auth/csrf | Obtener token CSRF y preparar la cookie de sesión. |
| POST | /auth/register | Registrar cliente. |
| POST | /auth/login | Iniciar sesión. |
| POST | /auth/logout | Cerrar sesión. |
| GET | /productos | Consultar catálogo con filtros y paginación. |
| GET | /productos/{id} | Consultar detalle del producto. |
| GET | /carrito | Consultar carrito propio. |
| POST | /carrito/items | Agregar SKU y acumular cantidad. |
| PUT | /carrito/items/{skuId} | Reemplazar cantidad de una línea. |
| DELETE | /carrito/items/{skuId} | Eliminar una línea. |
| DELETE | /carrito | Vaciar el carrito. |
| POST | /checkout/resumen | Revisar compra y datos de entrega. |
| POST | /pedidos | Confirmar compra simulada. |
| GET | /pedidos | Consultar historial propio. |
| GET | /pedidos/{id} | Consultar detalle de un pedido propio. |

El catálogo acepta busqueda, categoria, precioMin, precioMax, page y size. Las categorías son MOTO, ACCESORIO y REFACCION; page inicia en 0 y size vale 12 por defecto.

Carrito, checkout, pedidos y logout requieren sesión. Todos los POST, PUT y DELETE, incluidos registro y login, requieren cookie y X-CSRF-TOKEN. El registro no inicia sesión automáticamente. El cliente de las operaciones privadas se obtiene de la sesión, no de un identificador enviado en el cuerpo.

## Equipo

| Integrante | Responsabilidad principal |
|---|---|
| Kevin Pérez | Autenticación, usuarios y configuración inicial del backend. |
| Emiliano | Catálogo de productos y variantes. |
| Erik | Carrito persistente. |
| Kevin Cravioto | Compras simuladas, inventario e historial. |

