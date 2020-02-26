/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.VatAccountType;
import com.smb.erp.repo.VatAccountTypeRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "vatacounttypeController")
@ViewScoped
public class VatAccountTypeController extends AbstractController<VatAccountType> {
    
    VatAccountTypeRepository accRepo;
    
    private List<VatAccountType> list;
    
    @Autowired
    public VatAccountTypeController(VatAccountTypeRepository repo){
        this.accRepo = repo;
    }
    
    public List<VatAccountType> getVatAccountTypeAll(){
        if(list==null){
            list = accRepo.findAll();
        }
        return list;
    }
}
