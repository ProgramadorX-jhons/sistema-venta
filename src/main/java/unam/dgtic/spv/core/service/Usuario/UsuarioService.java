package unam.dgtic.spv.core.service.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.dto.UsuarioDTO;
import unam.dgtic.spv.core.exception.UsuarioNotFoundException;
import unam.dgtic.spv.core.model.Usuario;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    Page<Usuario> buscarUsuariosPaginado(Pageable pageable);
    List<Usuario> buscarUsuarios();
    Usuario buscarUsuarioPorId(Integer id);
    public Usuario buscarUsuarioPorNombreCompleto(String fullName);


    UsuarioDTO findById(Integer id) throws UsuarioNotFoundException;
    UsuarioDTO save(UsuarioDTO userAdmin) throws UsuarioNotFoundException;
    UsuarioDTO findByUseEmail(String email) throws UsuarioNotFoundException;
}
