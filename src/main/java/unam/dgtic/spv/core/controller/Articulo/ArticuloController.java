package unam.dgtic.spv.core.controller.Articulo;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unam.dgtic.spv.core.model.Articulo;
import unam.dgtic.spv.core.model.Categoria;
import unam.dgtic.spv.core.service.Articulo.ArticuloService;
import unam.dgtic.spv.core.service.Categoria.CategoriaService;
import unam.dgtic.spv.core.util.Archivos;
import unam.dgtic.spv.core.util.RenderPagina;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping(value = "articulo")
@SessionAttributes("articulo")
public class  ArticuloController {
    @Autowired
    ArticuloService articuloService;

    @Autowired
    CategoriaService categoriaService;
    @Value("${ejemplo.imagen.ruta}")
    private String archivoRuta;


    @GetMapping("alta-articulo")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoAltaArticulo(Model model){
        model.addAttribute("contenido","Alta de un Articulo");
        model.addAttribute("articulo", new Articulo());
        model.addAttribute("selectCategoria", categoriaService.buscarPorActivo());
        //ruta del recurso
        return "articulo/alta-articulo";
    }

    @PostMapping(value = "salvar-imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@RequestParam("imagenarchivo") MultipartFile multipartFile
            , HttpSession session, Model model) {
        Articulo articulo = null;
        if (!multipartFile.isEmpty()) {
            String imagenNombre = Archivos.almacenar(multipartFile, archivoRuta);
            if (imagenNombre != null) {
                articulo = (Articulo) session.getAttribute("articulo");
                articulo.setImagen(imagenNombre);
            }
        }
        model.addAttribute("selectCategoria", categoriaService.buscarPorActivo());
        return "articulo/alta-articulo";
    }

    //Persistencia de datos en el intercambio de plantillas con RedirectAttributes
    @PostMapping("salvar-articulo")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvarObjectThymeleaf(@Valid @ModelAttribute("articulo") Articulo articulo,
                                        BindingResult result,
                                        Model model,
                                        RedirectAttributes flash,
                                        SessionStatus sesion) {
        if (result.hasErrors()) {
            model.addAttribute("contenido","Alta de un Articulo");
            model.addAttribute("selectCategoria", categoriaService.buscarPorActivo());
            return "articulo/alta-articulo";
        }
        articuloService.guardar(articulo);
        sesion.setComplete();
        flash.addFlashAttribute("success", "Articulo se almaceno con exito");
        return "redirect:/articulo/lista-articulo"; // Solo redireccionar si no hay errores
    }

    @GetMapping("/lista-articulo")
    public String listaArticulos(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "categoriaId", defaultValue = "") Integer categoriaId,
            Model model) {
        Pageable pagReq = PageRequest.of(page, pageSize);
        Page<Articulo> articuloEntities = articuloService.buscarArticulosConFiltros(search,categoriaId, pagReq);
        // Construir la base URL para la paginación y los filtros
        String baseUrl = "/articulo/lista-articulo?";
        StringBuilder filterParams = new StringBuilder(baseUrl);
        if (!search.isEmpty()) {
            filterParams.append("search=").append(search).append("&");
        }
        if (categoriaId != null) {
            filterParams.append("categoriaId=").append(categoriaId).append("&");
        }
        RenderPagina<Articulo> render = new RenderPagina<>(filterParams.toString(), articuloEntities);
        model.addAttribute("articulo", articuloEntities.getContent());
        model.addAttribute("categoria", categoriaService.buscarCategorias());
        model.addAttribute("page", render);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("search", search);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("contenido", "Lista de Artículos");
        return "/articulo/lista-articulo";
    }


    @GetMapping("modificar-articulo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String saltoModificar(@PathVariable("id") Integer id, Model model){
        Articulo articulo= articuloService.buscarArticuloPorId(id);
        List<Categoria> categoria= categoriaService.buscarPorActivo();
        model.addAttribute("articulo",articulo);
        model.addAttribute("contenido","Modificar Articulo");
        model.addAttribute("selectCategoria", categoria);
        return "/articulo/alta-articulo";
    }

    @GetMapping("eliminar-articulo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable("id") Integer id,RedirectAttributes flash){
        articuloService.borrar(id);
        flash.addFlashAttribute("success","Se borro con exito el Articulo");
        return "redirect:/articulo/lista-articulo";
    }
    @GetMapping("detalle-articulo/{id}")
    public String verDetallesArticulo(@PathVariable Integer id, Model model) {
        var articulo = articuloService.buscarArticuloPorId(id);
        if (articulo == null) {
            return "redirect:/404"; // Redirecciona a una p�gina de error si el art�culo no existe.
        }
        model.addAttribute("articulo", articulo);
        return "articulo/detalle-articulo"; // Nombre del archivo HTML de la vista (debe estar en src/main/resources/templates)
    }


    @GetMapping("descargar-reporte")
    public void descargarReporte(HttpServletResponse response) {
        try {
            List<Articulo> articulos = articuloService.buscarArticulos();
            JasperPrint jasperPrint = articuloService.generateArticuloReport(articulos);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=articulo_report.pdf");
            final OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}