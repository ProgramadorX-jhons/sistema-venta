package unam.dgtic.spv.core.controller.Categoria;


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
import unam.dgtic.spv.core.model.Categoria;
import unam.dgtic.spv.core.service.Categoria.CategoriaService;
import unam.dgtic.spv.core.util.RenderPagina;

@Controller
@RequestMapping(value = "categoria")
@SessionAttributes("categoria")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    @GetMapping("alta-categoria")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoAltaCategoria(Model model){
        model.addAttribute("contenido","Alta de una Categoria");
        model.addAttribute("categoria", new Categoria());
        //ruta del recurso
        return "categoria/alta-categoria";
    }
    //Persistencia de datos en el intercambio de plantillas con RedirectAttributes
    @PostMapping("salvar-categoria")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvarObjectThymeleaf(@Valid @ModelAttribute("categoria") Categoria categoria,
                                        BindingResult result,
                                        Model model,
                                        SessionStatus sesion,
                                        RedirectAttributes flash){
        if (result.hasErrors()) {
            model.addAttribute("contenido","Alta de una Categoria");
            return "categoria/alta-categoria";
        }
        categoriaService.guardar(categoria);
        sesion.setComplete();
        flash.addFlashAttribute("success","Categoria guardado con éxito");
        return "redirect:/categoria/alta-categoria";
    }

    @GetMapping("lista-categoria")
    public String listaCategoria(@RequestParam(name="page",defaultValue = "0")int page,
                                 Model model){
        Pageable pagReq= PageRequest.of(page,15);
        //Solo mostramos los articulos que estan activos
        Page<Categoria> categoriaEntities= categoriaService.buscarCategoriasPaginado(pagReq);
        RenderPagina<Categoria> render=new RenderPagina<>("lista-categoria", categoriaEntities);
        model.addAttribute("categoria",categoriaEntities);
        model.addAttribute("page",render);
        model.addAttribute("contenido","Lista de Categorias");
        return "/categoria/lista-categoria";
    }
    @GetMapping("modificar-categoria/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoModificar(@PathVariable("id") Integer id, Model model){
        Categoria categoria= categoriaService.buscarCategoriaPorId(id);
        model.addAttribute("contenido","Modificar Categoria");
        model.addAttribute("categoria", categoria);
        return "/categoria/alta-categoria";
    }

    @GetMapping("eliminar-categoria/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable("id") Integer id,RedirectAttributes flash){
        categoriaService.borrar(id);
        flash.addFlashAttribute("success","Se borro con exito la Categoria");
        return "redirect:/categoria/lista-categoria";
    }
}