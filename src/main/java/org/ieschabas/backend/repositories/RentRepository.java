package org.ieschabas.backend.repositories;

import org.ieschabas.backend.model.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * This is the repository that includes all CRUD methods for film rents entity.
 * @author Antonio Mas Esteve
 */
@Repository
public interface RentRepository extends JpaRepository<Alquiler, Integer>{
    
}
