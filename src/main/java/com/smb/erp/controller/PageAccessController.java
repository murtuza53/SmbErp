/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.EmpRole;
import com.smb.erp.entity.PageAccess;
import com.smb.erp.repo.PageAccessRepository;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "pageaccessController")
@ViewScoped
public class PageAccessController extends AbstractController<PageAccess>{
    
    PageAccessRepository repo;
    
    @Autowired
    UserSession userSession;
    
    private EmpRole selectedRole;
    
    @Autowired
    public PageAccessController(PageAccessRepository repo){
        this.repo = repo;
    }
    
    @PostConstruct
    public void init(){
        selectedRole = userSession.getLoggedInRole();
    }
    
    public List<PageAccess> getItems(){
        //return repo.findAll(Sort.by(Sort.Direction.ASC, "accessid"));
        if(getSelectedRole()==null){
            return new LinkedList<>();
        }
        return repo.findAccessByRoleOrderByModule(getSelectedRole().getRoleid());
    } 

    public void save(){
        repo.save(getSelected());
        JsfUtil.addSuccessMessage("Webpage Access save successfuly");
    }
    
    public void createNew(){
        PageAccess pa = new PageAccess();
        pa.setCreatedon(new Date());
        pa.setRoleid(getSelectedRole());
        setSelected(pa);
    }
    
    public List<PageAccess> findAccessByRole(EmpRole role){
        return repo.findAccessByRole(role.getRoleid());
    }

    /**
     * @return the selectedRole
     */
    public EmpRole getSelectedRole() {
        return selectedRole;
    }

    /**
     * @param selectedRole the selectedRole to set
     */
    public void setSelectedRole(EmpRole selectedRole) {
        this.selectedRole = selectedRole;
    }
    
}
