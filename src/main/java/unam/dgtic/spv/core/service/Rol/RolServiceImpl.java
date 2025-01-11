package unam.dgtic.spv.core.service.Rol;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.dto.RolDTO;
import unam.dgtic.spv.core.exception.RolNotFoundException;
import unam.dgtic.spv.core.model.Rol;
import unam.dgtic.spv.core.repository.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;
    @Override
    public Page<Rol> buscarRolesPaginado(Pageable pageable) {
        return rolRepository.findAll(pageable);
    }

    @Override
    public List<Rol> buscarRoles() {
        return rolRepository.findAll();
    }
    @Override
    public Rol buscarRolPorId(Integer id) {
        Optional<Rol> op =rolRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(Rol rol) {
        rolRepository.save(rol);
    }

    @Override
    public void borrar(Integer id) {
        rolRepository.deleteById(id);
    }

    @Autowired
    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<RolDTO> findAll() {
        log.info("Service - UserInfoRoleServiceImpl.findAll");
        List<Rol> theList = rolRepository.findAllByOrderByIdAsc();
        return theList.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RolDTO> findAllOrderByUsrRoleName() {
        log.info("Service - UserInfoRoleServiceImpl.findAll");
        List<Rol> theList = rolRepository.findAllByOrderByNombreAsc();
        return theList.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public RolDTO findById(Integer id) throws RolNotFoundException {
        log.info("Service - UserInfoRoleServiceImpl.findById {}", id);
        Rol object = rolRepository.findById(id).orElseThrow(() ->
                new RolNotFoundException(id));
        return convertEntityToDTO(object);
    }

    @Override
    public RolDTO save(RolDTO role) {
        log.info("Service - UserInfoRoleServiceImpl.save object {} ", role);
        Rol finalStatus = convertDTOtoEntity(role);
        finalStatus = rolRepository.save(finalStatus);
        return convertEntityToDTO(finalStatus);
    }

    public RolDTO convertEntityToDTO(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        return dto;
    }

    public Rol convertDTOtoEntity(RolDTO rolDTO) {
        Rol entity = new Rol();
        entity.setId(rolDTO.getId());
        entity.setNombre(rolDTO.getNombre());
        entity.setDescripcion(rolDTO.getDescripcion());
        return entity;
    }
}
