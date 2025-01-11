package unam.dgtic.spv.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import unam.dgtic.spv.core.model.Persona;

import java.util.List;

public interface PersonaRepository extends JpaRepository<Persona,Integer> {
    List<Persona> findByTipoPersona(String tipoPersona);
}
