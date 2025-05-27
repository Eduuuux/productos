package m1.productos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import m1.productos.model.Usuario;
import m1.productos.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findById(int id) {
        return usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombre(nombreUsuario);
    }

    public Usuario deleteById(int id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
            return usuario;
        }
        return null;
    }
    public Usuario updateById(int id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente != null) {
            if (usuario.getNombre() != null) {
                usuarioExistente.setNombre(usuario.getNombre());
            }
            if (usuario.getCargo() != null) {
                usuarioExistente.setCargo(usuario.getCargo());
            }
            return usuarioRepository.save(usuarioExistente);
        }
        return null;
    }
}
