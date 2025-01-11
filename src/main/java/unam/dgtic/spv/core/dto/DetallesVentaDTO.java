package unam.dgtic.spv.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallesVentaDTO {
    private Integer cantidad;
    private String nombreArticulo;
    private Float precio;
    private String numComprobante;
    private Date fechaVenta;
    private String nombreUsuario;
    private String apellidoUsuario;
}
