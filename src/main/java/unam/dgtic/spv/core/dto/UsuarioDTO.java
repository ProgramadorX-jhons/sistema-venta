package unam.dgtic.spv.core.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private Integer id;
    private String nombre ;
    private String apellido;
    private String curp;
    private String direccion;
    private String telefono;
    private String email;
    private String password;
    private Boolean activo;
    private Set<RolDTO> useInfoRoles = new HashSet<>();
}
