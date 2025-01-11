package unam.dgtic.spv.core.service.Persona;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unam.dgtic.spv.core.model.Persona;
import unam.dgtic.spv.core.repository.PersonaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaServiceImpl implements PersonaService{
    @Autowired
    PersonaRepository personaRepository;

    @Override
    public Page<Persona> buscarPersonasPaginado(Pageable pageable) {
        return personaRepository.findAll(pageable);
    }

    @Override
    public List<Persona> buscarPersonas() {
        return personaRepository.findAll();
    }

    @Override
    public Persona buscarPersonaPorId(Integer id) {
        Optional<Persona> op = personaRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(Persona persona) {
        personaRepository.save(persona);
    }

    @Override
    public void borrar(Integer id) {
        personaRepository.deleteById(id);
    }

    @Override
    public List<Persona> buscarProveedores() {
        return personaRepository.findByTipoPersona("Proveedor");
    }

    @Override
    public List<Persona> buscarClientes() {
        return personaRepository.findByTipoPersona("Cliente");
    }
}
