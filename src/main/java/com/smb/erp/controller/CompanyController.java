/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Company;
import com.smb.erp.entity.CompanyGroup;
import com.smb.erp.repo.CompanyGroupRepository;
import com.smb.erp.repo.CompanyRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "companyController")
@ViewScoped
public class CompanyController extends AbstractController<Company> {

    CompanyRepository repo;

    @Autowired
    CompanyGroupRepository groupRepo;
    
    private List<CompanyGroup> groupList;
    
    @Autowired
    public CompanyController(CompanyRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(Company.class, repo);
        this.repo = repo;
    }
    
    public List<CompanyGroup> getGroupList(){
        if(groupList==null){
            groupList = groupRepo.findAll();
        }
        return groupList;
    }

}
