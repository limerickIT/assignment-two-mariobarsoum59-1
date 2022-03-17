/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.controller;

import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mario
 */
@RestController
public class BreweryController {
    
    @Autowired
    private BeerService beerService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BreweryService breweryService;
    
    
    
    @GetMapping(value = "brewery/GetAll", produces = MediaTypes.HAL_JSON_VALUE)
    public List<Brewery> getAllBreweries() {
        List<Brewery> BreweryList = breweryService.findAll();
        return BreweryList;
    }
    
    
    @GetMapping(value = "brewery/GetById/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Brewery> getOne(@PathVariable long id) {
        Optional<Brewery> b = breweryService.findOne(id);
        if (!b.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(b.get());
        }
    }
    
    
    @DeleteMapping(value = "brewery/Delete/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity delete(@PathVariable long id) {
        breweryService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/brewery/Add/", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity add(@RequestBody Brewery b) {
        b.setLast_mod(new Date());
        breweryService.saveBrewery(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping(value = "/brewery/Put/", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity edit(@RequestBody Brewery b) { //the edit method should check if the Author object is already in the DB before attempting to save it.
        b.setLast_mod(new Date());
        breweryService.saveBrewery(b);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    
    
    
    
}
