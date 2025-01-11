package unam.dgtic.spv.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unam.dgtic.spv.core.model.Usuario;


import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findAllByOrderByIdAsc();
    Usuario findByEmail(String email);

    Usuario findByNombreAndApellido( String firstName,  String lastName);
    boolean existsByEmail(String email);

}