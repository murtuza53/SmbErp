/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Unit;
import com.smb.erp.repo.UnitRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "unitController")
@ViewScoped
public class UnitController extends AbstractController<Unit>{
    
    UnitRepository unitRepo;
    
    private List<Unit> unitList;
    
    @Autowired
    public UnitController(UnitRepository repo){
        this.unitRepo = repo;
    }
    
    public List<Unit> getUnitAll(){
        if(unitList==null){
            unitList = unitRepo.findByOrderByUnitname();
        }
        return unitList;
    } 
    
    public List<Unit> getUnitSymList(){
        return unitRepo.findByOrderByUnitsym();
    }
}
