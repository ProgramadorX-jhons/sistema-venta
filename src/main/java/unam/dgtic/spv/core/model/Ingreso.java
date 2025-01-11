package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "ingreso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @Column(name = "num_comprobante", length = 10, nullable = false)
    private Integer numComprobante;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    private Date fecha;

    @Column(nullable = false)
    private Float total;

    @Column(length = 20, nullable = false)
    private String estado;

    @OneToMany(mappedBy = "ingreso")
    private Set<DetalleIngreso> detalleIngresos;

    public static int generateRandomNumber() {
        Random random = new Random();
        int numberOfDigits = random.nextInt(10) + 1; // Número de dígitos entre 1 y 10
        int min = (int) Math.pow(10, numberOfDigits - 1);
        int max = (int) Math.pow(10, numberOfDigits) - 1;
        return min + random.nextInt(max - min + 1);
    }
}