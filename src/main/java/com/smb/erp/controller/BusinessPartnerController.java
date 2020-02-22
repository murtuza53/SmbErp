/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burhani152
 */
@Named(value = "businessPartnerController")
@ViewScoped
@Service
public class BusinessPartnerController extends AbstractController<BusinessPartner> {

    BusinessPartnerRepository repo;

    @Autowired
    SystemDefaultsController defaultsController;

    @Autowired
    AccountRepository accRepo;
    
    List<String> companyTypes = Arrays.asList(BusinessPartner.getAvailableCompanyTypes());
    
    String selectedType = "All";
    
    @Autowired
    public BusinessPartnerController(BusinessPartnerRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusinessPartner.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init(){
        companyTypes.add(0, "All");
    }
    
}
