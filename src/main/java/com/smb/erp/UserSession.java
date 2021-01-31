/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp;

import com.smb.erp.controller.SystemDefaultsController;
import com.smb.erp.entity.Branch;
import com.smb.erp.entity.Company;
import com.smb.erp.entity.Emp;
import com.smb.erp.entity.EmpRole;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.repo.EmpRepository;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    EmpRepository empRepo;

    @Autowired
    SystemDefaultsController sysCon;

    Branch loggedInBranch;

    Emp loggedInEmp;
    
    EmpRole demoRole;

    public UserSession() {
    }

    @PostConstruct
    public void init() {
        loggedInBranch = branchRepo.findById((long) 1).get();
        //loggedInEmp = empRepo.findById((long) 1).get();
        loggedInEmp = empRepo.findEmpByUsername(getUsername());

        demoRole = new EmpRole();
        demoRole.setRoleid(0l);
        demoRole.setRolename("Demo");
        System.out.println("Logged in user: " + loggedInEmp);
    }

    public Branch getLoggedInBranch() {
        return loggedInBranch;
    }

    public Company getLoggedInCompany() {
        return loggedInBranch.getCompany();
    }

    public Emp getLoggedInEmp() {
        return loggedInEmp;
    }
    
    public EmpRole getLoggedInRole(){
        if(getLoggedInEmp().getEmproleSingle()==null){
            return demoRole;
        }
        return getLoggedInEmp().getEmproleSingle();
    }

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return auth.getName();
    }
}
