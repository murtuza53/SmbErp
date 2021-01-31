/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Authentication;
import com.smb.erp.entity.Emp;
import com.smb.erp.entity.EmpRole;
import com.smb.erp.repo.AuthenticationRepository;
import com.smb.erp.repo.EmpRepository;
import com.smb.erp.repo.EmpRoleRepository;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "empController")
@ViewScoped
public class EmpController extends AbstractController<Emp> {

    EmpRepository empRepo;

    @Autowired
    UserSession userSession;

    @Autowired
    EmpRoleRepository roleRepo;

    @Autowired
    AuthenticationRepository authRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private EmpRole selectedRole;

    private String username;

    private String password;
    
    private String password2;

    @Autowired
    public EmpController(EmpRepository repo) {
        this.empRepo = repo;
    }

    public List<Emp> getItems() {
        //return empRepo.findAll(Sort.by(Sort.Direction.ASC, "empname"));
        return empRepo.findEmpByCompany(userSession.getLoggedInCompany().getCompanyid());
    }

    public List<Emp> getEmpByCompany(long companyid) {
        return empRepo.findEmpByCompany(companyid);
    }

    public void save() {
        if (selectedRole == null) {
            JsfUtil.addErrorMessage("No Role selected");
            return;
        }
        if (getSelected().getEmproles() == null || getSelected().getEmproles().isEmpty()) {
            getSelected().setEmproles(new LinkedList<EmpRole>());
        }
        if (selectedRole.getEmps() == null || selectedRole.getEmps().isEmpty()) {
            selectedRole.setEmps(new LinkedList<Emp>());
        }

        Authentication auth = getSelected().getAuthentication();
        if (auth == null) {
            auth = new Authentication();
            auth.setUsername(username);
            //auth.setPassword(bCryptPasswordEncoder.encode(password));
            authRepo.save(auth);
        }
        
        //auth.setPassword(bCryptPasswordEncoder.encode(password));

        getSelected().getEmproles().clear();
        getSelected().getEmproles().add(selectedRole);

        getSelected().setAuthentication(auth);
        empRepo.save(getSelected());
        JsfUtil.addSuccessMessage(getSelected().getEmpname() + " saved successfuly");
    }

    public void savePassword(){
        if(getSelected()!=null){
            Authentication auth = getSelected().getAuthentication();
            auth.setPassword(bCryptPasswordEncoder.encode(password));
            authRepo.save(auth);
            JsfUtil.addSuccessMessage("Password changed");
        }
    }
    
    public void createNew() {
        Emp emp = new Emp();
        emp.setActive(true);
        emp.setCompany(userSession.getLoggedInCompany());
        emp.setDoj(new Date());
        emp.setDob(new Date());
        emp.setEmproles(new LinkedList<EmpRole>());
        setSelected(emp);
        username = "";
        password = "";
        password2 = "";
    }

    public String getEmpTitle() {
        if (getSelected() == null) {
            return "N/A";
        }
        if (getSelected().getEmpid() == null) {
            return "New Employee";
        }
        return "Edit - " + getSelected().getEmpname();
    }

    public void empSelected() {
        selectedRole = getSelected().getEmproleSingle();
        if(getSelected().getAuthentication()!=null){
            username = getSelected().getAuthentication().getUsername();
        }
        password = "";
        password2 = "";
    }
    
    public boolean disablePasswordBtn(){
        if(getSelected()==null){
            return true;
        }
        if(getSelected().getAuthentication()==null){
            return true;
        }
        return false;
    }

    public EmpRole getFirstRole() {
        List<EmpRole> list = roleRepo.findAll();
        if (list != null) {
            return list.get(0);
        }
        return null;
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

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password2
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * @param password2 the password2 to set
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

}
