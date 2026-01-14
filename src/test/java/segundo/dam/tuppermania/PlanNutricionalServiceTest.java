package segundo.dam.tuppermania;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import segundo.dam.tuppermania.model.PlanNutricional;
import segundo.dam.tuppermania.repository.PlanNutricionalRepository;
import segundo.dam.tuppermania.service.PlanNutricionalService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanNutricionalServiceTest {

    @Mock
    private PlanNutricionalRepository planRepository;

    @InjectMocks
    private PlanNutricionalService planService;

    @Test
    void testObtenerPlanPorId_Existente() {
        Long idPrueba = 1L;
        PlanNutricional planSimulado = new PlanNutricional();
        planSimulado.setIdPlan(idPrueba);
        planSimulado.setObjetivo("GANAR_MASA_MUSCULAR");

        when(planRepository.findById(idPrueba)).thenReturn(Optional.of(planSimulado));

        PlanNutricional resultado = planService.obtenerPlanPorId(idPrueba);

        assertNotNull(resultado, "El plan no debería ser nulo");
        assertEquals(idPrueba, resultado.getIdPlan(), "El ID del plan debe coincidir");
        assertEquals("GANAR_MASA_MUSCULAR", resultado.getObjetivo());

        verify(planRepository, times(1)).findById(idPrueba);
    }

    @Test
    void testObtenerPlanPorId_NoExistente() {
        Long idMalo = 99L;
        when(planRepository.findById(idMalo)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            planService.obtenerPlanPorId(idMalo);
        });

        assertTrue(exception.getMessage().contains("Plan no encontrado"));
    }
}