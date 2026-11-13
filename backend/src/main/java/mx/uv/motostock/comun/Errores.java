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
        MethodArgumentNotValidException.class,org.springframework.http.converter.HttpMessageNotReadableException.class,org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,jakarta.validation.ConstraintViolationException.class
    }
    ) ResponseEntity<?> entrada(Exception e){
        return ResponseEntity.badRequest().body(Map.of("error","Entrada inválida: revise campos, tipos y límites"));
    }
    @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<?> integridad(Exception e){
        return ResponseEntity.status(409).body(Map.of("error","Conflicto con datos existentes o restricciones"));
    }
    @ExceptionHandler(Exception.class) ResponseEntity<?> interno(Exception e){
        org.slf4j.LoggerFactory.getLogger(Errores.class).error("Error no previsto",e);
        return ResponseEntity.internalServerError().body(Map.of("error","No se pudo completar la operación"));
    }
}
