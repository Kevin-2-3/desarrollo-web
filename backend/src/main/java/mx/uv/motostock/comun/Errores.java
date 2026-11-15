package mx.uv.motostock.comun;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
@RestControllerAdvice public class Errores {
    @ExceptionHandler(ApiError.class) ResponseEntity<?> api(ApiError e){
        return ResponseEntity.status(e.estado).body(Map.of("error",e.getMessage()));
    }
    @ExceptionHandler({
        org.springframework.http.converter.HttpMessageNotReadableException.class,org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,jakarta.validation.ConstraintViolationException.class
    }
    ) ResponseEntity<?> entrada(Exception e){
        return ResponseEntity.badRequest().body(Map.of("error","Entrada inválida: revise campos, tipos y límites"));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> campos(MethodArgumentNotValidException e){
        Map<String,String> campos=new java.util.LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f->campos.putIfAbsent(f.getField(),f.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("error","Revise los campos indicados","campos",campos));
    }
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    ResponseEntity<?> metodo(org.springframework.web.HttpRequestMethodNotSupportedException e){
        var respuesta=ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED);
        if(e.getSupportedHttpMethods()!=null)respuesta.allow(e.getSupportedHttpMethods().toArray(HttpMethod[]::new));
        return respuesta.body(Map.of("error","Método HTTP no permitido"));
    }
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    ResponseEntity<?> formato(Exception e){
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(Map.of("error","Use Content-Type: application/json"));
    }
    @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<?> integridad(Exception e){
        return ResponseEntity.status(409).body(Map.of("error","Conflicto con datos existentes o restricciones"));
    }
    @ExceptionHandler(Exception.class) ResponseEntity<?> interno(Exception e){
        org.slf4j.LoggerFactory.getLogger(Errores.class).error("Error no previsto",e);
        return ResponseEntity.internalServerError().body(Map.of("error","No se pudo completar la operación"));
    }
}
