package org.ieschabas.backend.services;

import java.util.List;

import org.ieschabas.backend.model.Alquiler;
import org.ieschabas.backend.repositories.RentRepository;
import org.springframework.stereotype.Service;

/**
 * This class includes all methods to request and serve data from repositories in an appropiate way.
 * It also may include logic business related with data transform.
 * for rents entity
 * @author Antonio Mas Esteve
 */
@Service
public class RentService {

    private final RentRepository rentRepository;

    public RentService(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    public List<Alquiler> findAll(){
        return rentRepository.findAll();
    }
    public Alquiler findById(int id){
        return rentRepository.findById(id).orElse(null);
    }
    public boolean insert(Alquiler item){
        try {
            if(item != null){
                rentRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }
    public boolean remove(int id){
            rentRepository.deleteById(id);
            return true;
    }
    public boolean update(Alquiler item){
        try {
            if(item != null){
                rentRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }
}
