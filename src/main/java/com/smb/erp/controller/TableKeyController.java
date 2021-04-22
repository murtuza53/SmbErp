/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.TableKey;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.TableKeyRepository;
import java.util.HashMap;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author Burhani152
 */
@Named(value = "tablekeyController")
@SessionScope
public class TableKeyController extends AbstractController<TableKey> {

    TableKeyRepository tableRepo;
    
    HashMap<String, String> propertyTable;

    @Autowired
    UserSession userSession;
    
    @Autowired
    AccountRepository accountRepo;
    
    @Autowired
    public TableKeyController(TableKeyRepository repo) {
        super(TableKey.class, repo);
        this.tableRepo = repo;
    }
    
    @Transactional
    protected synchronized long getNextKey(String tableName){
        TableKey key = tableRepo.findByTablename(tableName);
        
        if(key==null){
            key = new TableKey();
            key.setTablename(tableName);
            key.setNextvalue("2");
            setSelected(key);
            super.save();
            return 1;
        }else{
            long returnVal = Long.parseLong(key.getNextvalue());
            key.setNextvalue((returnVal+1)+"");
            setSelected(key);
            super.save();
            return returnVal;
        }
    }

    @Transactional
    public synchronized String getDocNo(String prefix, int year){
        return prefix + "-" + userSession.getLoggedInCompany().getAbbreviation() + "-" + (year-2000) + "-" + getNextKey(prefix+year);
    }
    
    @Transactional
    public synchronized long getBusinessPartnerNextId(){
        return getNextKey("businesspartner");
    }

    @Transactional
    public synchronized long getCompanyNextId(){
        return getNextKey("company");
    }

    @Transactional
    public synchronized long getProductCategoryNextId(){
        return getNextKey("prodcategory");
    }

    @Transactional
    public synchronized long getBranchNextId(){
        return getNextKey("branch");
    }
    
    @Transactional
    public synchronized long getUnitNextId(){
        return getNextKey("unit");
    }
    
    @Transactional
    public synchronized long getBrandNextId(){
        return getNextKey("brand");
    }
    
    @Transactional
    public synchronized long getGroupNextId(){
        return getNextKey("groups");
    }
    
    @Transactional
    public synchronized long getProductNextId(){
        return getNextKey("product");
    }

}
