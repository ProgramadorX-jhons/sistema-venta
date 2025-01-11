package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_ingreso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleIngreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    @ManyToOne
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Float precio;

    public DetalleIngreso(Ingreso ingreso, Articulo articulo, Integer cantidad, Float precio) {
        this.ingreso = ingreso;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precio = precio;
    }

}
