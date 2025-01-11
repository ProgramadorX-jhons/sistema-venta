package unam.dgtic.spv.core.service.DetalleIngreso;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unam.dgtic.spv.core.model.DetalleIngreso;
import unam.dgtic.spv.core.repository.DetalleIngresoRepository;

import java.util.List;
import java.util.Optional;
@Service
public class DetalleIngresoServiceImpl implements DetalleIngresoService{
    @Autowired
    DetalleIngresoRepository detalleIngresoRepository;
    @Override
    public Page<DetalleIngreso> buscarDetalleIngresosPaginado(Pageable pageable) {
        return detalleIngresoRepository.findAll(pageable);
    }

    @Override
    public List<DetalleIngreso> buscarDetalleIngreso() {
        return detalleIngresoRepository.findAll();
    }

    @Override
    public DetalleIngreso buscarDetalleIngresoPorId(Integer id) {
        Optional<DetalleIngreso> op = detalleIngresoRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(DetalleIngreso detalleIngreso) {
        detalleIngresoRepository.save(detalleIngreso);
    }

    @Override
    public void borrar(Integer id) {
        detalleIngresoRepository.deleteById(id);
    }

    @Override
    public List<DetalleIngreso> buscarPorIngresoId(Integer id) {
        return detalleIngresoRepository.findByIngresoId(id);
    }
}
