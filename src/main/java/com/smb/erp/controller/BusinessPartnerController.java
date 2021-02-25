/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.CreditLimit;
import com.smb.erp.entity.VatBusinessRegister;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.CountryRepository;
import com.smb.erp.service.TransactionImportService;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    AccountController accController;

    @Autowired
    SystemDefaultsController defaultController;

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    TableKeyController keyController;

    @Autowired
    private TransactionImportService importService;

    private List<String> companyTypes;

    private String companyTypeAll;

    private List<String> creditStatusTypes;

    private String selectedType = "Both";

    private String criteria;

    private boolean local;

    private VatBusinessRegister selectedVatRegister;
    
    private BusinessPartner all = new BusinessPartner("All");

    @Autowired
    public BusinessPartnerController(BusinessPartnerRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusinessPartner.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        //companyTypes = new LinkedList<>(Arrays.asList(BusinessPartner.getAvailableCompanyTypes()));
        companyTypes = defaultController.getAsList("BusinessPartnerType");
        companyTypeAll = defaultController.getDefaultList("BusinessPartnerType");
        //System.out.println("getAsList => CreditStatus => " + defaultController.getAsList("CreditStatus"));
        creditStatusTypes = defaultController.getAsList("CreditStatus");
        //companyTypes.addFirst("All");
        importService.setClz(BusinessPartner.class);
        importService.setTransactionList(new LinkedList<>());

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");
        if (m != null) {
            if (m.equalsIgnoreCase("0")) { // new business partner
                BusinessPartner bus = new BusinessPartner();
                bus.setCountry(countryRepo.findCountryDefault());
                bus.setCurrency(bus.getCountry());
                bus.setCreditlimit(new CreditLimit(0));
                setSelected(bus);
                newVatRegister();
                mode = DocumentTab.MODE.NEW;
            } else {
                String bpid = req.getParameter("bpid");
                if (bpid != null) {
                    setSelected(repo.getOne(Long.parseLong(bpid)));
                    if (getSelected().getBusinessRegisters() != null && getSelected().getBusinessRegisters().size() > 0) {
                        selectedVatRegister = getSelected().getBusinessRegisters().get(getSelected().getBusinessRegisters().size() - 1);
                    } else {
                        newVatRegister();
                    }
                }
                mode = DocumentTab.MODE.EDIT;
            }
        }
    }

    @Override
    public List<BusinessPartner> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            items = repo.findBusinessPartnerBySearchCriteria("");
        }
        return items;
    }

    public List<BusinessPartner> getBusinessPartnerListWithAll(){
        if(items==null){
            items = repo.findBusinessPartnerBySearchCriteria("");
            items.add(0, getAll());
        }
        return items;
    }
    
    @Transactional
    public void saveBusiness() {
        try {
            long id = getSelected().getPartnerid();
            if (getSelected().getPartnerid() == null || getSelected().getPartnerid() == 0) {
                getSelected().setPartnerid(keyController.getBusinessPartnerNextId());
                id = getSelected().getPartnerid();
            }
            
            super.save();
            //System.out.println("Trying to save: " + getSelected());
            //setSelected(repo.getOne(id));
            accController.saveAccount(getSelected());
            JsfUtil.addSuccessMessage("Success", getSelected().getCompanyname() + " saved successfuly");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "Could not save due to error");
        }
    }

    public void newVatRegister() {
        selectedVatRegister = new VatBusinessRegister();
        getSelectedVatRegister().setPartnerid(getSelected());
        List<VatBusinessRegister> list = new LinkedList<VatBusinessRegister>();
        list.add(getSelectedVatRegister());
        getSelected().setBusinessRegisters(list);
    }

    public boolean getCompanyDisabled() {
        if (getSelected() == null) {
            return true;
        }
        return false;
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
        if (selectedType.equalsIgnoreCase("Both")) {
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

    public void handleFileUpload(FileUploadEvent event) {
        getImportService().handleFileUpload(event, BusinessPartner.class);
        //importService.processData();
    }

    public void importTransactions() {
        System.out.println("importTransactions:....");
        if (importService.getTransactionList() != null) {
            for (BusinessPartner bus : (List<BusinessPartner>) importService.getTransactionList()) {
                if (bus.getCurrentVatRegister() != null) {
                    bus.getCurrentVatRegister().setPartnerid(bus);
                    bus.getCurrentVatRegister().setWef(new Date());
                }
                bus.setCreditlimit(new CreditLimit());
                //repo.save(bus);
                setSelected(bus);
                saveBusiness();
                System.out.println(bus + " => Saved");
            }
        }
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

    public List<String> getCreditStatusTypes() {
        return creditStatusTypes;
    }

    /**
     * @return the selectedVatRegister
     */
    public VatBusinessRegister getSelectedVatRegister() {
        return selectedVatRegister;
    }

    /**
     * @return the companyTypeAll
     */
    public String getCompanyTypeAll() {
        return companyTypeAll;
    }

    /**
     * @param companyTypeAll the companyTypeAll to set
     */
    public void setCompanyTypeAll(String companyTypeAll) {
        this.companyTypeAll = companyTypeAll;
    }

    /**
     * @return the importService
     */
    public TransactionImportService getImportService() {
        return importService;
    }

    /**
     * @param importService the importService to set
     */
    public void setImportService(TransactionImportService importService) {
        this.importService = importService;
    }

    /**
     * @return the all
     */
    public BusinessPartner getAll() {
        return all;
    }

    /**
     * @param all the all to set
     */
    public void setAll(BusinessPartner all) {
        this.all = all;
    }
}
