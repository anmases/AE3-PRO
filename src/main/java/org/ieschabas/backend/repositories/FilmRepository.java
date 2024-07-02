package org.ieschabas.backend.repositories;
import org.ieschabas.backend.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the repository that includes all CRUD methods for films entity.
 * @author Antonio Mas Esteve
 */
@Repository
public interface FilmRepository extends JpaRepository<Pelicula, Integer>{
    
}