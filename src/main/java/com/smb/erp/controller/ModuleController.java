/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Module;
import com.smb.erp.repo.ModuleRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "moduleController")
@ViewScoped
public class ModuleController extends AbstractController<Module>{
    
    ModuleRepository moduleRepo;
    
    @Autowired
    public ModuleController(ModuleRepository repo){
        this.moduleRepo = repo;
    }
    
    public List<Module> getItems(){
        return moduleRepo.findAll(Sort.by(Sort.Direction.ASC, "modulename"));
    } 
    
    public List<Module> getActiveAll(){
        return moduleRepo.findActiveAll();
    }
}
