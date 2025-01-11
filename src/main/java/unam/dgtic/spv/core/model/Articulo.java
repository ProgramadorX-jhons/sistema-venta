package unam.dgtic.spv.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articulo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "Categoria_id")
    @NotNull(message = "La categoría es obligatoria.")
    private Categoria categoria;

    @Column(name = "codigo", length = 50)
    @NotBlank(message = "El codigo es obligatorio.")
    @Size(max = 50, message = "El código no debe exceder los 50 caracteres.")
    private String codigo;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
    private String nombre;

    @Column(name = "precio_venta", nullable = false)
    @NotNull(message = "El precio de venta es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor que cero.")
    private Float precioVenta;

    @Column(name = "stock", nullable = false)
    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock;

    @Column(name = "descripcion")
    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres.")
    private String descripcion;

    @Column(name = "imagen", nullable = false, length = 50)
    private String imagen;

    @Column(name = "activo")
    @NotNull(message = "La categoría es obligatoria.")
    private Boolean activo;

    public Articulo(Integer id, String codigo, String nombre, Float precioVenta, Integer stock) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    public Articulo(String codigo, String nombre, Float precioVenta, Integer stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    /**
     * Este método es utilizado por JasperReports para obtener el nombre de la categoría.
     * @return Nombre de la categoría del artículo.
     */
    public boolean sinStock(){
        return this.stock<=0;
    }

    public void restarStock(Integer stock){
        this.stock -= stock;
    }

    public void sumarStock(Integer stock){
        this.stock += stock;
    }
    public String getCategoriaNombre() {
        return categoria != null ? categoria.getNombre() : null;
    }

    @Override
    public String toString() {
        return nombre;

    }
}
