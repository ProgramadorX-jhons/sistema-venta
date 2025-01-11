package unam.dgtic.spv.core.service.Venta;


import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.model.Venta;

import java.util.List;

public interface VentaService {
    Page<Venta> buscarVentasPaginado(Pageable pageable);
    List<Venta> buscarVentas();
    Venta buscarVentaPorId(Integer id);
    void guardar(Venta venta);
    void borrar(Integer id);
    JasperPrint generateVentaReport (Integer ventaId) throws Exception;
}