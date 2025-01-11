package unam.dgtic.spv.security;

import unam.dgtic.spv.security.jwt.JWTAuthenticationFilter;
import unam.dgtic.spv.security.jwt.JWTTokenProvider;
import unam.dgtic.spv.security.logout.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
//Esta clase configura la seguridad de la aplicación utilizando Spring Security.
// Define la cadena de filtros de seguridad, el proveedor de autenticación y el codificador de contraseñas.
public class SecurityConfiguration {
    @Autowired
    private UserDetailsService uds;
    @Autowired
    private JWTTokenProvider tokenProvider;
    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    // Configura la cadena de filtros de seguridad.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        // Permite el acceso sin autenticación a ciertas rutas.
                        .requestMatchers("/bootstrap/**","/css/**", "/favicon.ico","/", "/index", "/token" ,"/error").permitAll()
                        // Requiere que el usuario tenga la autoridad "ADMIN" para acceder a ciertas rutas.
                        .requestMatchers(
                                //ARTICULO
                                "/articulo/alta-articulo",
                                "/articulo/salvar-imagen",
                                "/articulo/salvar-imagen",
                                "/articulo/salvar-articulo",
                                "/articulo/modificar-articulo/{id}",
                                "/articulo/eliminar-articulo/{id}",
                                //CATEGORIA
                                "/categoria/alta-categoria",
                                "/categoria/salvar-categoria",
                                "/categoria/modificar-categoria/{id}",
                                "/categoria/eliminar-categoria/{id}",
                                //INGRESO
                                "/ingreso/alta-ingreso",
                                "/ingreso/agregar",
                                "/ingreso/quitar/{indice}",
                                "/ingreso/limpiar",
                                "/ingreso/terminar",
                                //PERSONA
                                "/persona/alta-persona",
                                "/persona/salvar-persona",
                                "/persona/modificar-persona/{id}",
                                "/persona/eliminar-persona/{id}",
                                //VENTA
                                "/venta/modificar-venta/{id}",
                                "/venta/eliminar-venta/{id}",
                                //ROL
                                "/rol/**",
                                //USUARIO
                                "/usuario/**"
                        ).hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        // Configura la página de inicio de sesión y las URL de éxito.
                        .loginPage("/login")
                        .defaultSuccessUrl("/index")
                        .successForwardUrl("/login_success_handler")
                        .permitAll())
                .logout(logout -> logout
                        // Configura la URL de cierre de sesión y el manejador de éxito.
                        .logoutUrl("/doLogout")
                        .logoutSuccessUrl("/index")
                        .deleteCookies("JSESSIONID") // Borra la cookie de sesión.
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .clearAuthentication(true)
                        .invalidateHttpSession(true))
                // Añade el filtro de autenticación JWT después del filtro de autenticación de usuario y contraseña.
                .addFilterAfter(new JWTAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        return http.build();
    }

    // Configura el codificador de contraseñas.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11, new SecureRandom());
    }

    // Configura el proveedor de autenticación.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(uds);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Configura el administrador de autenticación.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        // El proveedor de autenticación debe retornar un objeto UserDetails.
        return new ProviderManager(authenticationProvider());
    }
}
