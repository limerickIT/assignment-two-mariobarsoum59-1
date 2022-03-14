/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.service;

import com.sd4.model.Style;
import com.sd4.repository.StyleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mario
 */
@Service
public class StyleService {
    
    @Autowired
    private StyleRepository styleRepo;

    public Optional<Style> findOne(Long id) {
        return styleRepo.findById(id);
    }

    public List<Style> findAll() {
        return (List<Style>) styleRepo.findAll();
    }

    public long count() {
        return styleRepo.count();
    }

    public void deleteByID(long ID) {
        styleRepo.deleteById(ID);
    }

    public void saveBook(Style s) {
        styleRepo.save(s);
    }
}
