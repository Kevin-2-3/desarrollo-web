# Reporte de Avance del Proyecto

**Equipo:** 5  
**Fecha:** 21/11/2026  
**Número de reporte:** 05  
**Repositorio GitHub:** [Kevin-2-3/desarrollo-web](https://github.com/Kevin-2-3/desarrollo-web)

---

# 1. Actividades realizadas

En estos dias se actualizó el readme para instalar Motostock en wsl con el catalogo de postgresql y abir la tienda desde Spring Boot. Tambien probamos los casos de exito y error con curl en wsl y desde microsoft edge y chrome. Guardamos las respuestas y las capturas de cada prueba.

---

# 2. Evidencias de trabajo

| Actividad                        | Integrante responsable | Enlace a commit |
| -------------------------------- | ---------------------- | --------------- |
| Actualización del README | Kevin Cravioto | [5942435](https://github.com/Kevin-2-3/desarrollo-web/commit/5942435534b35e62f0c7a620466dec158c1ac5a9) |
| Casos de prueba en WSL | Erik | [04f31e6](https://github.com/Kevin-2-3/desarrollo-web/commit/04f31e64437fe601995d985fb4b525940d8163ff) |
| Comprobación de la prueba en GUI | Kevin Pérez | [8688b07](https://github.com/Kevin-2-3/desarrollo-web/commit/8688b0755b4f9b08d4a92f16e8fcbc887acf16a6) |


---

# 3. Problemas encontrados

El readme todavía pedía cargar un archivo de datos que habíamos movido a pruebas históricas. Cambiamos esa instrucción para que ahora se cargue `database/catalogo.sql`. 

---

# 4. Avance funcional

La versión integrada ya incluye registro, inicio de sesión, catálogo, variantes, carrito, compra simulada e historial de pedidos. Empaquetamos el proyecto y lo arrancamos en WSL con Java 21 y una base de datos PostgreSQL. Comprobamos que el registro, el inicio de sesión, los filtros, las variantes y el carrito funcionan bien. Cuando el pago se rechaza, el carrito se conserva. Cuando se aprueba, se genera el pedido, el carrito se vacía y las existencias se actualizan.

---

# 5. Uso de IA generativa

- **Herramienta utilizada:** Claude.
- **Actividad para la que se utilizó:** para actualizar la descripción del readme.
- **Código o contenido generado:** actualización del readme.
- **Modificaciones realizadas por el equipo:** Cambios pequeños a el md generado.
- **Forma en que se verificó el resultado:** comparamos las rutas que menciona el readme con los archivos reales del repositorio

---

# 6. Participación individual

- **Integrante 1:** Kevin Pérez. Instalación en WSL, registro e inicio de sesión.
- **Integrante 2:** Emiliano García. Datos de entrega, resumen, pago simulado e historial.
- **Integrante 3:** Erik. Selección de variante y operaciones del carrito.
- **Integrante 4:** Kevin Cravioto. Carga del catálogo en PostgreSQL, filtros y ficha de producto.

---

# 7. Próximas actividades

- Revisar la guía de instalación desde otro entorno WSL.
- Revisar las respuestas de CURL antes de integrar las evidencias.
- Integrar los reportes IAG y registrar los enlaces a los commits de cierre.
- Registrar el enlace al release V5 al publicarlo.
- Realizar el documento final de intergracion de evidencias y el video explicativo.

---

## Observaciones adicionales

El registro de pruebas reune los resultados y las capturas de la versión. 
