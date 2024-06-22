package org.ieschabas.backend.repositories;
import org.ieschabas.backend.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Pelicula, Integer>{
    
}