package unam.dgtic.spv.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "username",
        "password"
})
//Esta clase representa una solicitud de inicio de sesión de un usuario.
//Contiene el nombre de usuario y la contraseña necesarios para la autenticación.
public class LoginUserRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
