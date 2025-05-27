package m1.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import m1.productos.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findAll();
    @SuppressWarnings("unchecked")
    Usuario save(Usuario usuario);

    Usuario findById(int id);

    Usuario findByNombre(String nombreUsuario);


}
