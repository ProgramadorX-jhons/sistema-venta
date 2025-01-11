package unam.dgtic.spv.core.service.Rol;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.dto.RolDTO;
import unam.dgtic.spv.core.exception.RolNotFoundException;
import unam.dgtic.spv.core.model.Rol;

import java.util.List;

public interface RolService {
    Page<Rol> buscarRolesPaginado(Pageable pageable);
    List<Rol> buscarRoles();
    Rol buscarRolPorId(Integer id);
    void guardar(Rol rol);
    void borrar(Integer id);
    List<RolDTO> findAll();
    List<RolDTO> findAllOrderByUsrRoleName();
    RolDTO findById(Integer id) throws RolNotFoundException;
    RolDTO save(RolDTO role);
    RolDTO convertEntityToDTO(Rol userInfo);
    Rol convertDTOtoEntity(RolDTO userInfo);
}
