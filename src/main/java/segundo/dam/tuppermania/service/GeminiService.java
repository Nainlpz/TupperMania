package segundo.dam.tuppermania.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import segundo.dam.tuppermania.model.PerfilFisico;
import segundo.dam.tuppermania.model.dto.DietaGeneradaDTO;

/**
 * Servicio encargado de la comunicación con la API de Google Gemini.
 * Transforma los requerimientos del perfil físico en un prompt estructurado
 * y parsea la respuesta para convertirla en objetos de dominio.
 */
@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Genera una estructura de dieta completa basada en el perfil del usuario.
     * @param perfil Datos físicos y objetivos del usuario.
     * @return DTO con la dieta estructurada y lista de compra.
     * @throws RuntimeException si falla la comunicación o el parseo del JSON.
     */
    public DietaGeneradaDTO generarDieta(PerfilFisico perfil) {
        // Inicializamos el cliente de Gemini con la API Key inyectada
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        String prompt = construirPrompt(perfil);

        try {
            // Solicitud al modelo flash para mayor velocidad de respuesta
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null
            );

            String textoRespuesta = response.text();

            // Procesamos el texto para asegurar que sea un JSON válido antes de mapearlo
            return parsearRespuestaGemini(textoRespuesta);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con la IA de Google: " + e.getMessage());
        }
    }

    /**
     * Construye el prompt con ingeniería de instrucciones para forzar
     * una salida JSON estricta y consolidación de ingredientes.
     */
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
                {
                "tituloPlan": "Nombre del plan",
                "explicacion": "Resumen",
                "listaCompraConsolidada": [
                                      "12 Huevos (Total semana)",
                                      "500g de Pechuga de Pollo",
                                      "1kg de Arroz Integral"
                                  ],
                "dias": [
                    {
                    "diaSemana": "LUNES",
                    "comidas": [
                        {\s
                            "tipoComida": "DESAYUNO",\s
                            "nombrePlato": "Tortitas de Avena",\s
                            "descripcion": "Receta rápida",\s
                            "caloriasAprox": 300,
                            "ingredientes": ["50g Avena", "1 Huevo", "Canela"]
                        }
                        // ... resto de comidas
                    ]
                    }
                // ... resto de días
                ]
                }
                ... Repetir hasta DOMINGO
                Asegúrate OBLIGATORIAMENTE de incluir objetos para los 7 días de la semana: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO.
              ]
            }
            Genera al menos Desayuno, Comida y Cena para cada día.
            
            INSTRUCCIONES CLAVE PARA LA LISTA DE COMPRA:
            1. Recorre todas las recetas de la semana.
            2. Agrupa los ingredientes iguales.
            3. SUMA sus cantidades (Ej: si el lunes hay 2 huevos y el viernes 2 huevos, pon "4 Huevos" en la lista consolidada).
            4. Ordena la lista alfabéticamente.
            """.formatted(
                p.getObjetivo(), p.getPeso(), p.getAltura(), p.getEdad(),
                p.getNivelActividad(), p.getSexo(),
                p.getAlergias() != null ? p.getAlergias() : "Ninguna",
                p.getIntolerancias() != null ? p.getIntolerancias() : "Ninguna"
        );
    }

    /**
     * Limpia la respuesta de la IA. A veces Gemini envuelve el JSON en bloques
     * de código markdown (```json ... ```), lo cual rompe el parser de Jackson.
     */
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