/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Account;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocTransactionType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.Webpage;
import com.smb.erp.helper.AgeAnalysisHelper;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.WebpageRepository;
import com.smb.erp.rest.JasperPrintReportGenerator;
import com.smb.erp.util.AgeingAnalysisRangeCalculator;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.Speller;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.ArrayUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "ageAnalysisController")
@ViewScoped
public class AgeAnalysisController implements Serializable {

    @Autowired
    AccountRepository accRepo;

    @Autowired
    LedgerLineController ledCon;

    @Autowired
    BusinessPartnerRepository businessRepo;

    @Autowired
    WebpageRepository pageRepo;

    @Autowired
    BusDocRepository busdocRepo;

    @Autowired
    UserSession userSession;

    @Autowired
    JasperPrintReportGenerator reportGenerator;

    @Autowired
    SystemDefaultsController systemCon;

    //private final String[] headers = new String[]{"Date", "Doc No", "Ref", "Amount", "Paid", "Balance", "Cumulative", "Days"};
    private List<BusinessPartner> businessPartners = new LinkedList<>();
    private HashMap<String, String> docTypes;

    private Date statementDate = new Date();

    private String selectedDocType;

    AgeingAnalysisRangeCalculator rangeCalculator = new AgeingAnalysisRangeCalculator();

    private long webpageid = 51;

    private Webpage webpage;

    private String amountInWords;

    private List<AgeAnalysisHelper> reportList;

    private AgeAnalysisHelper totalRow = new AgeAnalysisHelper();

    public AgeAnalysisController() {
    }

    @PostConstruct
    public void init() {
        rangeCalculator.init();
        reloadDocTypes();
        loadWebpage();
    }

    public void refresh() {
        reportList = new LinkedList();
        rangeCalculator.resetAll();
        
        List<Account> accounts = accRepo.findAccountByParent(docTypes.get(getSelectedDocType()));
        if (accounts == null) {
            JsfUtil.addErrorMessage("No Company found in " + getSelectedDocType());
        }
        String dtype = BusDocTransactionType.ACCOUNTS_RECEIVABLE.getValue();
        if (getSelectedDocType().contains("Supplier")) {
            dtype = BusDocTransactionType.ACCOUNTS_PAYABLE.getValue();
        }

        AgeingAnalysisRangeCalculator calc = new AgeingAnalysisRangeCalculator();
        for (Account acc : accounts) {
            BusinessPartner bp = acc.getBusinesspartner();
            if (bp != null) {
                List<BusDoc> list = busdocRepo.findByBusDocByBusinessPartnerAndType(bp.getPartnerid(), getStatementDate(), dtype);
                //System.out.println("AgeingAnalysisController: " + bp.getPartnerid() + " => " + getStatementDate() + " => " + dtype);
                AgeAnalysisHelper aah = new AgeAnalysisHelper(calc.calculateRange(list, statementDate), bp, "", null, 0.0);
                if (Math.abs(aah.getTotalAmount())+Math.abs(aah.getOnAccount()) > 0) {
                    reportList.add(aah);
                    System.out.println("AgeingAnalysisController: " + aah.getBusinessPartner().getCompanyname() + " => " + aah.getTotalAmount() + "\t" + aah.getOnAccount() + "\t" + aah.getNetBalance() + " => " + aah.getRangeAmount());
                }
            }
        }
        //for(AgeAnalysisHelper aah: getReportList()){
        //    System.out.println("AgeingAnalysisController: " + aah.getBusinessPartner().getCompanyname() + " => " + aah.getTotalAmount() + "\t" + aah.getOnAccount() + "\t" + aah.getNetBalance() + " => " + aah.getRangeAmount());
        //}
        calculateTotalRow();
        System.out.println("Total Row: " + getTotalRow().getRangeAmount());
        
        JsfUtil.addSuccessMessage(reportList.size() + " accounts listed");
    }
    
    public void calculateTotalRow(){
        totalRow = new AgeAnalysisHelper();
        if(reportList==null){
            return;
        }
        Double[] totals = new Double[totalRow.getRangeAmount().size()];
        for(int i=0; i<totals.length; i++){
            final int index = i+1;
            totals[i] = reportList.stream().mapToDouble(o->o.getRangeAmount(index)).sum();
        }
        totalRow = new AgeAnalysisHelper(totals, null, "", null, 0.0);
        totalRow.setOnAccount(reportList.stream().mapToDouble(o->o.getOnAccount()).sum());
        totalRow.setLastAmount(reportList.stream().mapToDouble(o->o.getLastAmount()).sum());
        System.out.println("TotalRow: " + totalRow.getRangeAmount());
    }

    public void loadWebpage() {
        webpage = pageRepo.findById(webpageid).get();
        System.out.println("StatementController: " + webpage.getReportid());
    }

    public void reloadDocTypes() {
        docTypes = new HashMap();
        docTypes.put("Local Customer", systemCon.getByPropertyname("LocalCustomerGroup").getValue());
        docTypes.put("Foreign Customer", systemCon.getByPropertyname("ForeignCustomerGroup").getValue());
        docTypes.put("Local Supplier", systemCon.getByPropertyname("LocalSupplierGroup").getValue());
        docTypes.put("Foreign Supplier", systemCon.getByPropertyname("ForeignSupplierGroup").getValue());
        /*Account acc = systemCon.getDefaultAccount("LocalCustomerGroup");
        if (acc != null) {
            docTypes.add(acc);
        }
        acc = systemCon.getDefaultAccount("ForeignCustomerGroup");
        if (acc != null) {
            docTypes.add(acc);
        }
        acc = systemCon.getDefaultAccount("LocalSupplierGroup");
        if (acc != null) {
            docTypes.add(acc);
        }
        acc = systemCon.getDefaultAccount("ForeignSupplierGroup");
        if (acc != null) {
            docTypes.add(acc);
        }*/
        
        setSelectedDocType("Local Customer");
        //System.out.println(docTypes + " => " + getSelectedDocType());
    }

    public StreamedContent getExportPdf() throws JRException {
        if (webpage.getReportid() == null || webpage.getReportid().isEmpty()) {
            JsfUtil.addErrorMessage("No Report Linked to Export");
            return null;
        }
        return reportGenerator.downloadPdf(webpage.getReportid().get(0), this, "Ageing_" + getSelectedDocType() + "");
    }

    public StreamedContent getExportXlsx() throws JRException {
        if (webpage.getReportid() == null || webpage.getReportid().isEmpty()) {
            JsfUtil.addErrorMessage("No Report Linked to Export");
            return null;
        }
        try {
            return reportGenerator.downloadXlsx(webpage.getReportid().get(0), this, "Ageing_" + getSelectedDocType() + "");
        } catch (IOException ex) {
            Logger.getLogger(AgeAnalysisController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Error Exporting");
        }
        return null;
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
     * @return the businessPartners
     */
    public List<BusinessPartner> getBusinessPartners() {
        return businessPartners;
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
    public List<AgeAnalysisHelper> getReportList() {
        return reportList;
    }

    /**
     * @param reportList the reportList to set
     */
    public void setReportList(List<AgeAnalysisHelper> reportList) {
        this.reportList = reportList;
    }

    /**
     * @return the docTypes
     */
    public List<String> getDocTypes() {
        return new LinkedList(docTypes.keySet());
    }

    /**
     * @return the amountInWords
     */
    public String getAmountInWords() {
        if (getTotalRow() == null) {
            return Speller.spellAmount(userSession.getDefaultCurrency(), 0.0);
        }
        return Speller.spellAmount(userSession.getDefaultCurrency(), getTotalRow().getNetBalance());
    }

    /**
     * @param amountInWords the amountInWords to set
     */
    public void setAmountInWords(String amountInWords) {
        this.amountInWords = amountInWords;
    }

    /**
     * @return the totalRow
     */
    public AgeAnalysisHelper getTotalRow() {
        return totalRow;
    }

    /**
     * @param totalRow the totalRow to set
     */
    public void setTotalRow(AgeAnalysisHelper totalRow) {
        this.totalRow = totalRow;
    }

    public Double getGrandBalance(){
        if(totalRow==null){
            return 0.0;
        }
        return totalRow.getNetBalance();
    }

    public Double getTotalRange1(){
        if(totalRow==null){
            return 0.0;
        }
        return totalRow.getRange1();
    }
    
    public String getReportTitle(){
        return "Ageing Analysis - " + getSelectedDocType();
    }
    
    public String getCurrency(){
        if(getSelectedDocType().contains("Local")){
            return userSession.getDefaultCurrency();
        }
        return "FC";
    }
}
