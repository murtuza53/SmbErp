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
import com.smb.erp.util.StringUtils;
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
        System.out.println("SystemDefaultsController: " + propertyTable);
    }
    
    public Account getDefaultAccount(String propertyname){
        return accountRepo.getOne(propertyTable.get("Account." + propertyname));
    }
    
    public String getDefaultList(String propertyname){
        return propertyTable.get("List." + propertyname);
    }
    
    public List<String> getAsList(String propertyname){
        //System.out.println("getAsList => " + propertyname + " => " + propertyTable.get("List." + propertyname));
        return StringUtils.tokensToList(propertyTable.get("List." + propertyname));
    }

    public List<String> getAsList(String propertyname, String firstValue){
        return StringUtils.tokensToList(propertyTable.get("List." + propertyname), firstValue);
    }
    
    public List<SystemDefaults> findAllData(){
        return systemRepo.findAll();
    }
    
    public SystemDefaults getByPropertyname(String propertyName){
        return systemRepo.findByPropertyname(propertyName);
    }
}
