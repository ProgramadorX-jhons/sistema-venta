package unam.dgtic.spv.core.controller.Persona;


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
import unam.dgtic.spv.core.model.Persona;
import unam.dgtic.spv.core.service.Persona.PersonaService;
import unam.dgtic.spv.core.util.RenderPagina;

@Controller
@RequestMapping(value = "persona")
public class PersonaController {
    @Autowired
    PersonaService personaService;


    @GetMapping("alta-persona")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoAltaPersona(Model model) {
        model.addAttribute("contenido", "Alta de una Persona");
        model.addAttribute("persona", new Persona());
        return "/persona/alta-persona";
    }

    //Persistencia de datos en el intercambio de plantillas con RedirectAttributes
    @PostMapping("salvar-persona")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvarPersona(@Valid @ModelAttribute("persona") Persona persona,
                                BindingResult result,
                                Model model,
                                SessionStatus sesion,
                                RedirectAttributes flash) {
        if (result.hasErrors()){
            model.addAttribute("contenido", "Alta de una Persona");
            return "persona/alta-persona";
        }
        personaService.guardar(persona);
        sesion.setComplete();
        flash.addFlashAttribute("success", "Persona guardado con éxito");
        return "redirect:/persona/alta-persona";
    }

    @GetMapping("lista-persona")
    public String listaPersona(@RequestParam(name = "page", defaultValue = "0") int page,
                                 Model model) {
        Pageable pagReq = PageRequest.of(page, 15);
        //Solo mostramos los articulos que estan activos
        Page<Persona> personaEntities = personaService.buscarPersonasPaginado(pagReq);
        RenderPagina<Persona> render = new RenderPagina<>("lista-persona", personaEntities);
        model.addAttribute("persona", personaEntities);
        model.addAttribute("page", render);
        model.addAttribute("contenido", "Lista de Personas");
        return "/persona/lista-persona";
    }

    @GetMapping("modificar-persona/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoModificar(@PathVariable("id") Integer id, Model model) {
        Persona persona = personaService.buscarPersonaPorId(id);
        model.addAttribute("contenido", "Modificar Persona");
        model.addAttribute("persona", persona);
        return "/persona/alta-persona";
    }

    @GetMapping("eliminar-persona/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes flash) {
        personaService.borrar(id);
        flash.addFlashAttribute("success", "Se borro con exito la Persona");
        return "redirect:/persona/lista-persona";
    }
}

