package unam.dgtic.spv.core.service.Categoria;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unam.dgtic.spv.core.model.Categoria;
import unam.dgtic.spv.core.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService{
    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    public Page<Categoria> buscarCategoriasPaginado(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    @Override
    public List<Categoria> buscarCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public List<Categoria> buscarPorActivo() {
        return categoriaRepository.findByActivo(true);
    }

    @Override
    public Categoria buscarCategoriaPorId(Integer id) {
        Optional<Categoria> op = categoriaRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    public void borrar(Integer id) {
        categoriaRepository.deleteById(id);
    }
}
