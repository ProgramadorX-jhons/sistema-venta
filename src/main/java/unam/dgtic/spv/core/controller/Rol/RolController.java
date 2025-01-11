package unam.dgtic.spv.core.controller.Rol;


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
import unam.dgtic.spv.core.model.Rol;
import unam.dgtic.spv.core.service.Rol.RolService;
import unam.dgtic.spv.core.util.RenderPagina;

@Controller
@RequestMapping(value = "rol")
public class RolController {
    @Autowired
    RolService rolService;

    @GetMapping("alta-rol")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoAltaRol(Model model) {
        model.addAttribute("contenido", "Alta de un Rol");
        model.addAttribute("rol", new Rol());
        //ruta del recurso
        return "rol/alta-rol";
    }

    //Persistencia de datos en el intercambio de plantillas con RedirectAttributes
    @PostMapping("salvar-rol")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvarRol(@Valid @ModelAttribute("rol") Rol rol,
                            BindingResult result,
                            Model model,
                            RedirectAttributes flash,
                            SessionStatus sesion) {
        if (result.hasErrors()){
            model.addAttribute("contenido", "Alta de un Rol");
            return "rol/alta-rol";
        }
        rolService.guardar(rol);
        sesion.setComplete();
        flash.addFlashAttribute("success", "Rol guardado con éxito");
        return "redirect:/rol/alta-rol";
    }

    @GetMapping("lista-rol")
    @PreAuthorize("hasRole('ADMIN')")
    public String listaRol(@RequestParam(name = "page", defaultValue = "0") int page,
                               Model model) {
        Pageable pagReq = PageRequest.of(page, 15);
        Page<Rol> rolEntities = rolService.buscarRolesPaginado(pagReq);
        RenderPagina<Rol> render = new RenderPagina<>("lista-rol", rolEntities);
        model.addAttribute("rol", rolEntities);
        model.addAttribute("page", render);
        model.addAttribute("contenido", "Lista de Roles");
        return "rol/lista-rol";
    }

    @GetMapping("modificar-rol/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoModificar(@PathVariable("id") Integer id, Model model) {
        Rol rol = rolService.buscarRolPorId(id);
        model.addAttribute("contenido", "Modificar Rol");
        model.addAttribute("rol", rol);
        return "rol/alta-rol";
    }

    @GetMapping("eliminar-rol/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes flash) {
        rolService.borrar(id);
        flash.addFlashAttribute("success", "Se borro con exito el Rol");
        return "redirect:rol/lista-rol";
    }
}