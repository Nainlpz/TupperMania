package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import segundo.dam.tuppermania.model.PlanNutricional;
import segundo.dam.tuppermania.model.Plato;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.model.enums.DiaSemana;
import segundo.dam.tuppermania.model.enums.TipoComida;
import segundo.dam.tuppermania.repository.PlanNutricionalRepository;
import segundo.dam.tuppermania.repository.UsuarioRepository;
import segundo.dam.tuppermania.service.PlanNutricionalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/planes")
public class PlanController {

    @Autowired
    private PlanNutricionalService planService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlanNutricionalRepository planNutricionalRepository;

    @GetMapping
    public String misPlanes(Model model, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        model.addAttribute("planes", planService.obtenerPlanesPorUsuario(usuario));
        return "planes/lista";
    }

    @GetMapping("/{id}")
    public String verPlan(@PathVariable Long id, Model model, Authentication auth) {
        PlanNutricional plan = planService.obtenerPlanPorId(id);

        // Recuperar usuario para saber sus favoritos
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();

        // Creamos una lista SOLO con los IDs de los platos favoritos para comprobar rápido en el HTML
        List<Long> idsFavoritos = usuario.getPlatosFavoritos().stream()
                .map(Plato::getIdPlato)
                .collect(Collectors.toList());

        model.addAttribute("plan", plan);
        model.addAttribute("dias", DiaSemana.values());

        // FILTRO DE COMIDAS: Solo mostramos Desayuno, Comida y Cena en la vista
        model.addAttribute("comidas", List.of(TipoComida.DESAYUNO, TipoComida.COMIDA, TipoComida.CENA));

        model.addAttribute("idsFavoritos", idsFavoritos); // <--- NUEVO: Pasamos los favoritos

        return "planes/detalle";
    }

    @GetMapping("/borrar/{id}")
    public String borrarPlan(@PathVariable Long id) {
        planNutricionalRepository.deleteById(id);
        return "redirect:/planes";
    }

    @GetMapping("/editar/{id}")
    public String editarPlan(@PathVariable Long id) {
        return "redirect:/planes/manual/editor/" + id;
    }

    @GetMapping("/{id}/lista-compra")
    public String verListaCompra(@PathVariable Long id, Model model) {
        PlanNutricional plan = planService.obtenerPlanPorId(id);

        List<String> totalIngredientes = new ArrayList<>();

        if (plan.getListaCompraResumida() != null && !plan.getListaCompraResumida().isEmpty()) {
            String[] items = plan.getListaCompraResumida().split(";");
            totalIngredientes = List.of(items);
        }

        model.addAttribute("plan", plan);
        model.addAttribute("totalIngredientes", totalIngredientes);

        return "planes/compra";
    }

    @PostMapping("/generar")
    public String generarPlan(Authentication auth, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();

        try {
            PlanNutricional nuevoPlan = planService.generarYGuardarDieta(usuario);
            return "redirect:/planes/" + nuevoPlan.getIdPlan();

        } catch (RuntimeException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensajeError", "⚠️ Antes de generar una dieta, necesitas completar tu Perfil Físico.");

            return "redirect:/perfil/nuevo";
        }
    }
}