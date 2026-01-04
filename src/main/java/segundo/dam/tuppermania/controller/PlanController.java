package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import segundo.dam.tuppermania.model.PlanNutricional;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.model.enums.DiaSemana;
import segundo.dam.tuppermania.model.enums.TipoComida;
import segundo.dam.tuppermania.repository.UsuarioRepository;
import segundo.dam.tuppermania.service.PlanNutricionalService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/planes")
public class PlanController {

    @Autowired
    private PlanNutricionalService planService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String misPlanes(Model model, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        model.addAttribute("planes", planService.obtenerPlanesPorUsuario(usuario));
        return "planes/lista";
    }

    @GetMapping("/{id}")
    public String verPlan(@PathVariable Long id, Model model) {
        PlanNutricional plan = planService.obtenerPlanPorId(id);

        model.addAttribute("plan", plan);
        model.addAttribute("dias", DiaSemana.values());
        model.addAttribute("comidas", TipoComida.values());

        return "planes/detalle";
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