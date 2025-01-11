package unam.dgtic.spv.core.service.DetalleIngreso;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.model.DetalleIngreso;

import java.util.List;

public interface DetalleIngresoService {
    Page<DetalleIngreso> buscarDetalleIngresosPaginado(Pageable pageable);
    List<DetalleIngreso> buscarDetalleIngreso();
    DetalleIngreso buscarDetalleIngresoPorId(Integer id);
    void guardar(DetalleIngreso detalleIngreso);
    void borrar(Integer id);
    List<DetalleIngreso> buscarPorIngresoId(Integer id);
}