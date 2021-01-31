/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Module;
import com.smb.erp.entity.Webpage;
import com.smb.erp.repo.WebpageRepository;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "webpageController")
@ViewScoped
public class WebpageController extends AbstractController<Webpage>{
    
    WebpageRepository webpageRepo;

    @Autowired
    ModuleController moduleCon;
    
    private Module selectedModule;
    
    @Autowired
    public WebpageController(WebpageRepository repo){
        this.webpageRepo = repo;
    }
    
    @PostConstruct
    public void init(){
        selectedModule = moduleCon.getItems().get(0);
    }
    
    @Override
    public void save(){
        webpageRepo.save(getSelected());
        JsfUtil.addSuccessMessage("Webpage added successfuly");
    }
    
    public List<Webpage> getItems(){
        return webpageRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    } 
   
    public List<Webpage> getWebpagesByModule(Long moduleid){
        return webpageRepo.findWebpageByModuleid(moduleid);
    }

    public List<Webpage> getWebpagesByModule(){
        if(getSelectedModule()==null){
            return new LinkedList();
        }
        return getWebpagesByModule(getSelectedModule().getModuleid());
    }
    
    public void createNew(){
        Webpage page = new Webpage();
        page.setActive(true);
        page.setCreatedon(new Date());
        page.setModuleid(selectedModule);
        setSelected(page);
    }
    
    /**
     * @return the selectedModule
     */
    public Module getSelectedModule() {
        return selectedModule;
    }

    /**
     * @param selectedModule the selectedModule to set
     */
    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }
}
