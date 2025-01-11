package unam.dgtic.spv.core.service.DetalleVenta;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unam.dgtic.spv.core.model.DetalleVenta;
import unam.dgtic.spv.core.repository.DetalleVentaRepository;

import java.util.List;
import java.util.Optional;
@Service
public class DetalleVentaServiceImpl implements DetalleVentaService{
    @Autowired
    DetalleVentaRepository detalleVentaRepository;
    @Override
    public Page<DetalleVenta> buscarDetalleVentasPaginado(Pageable pageable) {
        return detalleVentaRepository.findAll(pageable);
    }

    @Override
    public List<DetalleVenta> buscarDetalleVentas() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta buscarDetalleVentaPorId(Integer id) {
        Optional<DetalleVenta> op = detalleVentaRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(DetalleVenta detalleVentas) {
        detalleVentaRepository.save(detalleVentas);
    }

    @Override
    public void borrar(Integer id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    public List<DetalleVenta> buscarPorVentaId(Integer id) {
        return detalleVentaRepository.findByVentaId(id);
    }
}