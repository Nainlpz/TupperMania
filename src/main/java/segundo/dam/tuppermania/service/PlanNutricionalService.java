package segundo.dam.tuppermania.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import segundo.dam.tuppermania.model.*;
import segundo.dam.tuppermania.model.dto.*;
import segundo.dam.tuppermania.model.enums.DiaSemana;
import segundo.dam.tuppermania.model.enums.TipoComida;
import segundo.dam.tuppermania.repository.*;
import java.time.LocalDate;

@Service
public class PlanNutricionalService {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    public PlanNutricionalRepository planRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @Autowired
    private PlanPlatoRepository planPlatoRepository;

    @Transactional
    public PlanNutricional generarYGuardarDieta(Usuario usuario) {
        PerfilFisico perfil = usuario.getPerfilFisico();

        if (perfil == null) {
            throw new RuntimeException("El usuario no tiene perfil físico completado");
        }

        DietaGeneradaDTO dietaDTO = geminiService.generarDieta(perfil);

        PlanNutricional plan = new PlanNutricional();
        plan.setUsuario(usuario);
        plan.setFechaInicio(LocalDate.now());
        plan.setFechaFin(LocalDate.now().plusDays(7));
        plan.setObjetivo(perfil.getObjetivo().name());
        plan.setCaloriasTotales(calcularCaloriasTotales(dietaDTO));

        if (dietaDTO.getListaCompraConsolidada() != null) {
            String listaString = String.join(";", dietaDTO.getListaCompraConsolidada());
            plan.setListaCompraResumida(listaString);
        }

        plan = planRepository.save(plan);

        for (DiaDietaDTO diaDTO : dietaDTO.getDias()) {
            DiaSemana diaEnum;
            try {
                diaEnum = DiaSemana.valueOf(diaDTO.getDiaSemana().toUpperCase());
            } catch (IllegalArgumentException e) {
                continue;
            }

            for (ComidaDTO comidaDTO : diaDTO.getComidas()) {
                Plato plato = new Plato();
                plato.setNombre(comidaDTO.getNombrePlato());
                plato.setDescripcion(comidaDTO.getDescripcion());
                plato.setCalorias(comidaDTO.getCaloriasAprox());

                if (comidaDTO.getIngredientes() != null) {
                    plato.setIngredientes(String.join(", ", comidaDTO.getIngredientes()));
                } else {
                    plato.setIngredientes("Consultar receta");
                }

                plato = platoRepository.save(plato);

                PlanPlato planPlato = new PlanPlato();
                planPlato.setPlan(plan);
                planPlato.setPlato(plato);
                planPlato.setDiaSemana(diaEnum);

                try {
                    TipoComida tipoEnum = TipoComida.valueOf(comidaDTO.getTipoComida().toUpperCase());
                    planPlato.setTipoComida(tipoEnum);
                } catch (IllegalArgumentException e) {
                    planPlato.setTipoComida(TipoComida.COMIDA);
                }

                planPlatoRepository.save(planPlato);
            }
        }

        return plan;
    }

    public void asignarPlatoManual(Long idPlan, Long idPlato, String dia, String comida) {
        PlanNutricional plan = obtenerPlanPorId(idPlan);
        Plato plato = platoRepository.findById(idPlato).orElseThrow();

        DiaSemana diaEnum = DiaSemana.valueOf(dia);
        TipoComida comidaEnum = TipoComida.valueOf(comida);

        PlanPlato asignacion = plan.getPlatosAsignados().stream()
                .filter(pp -> pp.getDiaSemana() == diaEnum && pp.getTipoComida() == comidaEnum)
                .findFirst()
                .orElse(null);

        if (asignacion != null) {
            asignacion.setPlato(plato);
        } else {
            asignacion = new PlanPlato();
            asignacion.setPlan(plan);
            asignacion.setPlato(plato);
            asignacion.setDiaSemana(diaEnum);
            asignacion.setTipoComida(comidaEnum);
            plan.getPlatosAsignados().add(asignacion);
        }
        planRepository.save(plan);
    }

    private Integer calcularCaloriasTotales(DietaGeneradaDTO dto) {
        int totalSemana = dto.getDias().stream()
                .flatMap(d -> d.getComidas().stream())
                .mapToInt(c -> c.getCaloriasAprox() != null ? c.getCaloriasAprox() : 0)
                .sum();
        return totalSemana / 7;
    }

    public java.util.List<PlanNutricional> obtenerPlanesPorUsuario(Usuario usuario) {
        return planRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
    }

    public PlanNutricional obtenerPlanPorId(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con id: " + id));
    }
}