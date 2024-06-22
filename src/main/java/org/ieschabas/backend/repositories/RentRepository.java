package org.ieschabas.backend.repositories;

import org.ieschabas.backend.model.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRepository extends JpaRepository<Alquiler, Integer>{
    
}
