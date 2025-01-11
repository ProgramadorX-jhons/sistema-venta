package unam.dgtic.spv.core.repository;


import unam.dgtic.spv.core.model.Articulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticuloRepository extends JpaRepository<Articulo,Integer> {
    // Consulta derivada para buscar artículos activos con paginación
    Page<Articulo> findAllByActivo(boolean activo, Pageable pageable);
    //Consulta Derivada para buscar articulo por el codigo
    Articulo findFirstByCodigo(String codigo);
    Page<Articulo>findByNombreContaining(String searchNomb,Pageable pageable);
    Page<Articulo>findByCodigoContaining(String searchCod,Pageable pageable);
    Page<Articulo> findByCategoriaId(Integer categoriaId, Pageable pageable);

}
