package org.ieschabas.backend.repositories;

import org.ieschabas.backend.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * This is the repository that includes all CRUD methods for Actors and Directors entities.
 * @author Antonio Mas Esteve
 */
@Repository
public interface TeamRepository extends JpaRepository<Equipo, Integer>{
}
