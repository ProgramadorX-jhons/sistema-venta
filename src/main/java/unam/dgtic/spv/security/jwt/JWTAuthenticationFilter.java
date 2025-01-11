package unam.dgtic.spv.security.jwt;

import unam.dgtic.spv.security.dto.CredentialsDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
//Esta clase intercepta las solicitudes HTTP entrantes, extrae el token JWT de las cookies, valida el token, y si es
// válido, establece la autenticación en el contexto de seguridad de Spring.
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTTokenProvider tokenProvider;
    // Constructor que recibe un JWTTokenProvider para validar y obtener información del token.
    public JWTAuthenticationFilter(JWTTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    // La función principal de esta clase es interceptar las solicitudes HTTP,
    // extraer y validar el token JWT, y configurar la autenticación en el contexto de seguridad de Spring.
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Extrae el token JWT de las cookies en la solicitud.
        String jwt = "";
        if(request.getCookies() != null)
            for(Cookie cookie: request.getCookies())
                if(cookie.getName().equals("token"))
                    jwt = cookie.getValue();
        // Si no hay token, continúa con el siguiente filtro.
        if(jwt == null || jwt.equals("")){
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // Si el token es válido, obtiene las reclamaciones (claims) y configura la autenticación.
            if (tokenProvider.validateJwtToken(jwt)) {
                Claims body = tokenProvider.getClaims(jwt);
                var authorities = (List<Map<String, String>>) body.get("auth");
                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                        .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                        .collect(Collectors.toSet());
                // Obtiene el nombre de usuario y crea un objeto de credenciales.
                String username = tokenProvider.getFullName(jwt);
                CredentialsDTO credentials = CredentialsDTO.builder()
                        .sub(tokenProvider.getSubject(jwt)).aud(tokenProvider.getAudience(jwt))
                        .exp(tokenProvider.getTokenExpiryFromJWT(jwt).getTime())
                        .iat(tokenProvider.getTokenIatFromJWT(jwt).getTime())
                        .build();
                // Crea un token de autenticación y lo establece en el contexto de seguridad.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, credentials, simpleGrantedAuthorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            // Si hay un error al configurar la autenticación, registra el error.
            log.error("Can NOT set user authentication -> Message: {}", exception.getMessage());
        }
        // Continúa con el siguiente filtro.
        filterChain.doFilter(request, response);
    }
}
