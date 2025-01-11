package unam.dgtic.spv.core.repository;

import unam.dgtic.spv.core.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    List<Rol> findAllByOrderByIdAsc();
    List<Rol> findAllByOrderByNombreAsc();
}
