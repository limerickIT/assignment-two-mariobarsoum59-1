/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.controller;

import com.sd4.model.Breweries_Geocode;
import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.Breweries_GeocodeService;
import com.sd4.service.BreweryService;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mario
 */
@RestController
public class BreweriesGeoCodeController {
    
    @Autowired
    private BeerService beerService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BreweryService breweryService;
    @Autowired
    private Breweries_GeocodeService breweryGeocodeService;    
    
    
   @GetMapping(value = "brewery/map/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getMap(@PathVariable("id") long id) {
        
        Optional<Brewery> brewery = breweryService.findOne(id);
        
        System.out.println("Brewery Table ID: " + brewery.get().getId());
        
        Optional<Breweries_Geocode> geocode = breweryGeocodeService.findOne(id);
        System.out.println("Brewery Geocode Table ID: " + geocode.get().getId());
        
        String output = "<html><body><h1>" + brewery.get().getName() +" "+ brewery.get().getAddress1() + " "+ brewery.get().getAddress2()
                + " "+brewery.get().getCity()+ " " +brewery.get().getCountry()
                + "</h1><h2><br>Latitude: " + geocode.get().getLatitude() + "<br>Longitude: " + geocode.get().getLongitude() 
                + "</h2><div style=\"width: 100%\"><iframe width=\"100%\" height=\"600\" src=\"https://maps.google.com/maps?q=" + geocode.get().getLatitude() 
                + "," + geocode.get().getLongitude() 
                + "&hl=ie;z=3&amp;output=embed\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe></div></body></html>";

        return output;
    } 
    
}
