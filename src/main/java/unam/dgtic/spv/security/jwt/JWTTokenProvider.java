package unam.dgtic.spv.security.jwt;

import unam.dgtic.spv.core.dto.UsuarioDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
//Esta clase se encarga de la generación, validación y obtención de información de los tokens JWT utilizados para la autenticación en la aplicación.
public class JWTTokenProvider {
    private String secret;
    private int jwtExpirationInMs;
    private SecretKey key;

    // Inyecta la clave secreta utilizada para firmar el token JWT.
    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    // Inyecta la duración de expiración del token JWT.
    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    // Genera un token JWT utilizando la autenticación y la información del usuario.
    public String generateJwtToken(Authentication authentication, UsuarioDTO user) {
        Claims claims = Jwts.claims().setSubject("UNAM").setIssuer(user.getEmail())
                .setAudience("SPV");
        claims.put("principal", authentication.getPrincipal());
        claims.put("auth", authentication.getAuthorities().stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                .collect(Collectors.toList()));
        claims.put("issid", user.getId());
        claims.put("issname", user.getNombre() + " " + user.getApellido());
        key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs * 1000L))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Obtiene las reclamaciones (claims) del token JWT.
    public Claims getClaims(String token) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // Obtiene el nombre completo del token JWT.
    public String getFullName(String token) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        var body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return (String) body.get("issname");
    }

    // Obtiene el sujeto del token JWT.
    public String getSubject(String token) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    // Obtiene el emisor del token JWT.
    public String getIssuer(String token) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getIssuer();
    }

    // Obtiene la audiencia del token JWT.
    public String getAudience(String token) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getAudience();
    }

    // Obtiene la fecha de expiración del token JWT.
    public Date getTokenExpiryFromJWT(String token) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }

    // Obtiene la fecha de emisión del token JWT.
    public Date getTokenIatFromJWT(String token) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getIssuedAt();
    }

    // Valida el token JWT.
    public boolean validateJwtToken(String authToken) {
        try {
            key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT token -> Message: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT token -> Message: {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT token -> Message: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims string is empty -> Message: {}", exception.getMessage());
        }
        return false;
    }

    // Obtiene la duración de expiración del token en milisegundos.
    public long getExpiryDuration() {
        return jwtExpirationInMs * 1000L;
    }
}
