package segundo.dam.tuppermania.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "id_usuario") // Consistente con el id_usuario de la tabla Usuario
    private Usuario usuario;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<PlanPlato> platosAsignados;

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
        return platosAsignados;
    }

    public void setPlatosAsignados(List<PlanPlato> platosAsignados) {
        this.platosAsignados = platosAsignados;
    }
}