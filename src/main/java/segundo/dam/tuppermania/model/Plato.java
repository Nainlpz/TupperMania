package segundo.dam.tuppermania.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Representa una receta o alimento individual disponible en el sistema.
 * Actúa como la unidad básica para la construcción de dietas.
 * Contiene información nutricional y detalles de preparación/ingredientes.
 */
@Entity
@Table(name = "plato")
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plato")
    private Long idPlato; // Refactorizado a camelCase

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private Integer calorias;

    @Column(length = 1000)
    private String ingredientes;

    @OneToMany(mappedBy = "plato")
    private List<PlanPlato> planesDondeAparece;

    // --- GETTERS Y SETTERS ACTUALIZADOS ---

    public Long getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(Long idPlato) {
        this.idPlato = idPlato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

    public List<PlanPlato> getPlanesDondeAparece() {
        return planesDondeAparece;
    }

    public void setPlanesDondeAparece(List<PlanPlato> planesDondeAparece) {
        this.planesDondeAparece = planesDondeAparece;
    }

    public String getIngredientes() { return ingredientes; }

    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }
}