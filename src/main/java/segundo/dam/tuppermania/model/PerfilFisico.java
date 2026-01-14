package segundo.dam.tuppermania.model;

import jakarta.persistence.*;
import segundo.dam.tuppermania.model.enums.NivelActividad;
import segundo.dam.tuppermania.model.enums.Objetivo;
import segundo.dam.tuppermania.model.enums.Sexo;

/**
 * Contiene la información antropométrica y de salud del usuario.
 * Estos datos son esenciales para que el algoritmo de IA (GeminiService)
 * pueda personalizar la dieta correctamente.
 */
@Entity
@Table(name = "perfil_fisico")
public class PerfilFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long idPerfil; // Refactorizado a camelCase

    private Double peso;
    private Double altura;
    private Integer edad;
    private String alergias;
    private String intolerancias;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_actividad")
    private NivelActividad nivelActividad;

    @Enumerated(EnumType.STRING)
    private Objetivo objetivo;

    @OneToOne
    @JoinColumn(name = "id_usuario") // Apunta a la columna física en la DB
    private Usuario usuario;

    // --- GETTERS Y SETTERS ---

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getIntolerancias() {
        return intolerancias;
    }

    public void setIntolerancias(String intolerancias) {
        this.intolerancias = intolerancias;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public NivelActividad getNivelActividad() { return nivelActividad; }

    public void setNivelActividad(NivelActividad nivelActividad) { this.nivelActividad = nivelActividad; }

    public Objetivo getObjetivo() { return objetivo; }

    public void setObjetivo(Objetivo objetivo) { this.objetivo = objetivo; }
}