/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp;

import com.smb.erp.controller.CountryController;
import com.smb.erp.controller.SystemDefaultsController;
import com.smb.erp.entity.Branch;
import com.smb.erp.entity.Company;
import com.smb.erp.entity.Country;
import com.smb.erp.entity.Emp;
import com.smb.erp.entity.EmpRole;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.repo.EmpRepository;
import com.smb.erp.util.BeanPropertyUtil;
import com.smb.erp.util.SystemConfig;
import com.smb.erp.util.Utils;
import java.awt.Image;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
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
    CountryController countryCon;

    @Autowired
    SystemDefaultsController sysCon;

    Branch loggedInBranch;

    Emp loggedInEmp;

    EmpRole demoRole;

    private static List<String> SESSION_PARAMETERS = Arrays.asList(new String[]{"loggedInEmp", "loggedInBranch",
        "loggedInCompany", "loggedInRole", "loggedInCountry", "headerImage", "defaultCurrency", "creditDays"});

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
        System.out.println("User Roles: " + loggedInEmp.getEmproles());
        
        //set default currency
        SystemConfig.CURRENCY_SYMBOL = countryCon.findCountryDefault().getCurrencysym();
    }

    public Branch getLoggedInBranch() {
        return loggedInBranch;
    }

    public Company getLoggedInCompany() {
        return loggedInBranch.getCompany();
    }

    public String getLoggedInCurrency() {
        return countryCon.findCountryDefault().getCurrencysym();
    }

    public Country getLoggedInCountry() {
        return countryCon.findCountryDefault();
    }

    public Emp getLoggedInEmp() {
        return loggedInEmp;
    }

    public EmpRole getLoggedInRole() {
        if (getLoggedInEmp().getEmproleSingle() == null) {
            return demoRole;
        }
        return getLoggedInEmp().getEmproleSingle();
    }

    public int getCreditDays() {
        return 90;
    }

    public String getDefaultCurrency() {
        return countryCon.findCountryDefault().getCurrencysym();
    }

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return auth.getName();
    }

    public boolean isAdmin() {
        if (getLoggedInRole() == null) {
            return false;
        }
        return getLoggedInRole().getRolename().equalsIgnoreCase("Admin");
    }

    public List<Class> getDataTypeClasses() {
        return Utils.DATA_TYPES;
    }

    public Image getHeaderImage() {
        return SystemConfig.PRINT_LETTERHEAD_IMAGE;
    }

    public boolean isSystemParameter(String param) {
        if (SESSION_PARAMETERS.contains(param) || sysCon.isSystemProperty(param)) {
            return true;
        }
        return false;
    }

    public Object getSystemParamter(String param) {
        if (SESSION_PARAMETERS.contains(param)) {
            return BeanPropertyUtil.getProperty(param, this);
        } else if (sysCon.isSystemProperty(param)) {
            return sysCon.getSystemPropertyAsObject(param);
        }
        return null;
    }

    public Object getSystemParamterAsString(String param) {
        Object val = getSystemParamter(param);
        if (val != null) {
            return val.toString();
        }
        return "";
    }

}
