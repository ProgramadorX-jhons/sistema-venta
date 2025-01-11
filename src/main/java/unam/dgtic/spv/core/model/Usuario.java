package unam.dgtic.spv.core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 70)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 70, message = "El nombre no debe exceder los 70 caracteres.")
    private String nombre;

    @Column(name = "apellido", length = 150)
    @Size(max = 150, message = "El apellido no debe exceder los 150 caracteres.")
    @NotBlank(message = "El apellido es obligatorio.")
    private String apellido;

    @Column(name = "curp", length = 18, unique = true)
    @Size(max = 18, message = "El CURP no debe exceder los 18 caracteres.")
    @NotBlank(message = "El curp es obligatorio.")
    private String curp;

    @Column(name = "direccion", length = 70)
    @Size(max = 70, message = "La dirección no debe exceder los 70 caracteres.")
    @NotBlank(message = "La direccion es obligatoria.")
    private String direccion;

    @Column(name = "telefono", length = 10)
    @Size(max = 10, message = "El teléfono no debe exceder los 15 caracteres.")
    @NotBlank(message = "El numero de telefono es obligatorio.")
    private String telefono;

    @Column(name = "email", columnDefinition = "varchar(45)", length = 45, nullable = false, unique = true)
    @Email(message = "Debe proporcionar un email válido.")
    @Size(max = 50, message = "El email no debe exceder los 50 caracteres.")
    @NotBlank(message = "El email es obligatorio.")
    private String email;

    @Column(name = "password", columnDefinition = "varchar(64)", length = 64, nullable = false)
    @NotBlank(message = "La clave es obligatoria.")
    @Size(max = 128, message = "La clave no debe exceder los 128 caracteres.")
    private String password;

    @Column(name = "activo", nullable = false)
    @NotNull(message = "El estado activo es obligatorio.")
    private Boolean activo;


    //ROLES
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "user_adm",
            name = "relacion_usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @JsonManagedReference
    private Set<Rol> useInfoRoles = new HashSet<>();

    public String getFullName() {
        return this.nombre + ' ' + this.apellido;
    }
    @Override
    public String toString() {
        return nombre + ' ' + apellido;
    }
}
