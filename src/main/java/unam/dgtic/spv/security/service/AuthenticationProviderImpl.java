package unam.dgtic.spv.security.service;

import unam.dgtic.spv.core.model.Usuario;
import unam.dgtic.spv.core.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
//Implementa la interfaz AuthenticationProvider de Spring Security para autenticar a los usuarios utilizando
// sus credenciales (correo electrónico y contraseña) contra los datos almacenados en la base de datos.
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Método principal que autentica al usuario.
    @Override
    public Authentication authenticate(Authentication authentication) {
        // Obtiene el nombre de usuario y la contraseña de la autenticación proporcionada.
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        // Busca el usuario en la base de datos por su correo electrónico.
        Usuario userAdmin = Optional.ofNullable(usuarioRepository.findByEmail(username))
                .orElseThrow(() -> new BadCredentialsException("User not found in database"));
        // Verifica si la contraseña proporcionada coincide con la contraseña del usuario en la base de datos.
        if (passwordEncoder.matches(pwd, userAdmin.getPassword())) {
            // Si las credenciales son correctas, obtiene las autoridades (roles) del usuario.
            List<GrantedAuthority> authorities = userAdmin.getUseInfoRoles().stream().map(role ->
                    new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
            // Retorna un token de autenticación con el nombre de usuario, la contraseña y las autoridades.
            return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
        } else {
            // Si la contraseña es incorrecta, lanza una excepción de credenciales inválidas.
            throw new BadCredentialsException("Invalid password!");
        }
    }

    // Método que verifica si este proveedor de autenticación soporta el tipo de autenticación proporcionado.
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
