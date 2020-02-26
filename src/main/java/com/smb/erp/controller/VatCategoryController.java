/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.VatCategory;
import com.smb.erp.repo.VatCategoryRepository;
import java.util.List;
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
    
    VatCategoryRepository catRepo;
    
    private List<VatCategory> catList;
    
    @Autowired
    public VatCategoryController(VatCategoryRepository repo){
        this.catRepo = repo;
    }
    
    public List<VatCategory> getVatCategoryAll(){
        if(catList==null){
            catList = catRepo.findAll();
        }
        return catList;
    }
}
