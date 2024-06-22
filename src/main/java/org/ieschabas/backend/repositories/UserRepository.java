package org.ieschabas.backend.repositories;

import java.util.List;
import org.ieschabas.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer>{
    List<Usuario> findByEmail(String email);
}
