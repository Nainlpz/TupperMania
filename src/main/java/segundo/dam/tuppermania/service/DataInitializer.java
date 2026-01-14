package segundo.dam.tuppermania.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import segundo.dam.tuppermania.model.Usuario;
import segundo.dam.tuppermania.model.enums.Rol;
import segundo.dam.tuppermania.repository.UsuarioRepository;

/**
 * Componente de arranque de la aplicación.
 * Se ejecuta automáticamente al iniciar el contexto de Spring.
 * Su responsabilidad principal es garantizar la existencia de datos esenciales,
 * como la cuenta de administrador, evitando configuraciones manuales en base de datos.
 */
@Configuration
public class DataInitializer {

    /**
     * Verifica la existencia del usuario administrador por defecto y lo crea si es necesario.
     * Utiliza el PasswordEncoder para asegurar que las credenciales no se guarden en texto plano.
     *
     * @param repository Repositorio para persistencia de usuarios.
     * @param encoder Componente de encriptación inyectado (BCrypt).
     * @return CommandLineRunner que ejecuta la lógica de inicialización.
     */
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@tuppermania.com";
            if (repository.findByCorreo(adminEmail).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("Administrador");
                admin.setCorreo(adminEmail);
                admin.setContrasena(encoder.encode("admin123")); // Contraseña encriptada
                admin.setRol(Rol.ADMIN);
                repository.save(admin);
                System.out.println(">>> Usuario administrador creado: admin@tuppermania.com / admin123");
            }
        };
    }
}