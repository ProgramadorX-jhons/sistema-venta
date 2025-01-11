package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Float precio;

    @Column(nullable = false)
    private Float descuento;

    public DetalleVenta(Venta venta, Articulo articulo, Integer cantidad, Float precio, Float descuento) {
        this.venta = venta;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento= descuento;
    }
}
