package unam.dgtic.spv.core.service.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.dto.UsuarioDTO;
import unam.dgtic.spv.core.dto.RolDTO;
import unam.dgtic.spv.core.exception.UsuarioNotFoundException;
import unam.dgtic.spv.core.model.Usuario;
import unam.dgtic.spv.core.model.Rol;
import unam.dgtic.spv.core.repository.UsuarioRepository;
import unam.dgtic.spv.core.service.Rol.RolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolService rolService; //inyectamos el servicio!!! (DIP)

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder,
                              RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolService = rolService;
    }

    @Override
    public List<UsuarioDTO> findAll() {
        log.info("Service - UserInfoServiceImpl.findAll");
        List<Usuario> theList = usuarioRepository.findAllByOrderByIdAsc();
        return theList.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public Page<Usuario> buscarUsuariosPaginado(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public List<Usuario> buscarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarUsuarioPorId(Integer id) {
        Optional<Usuario> op= usuarioRepository.findById(id);
        return op.orElse(null);
    }

    public Usuario buscarUsuarioPorNombreCompleto(String fullName) {
        // Suponiendo que el nombre y el apellido están separados por un espacio
        String[] parts = fullName.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("El nombre completo debe contener al menos un nombre y un apellido.");
        }

        String firstName = parts[0];
        String lastName = parts[1];

        return usuarioRepository.findByNombreAndApellido(firstName, lastName);
    }

    @Override
    public UsuarioDTO findById(Integer id) throws UsuarioNotFoundException {
        log.info("Service - UserAdmin.findById {}", id);
        Usuario object = usuarioRepository.findById(id).orElseThrow(() ->
                new UsuarioNotFoundException(id));
        return convertEntityToDTO(object);
    }

    @Override
    public UsuarioDTO save(UsuarioDTO userAdmin) throws UsuarioNotFoundException {
        log.info("Service - UserAdmin.save");
        log.info("Service - UserAdmin.save object {} ", userAdmin);
        if (existsByUseEmail(userAdmin.getEmail()))
            throw new UsuarioNotFoundException(userAdmin.getEmail());
        userAdmin.setPassword(passwordEncoder.encode(userAdmin.getPassword()));
        Usuario finalStatus = convertDTOtoEntity(userAdmin);
        finalStatus = usuarioRepository.save(finalStatus);
        return convertEntityToDTO(finalStatus);
    }

    @Override
    public UsuarioDTO findByUseEmail(String email) throws UsuarioNotFoundException {
        Usuario object = usuarioRepository.findByEmail(email);
        if(object == null)
            throw new UsuarioNotFoundException(email);
        return convertEntityToDTO(object);
    }

    private boolean existsByUseEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    private UsuarioDTO convertEntityToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCurp(usuario.getCurp());
        dto.setDireccion(usuario.getDireccion());
        dto.setTelefono(usuario.getTelefono());
        dto.setEmail(usuario.getEmail());
        dto.setPassword(usuario.getPassword());
        dto.setActivo(usuario.getActivo());
        Set<RolDTO> userInfoRoles = new HashSet<>();
        for (Rol role : usuario.getUseInfoRoles()) {
            userInfoRoles.add(rolService.convertEntityToDTO(role));
        }
        dto.setUseInfoRoles(userInfoRoles);
        /*dto.setUseInfoRoles(userInfo.getUseInfoRoles()
                .stream()
                .map(userInfoRoleService::convertEntityToDTO)
                .collect(Collectors.toSet()));*/
        return dto;
    }

    private Usuario convertDTOtoEntity(UsuarioDTO usuarioDTO) {
        Usuario entity = new Usuario();
        entity.setId(usuarioDTO.getId());
        entity.setNombre(usuarioDTO.getNombre());
        entity.setApellido(usuarioDTO.getApellido());
        entity.setCurp(usuarioDTO.getCurp());
        entity.setDireccion(usuarioDTO.getDireccion());
        entity.setTelefono(usuarioDTO.getTelefono());
        entity.setEmail(usuarioDTO.getEmail());
        entity.setPassword(usuarioDTO.getPassword());
        entity.setActivo(usuarioDTO.getActivo());
        Set<Rol> rols = new HashSet<>();
        for (RolDTO role : usuarioDTO.getUseInfoRoles()) {
            rols.add(rolService.convertDTOtoEntity(role));
        }
        entity.setUseInfoRoles(rols);
        /*entity.setUseInfoRoles(userInfo.getUseInfoRoles()
                .stream()
                .map(userInfoRoleService::convertDTOtoEntity)
                .collect(Collectors.toSet()));*/
        return entity;
    }
}
