package segundo.dam.tuppermania.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import segundo.dam.tuppermania.model.PerfilFisico;
import segundo.dam.tuppermania.model.dto.DietaGeneradaDTO;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.objectMapper = new ObjectMapper();
    }

    public DietaGeneradaDTO generarDieta(PerfilFisico perfil) {
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        String prompt = construirPrompt(perfil);

        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null
            );

            String textoRespuesta = response.text();

            return parsearRespuestaGemini(textoRespuesta);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con la IA de Google: " + e.getMessage());
        }
    }

    private String construirPrompt(PerfilFisico p) {
        return """
            Actúa como un nutricionista experto. Crea un plan semanal JSON para:
            - Objetivo: %s
            - Peso: %s kg, Altura: %s cm, Edad: %s
            - Actividad: %s
            - Sexo: %s
            - Alergias: %s
            - Intolerancias: %s
            
            IMPORTANTE: Responde ÚNICAMENTE con un JSON válido siguiendo ESTRICTAMENTE esta estructura, sin texto adicional ni markdown (no uses ```json):
            {
              "tituloPlan": "Nombre creativo del plan",
              "explicacion": "Breve resumen nutricional",
              "dias": [
                {
                  "diaSemana": "LUNES",
                  "comidas": [
                    { "tipoComida": "DESAYUNO", "nombrePlato": "Ejemplo", "descripcion": "Breve receta", "caloriasAprox": 300 },
                    { "tipoComida": "COMIDA", "nombrePlato": "Ejemplo", "descripcion": "Breve receta", "caloriasAprox": 600 },
                    { "tipoComida": "CENA", "nombrePlato": "Ejemplo", "descripcion": "Breve receta", "caloriasAprox": 400 }
                  ]
                }
                ... Repetir hasta DOMINGO
                Asegúrate OBLIGATORIAMENTE de incluir objetos para los 7 días de la semana: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO.
              ]
            }
            Genera al menos Desayuno, Comida y Cena para cada día.
            """.formatted(
                p.getObjetivo(), p.getPeso(), p.getAltura(), p.getEdad(),
                p.getNivelActividad(), p.getSexo(),
                p.getAlergias() != null ? p.getAlergias() : "Ninguna",
                p.getIntolerancias() != null ? p.getIntolerancias() : "Ninguna"
        );
    }

    private DietaGeneradaDTO parsearRespuestaGemini(String textoJson) {
        try {
            if (textoJson.contains("```json")) {
                textoJson = textoJson.replace("```json", "").replace("```", "");
            }
            return objectMapper.readValue(textoJson.trim(), DietaGeneradaDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error al leer el JSON de la IA: " + e.getMessage() + " | Respuesta recibida: " + textoJson);
        }
    }
}