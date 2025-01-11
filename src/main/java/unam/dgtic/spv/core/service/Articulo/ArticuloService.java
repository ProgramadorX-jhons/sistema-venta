package unam.dgtic.spv.core.service.Articulo;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.model.Articulo;

import java.util.List;

public interface ArticuloService {
    Page<Articulo> buscarArticulosPaginado(Pageable pageable);
    Page<Articulo> buscarArticulosPorNombrePaginado(String searchNom, Pageable pageable);
    Page<Articulo> buscarArticulosPorCodigoPaginado(String seachCod, Pageable pageable);
    List<Articulo> buscarArticulos();
    Articulo buscarArticuloPorCodigo(String codigo);
    Articulo buscarArticuloPorId(Integer id);
    void guardar(Articulo articulo);
    void borrar(Integer id);

    Page<Articulo> buscarArticulosConFiltros(String search, Integer categoriaId, Pageable pageable);

    JasperPrint generateArticuloReport(List<Articulo> articulos) throws JRException;
}
