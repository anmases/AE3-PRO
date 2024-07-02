package org.ieschabas.backend.repositories;

import java.util.List;
import org.ieschabas.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the repository that includes all CRUD methods for users entity.
 * @author Antonio Mas Esteve
 */
@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer>{
    List<Usuario> findByEmail(String email);
}
