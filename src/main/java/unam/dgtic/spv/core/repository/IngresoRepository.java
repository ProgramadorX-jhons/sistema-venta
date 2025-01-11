package unam.dgtic.spv.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import unam.dgtic.spv.core.model.Ingreso;

public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {
}
