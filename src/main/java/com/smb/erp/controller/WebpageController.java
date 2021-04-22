/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Module;
import com.smb.erp.entity.PrintReport;
import com.smb.erp.entity.Webpage;
import com.smb.erp.repo.WebpageRepository;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "webpageController")
@ViewScoped
public class WebpageController extends AbstractController<Webpage> {

    WebpageRepository webpageRepo;

    @Autowired
    ModuleController moduleCon;

    private Module selectedModule;
    
    private PrintReport selectedPrintReport;
    
    @Autowired
    public WebpageController(WebpageRepository repo) {
        this.webpageRepo = repo;
    }

    @PostConstruct
    public void init() {
        selectedModule = moduleCon.getItems().get(0);
    }

    @Override
    public void save() {
        webpageRepo.save(getSelected());
        JsfUtil.addSuccessMessage("Webpage added successfuly");
    }

    public void saveWebpage(RowEditEvent<Webpage> event) {
        if (event.getObject() == null) {
            JsfUtil.addErrorMessage("Cannot save empty Webpage");
            return;
        }
        setSelected(event.getObject());
        save();
    }

    public List<Webpage> getItems() {
        return webpageRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Webpage> getWebpagesByModule(Long moduleid) {
        return webpageRepo.findWebpageByModuleid(moduleid);
    }

    public List<Webpage> getWebpagesByModule() {
        if (getSelectedModule() == null) {
            return new LinkedList();
        }
        return getWebpagesByModule(getSelectedModule().getModuleid());
    }

    public void createNew() {
        Webpage page = new Webpage();
        page.setActive(true);
        page.setCreatedon(new Date());
        page.setModuleid(selectedModule);
        setSelected(page);
    }

    public void createNewJasperReport(Webpage page) throws IOException {
        setSelected(page);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //facesContext.getExternalContext().getRequestMap().put("mode", "n");
        //facesContext.getExternalContext().getRequestMap().put("bdid", "" + getSelected().getBdinfoid());
        facesContext.getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/report/jdesigner.xhtml?mode=n&pageid=" + getSelected().getPageid());
    }

    public void editJasperReport(Webpage page) throws IOException {
        if (getSelectedPrintReport() == null) {
            JsfUtil.addErrorMessage("Error", "No Print Template selected to edit");
            return;
        }
        setSelected(page);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/report/jdesigner.xhtml?mode=e&pageid=" + getSelected().getPageid() + "&reportid=" + getSelectedPrintReport().getReportid());
    }

    public void deletePrintReport(Webpage page) {
        if (getSelectedPrintReport() == null) {
            JsfUtil.addErrorMessage("No Print Template selected to Delete");
            return;
        }
        setSelected(page);
        getSelected().removeReportid(selectedPrintReport);
        selectedPrintReport = null;
        save();
        JsfUtil.addSuccessMessage("Deleted Successfuly");
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
    
        /**
     * @return the selectedPrintReport
     */
    public PrintReport getSelectedPrintReport() {
        return selectedPrintReport;
    }

    /**
     * @param selectedPrintReport the selectedPrintReport to set
     */
    public void setSelectedPrintReport(PrintReport selectedPrintReport) {
        this.selectedPrintReport = selectedPrintReport;
    }
}
