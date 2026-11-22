# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 21/11/2026

**Número de reporte:** 05

---

# 1. Breve descripción de uso

Para mi caso como el README de MotoStock tenía información de las primeras entregas, usé a Claude como apoyo para revisar qué partes necesitaban actualizarse, y principalmente se revisó la estructura de carpetas, los archivos del frontend y los enlaces a la documentación y pruebas del proyecto

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
| Herramienta | Claude (Anthropic) |
| Modelo/temperatura | No registrados en la consulta. |
| Prompt original | [Prompt](#prompt) |
| Respuesta de IA | [Respuesta](#respuesta) |
| Código anterior | [Commit 8688b07](https://github.com/Kevin-2-3/desarrollo-web/commit/8688b0755b4f9b08d4a92f16e8fcbc887acf16a6) |
| Código posterior | [Commit 5942435](https://github.com/Kevin-2-3/desarrollo-web/commit/5942435534b35e62f0c7a620466dec158c1ac5a9) |
| Diferencias entre versiones | [Salida de git diff --numstat](#diferencias-entre-versiones) |
| Prueba de funcionamiento | Se cotejaron los seis enlaces del README con el [árbol de la rama main en GitHub](https://github.com/Kevin-2-3/desarrollo-web/tree/main). Todos apuntaban a rutas publicadas y `git diff --check` terminó sin errores. |
| Código de uso | Modificación / Documentación / Revisión |
| Reflexión del estudiante | Esta vez usé IA para apoyarme en la actualización del README principalmente para identificar los cambios que se habían realizado en la estructura del proyecto. Tenía claro lo que debía llevar mas o menos, pero igual me sirvió para revisar que no faltaran archivos o carpetas de las últimas entregas. También comprobé los cambios con Git y corregí algunos detalles de formato antes de subirlo al repositorio. |

### Prompt

Necesito actualizar el README de mi proyecto MotoStock porque todavía tiene información de entregas anteriores. ¿Me podrías ayudar a revisar la estructura actual del repositorio y qué debería cambiar? Quiero que aparezcan las carpetas y archivos que tenemos actualmente, incluye las páginas del frontend, las pruebas y la documentación. También quiero revisar que los enlaces funcionen pero sin modificar el código del proyecto.

### Respuesta

Para actualizar el README de MotoStock, lo primero es comparar su contenido con la estructura actual del repositorio.

Los principales cambios serían:

- Actualizar el árbol de carpetas para incluir los archivos que se agregaron durante las últimas entregas.
- Incluir las 10 páginas HTML del frontend y las imágenes del catálogo.
- Agregar las carpetas de pruebas correspondientes a las entregas 2, 3 y 5.
- Revisar los enlaces de la documentación para que apunten a rutas existentes en GitHub.
- Eliminar las referencias a archivos que ya no forman parte del proyecto.

Después de realizar los cambios, puedes utilizar `git diff` para comprobar qué se modificó y `git diff --check` para detectar problemas de formato.

De esta manera, el README quedará actualizado y será más sencillo para otra persona comprender cómo está organizado y cómo ejecutar MotoStock.

### Diferencias entre versiones

~~~text
git diff --numstat 8688b07 5942435 -- README.md
49	31	README.md
~~~

---

## Observaciones adicionales

GitHub se consultó en modo de solo lectura. La [comparación en GitHub](https://github.com/Kevin-2-3/desarrollo-web/compare/8688b0755b4f9b08d4a92f16e8fcbc887acf16a6...5942435534b35e62f0c7a620466dec158c1ac5a9) confirma las 49 líneas añadidas y 31 eliminadas del README entre el commit anterior y el que publicó el cambio.
