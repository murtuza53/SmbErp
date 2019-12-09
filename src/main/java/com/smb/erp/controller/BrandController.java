/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Brand;
import com.smb.erp.repo.BrandRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "brandController")
@ViewScoped
public class BrandController extends AbstractController<Brand> {
    
    BrandRepository brandRepo;
    
    private List<Brand> brandList;
    
    @Autowired
    public BrandController(BrandRepository repo){
        this.brandRepo = repo;
    }
    
    public List<Brand> getBrandAll(){
        if(brandList==null){
            brandList = brandRepo.findByOrderByBrandnameAsc();
        }
        return brandList;
    }
    
    public List<Brand> getBrandByCriteria(String criteria){
        return brandRepo.findByBrandnameContainsOrAbbreviationContainsAllIgnoreCaseOrderByBrandnameAsc(criteria, criteria);
    }
}
