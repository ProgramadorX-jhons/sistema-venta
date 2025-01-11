package unam.dgtic.spv.core.controller.Ingreso;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unam.dgtic.spv.core.model.Articulo;
import unam.dgtic.spv.core.model.ArticuloParaVender;
import unam.dgtic.spv.core.model.DetalleIngreso;
import unam.dgtic.spv.core.model.Ingreso;
import unam.dgtic.spv.core.service.Articulo.ArticuloService;
import unam.dgtic.spv.core.service.DetalleIngreso.DetalleIngresoService;
import unam.dgtic.spv.core.service.Ingreso.IngresoService;
import unam.dgtic.spv.core.service.Persona.PersonaService;
import unam.dgtic.spv.core.service.Usuario.UsuarioService;
import unam.dgtic.spv.core.util.DateUtils;
import unam.dgtic.spv.core.util.RenderPagina;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "ingreso")
@SessionAttributes("ingreso")
public class IngresoController {
    @Autowired
    PersonaService personaService;
    @Autowired
    ArticuloService articuloService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    IngresoService ingresoService;
    @Autowired
    DetalleIngresoService detalleIngresoService;
    @GetMapping("alta-ingreso")
    @PreAuthorize("hasRole('ADMIN')")
    public String interfazAltaIngreso(Model model, HttpServletRequest request) {
        float total=0;
        model.addAttribute("articulo", new Articulo());
        model.addAttribute("ingreso", new Ingreso());
        model.addAttribute("personas", personaService.buscarProveedores());
        model.addAttribute("contenido","Ingreso a Almacen");
        return "/ingreso/alta-ingreso";
    }
    @PostMapping(value = "/agregar")
    public String agregarAlCarritoIngreso(@ModelAttribute Articulo articulo,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttrs) {
        ArrayList<ArticuloParaVender> carritoIngreso = this.obtenerCarritoIngreso(request);
        Articulo productoBuscadoPorCodigo = articuloService.buscarArticuloPorCodigo(articulo.getCodigo());
        if (productoBuscadoPorCodigo == null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El articulo con el código " + articulo.getCodigo() + " no existe")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/ingreso/alta-ingreso";
        }

        boolean encontrado = false;
        for (ArticuloParaVender productoParaVenderActual : carritoIngreso) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                productoParaVenderActual.aumentarCantidad();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            carritoIngreso.add(new ArticuloParaVender(productoBuscadoPorCodigo.getId(), productoBuscadoPorCodigo.getCodigo(), productoBuscadoPorCodigo.getNombre(), productoBuscadoPorCodigo.getPrecioVenta(), productoBuscadoPorCodigo.getStock(), 1));
        }
        this.guardarCarritoIngreso(carritoIngreso, request);
        return "redirect:/ingreso/alta-ingreso";
    }

    @PostMapping(value = "/quitar/{indice}")
    public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarritoIngreso(request);
        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
            carrito.remove(indice);
            this.guardarCarritoIngreso(carrito, request);
        }
        return "redirect:/ingreso/alta-ingreso";
    }

    @GetMapping(value = "/limpiar")
    public String cancelarIngreso(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        this.limpiarCarrito(request);
        redirectAttrs
                .addFlashAttribute("mensaje", "Ingreso cancelado")
                .addFlashAttribute("clase", "info");
        return "redirect:/ingreso/alta-ingreso";
    }

    @PostMapping(value = "/terminar")
    public String terminarIngreso(@Valid @ModelAttribute("ingreso") Ingreso ingreso,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttrs) {
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarritoIngreso(request);
        // Si no hay carrito o está vacío, regresamos inmediatamente
        if (carrito == null || carrito.size() <= 0) {
            return "redirect:/ingreso/alta-ingreso";
        }
        float total = 0;
        ingreso.setNumComprobante(ingreso.generateRandomNumber());
        for (ArticuloParaVender p : carrito) total += p.getTotal();
        ingreso.setTotal(total);
        ingreso.setUsuario(usuarioService.buscarUsuarioPorId(1));
        ingresoService.guardar(ingreso);
        // Recorrer el carrito
        for (ArticuloParaVender articuloParaVender : carrito) {
            // Obtener el producto fresco desde la base de datos
            Articulo a = articuloService.buscarArticuloPorId(articuloParaVender.getId());
            if (a == null) continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
            // Le sumamos existencia
            a.sumarStock(articuloParaVender.getCantidad());
            // Lo guardamos con la existencia ya sumada
            articuloService.guardar(a);
            // Creamos un nuevo producto que será el que se guarda junto con la ingreso
            DetalleIngreso detalleIngreso = new DetalleIngreso(ingreso, a, articuloParaVender.getCantidad(), articuloParaVender.getPrecioVenta());
            // Y lo guardamos
            detalleIngresoService.guardar(detalleIngreso);
        }

        // Al final limpiamos el carrito
        this.limpiarCarrito(request);
        // e indicamos una ingreso exitosa
        redirectAttrs
                .addFlashAttribute("mensaje", "Ingreso realizada correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/ingreso/alta-ingreso";
    }

    @GetMapping("lista-ingreso")
    public String interfazListaVenta(@RequestParam(name = "page", defaultValue = "0") int page,
                                     Model model, HttpServletRequest request) {
        Pageable pagReq = PageRequest.of(page, 10);
        Page<Ingreso> ingresoEntities = ingresoService.buscarIngresosPaginado(pagReq);
        RenderPagina<Ingreso> render = new RenderPagina<>("lista-ingreso", ingresoEntities);
        model.addAttribute("ingreso", ingresoEntities);
        model.addAttribute("page", render);
        model.addAttribute("detalleIngreso", detalleIngresoService.buscarDetalleIngreso());
        model.addAttribute("articulo", articuloService.buscarArticulos());
        model.addAttribute("usuario", usuarioService.buscarUsuarios());
        model.addAttribute("contenido", "Ingresos");
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarritoIngreso(request);
        //ruta del recurso
        return "/ingreso/lista-ingreso";
    }
    @GetMapping("detalle-ingreso/{id}")
    public String mostrarDetalleIngreso(@PathVariable("id") Integer ingresoId, Model model) {
        Ingreso ingreso = ingresoService.buscarIngresoPorId(ingresoId);
        model.addAttribute("ingreso", ingreso);
        model.addAttribute("detalleIngreso", detalleIngresoService.buscarPorIngresoId(ingresoId));
        model.addAttribute("fecha", DateUtils.formatDateString(ingreso.getFecha().toString()));
        return "ingreso/detalle-ingreso";
    }




    private ArrayList<ArticuloParaVender> obtenerCarritoIngreso(HttpServletRequest request) {
        ArrayList<ArticuloParaVender> carritoIngreso = (ArrayList<ArticuloParaVender>) request.getSession().getAttribute("carritoIngreso");
        if (carritoIngreso == null) {
            carritoIngreso = new ArrayList<>();
        }
        return carritoIngreso;
    }
    private void limpiarCarrito(HttpServletRequest request) {
        this.guardarCarritoIngreso(new ArrayList<>(), request);
    }

    private void guardarCarritoIngreso(ArrayList<ArticuloParaVender> carritoIngreso, HttpServletRequest request) {
        request.getSession().setAttribute("carritoIngreso", carritoIngreso);
    }
}
