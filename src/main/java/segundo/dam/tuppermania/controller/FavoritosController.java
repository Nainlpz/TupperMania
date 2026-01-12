package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import segundo.dam.tuppermania.model.Plato;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.repository.PlatoRepository;
import segundo.dam.tuppermania.repository.UsuarioRepository;

@Controller
@RequestMapping("/favoritos")
public class FavoritosController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @GetMapping("/toggle/{idPlato}")
    public String toggleFavorito(@PathVariable Long idPlato, Authentication auth, @RequestHeader(value = "referer", required = false) String referer) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        Plato plato = platoRepository.findById(idPlato).orElseThrow();

        if (usuario.getPlatosFavoritos().contains(plato)) {
            usuario.getPlatosFavoritos().remove(plato); // Quitar
        } else {
            usuario.getPlatosFavoritos().add(plato); // Añadir
        }

        usuarioRepository.save(usuario);

        return "redirect:" + (referer != null ? referer : "/planes");
    }

    @GetMapping
    public String verFavoritos(Model model, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        model.addAttribute("favoritos", usuario.getPlatosFavoritos());
        return "perfil/favoritos";
    }
}