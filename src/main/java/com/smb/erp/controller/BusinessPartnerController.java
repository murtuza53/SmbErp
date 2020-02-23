/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.CreditLimit;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.CountryRepository;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burhani152
 */
@Named(value = "businessPartnerController")
@ViewScoped
@Service
public class BusinessPartnerController extends AbstractController<BusinessPartner> {

    BusinessPartnerRepository repo;

    @Autowired
    SystemDefaultsController defaultsController;

    @Autowired
    AccountRepository accRepo;
    
    @Autowired
    SystemDefaultsController defaultController;
    
    @Autowired
    CountryRepository countryRepo;

    private List<String> companyTypes;
    
    private List<String> creditStatusTypes;

    private String selectedType = "All";

    private String criteria;

    private boolean local;

    private DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    @Autowired
    public BusinessPartnerController(BusinessPartnerRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusinessPartner.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        //companyTypes = new LinkedList<>(Arrays.asList(BusinessPartner.getAvailableCompanyTypes()));
        companyTypes = defaultController.getAsList("BusinessPartnerType", "All");
        creditStatusTypes = defaultController.getAsList("CreditStatus");
        //companyTypes.addFirst("All");

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");
        if (m != null) {
            if (m.equalsIgnoreCase("0")) { // new business partner
                BusinessPartner bus = new BusinessPartner();
                bus.setCountry(countryRepo.findCountryDefault());
                bus.setCreditlimit(new CreditLimit(0));
                setSelected(bus);
                mode = DocumentTab.MODE.NEW;
            } else {
                String bpid = req.getParameter("bpid");
                if (bpid != null) {
                    setSelected(repo.getOne(Integer.parseInt(bpid)));
                }
                mode = DocumentTab.MODE.EDIT;
            }
        }
    }

    public String getTitle() {
        if (mode == DocumentTab.MODE.LIST) {
            return "Business Partner List";
        } else if (mode == DocumentTab.MODE.NEW) {
            return "New Business Partner";
        }
        if (getSelected() != null) {
            return "Edit - " + getSelected().getCompanyname();
        }
        return "Invalid";
    }

    public void refresh() {
        if (selectedType.equalsIgnoreCase("All")) {
            setItems(repo.findBusinessPartnerBySearchCriteria(criteria));
        } else {
            setItems(repo.findBusinessPartnerByTypeBySearchCriteria(criteria, selectedType));
        }
    }

    public void newTab_In_newBtab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("businesspartner.xhtml?mode=0");
    }

    public void editTab_In_newBtab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No Business Partner selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("businesspartner.xhtml?mode=1&bpid=" + getSelected().getPartnerid());
    }

    /**
     * @return the selectedType
     */
    public String getSelectedType() {
        return selectedType;
    }

    /**
     * @param selectedType the selectedType to set
     */
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    /**
     * @return the companyTypes
     */
    public List<String> getCompanyTypes() {
        return companyTypes;
    }

    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the local
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(boolean local) {
        this.local = local;
    }

    public List<String> getCreditStatusTypes(){
        return creditStatusTypes;
    }
}
