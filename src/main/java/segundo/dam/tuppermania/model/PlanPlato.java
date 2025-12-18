package segundo.dam.tuppermania.model;

import jakarta.persistence.*;
import segundo.dam.tuppermania.model.enums.DiaSemana;
import segundo.dam.tuppermania.model.enums.TipoComida;

@Entity
@Table(name = "plan_plato")
public class PlanPlato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan_plato")
    private Long idPlanPlato;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private PlanNutricional plan;

    @ManyToOne
    @JoinColumn(name = "id_plato")
    private Plato plato;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    private DiaSemana diaSemana;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comida")
    private TipoComida tipoComida;

    // --- GETTERS Y SETTERS (Añadidos para que funcione el proyecto) ---

    public Long getIdPlanPlato() {
        return idPlanPlato;
    }

    public void setIdPlanPlato(Long idPlanPlato) {
        this.idPlanPlato = idPlanPlato;
    }

    public PlanNutricional getPlan() {
        return plan;
    }

    public void setPlan(PlanNutricional plan) {
        this.plan = plan;
    }

    public Plato getPlato() {
        return plato;
    }

    public void setPlato(Plato plato) {
        this.plato = plato;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public TipoComida getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(TipoComida tipoComida) {
        this.tipoComida = tipoComida;
    }
}