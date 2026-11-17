# Reporte de Uso Documental en el Proyecto

**Fecha:** 15/11/2026  
**Número de reporte:** 03  

# 1. Breve descripción de uso

Use Claude para preguntar una duda que tenia en la busqueda y filtros

# 2. Evidencias de uso

| Fecha | Herramienta | Enlace a prompt | Enlace a respuesta |
|---|---|---|---|
| 15/11/2026 | Claude (Anthropic) | [Consulta](#prompt) | [Explicación](#respuesta) |

### Prompt

Si pongo % en la búsqueda, porque salen todos los productos?

### Respuesta

LIKE interpreta % como cualquier secuencia y _ como un carácter. El servicio escapa primero ! y luego esos dos símbolos y declara ! como escape en CriteriaBuilder.like. Así la búsqueda sigue siendo parcial, pero los caracteres escritos por el usuario se consideran literales. La prueba busca % y no espera coincidencias en los datos de ensayo.

### Observaciones adicionales

Con esto supe que se le tenia que poner algo para que la busqueda separara el texto de los caracteres especiales
