package unam.dgtic.spv.core.controller.Usuario;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unam.dgtic.spv.core.dto.RolDTO;
import unam.dgtic.spv.core.dto.UsuarioDTO;
import unam.dgtic.spv.core.exception.UsuarioNotFoundException;
import unam.dgtic.spv.core.model.Rol;
import unam.dgtic.spv.core.model.Usuario;
import unam.dgtic.spv.core.service.Rol.RolService;
import unam.dgtic.spv.core.service.Usuario.UsuarioService;
import unam.dgtic.spv.core.util.RenderPagina;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "usuario")
@SessionAttributes("usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;

    @GetMapping("alta-usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoAltaUsuario(Model model) {
        model.addAttribute("contenido", "Alta de un Usuario");
        model.addAttribute("usuario", new Usuario());
        return "usuario/alta-usuario";
    }
    @PostMapping("salvar-usuario")
    public String salvarUsuario(@Valid @ModelAttribute("usuario")Usuario usuario,
                                BindingResult result,
                                Model model,
                                RedirectAttributes flash,
                                SessionStatus sesion) throws UsuarioNotFoundException {
        if (result.hasErrors()) {
            model.addAttribute("contenido","Alta de un Usuario");
            return "usuario/alta-usuario";
        }
        UsuarioDTO user = this.convertEntityToDTO(usuario);
        Set<RolDTO> roles = new HashSet<>();
        roles.add(RolDTO.builder().id(1).build());
        user.setUseInfoRoles(roles);
        usuarioService.save(user);
        sesion.setComplete();
        flash.addFlashAttribute("success", "Usuario guardado con éxito");
        return "redirect:/usuario/alta-usuario";
    }

    @GetMapping("lista-usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public String listaUsuario(@RequestParam(name = "page", defaultValue = "0") int page,
                           Model model) {
        Pageable pagReq  = PageRequest.of(page, 15);
        Page<Usuario> usuarioEntities = usuarioService.buscarUsuariosPaginado(pagReq);
        RenderPagina<Usuario> render = new RenderPagina<>("lista-usuario", usuarioEntities);
        model.addAttribute("usuario", usuarioEntities);
        model.addAttribute("page", render);
        model.addAttribute("contenido", "Lista de Usuarios");
        return "/usuario/lista-usuario";
    }

    @GetMapping("modificar-usuario/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoModificar(@PathVariable("id") Integer id,
                                 Model model) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        model.addAttribute("contenido", "Modificar Usuario");
        model.addAttribute("usuario", usuario);
        return "/usuario/alta-usuario";
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
        return dto;
    }
}
