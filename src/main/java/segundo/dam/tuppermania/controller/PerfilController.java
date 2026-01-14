package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import segundo.dam.tuppermania.model.PerfilFisico;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.repository.PerfilFisicoRepository;
import segundo.dam.tuppermania.repository.UsuarioRepository;

/**
 * Gestiona la creación y edición del perfil físico del usuario.
 * Garantiza que cada usuario tenga vinculado un único perfil.
 */
@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilFisicoRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/nuevo")
    public String mostrarFormularioPerfil(Model model, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();

        if (usuario.getPerfilFisico() != null) {
            model.addAttribute("perfil", usuario.getPerfilFisico());
        } else {
            model.addAttribute("perfil", new PerfilFisico());
        }

        return "perfil/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPerfil(@ModelAttribute PerfilFisico perfil, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();

        if (usuario.getPerfilFisico() != null) {
            perfil.setIdPerfil(usuario.getPerfilFisico().getIdPerfil());
        }

        perfil.setUsuario(usuario);
        perfil = perfilRepository.save(perfil);
        usuario.setPerfilFisico(perfil);
        usuarioRepository.save(usuario);

        System.out.println("✅ Perfil guardado y VINCULADO correctamente para: " + usuario.getNombreUsuario());

        return "redirect:/planes";
    }

    @GetMapping("/contrasena")
    public String formContrasena() {
        return "perfil/contrasena";
    }

    @PostMapping("/contrasena")
    public String cambiarContrasena(@RequestParam String actual,
                                    @RequestParam String nueva,
                                    @RequestParam String confirmacion,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();

        // Verificar que la contraseña actual es correcta
        if (!passwordEncoder.matches(actual, usuario.getContrasena())) {
            redirectAttributes.addFlashAttribute("mensajeError", "⛔ La contraseña actual no es correcta.");
            return "redirect:/perfil/contrasena";
        }

        // Verificar que la nueva y la confirmación coinciden
        if (!nueva.equals(confirmacion)) {
            redirectAttributes.addFlashAttribute("mensajeError", "⚠️ Las nuevas contraseñas no coinciden.");
            return "redirect:/perfil/contrasena";
        }

        // Guardar la nueva contraseña encriptada
        usuario.setContrasena(passwordEncoder.encode(nueva));
        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Contraseña actualizada correctamente.");
        return "redirect:/perfil/contrasena";
    }
}