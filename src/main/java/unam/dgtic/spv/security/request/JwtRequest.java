package unam.dgtic.spv.security.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//Esta clase representa una solicitud de JWT. Contiene información relevante sobre el token,
// el usuario y sus autoridades (roles).
public class JwtRequest {
    private String token;
    private String tokenType;
    private Integer userId;
    private String userName;
    private Long expiryDuration;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor que inicializa los campos principales de la solicitud JWT.
    public JwtRequest(String token, Integer userId, String userName, Long expiryDuration,
                      Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.expiryDuration = expiryDuration;
        this.authorities = authorities;
        this.tokenType = "Bearer"; // Tipo de token por defecto es "Bearer".
    }
}
