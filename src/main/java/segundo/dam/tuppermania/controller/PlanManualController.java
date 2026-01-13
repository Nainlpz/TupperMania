package segundo.dam.tuppermania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import segundo.dam.tuppermania.model.*;
import segundo.dam.tuppermania.model.enums.DiaSemana;
import segundo.dam.tuppermania.model.enums.TipoComida;
import segundo.dam.tuppermania.repository.*;
import segundo.dam.tuppermania.service.PlanNutricionalService;

@Controller
@RequestMapping("/planes/manual")
public class PlanManualController {

    @Autowired
    private PlanNutricionalService planService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PlanNutricionalRepository planRepository;
    @Autowired
    private PlatoRepository platoRepository;

    @GetMapping("/nuevo")
    public String formularioNuevoPlan(Model model) {
        model.addAttribute("plan", new PlanNutricional());
        return "planes/manual-nuevo";
    }

    @PostMapping("/crear")
    public String crearEsqueletoPlan(@ModelAttribute PlanNutricional plan, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();

        plan.setUsuario(usuario);
        plan.setCaloriasTotales(0);
        plan = planRepository.save(plan);

        return "redirect:/planes/manual/editor/" + plan.getIdPlan();
    }

    @GetMapping("/editor/{id}")
    public String editorPlan(@PathVariable Long id, Model model) {
        PlanNutricional plan = planService.obtenerPlanPorId(id);

        model.addAttribute("plan", plan);
        model.addAttribute("dias", DiaSemana.values());
        model.addAttribute("comidas", java.util.List.of(
                TipoComida.DESAYUNO,
                TipoComida.COMIDA,
                TipoComida.CENA
        ));

        return "planes/manual-editor";
    }

    @GetMapping("/seleccionar")
    public String seleccionarPlato(@RequestParam Long idPlan,
                                   @RequestParam String dia,
                                   @RequestParam String comida,
                                   @RequestParam(required = false) String busqueda,
                                   Model model, Authentication auth) {

        Usuario usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();

        model.addAttribute("idPlan", idPlan);
        model.addAttribute("dia", dia);
        model.addAttribute("comida", comida);
        model.addAttribute("favoritos", usuario.getPlatosFavoritos());

        if (busqueda != null && !busqueda.isEmpty()) {
            model.addAttribute("todosLosPlatos", platoRepository.findByNombreContainingIgnoreCase(busqueda));
            model.addAttribute("busquedaActual", busqueda);
            model.addAttribute("tabActiva", "all");
        } else {
            model.addAttribute("todosLosPlatos", platoRepository.findAll());
            model.addAttribute("tabActiva", "favs");
        }

        return "planes/manual-selector";
    }

    @GetMapping("/asignar/{idPlato}")
    public String asignarPlato(@PathVariable Long idPlato,
                               @RequestParam Long idPlan,
                               @RequestParam String dia,
                               @RequestParam String comida) {

        planService.asignarPlatoManual(idPlan, idPlato, dia, comida);

        return "redirect:/planes/manual/editor/" + idPlan;
    }
}