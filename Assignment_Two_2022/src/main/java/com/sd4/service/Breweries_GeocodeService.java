/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.service;

import com.sd4.model.Beer;
import com.sd4.model.Breweries_Geocode;
import com.sd4.repository.Breweries_GeocodeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mario
 */
@Service
public class Breweries_GeocodeService {
    
    @Autowired
    private Breweries_GeocodeRepository breweries_GeocodeRepo;
    
     public Optional<Breweries_Geocode> findOne(Long id) {
        return breweries_GeocodeRepo.findById(id);
    }

    public List<Breweries_Geocode> findAll() {
        return (List<Breweries_Geocode>) breweries_GeocodeRepo.findAll();
    }

    public long count() {
        return breweries_GeocodeRepo.count();
    }

    public void deleteByID(long ID) {
        breweries_GeocodeRepo.deleteById(ID);
    }

    public void saveBook(Breweries_Geocode b) {
        breweries_GeocodeRepo.save(b);
    }
    
    public Optional<Breweries_Geocode> findbyBreweryId(Long id) {
        
        List<Breweries_Geocode> breweries = (List<Breweries_Geocode>) breweries_GeocodeRepo.findAll();
        Optional<Breweries_Geocode> result=null;
        
        
        for(Breweries_Geocode bg: breweries)
        {
            if(bg.getBrewery_id()==id)
            {
            result=Optional.of(bg);
            }
        }
        
        return result;
    }
}
