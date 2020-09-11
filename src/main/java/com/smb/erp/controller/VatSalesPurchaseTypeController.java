/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.VatSalesPurchaseType;
import com.smb.erp.repo.VatSalesPurchaseTypeRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "vatsalespurchaseController")
@ViewScoped
public class VatSalesPurchaseTypeController extends AbstractController<VatSalesPurchaseType> {
    
    VatSalesPurchaseTypeRepository spRepo;
    
    private List<VatSalesPurchaseType> list;
    
    @Autowired
    public VatSalesPurchaseTypeController(VatSalesPurchaseTypeRepository repo){
        this.spRepo = repo;
    }
    
    public List<VatSalesPurchaseType> getVatSalesPurchaseTypeAll(){
        if(list==null){
            list = spRepo.findAll();
        }
        return list;
    }
    
    public List<VatSalesPurchaseType> findVatSalesPurchaseType(String type){
        return spRepo.findByCategory(type);
    }
    
    public List<VatSalesPurchaseType> findVatSalesType(){
        return spRepo.findByCategory("Sales");
    }
    
    public List<VatSalesPurchaseType> findVatPurchaseType(){
        return spRepo.findByCategory("Purchase");
    }
}
