package unam.dgtic.spv.core.service.Ingreso;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.model.Ingreso;

import java.util.List;

public interface IngresoService {
    Page<Ingreso> buscarIngresosPaginado(Pageable pageable);
    List<Ingreso> buscarIngresos();
    Ingreso buscarIngresoPorId(Integer id);
    void guardar(Ingreso ingreso);
    void borrar(Integer id);
}
