/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Branch;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.util.DateUtil;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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

    private static List<Branch> LOGGED_IN_COMPANY_BRANCHS;

    @Autowired
    public BranchController(BranchRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(Branch.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        LOGGED_IN_COMPANY_BRANCHS = findBranchByCompanyId(userSession.getLoggedInCompany().getCompanyid());
    }

    public List<Branch> getAllBranchByLoggedInCompany() {
        //System.out.println("getAllBranchByLoggedInCompany: " + items);
        if (items == null) {
            items = repo.findBranchByCompanyId(userSession.getLoggedInCompany().getCompanyid());
        }
        return items;
    }

    public List<Branch> findBranchByCompanyId(long companyid) {
        if (LOGGED_IN_COMPANY_BRANCHS == null) {
            LOGGED_IN_COMPANY_BRANCHS = repo.findBranchByCompanyId(companyid);
        }
        return LOGGED_IN_COMPANY_BRANCHS;
    }

}
