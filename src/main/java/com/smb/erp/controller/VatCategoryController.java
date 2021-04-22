/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.SystemDefaults;
import com.smb.erp.entity.VatCategory;
import com.smb.erp.repo.VatCategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "vatcategoryController")
@ViewScoped
public class VatCategoryController extends AbstractController<VatCategory> {
    
    VatCategoryRepository repo;
    
    @Autowired
    SystemDefaultsController systemController;
    
    
    @Autowired
    public VatCategoryController(VatCategoryRepository repo){
        this.repo = repo;
    }
    
    @Override
    public List<VatCategory> getItems(){
        if(items==null){
            items = repo.findAll();
        }
        return items;
    }

    public List<VatCategory> completeFilter(String criteria) {
        return getItems().stream().filter(c->c.getCategoryname().contains(criteria)).collect(Collectors.toList());
    }
    
    public VatCategory getDefaultProductVatCategory(){
        VatCategory cat = null;
        SystemDefaults def = systemController.getByPropertyname("DefaultProductVatCategory");
        if(def!=null){
            cat = repo.getOne(Long.parseLong(def.getValue()));
        }
        
        if(cat==null){
            cat = repo.getOne((long)2);
        }
        return cat;
    }
}
