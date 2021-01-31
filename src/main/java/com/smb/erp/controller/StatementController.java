/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocTransactionType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.util.JsfUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burhani152
 */
@Named(value = "statementController")
@ViewScoped
@Service
public class StatementController implements Serializable {

    @Autowired
    AccountRepository accRepo;

    @Autowired
    LedgerLineController ledCon;

    @Autowired
    BusinessPartnerRepository businessRepo;

    @Autowired
    BusDocRepository busdocRepo;

    private final String[] headers = new String[]{"Date", "Doc No", "Ref", "Amount", "Paid", "Balance", "Cumulative", "Days"};
    private List<BusinessPartner> businessPartners = new LinkedList<>();
    private final List<String> docTypes = Arrays.asList(new String[]{"Customer", "Supplier"});
        
    private Date statementDate = new Date();

    private BusinessPartner selectedBusinessPartner;
    private String selectedDocType = "Customer";
    
    private List<BusDoc> reportList;
    
    public StatementController() {

    }

    @PostConstruct
    public void init() {
        reloadBusinessPartners();
        refresh();
    }

    public void refresh() {
        System.out.println("Refresh: " + getSelectedBusinessPartner());

        if (getSelectedBusinessPartner() == null) {
            JsfUtil.addErrorMessage("No Company selected for statement");
            return;
        }
        String dtype = BusDocTransactionType.ACCOUNTS_RECEIVABLE.toString();
        if(dtype.equalsIgnoreCase("Supplier")){
            dtype = BusDocTransactionType.ACCOUNTS_PAYABLE.toString();
        }
        reportList = busdocRepo.findByBusDocByBusinessPartnerAndType(selectedBusinessPartner.getPartnerid(), getStatementDate(), dtype).stream().filter(line -> line.getTotalPending() > 0).collect(Collectors.toList());
        if (reportList == null) {
            reportList = new LinkedList();
        } 

        double bal = 0;
        for(BusDoc doc: reportList){
            if(bal==0){
                bal = doc.getTotalPending();
                doc.setCumulative(bal);
            } else {
                bal = bal + doc.getTotalPending();
                doc.setCumulative(bal);
            }
        }
        JsfUtil.addSuccessMessage(reportList.size() + " documents listed");
    }

    public void reloadBusinessPartners(){
        businessPartners = businessRepo.findBusinessPartnerByCompanyType(selectedDocType);
        if (businessPartners != null && businessPartners.size() > 0) {
            selectedBusinessPartner = businessPartners.get(0);
        }
    }
    
    /**
     * @return the statementDate
     */
    public Date getStatementDate() {
        return statementDate;
    }

    /**
     * @param statementDate the statementDate to set
     */
    public void setStatementDate(Date statementDate) {
        this.statementDate = statementDate;
    }

    /**
     * @return the headers
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * @return the businessPartners
     */
    public List<BusinessPartner> getBusinessPartners() {
        return businessPartners;
    }

    /**
     * @return the selectedBusinessPartner
     */
    public BusinessPartner getSelectedBusinessPartner() {
        return selectedBusinessPartner;
    }

    /**
     * @param selectedBusinessPartner the selectedBusinessPartner to set
     */
    public void setSelectedBusinessPartner(BusinessPartner selectedBusinessPartner) {
        this.selectedBusinessPartner = selectedBusinessPartner;
    }

    /**
     * @return the selectedDocType
     */
    public String getSelectedDocType() {
        return selectedDocType;
    }

    /**
     * @param selectedDocType the selectedDocType to set
     */
    public void setSelectedDocType(String selectedDocType) {
        this.selectedDocType = selectedDocType;
    }

    /**
     * @return the reportList
     */
    public List<BusDoc> getReportList() {
        return reportList;
    }

    /**
     * @param reportList the reportList to set
     */
    public void setReportList(List<BusDoc> reportList) {
        this.reportList = reportList;
    }

    /**
     * @return the docTypes
     */
    public List<String> getDocTypes() {
        return docTypes;
    }
}
