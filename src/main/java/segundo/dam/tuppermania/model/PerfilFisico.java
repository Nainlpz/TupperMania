package segundo.dam.tuppermania.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perfil_fisico")
public class PerfilFisico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_perfil;

    private Double peso;
    private Double altura;
    private Integer edad;
    private String alergias;
    private String intolerancias;

    @Enumerated(EnumType.STRING)
    private Sexo sexo; // Crear Enum Sexo

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}