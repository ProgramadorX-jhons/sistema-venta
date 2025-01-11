package unam.dgtic.spv.core.service.DetalleVenta;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.model.DetalleVenta;

import java.util.List;

public interface DetalleVentaService {
    Page<DetalleVenta> buscarDetalleVentasPaginado(Pageable pageable);
    List<DetalleVenta> buscarDetalleVentas();
    DetalleVenta buscarDetalleVentaPorId(Integer id);
    void guardar(DetalleVenta detalleVentas);
    void borrar(Integer id);
    List<DetalleVenta> buscarPorVentaId(Integer id);
}