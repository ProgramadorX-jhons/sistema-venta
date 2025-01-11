package unam.dgtic.spv.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import unam.dgtic.spv.core.model.DetalleVenta;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    List<DetalleVenta> findByVentaId(Integer id);
}
