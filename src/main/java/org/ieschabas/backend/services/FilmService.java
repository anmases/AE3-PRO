package org.ieschabas.backend.services;

import java.util.List;
import org.ieschabas.backend.model.Pelicula;
import org.ieschabas.backend.repositories.FilmRepository;
import org.springframework.stereotype.Service;

/**
 * This class includes all methods to request and serve data from repositories in an appropiate way.
 * It also may include logic business related with data transform.
 * for films entity
 * @author Antonio Mas Esteve
 */
@Service
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Pelicula> findAll(){
        return filmRepository.findAll();
    }
    public Pelicula findById(int id){
        return filmRepository.findById(id).orElse(null);
    }
    public boolean insert(Pelicula item){
        try {
            if(item != null){
                filmRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }
    public boolean remove(int id){
            filmRepository.deleteById(id);
            return true;
    }
    public boolean update(Pelicula item){
        try {
            if(item != null){
                filmRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }
}
