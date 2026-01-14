package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.repository.UsuarioRepository;
import java.util.List;

/**
 * API REST para la gestión externa de usuarios.
 * Soporta el ciclo completo CRUD: Crear, Leer, Actualizar y Borrar.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. GET: Listar todos
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // 2. GET: Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST: Crear nuevo usuario (Recibe JSON)
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // Importante: Encriptar contraseña antes de guardar
        if (usuario.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        Usuario nuevo = usuarioRepository.save(usuario);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // 4. PUT: Actualizar usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombreUsuario(usuarioDetalles.getNombreUsuario());
            usuario.setCorreo(usuarioDetalles.getCorreo());

            // Solo actualizamos contraseña si viene una nueva
            if (usuarioDetalles.getContrasena() != null && !usuarioDetalles.getContrasena().isEmpty()) {
                usuario.setContrasena(passwordEncoder.encode(usuarioDetalles.getContrasena()));
            }

            // Si viene el rol, lo actualizamos
            if (usuarioDetalles.getRol() != null) {
                usuario.setRol(usuarioDetalles.getRol());
            }

            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. DELETE: Borrar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}