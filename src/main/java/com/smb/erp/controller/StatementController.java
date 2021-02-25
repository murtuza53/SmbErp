/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocTransactionType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.Webpage;
import com.smb.erp.helper.StatementHelper;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.WebpageRepository;
import com.smb.erp.rest.JasperPrintReportGenerator;
import com.smb.erp.util.AgeingAnalysisRangeCalculator;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.Speller;
import com.smb.erp.util.SystemConfig;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "statementController")
@ViewScoped
public class StatementController implements Serializable {

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

    private final String[] headers = new String[]{"Date", "Doc No", "Ref", "Amount", "Paid", "Balance", "Cumulative", "Days"};
    private List<BusinessPartner> businessPartners = new LinkedList<>();
    private final List<String> docTypes = Arrays.asList(new String[]{"Customer", "Supplier"});

    private Date statementDate = new Date();

    private BusinessPartner selectedBusinessPartner;
    private String selectedDocType = "Customer";

    private AgeingAnalysisRangeCalculator rangeCalculator = new AgeingAnalysisRangeCalculator();

    private long webpageid = 48;

    private Webpage webpage;

    private String amountInWords;

    private List<StatementHelper> reportList;

    private double netBalance;

    private double onAccount;

    private double range1;

    private double range2;

    private double range3;

    private double range4;

    private double range5;

    private double range6;

    public StatementController() {

    }

    @PostConstruct
    public void init() {
        reloadBusinessPartners();
        refresh();
        loadWebpage();
    }

    public void refresh() {
        System.out.println("Refresh: " + getSelectedBusinessPartner());

        if (getSelectedBusinessPartner() == null) {
            JsfUtil.addErrorMessage("No Company selected for statement");
            return;
        }
        String dtype = BusDocTransactionType.ACCOUNTS_RECEIVABLE.getValue();
        if (selectedDocType.equalsIgnoreCase("Supplier")) {
            dtype = BusDocTransactionType.ACCOUNTS_PAYABLE.getValue();
        }
        //reportList = busdocRepo.findByBusDocByBusinessPartnerAndType(selectedBusinessPartner.getPartnerid(), getStatementDate(), dtype).stream().filter(line -> line.getTotalPending() > 0).collect(Collectors.toList());
        List<BusDoc> list = busdocRepo.findByBusDocByBusinessPartnerAndType(selectedBusinessPartner.getPartnerid(), getStatementDate(), dtype);
        System.out.println("StatementController: " + selectedBusinessPartner.getPartnerid() + " => " + getStatementDate() + " => " + dtype);
        //System.out.println("StatementController: " + list);
        netBalance = 0;
        if (list != null) {
            reportList = new LinkedList();
            double bal = 0;
            for (BusDoc doc : list) {
                if (bal == 0) {
                    bal = doc.getTotalPending();
                    doc.setCumulative(bal);
                } else {
                    bal = bal + doc.getTotalPending();
                    doc.setCumulative(bal);
                }
                reportList.add(new StatementHelper(doc.getDocno(), doc.getRefno(), SystemConfig.DATE_FORMAT.format(doc.getDocdate()),
                        doc.getTotalamount(), doc.getTotalPaid(), doc.getTotalPending(),
                        doc.getCumulative(), doc.findInvoiceAge(statementDate)));
            }
            netBalance = bal;
            Double[] range = rangeCalculator.calculateRange(list, statementDate);
            if (range != null && range.length == 6) {
                setRange1(range[0]);
                setRange2(range[1]);
                setRange3(range[2]);
                setRange4(range[3]);
                setRange5(range[4]);
                setRange6(range[5]);
            }
        }

        JsfUtil.addSuccessMessage(reportList.size() + " documents listed for " + selectedBusinessPartner.getCompanyname());
    }

    public void loadWebpage() {
        webpage = pageRepo.findById(webpageid).get();
        System.out.println("StatementController: " + webpage.getReportid());
    }

    public void reloadBusinessPartners() {
        businessPartners = businessRepo.findBusinessPartnerByCompanyType(selectedDocType);
        if (businessPartners != null && businessPartners.size() > 0) {
            selectedBusinessPartner = businessPartners.get(0);
        }
    }

    public StreamedContent getExportPdf() throws JRException {
        if (webpage.getReportid() == null || webpage.getReportid().isEmpty()) {
            JsfUtil.addErrorMessage("No Report Linked to Export");
            return null;
        }
        return reportGenerator.downloadPdf(webpage.getReportid().get(0), this, "SOA_" + getSelectedBusinessPartner().getCompanyname() + "");
    }

    public StreamedContent getExportXlsx() throws JRException {
        if (webpage.getReportid() == null || webpage.getReportid().isEmpty()) {
            JsfUtil.addErrorMessage("No Report Linked to Export");
            return null;
        }
        try {
            return reportGenerator.downloadXlsx(webpage.getReportid().get(0), this, "SOA_" + getSelectedBusinessPartner().getCompanyname() + "");
        } catch (IOException ex) {
            Logger.getLogger(StatementController.class.getName()).log(Level.SEVERE, null, ex);
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
    public List<StatementHelper> getReportList() {
        return reportList;
    }

    /**
     * @param reportList the reportList to set
     */
    public void setReportList(List<StatementHelper> reportList) {
        this.reportList = reportList;
    }

    /**
     * @return the docTypes
     */
    public List<String> getDocTypes() {
        return docTypes;
    }

    /**
     * @return the amountInWords
     */
    public String getAmountInWords() {
        return Speller.spellAmount(userSession.getDefaultCurrency(), netBalance);
    }

    /**
     * @param amountInWords the amountInWords to set
     */
    public void setAmountInWords(String amountInWords) {
        this.amountInWords = amountInWords;
    }

    /**
     * @return the netBalance
     */
    public double getNetBalance() {
        return netBalance;
    }

    /**
     * @param netBalance the netBalance to set
     */
    public void setNetBalance(double netBalance) {
        this.netBalance = netBalance;
    }

    /**
     * @return the onAccount
     */
    public double getOnAccount() {
        return onAccount;
    }

    /**
     * @param onAccount the onAccount to set
     */
    public void setOnAccount(double onAccount) {
        this.onAccount = onAccount;
    }

    /**
     * @return the range1
     */
    public double getRange1() {
        return range1;
    }

    /**
     * @param range1 the range1 to set
     */
    public void setRange1(double range1) {
        this.range1 = range1;
    }

    /**
     * @return the range2
     */
    public double getRange2() {
        return range2;
    }

    /**
     * @param range2 the range2 to set
     */
    public void setRange2(double range2) {
        this.range2 = range2;
    }

    /**
     * @return the range3
     */
    public double getRange3() {
        return range3;
    }

    /**
     * @param range3 the range3 to set
     */
    public void setRange3(double range3) {
        this.range3 = range3;
    }

    /**
     * @return the range4
     */
    public double getRange4() {
        return range4;
    }

    /**
     * @param range4 the range4 to set
     */
    public void setRange4(double range4) {
        this.range4 = range4;
    }

    /**
     * @return the range5
     */
    public double getRange5() {
        return range5;
    }

    /**
     * @param range5 the range5 to set
     */
    public void setRange5(double range5) {
        this.range5 = range5;
    }

    /**
     * @return the range6
     */
    public double getRange6() {
        return range6;
    }

    /**
     * @param range6 the range6 to set
     */
    public void setRange6(double range6) {
        this.range6 = range6;
    }
}
