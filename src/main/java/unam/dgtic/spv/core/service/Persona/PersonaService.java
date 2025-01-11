package unam.dgtic.spv.core.service.Persona;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.model.Persona;

import java.util.List;

public interface PersonaService {
    Page<Persona> buscarPersonasPaginado(Pageable pageable);
    List<Persona> buscarPersonas();
    Persona buscarPersonaPorId(Integer id);
    void guardar(Persona persona);
    void borrar(Integer id);
    List<Persona> buscarProveedores();
    List<Persona> buscarClientes();
}
