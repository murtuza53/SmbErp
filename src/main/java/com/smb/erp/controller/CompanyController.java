/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Branch;
import com.smb.erp.entity.Company;
import com.smb.erp.entity.CompanyGroup;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.repo.CompanyGroupRepository;
import com.smb.erp.repo.CompanyRepository;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "companyController")
@ViewScoped
@Service
public class CompanyController extends AbstractController<Company> {

    CompanyRepository repo;

    @Autowired
    TableKeyController keyController;

    @Autowired
    CompanyGroupRepository groupRepo;

    @Autowired
    BranchRepository branchRepo;

    private List<CompanyGroup> groupList;

    private List<Branch> branchList;

    private CompanyGroup selectedCompanyGroup;

    private Branch selectedBranch;

    private Company selectedCompany;

    @Autowired
    public CompanyController(CompanyRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(Company.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void createNewCompany() {
        selectedCompany = new Company();
        selectedCompany.setCompanyname("New Company");
        System.out.println("New Company Created");
    }

    public void createNewBranch() {
        selectedBranch = new Branch();
        selectedBranch.setBranchname("New Branch");
    }

    public String getCompanyHeader() {
        if (selectedCompany == null) {
            return "No Company Selected";
        } else if (selectedCompany.getCompanyid() == 0) {
            return "New Company";
        }
        return "Edit - " + selectedCompany;
    }

    public String getBranchHeader() {
        if (selectedBranch == null) {
            return "No Branch Selected";
        } else if (selectedBranch.getBranchid() == 0) {
            return "New Branch";
        }
        return "Edit - " + selectedBranch;
    }

    @Transactional
    public void saveCompany() {
        int select = 0;
        System.out.println("Save Company: " + selectedCompany + "\t" + new Date());
        if (selectedCompany.getCompanyid() == 0) {
            selectedCompany.setCompanyid(keyController.getCompanyNextId());
            setSelected(selectedCompany);
            super.save();
        } else {
            setSelected(selectedCompany);
            super.save();
        }
        select = selectedCompany.getCompanyid();
        refresh();
        getCompanyList();
        selectedCompany = repo.getOne(select);
    }

    @Transactional
    public void saveBranch() {
        int select = 0;
        System.out.println("Save Branch: " + selectedBranch + "\t" + new Date());
        if (selectedBranch.getBranchid() == 0) {
            selectedBranch.setBranchid(keyController.getBranchNextId());
            selectedCompany.addBranch(selectedBranch);
            //setSelected(selectedCompany);
            //super.save();
            branchRepo.save(selectedBranch);
        } else {
            //setSelected(selectedCompany);
            //super.save();
            branchRepo.save(selectedBranch);
        }
        select = selectedBranch.getBranchid();
        refresh();
        getCompanyList();
        getBranchList();
        selectedBranch = branchRepo.getOne(select);
    }

    public void refresh() {
        setItems(null);
        groupList = null;
        branchList = null;
    }

    public List<Company> getCompanyList() {
        return getItems();
    }

    public List<CompanyGroup> getGroupList() {
        if (groupList == null) {
            groupList = groupRepo.findAll();
        }
        return groupList;
    }

    public void refreshBranch(){
        branchList = null;
    }
    
    public List<Branch> getBranchList() {
        if (branchList == null) {
            if (selectedCompany != null) {
                branchList = branchRepo.findBranchByCompanyId(selectedCompany.getCompanyid());
            }
        }
        return branchList;
    }

    /**
     * @return the selectedCompanyGroup
     */
    public CompanyGroup getSelectedCompanyGroup() {
        return selectedCompanyGroup;
    }

    /**
     * @param selectedCompanyGroup the selectedCompanyGroup to set
     */
    public void setSelectedCompanyGroup(CompanyGroup selectedCompanyGroup) {
        this.selectedCompanyGroup = selectedCompanyGroup;
    }

    /**
     * @return the selectedBranch
     */
    public Branch getSelectedBranch() {
        return selectedBranch;
    }

    /**
     * @param selectedBranch the selectedBranch to set
     */
    public void setSelectedBranch(Branch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    /**
     * @return the selectedCompany
     */
    public Company getSelectedCompany() {
        //System.out.println("getSelectedCompany: " + new Date() + "\t" + selectedCompany);
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(Company selectedCompany) {
        //System.out.println("setSelectedCompany: " + new Date() + "\t" + selectedCompany);
        this.selectedCompany = selectedCompany;
    }

}
