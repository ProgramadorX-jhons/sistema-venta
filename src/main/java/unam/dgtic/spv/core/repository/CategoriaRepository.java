package unam.dgtic.spv.core.repository;


import unam.dgtic.spv.core.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Definir una consulta derivada para buscar categorías activas
    List<Categoria> findByActivo(Boolean activo);
}
