package unam.dgtic.spv.core.service.Ingreso;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unam.dgtic.spv.core.model.Ingreso;
import unam.dgtic.spv.core.repository.IngresoRepository;

import java.util.List;
import java.util.Optional;
@Service
public class IngresoServiceImpl implements IngresoService{
    @Autowired
    IngresoRepository ingresoRepository;
    @Override
    public Page<Ingreso> buscarIngresosPaginado(Pageable pageable) {
        return ingresoRepository.findAll(pageable);
    }

    @Override
    public List<Ingreso> buscarIngresos() {
        return ingresoRepository.findAll();
    }

    @Override
    public Ingreso buscarIngresoPorId(Integer id) {
        Optional<Ingreso> op = ingresoRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(Ingreso ingreso) {
        ingresoRepository.save(ingreso);
    }

    @Override
    public void borrar(Integer id) {
        ingresoRepository.deleteById(id);
    }
}
