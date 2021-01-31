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
import java.io.Serializable;
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
@Named(value = "ageingController")
@ViewScoped
@Service
public class AgeingController implements Serializable {

    @Autowired
    AccountRepository accRepo;

    @Autowired
    LedgerLineController ledCon;

    @Autowired
    BusinessPartnerRepository businessRepo;

    @Autowired
    BusDocRepository busdocRepo;

    private final String[] headers = new String[]{"0-30", "31-60", "61-90", "91-120", ">120", "Total"};
    private final HashMap<String, Double> rowValues = new HashMap<>();
    private final List<BusinessPartner> businessPartners = new LinkedList<BusinessPartner>();
    private final HashMap<Long, HashMap<String, Double>> table = new HashMap<>();
    private final List<Double> totals = new LinkedList<>();

    private final List<Account> accountTypes = new LinkedList();
    
    private Account localSupplierAccount;
    private Account localCustomerAccount;
    private Account foreignSupplierAccount;
    private Account foreignCustomerAccount;

    private Date statementDate = new Date();

    private Account selectedAccount;

    private BusinessPartner selectedBusinessPartner;

    public AgeingController() {

    }

    @PostConstruct
    public void init() {
        if (localSupplierAccount == null) {
            localSupplierAccount = accRepo.getOne("211110");
            localSupplierAccount.getAccountid();
        }
        if (localCustomerAccount == null) {
            localCustomerAccount = accRepo.getOne("171110");
            localCustomerAccount.getAccountid();
        }
        if (foreignSupplierAccount == null) {
            foreignSupplierAccount = accRepo.getOne("211111");
            foreignSupplierAccount.getAccountid();
        }
        if (foreignCustomerAccount == null) {
            foreignCustomerAccount = accRepo.getOne("171111");
            foreignCustomerAccount.getAccountid();
        }
        getAccountTypes().add(localCustomerAccount);
        getAccountTypes().add(localSupplierAccount);
        getAccountTypes().add(foreignCustomerAccount);
        getAccountTypes().add(foreignSupplierAccount);
        setSelectedAccount(localCustomerAccount);
        
        refresh();
    }

    public void refresh() {
        clear();
        System.out.println("Refresh: " + getSelectedAccount());
        String busDocType = BusDocTransactionType.ACCOUNTS_RECEIVABLE.toString();
        if (getSelectedAccount().getAccountid().startsWith("2111")) {
            busDocType = BusDocTransactionType.ACCOUNTS_PAYABLE.toString();
        }
        List<Account> acclist = accRepo.findAccountByParent(getSelectedAccount().getAccountid());
        //System.out.println("BUSINESS_LIST: " + acclist);
        if (acclist == null) {
            return;
        }
        for (Account acc : acclist) {
            //System.out.println("AccountStatement_refresh: " + acc + " => " + ledCon);
            Double bal = ledCon.findAccountBalance(acc.getAccountid(), getStatementDate());
            if (bal == null) {
                bal = 0.0;
            }
            bal = Math.abs(bal);
            getTotals().add(bal);
            if (bal != 0) {
                //List<BusDoc> blist = busdocRepo.findByBusDocByBusinessPartnerAndType(acc.getBusinesspartner().getPartnerid(), getStatementDate(), busDocType);
                //System.out.println("RefresH_DocList: " + acc.getBusinesspartner().getPartnerid() + " => " + busDocType);
                List<BusDoc> blist = busdocRepo.findByBusDocByBusinessPartnerAndType(acc.getBusinesspartner().getPartnerid(), getStatementDate(), busDocType);
                //System.out.println("RefresH_DocList_2: " + blist);
                if (blist != null) {
                    addBusinessPartner(acc.getBusinesspartner(), blist.stream().filter(line -> line.getTotalPending() > 0).collect(Collectors.toList()), bal);
                } else {
                    addBusinessPartner(acc.getBusinesspartner(), new LinkedList(), bal);
                }
                //HashMap<String, Double> bpValues = getTable().get(acc.getBusinesspartner().getPartnerid());
                //bpValues.put(getHeaders()[5], bal);
            }
        }
    }

    public void clear() {
        rowValues.clear();
        getTable().clear();
        getTotals().clear();
        getBusinessPartners().clear();
    }

    public void addBusinessPartner(BusinessPartner bp, List<BusDoc> docs, Double total) {
        if (docs == null) {
            return;
        }
        HashMap<String, Double> bpValues = getTable().get(bp.getPartnerid());
        if (bpValues == null) {
            bpValues = new HashMap<>();
            getTable().put(bp.getPartnerid(), bpValues);
        }
        businessPartners.add(bp);
        if (docs.isEmpty()) {
            addCompanyValue(bpValues, 0, 0.0);
        } else {
            for (BusDoc doc : docs) {
                int age = doc.findInvoiceAge(getStatementDate());
                if (age <= 30) {
                    addCompanyValue(bpValues, 0, doc.getTotalPending());
                } else if (age >= 31 && age <= 60) {
                    addCompanyValue(bpValues, 1, doc.getTotalPending());
                } else if (age >= 61 && age <= 90) {
                    addCompanyValue(bpValues, 2, doc.getTotalPending());
                } else if (age >= 91 && age <= 120) {
                    addCompanyValue(bpValues, 3, doc.getTotalPending());
                } else {
                    addCompanyValue(bpValues, 4, doc.getTotalPending());
                }
            }
        }
        bpValues.put(getHeaders()[5], total);
    }

    public void addCompanyValue(HashMap<String, Double> line, int col, Double value) {
        for (int i = 0; i < getHeaders().length; i++) {
            Double total = line.remove(getHeaders()[i]);
            if (total == null) {
                total = 0.0;
            }
            if (i == col) {
                total = total + value;
            }
            line.put(getHeaders()[i], total);
        }
    }

    public double calculateTotal(int col) {
        if (table.keySet() != null) {
            double total = 0.0;
            for (Long key : table.keySet()) {
                if (table.get(key).get(getHeaders()[col]) != null) {
                    total = total + table.get(key).get(getHeaders()[col]);
                }
            }
            return total;
        }
        return 0.0;
    }

    /**
     * @return the localSupplierAccount
     */
    public Account getLocalSupplierAccount() {
        return localSupplierAccount;
    }

    /**
     * @param localSupplierAccount the localSupplierAccount to set
     */
    public void setLocalSupplierAccount(Account localSupplierAccount) {
        this.localSupplierAccount = localSupplierAccount;
    }

    /**
     * @return the localCustomerAccount
     */
    public Account getLocalCustomerAccount() {
        return localCustomerAccount;
    }

    /**
     * @param localCustomerAccount the localCustomerAccount to set
     */
    public void setLocalCustomerAccount(Account localCustomerAccount) {
        this.localCustomerAccount = localCustomerAccount;
    }

    /**
     * @return the foreignSupplierAccount
     */
    public Account getForeignSupplierAccount() {
        return foreignSupplierAccount;
    }

    /**
     * @param foreignSupplierAccount the foreignSupplierAccount to set
     */
    public void setForeignSupplierAccount(Account foreignSupplierAccount) {
        this.foreignSupplierAccount = foreignSupplierAccount;
    }

    /**
     * @return the foreignCustomerAccount
     */
    public Account getForeignCustomerAccount() {
        return foreignCustomerAccount;
    }

    /**
     * @param foreignCustomerAccount the foreignCustomerAccount to set
     */
    public void setForeignCustomerAccount(Account foreignCustomerAccount) {
        this.foreignCustomerAccount = foreignCustomerAccount;
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
     * @return the table
     */
    public HashMap<Long, HashMap<String, Double>> getTable() {
        return table;
    }

    /**
     * @return the selectedAccount
     */
    public Account getSelectedAccount() {
        return selectedAccount;
    }

    /**
     * @param selectedAccount the selectedAccount to set
     */
    public void setSelectedAccount(Account selectedAccount) {
        System.out.println("setSelectedAccount: " + selectedAccount);
        this.selectedAccount = selectedAccount;
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
     * @return the totals
     */
    public List<Double> getTotals() {
        return totals;
    }

    /**
     * @return the accountTypes
     */
    public List<Account> getAccountTypes() {
        return accountTypes;
    }

}
