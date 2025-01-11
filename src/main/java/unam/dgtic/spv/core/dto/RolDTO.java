package unam.dgtic.spv.core.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
}
