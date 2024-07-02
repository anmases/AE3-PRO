package org.ieschabas.backend.services;

import java.util.List;
import org.ieschabas.backend.model.Usuario;
import org.ieschabas.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * This class includes all methods to request and serve data from repositories in an appropiate way.
 * It also may include logic business related with data transform.
 * for users entity
 * @author Antonio Mas Esteve
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public List<Usuario> findAll(){
        return userRepository.findAll();
    }
    public Usuario findById(int id){
        return userRepository.findById(id).orElse(null);
    }
    public boolean insert(Usuario item){
        try {
            if(item != null){
                userRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }
    public boolean remove(int id){
            userRepository.deleteById(id);
            return true;
    }
    public boolean update(Usuario item){
        try {
            if(item != null){
                userRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }
    public Usuario findByEmail(String email){
        return userRepository.findByEmail(email).get(0);
    }
}
