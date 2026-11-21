# Reporte de Uso Documental en el Proyecto

**Fecha:** 19/11/2026  
**Número de reporte:** 04  
# 1. Breve descripción de uso

Quería conocer como es que el si alguien elige una moto y tiene que entrar a su cuenta, como consigo que regrese a esa misma moto? Con Codex hice la pregunta para que pudiera analizar el codigo hasta ahora.
# 2. Evidencias de uso

| Fecha      | Herramienta | Enlace a prompt   | Enlace a respuesta               |
| ---------- | ----------- | ----------------- | -------------------------------- |
| 19/11/2026 | Codex       | [Prompt](#prompt) | [Respuesta de la IA](#respuesta) |

### Prompt

Si un usuario entra al login desde un articulo e inicia sesión, como le hago para que despues me regrese a la fincha aunque tenia que registrarme antes?

### Respuesta

Guarda la ruta de la ficha junto con su consulta. Por ejemplo, en `/producto.html?id=8`, el `id=8` es lo que permite recuperar esa moto. Pasa la ruta en el parámetro `volver` al abrir el acceso y consérvala en el enlace de registro. Después de entrar, `destinoTrasLogin()` acepta las páginas previstas de Motostock. Si el destino falta o es inválido, abre el catálogo.

---

## Observaciones adicionales

Para revisar el regreso a la ficha tengo que comparar también el ID. Dos motos distintas abren el mismo archivo HTML.
