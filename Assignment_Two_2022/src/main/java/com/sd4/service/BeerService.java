/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.service;

import com.sd4.model.Beer;
import com.sd4.repository.BeerRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mario
 */
@Service
public class BeerService {

    @Autowired
    private BeerRepository beerRepo;

    public Optional<Beer> findOne(Long id) {
        return beerRepo.findById(id);
    }

    public List<Beer> findAll() {
        return (List<Beer>) beerRepo.findAll();
    }

    public long count() {
        return beerRepo.count();
    }

    public void deleteByID(long beerID) {
        beerRepo.deleteById(beerID);
    }

    public void saveBeer(Beer b) {
        beerRepo.save(b);
    }

    public List<Beer> findByName(String Name) {
        List<Beer> beers = (List<Beer>) beerRepo.findAll();
        List<Beer> result = new ArrayList();

        for (Beer b : beers) {
            if (b.getName().toLowerCase().contains(Name.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    public Page<Beer> findPage(int pageNumber){
    Pageable pageable = PageRequest.of(pageNumber - 1,125);
    return beerRepo.findAll(pageable);
}
}
