package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "num_comprobante", length = 10, nullable = false)
    private String numComprobante;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;


    @Column(nullable = false )
    private Float total;

    @Column(length = 20, nullable = false)
    private String estado;

    @OneToMany(mappedBy = "venta", fetch = FetchType.EAGER)
    private Set<DetalleVenta> detalleVentas;
    private static final AtomicLong COUNTER= new  AtomicLong(0);


    public String generateComprobanteNumber() {
        // Formato de fecha y secuencia
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return datePart + "-" + COUNTER.incrementAndGet();
    }

    }