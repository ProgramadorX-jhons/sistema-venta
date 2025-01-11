package unam.dgtic.spv.security.service;

import unam.dgtic.spv.core.model.Usuario;
import unam.dgtic.spv.core.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
//Implementa la interfaz UserDetailsService de Spring Security para cargar los detalles del usuario desde la base de
// datos utilizando su nombre de usuario (correo electrónico).
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    // Constructor que inicializa el repositorio de usuarios.
    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método principal que carga los detalles del usuario por su nombre de usuario.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Security - UserDetailsServiceImpl.loadUserByUsername {}", username);
        // Busca el usuario en la base de datos por su correo electrónico.
        Usuario usuario = Optional.ofNullable(usuarioRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found in database"));

        // Obtiene el nombre de usuario y la contraseña del usuario.
        String userName = usuario.getEmail();
        String password = usuario.getPassword();

        // Obtiene las autoridades (roles) del usuario.
        List<GrantedAuthority> authorities = usuario.getUseInfoRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());

        // Retorna un objeto User de Spring Security con el nombre de usuario, la contraseña y las autoridades.
        return new User(userName, password, authorities);
    }
}
