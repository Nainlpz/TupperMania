package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.model.enums.Rol;
import segundo.dam.tuppermania.repository.UsuarioRepository;

/**
 * Controlador encargado de la gestión de acceso y registro de nuevos usuarios.
 * Maneja las vistas públicas de login y registro.
 */
@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    /**
     * Procesa el formulario de registro.
     * Encripta la contraseña y asigna el rol por defecto (USUARIO) antes de persistir.
     */
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setRol(Rol.USUARIO);
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }
}