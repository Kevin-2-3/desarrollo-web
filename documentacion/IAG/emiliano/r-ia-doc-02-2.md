# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 14/11/2026  
**Número de reporte:** 02  

---

# 1. Breve descripción de uso

Usé Claude para que me ayudara a implementar el filtro de precios del catálogo y a entender cómo funcionaba. Lo que más me preocupaba era que el precio mínimo y el máximo se cumplieran en una misma variante, porque cada talla de un producto puede tener un precio diferente. Para comprobarlo, hice una prueba con un casco LS2 que viene incluida en este reporte.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
Herramienta | Claude (Anthropic)
Modelo/temperatura | Sin registro en el intercambio original.
Prompt original | [Formulación del caso documentado](#prompt-original)
Respuesta de IA | [Cita textual del módulo](#respuesta-de-ia)
Código anterior | [8f85282](https://github.com/Kevin-2-3/desarrollo-web/commit/8f8528282086bee94f9f524cd27828b8886922d8)
Código posterior | [8eb799d](https://github.com/Kevin-2-3/desarrollo-web/commit/8eb799d1dd2b0059062280f77ea88cc8e0e6e967)
Diferencias entre versiones | `git diff --numstat 8f85282 8eb799d`<br>`15	0	backend/src/main/java/mx/uv/motostock/catalogo/CatalogoController.java`<br>`81	0	backend/src/main/java/mx/uv/motostock/catalogo/CatalogoService.java`<br>`1	0	backend/src/main/resources/static/sin-imagen.svg`
Prueba de funcionamiento | GET /api/v1/productos?busqueda=LS2&precioMin=1510&precioMax=1540. Los dos SKU del casco valían 1500 y 1550 MXN.<br>`{"content":[],"pageable":{"pageNumber":0,"pageSize":12,"sort":{"sorted":true,"empty":false,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalPages":0,"totalElements":0,"size":12,"number":0,"sort":{"sorted":true,"empty":false,"unsorted":false},"first":true,"numberOfElements":0,"empty":true} HTTP 200`
Código de uso | Generación / Documentación / Pruebas
Reflexión del estudiante |Entendi que el filtro tiene que revisar el precio de cada talla por su cuenta. Si se juntan los precios de varias variantes, puede colarse un producto que en realidad no tiene ninguna opción dentro del rango que buscó el cliente.

### Prompt original

Tengo un casco LS2 con dos tallas: una cuesta 1500 y otra 1550. Si busco entre 1510 y 1540, no debería aparecer ninguna. ¿Cómo hago el filtro para que revise los dos límites en la misma variante?

### Respuesta de IA

En `CatalogoService`, el código usa `EXISTS` para comprobar que **una misma variante activa** cumpla el mínimo y el máximo.

**Ampliación técnica para este reporte**

Evalúa el rango de precio sobre cada variante que el cliente podría comprar. Piensa en el casco LS2 de los datos de prueba. La talla M cuesta 1500 MXN y supera la condición de ser menor o igual a 1540, pero queda por debajo de 1510. La talla L cuesta 1550 MXN y cumple el mínimo de 1510, pero rebasa el máximo. Ninguna talla satisface las dos condiciones a la vez.

Fragmento de `CatalogoService.java`:

```java
            var sub=q.subquery(Long.class);
            var sku=sub.from(Sku.class);
            List<Predicate> variantes=new ArrayList<>();
            variantes.add(cb.equal(sku.get("producto"),root));
            variantes.add(cb.isTrue(sku.get("activo")));
            if(min!=null)variantes.add(cb.greaterThanOrEqualTo(sku.get("precioMxn"),min));
            if(max!=null)variantes.add(cb.lessThanOrEqualTo(sku.get("precioMxn"),max));
            sub.select(sku.get("id")).where(variantes.toArray(Predicate[]::new));
            filtros.add(cb.exists(sub));
```

En la consulta JPA, `root` representa el producto que estás considerando y `sku` representa una variante. La primera condición vincula esa variante con el producto actual. Después se exige que esté activa y se agregan los límites que haya enviado el cliente. Al reunir todo en una sola subconsulta, `EXISTS` conserva el producto si encuentra al menos una fila que cumpla todas las condiciones. Aunque dos tallas coincidan, el casco ocupa una sola posición en la página.

Para LS2 con el rango de 1510 a 1540, CURL devolvió 200 con `totalElements: 0`. Eso es una búsqueda válida sin coincidencias. Como contraste para una prueba adicional, al cambiar el máximo a 1550 debería aparecer el casco porque el límite superior es inclusivo y la talla L cuesta exactamente ese importe.

---

## Observaciones adicionales

El cambio en el catálogo lo subi en el commit "catalogo, filtros y detalles". La prueba la corrí en la versión ya integrada, en WSL con Java 21 y PostgreSQL 18.6. Con el rango de 1510 a 1540 no salió ningún producto, como esperaba. Me falta hacer otra prueba subiendo el máximo a 1550 para confirmar que sí aparece la talla que cuesta justo eso.
