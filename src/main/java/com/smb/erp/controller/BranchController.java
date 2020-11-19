/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Branch;
import com.smb.erp.repo.BranchRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "branchController")
@ViewScoped
public class BranchController extends AbstractController<Branch> {

    BranchRepository repo;

    @Autowired
    UserSession userSession;
    
    @Autowired
    public BranchController(BranchRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(Branch.class, repo);
        this.repo = repo;
    }

    public List<Branch> getAllBranchByLoggedInCompany(){
        return repo.findBranchByCompanyId(userSession.getLoggedInCompany().getCompanyid());
    }
    
}
