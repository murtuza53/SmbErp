/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.EmpRole;
import com.smb.erp.repo.EmpRoleRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "emproleController")
@ViewScoped
public class EmpRoleController extends AbstractController<EmpRole>{
    
    EmpRoleRepository repo;
    
    @Autowired
    public EmpRoleController(EmpRoleRepository repo){
        this.repo = repo;
    }
    
    public List<EmpRole> getItems(){
        return repo.findAll(Sort.by(Sort.Direction.ASC, "rolename"));
    } 
}
