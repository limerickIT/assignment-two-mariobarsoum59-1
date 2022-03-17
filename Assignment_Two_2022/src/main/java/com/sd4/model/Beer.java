/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Beer extends RepresentationModel<Beer>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long brewery_id;
    @NotBlank(message="Beer name cannot be blank")
    private String name;
    private Integer cat_id;
    private Integer style_id;
    @NotNull(message="ABV Cannot be null")
    private Double abv;
    @NotNull(message="IBU Cannot be null")
    private Double ibu;
    @NotNull(message="SRM Cannot be null")
    private Double srm;
    
    @Lob 
    private String description;
    private Integer add_user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_mod;

    private String image;
    @NotNull(message="Buy price cannot be null")
    @DecimalMin(value="1.0", message="Buy price must be above 1.0")
    private Double buy_price;
    @NotNull(message="Selling price cannot be null")
    @DecimalMin(value="1.0", message="Selling price must be above 1.0")
    private Double sell_price;

}
