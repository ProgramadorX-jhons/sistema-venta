package unam.dgtic.spv.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unam.dgtic.spv.core.model.DetalleIngreso;

import java.util.List;

public interface DetalleIngresoRepository extends JpaRepository<DetalleIngreso, Integer> {
    List<DetalleIngreso> findByIngresoId(Integer id);

}
