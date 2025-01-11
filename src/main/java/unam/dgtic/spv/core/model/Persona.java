package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_persona", nullable = false, length = 20)
    @NotBlank(message = "El tipo de persona es obligatorio.")
    private String tipoPersona;

    @Column(name = "nombre", nullable = false, unique = true, length = 70)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 70, message = "El nombre no debe exceder los 100 caracteres.")
    private String nombre;

    @Column(name = "direccion", length = 70)
    @NotBlank(message = "La direccion es obligatoria.")
    private String direccion;

    @Column(name = "telefono", length = 15)
    @NotBlank(message = "El telefono es obligatorio.")
    @Pattern(regexp = "^\\+?[0-9]{10}$" , message = "Numeor de Telefono no valido")
    private String telefono;

    @Column(name = "email", length = 50, unique = true)
    @NotBlank(message = "El correo es obligatorio.")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Dirección de correo electrónico inválida")
    private String email;

    @Column(name = "activo", nullable = false)
    @NotNull(message = "La categoría es obligatoria.")
    private Boolean activo;

    // Getters y setters
}
