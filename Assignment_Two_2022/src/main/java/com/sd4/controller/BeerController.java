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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
          //Link selfLink = linkTo(methodOn(BeerController.class).getOne(id)).withSelfRel();
          Link selfLink = Link.of("http://localhost:8888/beers/GetAll");
          o.get().add(selfLink);
          return ResponseEntity.ok(o.get());
      }
    }
    
    @DeleteMapping(value= "beers/Delete/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity delete(@PathVariable long id) {
        beerService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping(value= "/beers/Add/", consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity add(@RequestBody Beer b) {
        beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @PutMapping(value= "/beers/Put/", consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity edit(@RequestBody Beer b) { //the edit method should check if the Author object is already in the DB before attempting to save it.
         beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.OK);
    }
    
}
