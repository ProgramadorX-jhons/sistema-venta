package unam.dgtic.spv.security.logout;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
// Esta clase maneja las acciones a tomar cuando un usuario cierra sesión exitosamente en la aplicación.
// Implementa la interfaz LogoutSuccessHandler de Spring Security.
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    // Método que se ejecuta cuando un usuario cierra sesión exitosamente.
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Logout Handler");

        // Obtiene las cookies de la solicitud.
        Cookie[] cookies2 = request.getCookies();

        // Recorre las cookies para encontrar la cookie con el nombre "token".
        for (Cookie cookie : cookies2) {
            if (cookie.getName().equals("token")) {
                // Si encuentra la cookie, la invalida estableciendo su edad máxima a 0.
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                log.info("Logout successfully");
                // Redirige al usuario a la página principal del contexto.
                response.sendRedirect(request.getContextPath());
            }
        }
    }
}
