package segundo.dam.tuppermania.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    private String nombre_usuario;
    private String contrasena;
    private String correo;

    @Enumerated(EnumType.STRING)
    private Rol rol; // Debes crear el Enum Rol

    // Relación 1:1 con PerfilFisico
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PerfilFisico perfilFisico;

    // Relación 1:N con PlanNutricional
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<PlanNutricional> planes;
}