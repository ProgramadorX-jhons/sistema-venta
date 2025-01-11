package unam.dgtic.spv.security.model;

import unam.dgtic.spv.core.model.Usuario;
import unam.dgtic.spv.core.model.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

//Implementa la interfaz UserDetails de Spring Security, proporcionando la información necesaria para la
// autenticación y autorización del usuario.
public class UserDetailsImpl implements UserDetails {
    private Integer id;
    private String name;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private Usuario usuario;

    // Constructor que inicializa la instancia con un objeto Usuario.
    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    // Constructor que inicializa la instancia con parámetros específicos.
    public UserDetailsImpl(Integer id, String name, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.authorities = authorities;
    }

    // Método estático para construir un UserDetailsImpl a partir de un objeto Usuario.
    public static UserDetailsImpl build(Usuario user) {
        List<GrantedAuthority> authorities = user.getUseInfoRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getNombre())
        ).collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                authorities
        );
    }

    // Obtiene las autoridades (roles) del usuario.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (null == usuario.getUseInfoRoles()) {
            return Collections.emptySet();
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Rol role : usuario.getUseInfoRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getNombre()));
        }
        return grantedAuthorities;
    }

    // Obtiene el nombre de usuario (correo electrónico).
    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    // Obtiene la contraseña del usuario.
    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    // Obtiene el nombre completo del usuario.
    public String getName() {
        return usuario.getNombre();
    }

    // Obtiene el correo electrónico del usuario.
    public String getEmail() {
        return usuario.getEmail();
    }

    // Indica si el usuario está habilitado.
    @Override
    public boolean isEnabled() {
        return usuario.getActivo() == true;
    }

    // Indica si la cuenta del usuario no está bloqueada.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indica si la cuenta del usuario no ha expirado.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indica si las credenciales del usuario no han expirado.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
