package segundo.dam.tuppermania.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad central que agrupa la planificación semanal de un usuario.
 * Almacena el resumen del plan (fechas, objetivo, calorías medias) y
 * mantiene la lista de recetas asignadas a través de la relación con PlanPlato.
 */
@Entity
@Table(name = "plan_nutricional")
public class PlanNutricional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Long idPlan;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    private String objetivo;

    @Column(name = "calorias_totales")
    private Integer caloriasTotales;

    @Column(length = 5000)
    private String listaCompraResumida;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PlanPlato> planPlatos = new ArrayList<>();

    // --- GETTERS Y SETTERS ---

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Integer getCaloriasTotales() {
        return caloriasTotales;
    }

    public void setCaloriasTotales(Integer caloriasTotales) {
        this.caloriasTotales = caloriasTotales;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<PlanPlato> getPlatosAsignados() {
        return planPlatos;
    }

    public void setPlatosAsignados(List<PlanPlato> platosAsignados) {
        this.planPlatos = platosAsignados;
    }

    public String getListaCompraResumida() { return listaCompraResumida; }
    public void setListaCompraResumida(String listaCompraResumida) { this.listaCompraResumida = listaCompraResumida; }
}