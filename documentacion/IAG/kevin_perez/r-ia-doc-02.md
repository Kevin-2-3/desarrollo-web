# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 14/11/2026  
**Número de reporte:** 02  

---

# 1. Breve descripción de uso

Se uso Codex instalado en la pc y con una copia de el repositorio para que tuviera un mejor contexto, se le pregunto y dio primero a entender que teníamos cuentas duplicadas y como se debían guardar las contraseñas hashedas con BCrypt.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
Herramienta | Codex (OpenAI)
Modelo/temperatura | Sin registro en el intercambio original.
Prompt original | [Formulación del caso documentado](#prompt-original)
Respuesta de IA | [Cita textual del módulo](#respuesta-de-ia)
Código anterior | [7a1d4af](https://github.com/Kevin-2-3/desarrollo-web/commit/7a1d4afcdbff863e469a49d84a6fd364004795a9)
Código posterior | [8f85282](https://github.com/Kevin-2-3/desarrollo-web/commit/8f8528282086bee94f9f524cd27828b8886922d8)
Diferencias entre versiones | `git diff --numstat 7a1d4af 8f85282`<br>`18	0	backend/src/main/java/mx/uv/motostock/comun/ApiError.java`<br>`24	0	backend/src/main/java/mx/uv/motostock/comun/Errores.java`<br>`33	0	backend/src/main/java/mx/uv/motostock/seguridad/Sesiones.java`<br>`34	0	backend/src/main/java/mx/uv/motostock/usuario/AuthController.java`<br>`38	0	backend/src/main/java/mx/uv/motostock/usuario/AuthService.java`
Prueba de funcionamiento | Se registró Juan@gmail.com
 y se repitió el registro con Juan@gmail.com
 La consulta SQL devolvió el número de cuentas y si todos los hashes comenzaban con $2. Finalmente se inició sesión.<br>`{"nombre":"QA","id":1,"correo":"juan@gmail.com
"} HTTP 201`<br>`{"error":"Correo ya registrado"} HTTP 409`<br>`1\|t`<br>`{"nombre":"QA","id":1} HTTP 200`
Código de uso | Generación / Documentación / Pruebas
Reflexión del estudiante | Entendí que el correo se puede convertir a minúsculas para evitar cuentas duplicadas, pero la contraseña se queda igual para no ser modificada . Con BCrypt se guarda un hash y al iniciar sesión se usa matches para comprobar la contraseña.

### Prompt original

Si tengo dos correos pero varian en su escritura, por ejemplo juan@gmail.com y Juan@gmail.com, como debo hacer para que se tomen como el mismo, tambien que se guarden con BCrypt y como se comprueba al iniciar seción

### Respuesta de IA

En `AuthService`, el código convierte el correo a minúsculas y protege la contraseña con BCrypt antes de guardar el cliente.

Convierte el correo a una forma común antes de buscar la cuenta y guarda esa misma forma en PostgreSQL. Si Juan escribe `Juan@gmail.com`, el servicio busca `juan@gmail.com
`. Cuando intenta registrarse de nuevo con el correo en minúsculas, encuentra el registro anterior. Conviene aplicar la misma conversión en el inicio de sesión para que Juan pueda entrar con cualquiera de las dos escrituras.

Fragmento de `AuthService.java`:

```java
    private String correo(String c){
        return c.trim().toLowerCase(Locale.ROOT);
    }
    @Transactional public Cliente registrar(AuthController.Registro r){
        if(!r.password().equals(r.confirmacionPassword()))throw ApiError.invalido("Las contraseñas no coinciden");
        if(r.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)throw ApiError.invalido("Contraseña demasiado larga para BCrypt");
        String email=correo(r.correo());
        if(clientes.findByCorreo(email).isPresent())throw ApiError.conflicto("Correo ya registrado");
        Cliente c=new Cliente();
        c.nombre=r.nombre().trim();
        c.correo=email;
        c.passwordHash=encoder.encode(r.password());
        return clientes.save(c);
    }
```

La contraseña requiere un tratamiento distinto. Si el usuario cambia una mayúscula de su contraseña, está escribiendo otra clave. El servicio conserva esa distinción, comprueba que coincida con la confirmación y entrega el valor a BCrypt. Para iniciar sesión, compara la contraseña recibida con el hash guardado mediante `matches`. La restricción de unicidad en PostgreSQL cubre además el caso en que dos solicitudes de registro lleguen casi al mismo tiempo.

En la prueba, el primer registro devolvió 201 y el segundo 409. La consulta a PostgreSQL mostró una sola fila y un hash con el prefijo de BCrypt. El inicio de sesión posterior devolvió 200. Así se comprobaron por separado la identidad de la cuenta y la validez de sus credenciales.

---

## Observaciones adicionales

 La prueba registrada se hizo después de la corrección y apoyo de la IA. El primer registro devolvió 201, el correo repetido devolvió 409 y el inicio de sesión devolvió 200. 
