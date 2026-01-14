package segundo.dam.tuppermania.model;

import jakarta.persistence.*;
import segundo.dam.tuppermania.model.enums.Rol;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad principal que representa al usuario del sistema.
 * Gestiona credenciales, roles y relaciones con perfiles y planes.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario")
    private String nombreUsuario; // Refactorizado a camelCase

    private String contrasena;
    private String correo;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PerfilFisico perfilFisico;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<PlanNutricional> planes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_favoritos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_plato")
    )
    private List<Plato> platosFavoritos = new ArrayList<>();

    // --- GETTERS Y SETTERS ---

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public PerfilFisico getPerfilFisico() {
        return perfilFisico;
    }

    public void setPerfilFisico(PerfilFisico perfilFisico) {
        this.perfilFisico = perfilFisico;
    }

    public List<PlanNutricional> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlanNutricional> planes) {
        this.planes = planes;
    }

    public List<Plato> getPlatosFavoritos() { return platosFavoritos; }
    public void setPlatosFavoritos(List<Plato> platosFavoritos) { this.platosFavoritos = platosFavoritos; }
}