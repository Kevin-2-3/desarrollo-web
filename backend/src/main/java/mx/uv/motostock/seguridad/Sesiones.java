package mx.uv.motostock.seguridad;
import jakarta.servlet.http.*;
import java.util.*;
import mx.uv.motostock.comun.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;
@Configuration public class Sesiones implements WebMvcConfigurer {
    public static Long cliente(HttpServletRequest r){
        var s=r.getSession(false);
        if(s==null||s.getAttribute("clienteId")==null)throw new ApiError(HttpStatus.UNAUTHORIZED,"Inicie sesión");
        return (Long)s.getAttribute("clienteId");
    }
    public static String csrf(HttpServletRequest r){
        var s=r.getSession();
        if(s.getAttribute("csrf")==null)s.setAttribute("csrf",UUID.randomUUID().toString());
        return (String)s.getAttribute("csrf");
    }
    @Override public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new HandlerInterceptor(){
            @Override public boolean preHandle(HttpServletRequest r,HttpServletResponse response,Object handler){
                String p=r.getRequestURI(),m=r.getMethod(); boolean publico=(m.equals("GET")&&(p.equals("/api/v1/auth/csrf")||p.equals("/api/v1/productos")||p.startsWith("/api/v1/productos/")))||(m.equals("POST")&&(p.equals("/api/v1/auth/register")||p.equals("/api/v1/auth/login")));
                if(!publico)cliente(r);
                if(!Set.of("GET","HEAD","OPTIONS").contains(m)){
                    var s=r.getSession(false);String expected=s==null?null:(String)s.getAttribute("csrf");String actual=r.getHeader("X-CSRF-TOKEN");if(expected==null||actual==null||!java.security.MessageDigest.isEqual(expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),actual.getBytes(java.nio.charset.StandardCharsets.UTF_8)))throw new ApiError(HttpStatus.FORBIDDEN,"Token CSRF requerido");
                }
                return true;
            }
        }
        ).addPathPatterns("/api/v1/**");
    }
}
