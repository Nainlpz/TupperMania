package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import segundo.dam.tuppermania.model.PerfilFisico;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.repository.PerfilFisicoRepository;
import segundo.dam.tuppermania.repository.UsuarioRepository;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilFisicoRepository perfilRepository;

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
}