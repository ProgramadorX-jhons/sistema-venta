package unam.dgtic.spv.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
//Esta clase es un objeto de transferencia de datos (DTO) que encapsula las credenciales de un usuario,
//incluyendo información del token JWT como el sujeto, la audiencia, la fecha de emisión y la fecha de expiración.
public class CredentialsDTO {
    private String sub; //Sujeto del Token
    private String aud; //La audiencia del token JWT.
    private Long iat; //La fecha de emisión del token JWT
    private Long exp; //La fecha de expiración del token JWT
}
