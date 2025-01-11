package unam.dgtic.spv.core.service.Venta;


import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unam.dgtic.spv.core.model.Venta;
import unam.dgtic.spv.core.repository.VentaRepository;
import unam.dgtic.spv.core.service.DetalleVenta.DetalleVentaService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    DetalleVentaService detalleVentaService;
    @Autowired
    private DataSource dataSource;
    @Override
    public Page<Venta> buscarVentasPaginado(Pageable pageable) {
        return ventaRepository.findAll(pageable);
    }

    @Override
    public List<Venta> buscarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta buscarVentaPorId(Integer id) {
        Optional<Venta> op = ventaRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(Venta venta) {
        ventaRepository.save(venta);
    }

    @Override
    public void borrar(Integer id) {
        ventaRepository.deleteById(id) ;
    }



    public JasperPrint generateVentaReport(Integer ventaId) throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String reportSrcFile = "src/main/resources/static/reports/DetalleVenta.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
            // Preparar los parámetros si es necesario, por ejemplo, pasando el ID de venta si se necesita filtrar en el reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("VentaID", ventaId);  // Pasamos el unico parametro del reporte
            //LLenamos el reporte con los datos
            return JasperFillManager.fillReport(jasperReport, parameters, connection);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
