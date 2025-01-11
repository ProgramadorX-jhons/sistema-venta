package unam.dgtic.spv.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import unam.dgtic.spv.core.model.Venta;


public interface VentaRepository extends JpaRepository<Venta,Integer> {
}
