package unam.dgtic.spv.core.controller.Venta;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unam.dgtic.spv.core.model.*;
import unam.dgtic.spv.core.service.Articulo.ArticuloService;
import unam.dgtic.spv.core.service.DetalleVenta.DetalleVentaService;
import unam.dgtic.spv.core.service.Persona.PersonaService;
import unam.dgtic.spv.core.service.Usuario.UsuarioService;
import unam.dgtic.spv.core.service.Venta.VentaService;
import unam.dgtic.spv.core.util.DateUtils;
import unam.dgtic.spv.core.util.RenderPagina;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "venta")
public class VentaControler {
    @Autowired
    VentaService ventaService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    PersonaService personaService;
    @Autowired
    ArticuloService articuloService;
    @Autowired
    DetalleVentaService detalleVentaService;



    @GetMapping("alta-venta")
    public String interfazAltaVenta(Model model, HttpServletRequest request) {
        float total = 0;
        model.addAttribute("articulo", new Articulo());
        model.addAttribute("venta", new Venta());
        model.addAttribute("personas", personaService.buscarClientes());
        model.addAttribute("contenido", "Ventas");
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarrito(request);
        for (ArticuloParaVender p : carrito) total += p.getTotal();
        model.addAttribute("total", total);
        //ruta del recurso
        return "/venta/alta-venta";
    }

    @GetMapping("modificar-venta/{id}")
    public String saltoModificar(@PathVariable("id") Integer id, Model model) {
        Venta venta = ventaService.buscarVentaPorId(id);
        List<Persona> personas = personaService.buscarClientes();
        model.addAttribute("contenido", "Modificar Venta");
        model.addAttribute("venta", venta);
        model.addAttribute("personas", personas);
        return "/venta/alta-venta";
    }

    @GetMapping("eliminar-venta/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes flash) {
        ventaService.borrar(id);
        flash.addFlashAttribute("success", "Se borro con exito la Venta");
        return "redirect:/venta/lista-venta";
    }

    //--------------------------------------Logica de Venta-------------------------------------------

    private ArrayList<ArticuloParaVender> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ArticuloParaVender> carrito = (ArrayList<ArticuloParaVender>) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    private void guardarCarrito(ArrayList<ArticuloParaVender> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }

    //Agregar un articulo al Carrito
    @PostMapping(value = "/agregar")
    public String agregarAlCarrito(@ModelAttribute Articulo articulo,
                                   HttpServletRequest request,
                                   RedirectAttributes redirectAttrs) {
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarrito(request);
        Articulo productoBuscadoPorCodigo = articuloService.buscarArticuloPorCodigo(articulo.getCodigo());
        if (productoBuscadoPorCodigo == null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El articulo con el código " + articulo.getCodigo() + " no existe")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/venta/alta-venta";
        }
        if (productoBuscadoPorCodigo.sinStock()) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El articulo está agotado")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/venta/alta-venta";
        }
        boolean encontrado = false;
        for (ArticuloParaVender productoParaVenderActual : carrito) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                productoParaVenderActual.aumentarCantidad();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            carrito.add(new ArticuloParaVender(productoBuscadoPorCodigo.getId(), productoBuscadoPorCodigo.getCodigo(), productoBuscadoPorCodigo.getNombre(), productoBuscadoPorCodigo.getPrecioVenta(), productoBuscadoPorCodigo.getStock(), 1));
        }
        this.guardarCarrito(carrito, request);
        return "redirect:/venta/alta-venta";
    }

    @PostMapping(value = "/quitar/{indice}")
    public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarrito(request);
        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
            carrito.remove(indice);
            this.guardarCarrito(carrito, request);
        }
        return "redirect:/venta/alta-venta";
    }

    private void limpiarCarrito(HttpServletRequest request) {
        this.guardarCarrito(new ArrayList<>(), request);
    }

    @GetMapping(value = "/limpiar")
    public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        this.limpiarCarrito(request);
        redirectAttrs
                .addFlashAttribute("mensaje", "Venta cancelada")
                .addFlashAttribute("clase", "info");
        return "redirect:/venta/alta-venta";
    }

    @PostMapping(value = "/terminar")
    public String terminarVenta(@Valid @ModelAttribute("venta") Venta venta, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarrito(request);
        // Si no hay carrito o está vacío, regresamos inmediatamente
        if (carrito == null || carrito.size() <= 0) {
            return "redirect:/venta/alta-venta";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        venta.setFecha(new Date());
        venta.setNumComprobante(venta.generateComprobanteNumber());
        float total = 0;
        for (ArticuloParaVender p : carrito) total += p.getTotal();
        venta.setTotal(total);
        log.info(auth.getName());
        venta.setUsuario(usuarioService.buscarUsuarioPorNombreCompleto(auth.getName()));
        log.info(usuarioService.buscarUsuarioPorNombreCompleto(auth.getName()).toString());
        ventaService.guardar(venta);
        // Recorrer el carrito
        for (ArticuloParaVender articuloParaVender : carrito) {
            // Obtener el producto fresco desde la base de datos
            Articulo a = articuloService.buscarArticuloPorId(articuloParaVender.getId());
            if (a == null) continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
            // Le restamos existencia
            a.restarStock(articuloParaVender.getCantidad());
            // Lo guardamos con la existencia ya restada
            articuloService.guardar(a);
            // Creamos un nuevo producto que será el que se guarda junto con la venta
            DetalleVenta detalleVenta = new DetalleVenta(venta, a, articuloParaVender.getCantidad(), articuloParaVender.getPrecioVenta(), 0.0f);
            // Y lo guardamos
            detalleVentaService.guardar(detalleVenta);
        }

        // Al final limpiamos el carrito
        this.limpiarCarrito(request);
        // e indicamos una venta exitosa
        redirectAttrs
                .addFlashAttribute("mensaje", "Venta realizada correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/venta/alta-venta";
    }

    @GetMapping("lista-venta")
    public String interfazListaVenta(@RequestParam(name = "page", defaultValue = "0") int page,
                                     Model model, HttpServletRequest request) {
        Pageable pagReq = PageRequest.of(page, 10);
        Page<Venta> ventaEntities = ventaService.buscarVentasPaginado(pagReq);
        RenderPagina<Venta> render = new RenderPagina<>("lista-venta", ventaEntities);
        model.addAttribute("venta", ventaEntities);
        model.addAttribute("page", render);
        model.addAttribute("detalleVentas", detalleVentaService.buscarDetalleVentas());
        model.addAttribute("articulo", articuloService.buscarArticulos());
        model.addAttribute("usuario", usuarioService.buscarUsuarios());
        model.addAttribute("contenido", "Ventas");
        ArrayList<ArticuloParaVender> carrito = this.obtenerCarrito(request);
        //ruta del recurso
        return "/venta/lista-venta";
    }

    @GetMapping("detalles-venta/{id}")
    public String mostrarDetallesVenta(@PathVariable("id") Integer ventaId, Model model) {
        Venta venta = ventaService.buscarVentaPorId(ventaId);
        // Asumiendo que el servicio puede lanzar una excepción si no se encuentra la venta
        model.addAttribute("venta", venta);
        model.addAttribute("detalleVenta", detalleVentaService.buscarPorVentaId(ventaId));
        model.addAttribute("fecha", DateUtils.formatDateString(venta.getFecha().toString()));
        return "venta/detalles-venta";
    }

    @GetMapping("generarTicket/{id}")
    public void descargarReporteVenta(@PathVariable("id") Integer ventaId,
                                      HttpServletResponse response) throws JRException, IOException {
        try {
            JasperPrint jasperPrint = ventaService.generateVentaReport(ventaId);
            //Indicamos el tipo de archivo
            response.setContentType("application/pdf");
            //Indicamos como se mostrara el archivo y que nombre tendra
            response.setHeader("Content-Disposition", "inline; filename=reporte_venta_" + ventaId + ".pdf");
            final OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (Exception e) {
            e.printStackTrace();
            // Apropiado manejo de errores
        }
    }
}