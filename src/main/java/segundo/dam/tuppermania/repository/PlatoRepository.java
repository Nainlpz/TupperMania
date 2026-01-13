package segundo.dam.tuppermania.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import segundo.dam.tuppermania.model.Plato;

import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
    List<Plato> findByNombreContainingIgnoreCase(String nombre);
}