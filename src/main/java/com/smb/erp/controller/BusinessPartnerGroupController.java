/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusinessPartnerGroup;
import com.smb.erp.repo.BusinessPartnerGroupRepository;
import com.smb.erp.util.JsfUtil;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "businessPartnerGroupController")
@ViewScoped
public class BusinessPartnerGroupController extends AbstractController<BusinessPartnerGroup> {

    BusinessPartnerGroupRepository repo;

    @Autowired
    public BusinessPartnerGroupController(BusinessPartnerGroupRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusinessPartnerGroup.class, repo);
        this.repo = repo;
    }

    public void refresh(){
        items = null;
        getItems();
    }
    
    @Override
    public List<BusinessPartnerGroup> getItems(){
        if(items==null){
            items = repo.findGroupByCriteria("");
        }
        return items;
    }
    
    public void prepareCreate(){
        setSelected(new BusinessPartnerGroup());
    }
    
    public void save(){
        if(getSelected()==null){
            JsfUtil.addErrorMessage("Invalid Business Group");
            return;
        }
        repo.save(getSelected());
        JsfUtil.addSuccessMessage(getSelected().getGroupname() + " saved successfuly");
    }
    
}
