package org.ieschabas.backend.services;

import java.util.ArrayList;
import java.util.List;

import org.ieschabas.backend.model.Actor;
import org.ieschabas.backend.model.Director;
import org.ieschabas.backend.model.Equipo;
import org.ieschabas.backend.repositories.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Equipo> findAll(){
        return teamRepository.findAll();
    }
    public Equipo findById(int id){
        return teamRepository.findById(id).orElse(null);
    }
    public boolean insert(Equipo item){
        try {
            if(item != null){
                teamRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }
    public boolean remove(int id){
            teamRepository.deleteById(id);
            return true;
    }
    public boolean update(Equipo item){
        try {
            if(item != null){
                teamRepository.save(item);
                return true;
            }else return false;
        } catch (Exception e) {
            return false;
        }        
    }

    public List<Actor> findActors(){
        List<Actor> actores = new ArrayList<>();
        for (Equipo equipo : findAll()) {
            if (equipo instanceof Actor) {
                actores.add((Actor) equipo);
            }
        }
        return actores;
    }
    public List<Director> findDirectors(){
        List<Director> directores = new ArrayList<>();
        for (Equipo equipo : findAll()) {
            if (equipo instanceof Director) {
                directores.add((Director) equipo);
            }
        }
        return directores;
    }
}
