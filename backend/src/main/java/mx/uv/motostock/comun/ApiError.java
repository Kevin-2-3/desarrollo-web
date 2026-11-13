package mx.uv.motostock.comun;
import org.springframework.http.HttpStatus;
public class ApiError extends RuntimeException {
    public final HttpStatus estado;
    public ApiError(HttpStatus e,String m){
        super(m);
        estado=e;
    }
    public static ApiError invalido(String m){
        return new ApiError(HttpStatus.BAD_REQUEST,m);
    }
    public static ApiError conflicto(String m){
        return new ApiError(HttpStatus.CONFLICT,m);
    }
    public static ApiError noExiste(){
        return new ApiError(HttpStatus.NOT_FOUND,"Recurso no disponible");
    }
}
