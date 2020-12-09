/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp;

import com.smb.erp.entity.Branch;
import com.smb.erp.entity.Company;
import com.smb.erp.repo.BranchRepository;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author FatemaLaptop
 */
@Named
//@ViewScoped
@SessionScope
public class UserSession implements Serializable {

    @Autowired
    BranchRepository branchRepo;

    Branch loggedInBranch;

    public UserSession() {

    }

    @PostConstruct
    public void init(){
        loggedInBranch = branchRepo.findById((long)1).get();
    }
    
    public Branch getLoggedInBranch(){
        return loggedInBranch;
    }
    
    public Company getLoggedInCompany(){
        return loggedInBranch.getCompany();
    }
    
}
