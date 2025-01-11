package unam.dgtic.spv.core.service.Articulo;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unam.dgtic.spv.core.model.Articulo;
import unam.dgtic.spv.core.repository.ArticuloRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticuloServiceImpl implements ArticuloService{
    @Autowired
    ArticuloRepository articuloRepository;
    @Override
    public Page<Articulo> buscarArticulosPaginado(Pageable pageable) {
        return articuloRepository.findAll(pageable);
    }

    private Page<Articulo> buscarArticulosPorCategoria(Integer categoriaId, Pageable pageable) {
        return articuloRepository.findByCategoriaId(categoriaId, pageable);
    }
    @Override
    public Page<Articulo> buscarArticulosPorNombrePaginado(String searchNom, Pageable pageable) {
        return articuloRepository.findByNombreContaining(searchNom, pageable);
    }
    @Override
    public Page<Articulo> buscarArticulosPorCodigoPaginado(String searchCod, Pageable pageable) {
        return articuloRepository.findByCodigoContaining(searchCod, pageable);
    }


    @Override
    public List<Articulo> buscarArticulos() {
        return articuloRepository.findAll();
    }

    @Override
    public Articulo buscarArticuloPorCodigo(String codigo) {
        return articuloRepository.findFirstByCodigo(codigo);
    }

    @Override
    public Articulo buscarArticuloPorId(Integer id) {
        Optional<Articulo> op =articuloRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(Articulo articulo) {
        articuloRepository.save(articulo);
    }

    @Override
    public void borrar(Integer id) {
        articuloRepository.deleteById(id);
    }

    public Page<Articulo> buscarArticulosConFiltros(String search, Integer categoriaId, Pageable pageable) {
        if (categoriaId != null && categoriaId > 0) {
            return buscarArticulosPorCategoria(categoriaId, pageable);
        } else if (search.matches("^\\d+.*$")) {  // Comienza con uno o más dígitos numéricos
            return buscarArticulosPorCodigoPaginado(search, pageable);
        } else if (search.matches("^[a-zA-Z ]+$")) {  // Contiene solo letras (y espacios, opcionalmente)
            return buscarArticulosPorNombrePaginado(search, pageable);
        } else {
            return buscarArticulosPaginado(pageable);  // Retornamos  todos los arcticlulos si no coincide con ninguno de los casos
        }
    }

    public JasperPrint generateArticuloReport(List<Articulo> articulos) throws JRException {
        String reportSrcFile = "src/main/resources/static/reports/ReporteArticulo.jrxml";
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        Map<String, Object> parameters = new HashMap<>();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(articulos);
        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }
}
