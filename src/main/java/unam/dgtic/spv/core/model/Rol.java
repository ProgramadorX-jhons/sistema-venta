package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "rol")
public class Rol {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", columnDefinition = "bigint", nullable = false, updatable = false)
    private Integer Id;
    @NotBlank(message = "El nombre del rol es obligatorio.")
    @Column(name = "nombre", columnDefinition = "varchar(30)", length = 30, nullable = false)
    @Size(max = 20, message = "El nombre del rol no debe exceder los 20 caracteres.")
    private String nombre;
    @Column(name = "descripcion")
    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres.")
    private String descripcion;
}
