package org.ieschabas.backend.repositories;

import java.util.List;

import org.ieschabas.backend.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Equipo, Integer>{
}
