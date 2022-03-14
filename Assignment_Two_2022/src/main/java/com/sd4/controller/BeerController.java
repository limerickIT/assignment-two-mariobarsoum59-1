/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.controller;

import com.sd4.model.Beer;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


/**
 *
 * @author Mario
 */
@RestController
public class BeerController {

    @Autowired
    private BeerService beerService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BreweryService breweryService;

    
    
    
    @GetMapping(value="beers/GetAll", produces = MediaTypes.HAL_JSON_VALUE)
    public List<Beer> getAllBeers() {
        List<Beer> beersList = beerService.findAll();
        
        for(Beer b : beersList)
        {
            long beerId = b.getId();
            Link selfLink = linkTo(methodOn(BeerController.class).getOne(beerId)).withSelfRel();
            b.add(selfLink);
        }
        
       return beersList;
    }
    
    @GetMapping(value="beers/GetById/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Beer> getOne(@PathVariable long id) {
      Optional<Beer> o =  beerService.findOne(id);
      if (!o.isPresent()) 
           return new ResponseEntity(HttpStatus.NOT_FOUND);
        else {
          Link selfLink = linkTo(methodOn(BeerController.class).getOne(id)).withSelfRel();
          o.get().add(selfLink);
          return ResponseEntity.ok(o.get());
      }
    }
    

    
}