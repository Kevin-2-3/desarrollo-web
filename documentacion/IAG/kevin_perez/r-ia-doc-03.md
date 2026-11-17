# Reporte de Uso Documental en el Proyecto

**Fecha:** 15/11/2026  
**Número de reporte:** 03

# 1. Breve descripción de uso

Con Codex abierto con una copia del proyecto como contexto, quise saber sobre la autenticación y respuestas http y a partir de eso, prepararon comandos para comprobarla manualmente con CURL desde el WSL
# 2. Evidencias de uso

| Fecha      | Herramienta | Enlace a prompt     | Enlace a respuesta        |
| ---------- | ----------- | ------------------- | ------------------------- |
| 15/11/2026 | Codex       | [Consulta](#prompt) | [Explicación](#respuesta) |

### Prompt

Para que sirven la sesión y el token CSRF si ya tengo contra?

### Respuesta

La contraseña se comprueba al iniciar sesión. Después la cookie identifica la sesión y el servidor obtiene de ella el cliente. El token CSRF se exige en las escrituras, incluido registro e inicio de sesión. No sustituye a la autenticación. La guía permite comprobar 401 sin sesión, 403 sin token y 401 después de cerrar sesión.

---

## Observaciones adicionales

A partir de las pruebas se comprobó  mejor como la validación y la autentificación varían para las peticiones
