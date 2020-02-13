/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.entity.SystemDefaults;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.SystemDefaultsRepository;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author Burhani152
 */
@Named(value = "systemDefaultsController")
@SessionScope
public class SystemDefaultsController extends AbstractController<SystemDefaults> {

    SystemDefaultsRepository systemRepo;
    
    HashMap<String, String> propertyTable;

    @Autowired
    AccountRepository accountRepo;
    
    @Autowired
    public SystemDefaultsController(SystemDefaultsRepository repo) {
        super(SystemDefaults.class, repo);
        this.systemRepo = repo;
    }
    
    @PostConstruct
    public void init(){
        refresh();
    }
    
    public void refresh(){
        propertyTable = new HashMap<String, String>();
        
        List<SystemDefaults> sds = findAllData();
        System.out.println("SystemDefaultsController:" + sds);
        for(SystemDefaults sd: sds){
            if(!propertyTable.containsKey(sd.getReferredclass() + "." + sd.getPropertyname())){
                propertyTable.put(sd.getReferredclass() + "." + sd.getPropertyname(), sd.getValue());
            }
        }
    }
    
    public Account getDefaultAccount(String propertyname){
        return accountRepo.getOne(propertyTable.get(propertyname));
    }
    
    public List<SystemDefaults> findAllData(){
        return systemRepo.findAll();
    }
}
