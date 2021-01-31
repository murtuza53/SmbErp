/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.ProductType;
import com.smb.erp.repo.ProductTypeRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "productTypeController")
@ViewScoped
public class ProductTypeController extends AbstractController<ProductType> {
    
    ProductTypeRepository repo;
    
    @Autowired
    public ProductTypeController(ProductTypeRepository repo){
        this.repo = repo;
    }

    @Override
    public List<ProductType> getItems(){
        return repo.findAll();
    }
    
}
