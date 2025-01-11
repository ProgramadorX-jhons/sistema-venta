package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 20)
    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    @Size(max = 20, message = "El nombre no debe exceder los 20 caracteres.")
    private String nombre;

    @Column(name = "descripcion")
    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres.")
    private String descripcion;

    @Column(name = "activo", nullable = false)
    @NotNull(message = "El estado es obligatorio.")
    private Boolean activo;

}
