# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 20/11/2026  
**Número de reporte:** 04

---

# 1. Breve descripción de uso

Quería mostrar un aviso en el formulario de registro cuando se escribieran dos contraseñas distintas que no coincidieran

---

# 2. Evidencias de uso

| Evidencia                   | Formato                                                                                                                                                                  |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Herramienta                 | Codex                                                                                                                                                                    |
| Modelo/temperatura          | No aplica                                                                                                                                                                |
| Prompt original             | [Prompt](#prompt)                                                                                                                                                        |
| Respuesta de IA             | [Respuesta](#respuesta)                                                                                                                                                  |
| Código anterior             | [1943cf0](https://github.com/Kevin-2-3/desarrollo-web/commit/1943cf0a91f7481b83815e8f0475e4ffd92f27b6)                                                                   |
| Código posterior            | [daaf97c](https://github.com/Kevin-2-3/desarrollo-web/commit/daaf97c95ee33e02718a05cf2a2be22280317517)                                                                   |
| Diferencias entre versiones | [Salida de git diff --numstat](#diferencias-entre-versiones)                                                                                                             |
| Prueba de funcionamiento    | Se registró una cuenta de prueba con contraseñas iguales. Después de iniciar sesión, la aplicación volvió a la ficha de la moto.  [Ver capturas](#capturas-de-la-prueba) |
| Código de uso               | Generación / Revisión                                                                                                                                                    |
| Reflexión del estudiante    | Si dejo el mensaje de error en el campo después de que el usuario lo corrige.                                                                                            |

### Prompt

Necesito realizar una validación de contraseña en la pagina de registro de nueva cuenta para que cuando en caso de que se pongan mal me lance un aviso y se quite cuando corrija cualquiera de las dos contraseñas.

### Respuesta

Usa `setCustomValidity()` en el campo de confirmación. Pon esta comprobación en el envío del formulario, antes de llamar a la API.

~~~js
if (registro && datos.password !== datos.confirmacionPassword) {
  form.elements.confirmacionPassword.setCustomValidity(
    'Las contraseñas deben coincidir.'
  );
}
if (!form.reportValidity()) return;
~~~

Al editar cualquiera de los dos campos, limpia el aviso con `setCustomValidity('')`. En el siguiente envío se comparan los valores otra vez. Si el formulario es válido, envía el registro a la API.

### Diferencias entre versiones

~~~text
git diff --numstat 1943cf0 daaf97c
2	1	backend/src/main/java/mx/uv/motostock/seguridad/Sesiones.java
7	0	backend/src/main/java/mx/uv/motostock/usuario/AuthController.java
45	4	frontend/css/tienda.css
5	1	frontend/index.html
162	0	frontend/js/app.js
38	0	frontend/login.html
38	0	frontend/registro.html
~~~

### Capturas de la prueba

**Acceso solicitado desde la ficha**

![Acceso solicitado desde la ficha](<capturas/Inicio de sesión desde la ficha.jpg>)

**Regreso a la ficha después de iniciar sesión con la cuenta de prueba**

![Regreso a la ficha después de iniciar sesión](<capturas/Regreso a la ficha después de iniciar sesión.jpg>)

---

## Observaciones adicionales

La comparación de contraseñas se hace en el formulario de registro. El aviso se elimina cuando se edita la confirmación o la contraseña principal, de modo que se pueda volver a enviar el formulario después de corregirlo.
