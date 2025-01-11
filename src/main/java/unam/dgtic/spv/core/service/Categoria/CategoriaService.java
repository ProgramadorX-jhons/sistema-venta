package unam.dgtic.spv.core.service.Categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unam.dgtic.spv.core.model.Categoria;

import java.util.List;

public interface CategoriaService {
    Page<Categoria> buscarCategoriasPaginado(Pageable pageable);
    List<Categoria> buscarCategorias();
    List<Categoria> buscarPorActivo();
    Categoria buscarCategoriaPorId(Integer id);
    void guardar(Categoria categoria);
    void borrar(Integer id);
}
