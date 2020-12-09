/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.AccDoc;
import com.smb.erp.entity.Account;
import com.smb.erp.entity.AccountTransactionType;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.LedgerLine;
import com.smb.erp.entity.ProductAccount;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.repo.AccDocRepository;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.EvaluateExpression;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "accDocController")
@ViewScoped
public class AccDocController extends AbstractController<AccDoc> {

    AccDocRepository repo;

    @Autowired
    TableKeyController keyCon;

    @Autowired
    SystemDefaultsController defaultController;

    Hashtable<String, LedgerLine> ledtable = new Hashtable<>();

    @Autowired
    public AccDocController(AccDocRepository repo) {
        super(AccDoc.class, repo);
        this.repo = repo;
    }

    public List<Account> getBusDocCashRegisterAccounts(String docNo) {
        AccDoc accdoc = repo.findByRefno(docNo);
        List<Account> accounts = null;
        if (accdoc != null) {
            accounts = new LinkedList<>();
            for (LedgerLine ll : accdoc.getLedlines()) {
                if (ll.getAccount().getParentid().getAccountid().equalsIgnoreCase("16")) {
                    Account acc = ll.getAccount();
                    acc.setAmount(ll.getDebit() + ll.getCredit());
                    acc.setDescription(ll.getDescription());
                    accounts.add(acc);
                }
            }
        }
        return accounts;
    }

    public List<Account> getBusDocCashRegisterAccounts(String docNo, List<Account> accountList) {
        if (accountList == null || accountList.isEmpty()) {
            return new LinkedList<>();
        }
        AccDoc accdoc = repo.findByRefno(docNo);
        List<Account> accounts = null;
        if (accdoc != null) {
            accounts = new LinkedList<>();
            for (LedgerLine ll : accdoc.getLedlines()) {
                if (contains(ll.getAccount(), accountList)) {
                    Account acc = ll.getAccount();
                    acc.setAmount(ll.getDebit() + ll.getCredit());
                    acc.setDescription(ll.getDescription());
                    accounts.add(acc);
                }
            }
        }
        return accounts;
    }

    public boolean contains(Account acc, List<Account> accounts) {
        for (Account a : accounts) {
            if (a.getAccountid().equalsIgnoreCase(acc.getAccountid())) {
                return true;
            }
        }
        return false;
    }

    public AccDoc prepareJVViewwer(BusDoc busdoc) {
        AccDoc accdoc = repo.findByRefno(busdoc.getDocno());
        if (accdoc == null) {
            accdoc = prepareBusDocJV(busdoc);
        }
        return accdoc;
    }

    public AccDoc prepareBusDocJV(BusDoc busdoc) {
        if (busdoc.getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.SALES.getValue())
                || busdoc.getBusdocinfo().getPrefix().equalsIgnoreCase("SHS")) {
            return prepareBusDocSPJV(busdoc);
        } else if(busdoc.getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.PURCHASE.getValue())
                || busdoc.getBusdocinfo().getPrefix().equalsIgnoreCase("EXS")){
            //createBusDocSalesJV(busdoc);
            return prepareBusDocSPJV(busdoc);
        }
        return null;
    }

    @Transactional
    public AccDoc createBusDocJV(BusDoc busdoc) {
        if (busdoc.getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.SALES.getValue())
                || busdoc.getBusdocinfo().getPrefix().equalsIgnoreCase("SHS")) {
            return createBusDocSalesJV(busdoc);
        } else if (busdoc.getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.PURCHASE.getValue())
                || busdoc.getBusdocinfo().getPrefix().equalsIgnoreCase("EXS")) {
            return createBusDocPurchaseJV(busdoc);
        }
        return null;
    }

    @Transactional
    public AccDoc createBusDocSalesJV(BusDoc busdoc) {
        String docNo = null;
        AccDoc accdoc = repo.findByRefno(busdoc.getDocno());
        if(accdoc!=null){
            docNo = accdoc.getDocno();
            repo.deleteById(docNo);
        }
        if(docNo==null){
            docNo = keyCon.getDocNo("AV", DateUtil.getYear(busdoc.getCreatedon()));
        }
        accdoc = prepareBusDocSPJV(busdoc);
        accdoc.setDocno(docNo);
        repo.save(accdoc);
        return accdoc;
    }

    @Transactional
    public AccDoc createBusDocPurchaseJV(BusDoc busdoc) {
        //AccDoc accdoc = prepareBusDocSPJV(busdoc);
        //accdoc.setDocno(keyCon.getDocNo("AV", DateUtil.getYear(busdoc.getCreatedon())));
        //repo.save(accdoc);
        //return accdoc;
        return createBusDocSalesJV(busdoc);
    }

    @Transactional
    public AccDoc prepareBusDocSPJV(BusDoc busdoc) {
        BusDocInfo info = busdoc.getBusdocinfo();

        AccDoc accdoc = repo.findByRefno(busdoc.getDocno());
        if (info.getTranstypeid() != null) {
            if (accdoc == null) {
                accdoc = new AccDoc();
                accdoc.setDocdate(busdoc.getDocdate());
                accdoc.setCreatedon(accdoc.getDocdate());
            }
            accdoc.setBranch(busdoc.getBranch());
            accdoc.setEmp(busdoc.getEmp1());
            accdoc.setUpdatedon(new Date());
            accdoc.setRefno(busdoc.getDocno());
            if (accdoc.getLedlines() != null) {
                while (accdoc.getLedlines().size() > 0) {      //remove all current ledgerlines
                    LedgerLine line = accdoc.getLedlines().get(0);
                    accdoc.removeLedline(line);
                }
                //repo.save(accdoc);  //save the changes and then create new ledgerlines
            } else {
                accdoc.setLedlines(new LinkedList<>());
            }
            ledtable = new Hashtable<String, LedgerLine>();
            for (AccountTransactionType att : info.getTranstypeid()) {
                System.out.println("AccountTransactionType:\t" + att.getTranstype() + "\t" + att.getFormula() + "\t" + att.getAccountid());
                if (att.getAccountid().getNodetype().equals("INTERNAL_ACCOUNT")) {  //internal accounts refer to products
                    if (att.getAccountid().getAccountname().equalsIgnoreCase("DefBusinessPartnerAccount")) {
                        //busdoc.getBusdocinfo().getDoctype().
                        prepareBusinessPartnerLine(accdoc, busdoc.getBusinesspartner(), att, busdoc);
                    } else if (att.getAccountid().getAccountname().equalsIgnoreCase("DefCashRegisterAccount")) {
                        if (busdoc.getAccounts() != null) {
                            for (Account acc : busdoc.getAccounts()) {
                                if (!acc.getAccountid().equalsIgnoreCase("Total")) {
                                    if (acc.getAmount() > 0) {
                                        prepareBusDocLedgerLine(accdoc, acc, att);
                                    }
                                }
                            }
                        }
                    } else {
                        for (ProductTransaction pt : busdoc.getProductTransactions()) {
                            prepareProductLedgerLine(accdoc, att, pt);
                        }
                    }
                } else {     //direct account reference
                    prepareBusDocLedgerLine(accdoc, att, busdoc);
                }
            }

            List<LedgerLine> deletedLedLine = new LinkedList<>();
            //ledgerline table is ready now but may contain ledgerline with credit & debit value
            //System.out.println("JV Entries for " + busdoc.getDocno());
            for (LedgerLine line : accdoc.getLedlines()) {
                line.setRefno(busdoc.getDocno());
                line.setReftype(busdoc.getBusdocinfo().getTransactiontype());
                line.setBranch(accdoc.getBranch());
                correctLedgerLine(line);
                if((line.getDebit()+line.getCredit())==0){
                    deletedLedLine.add(line);
                }
                System.out.println(line.getTransdate() + "\t" + line.getDebit() + "\t" + line.getCredit() + "\t" + line.getAccount().getAccountname());
            }

            //delete ledgerline that has zero value
            if(deletedLedLine.size()>0){
                for(LedgerLine ll: deletedLedLine){
                    accdoc.removeLedline(ll);
                }
            }
            //accdoc.setDocno(keyCon.getDocNo("JV", DateUtil.getYear(busdoc.getCreatedon())));
        }
        return accdoc;
    }

    //correct Credit and Debit if both amount is present
    public void correctLedgerLine(LedgerLine line) {
        if (line.getCredit() > line.getDebit()) {
            line.setCredit(line.getCredit() - line.getDebit());
            line.setDebit(0.0);
        } else {
            line.setDebit(line.getDebit() - line.getCredit());
            line.setCredit(0.0);
        }
    }

    public Account getAccountByBusDocType(String type, List<Account> accounts) {
        //customer 1711
        //supplier  2111
        if (accounts != null) {
            for (Account acc : accounts) {
                if (acc.getAccountid().startsWith("1711") && type.equalsIgnoreCase(BusDocType.SALES.getValue())) {
                    return acc;
                } else if (acc.getAccountid().startsWith("2111") && type.equalsIgnoreCase(BusDocType.PURCHASE.getValue())) {
                    return acc;
                }
            }
        }
        return null;
    }

    public void prepareBusinessPartnerLine(AccDoc accdoc, BusinessPartner partner, AccountTransactionType att, BusDoc busdoc) {
        Account acc = getAccountByBusDocType(busdoc.getBusdocinfo().getDoctype(), partner.getAccounts());
        System.out.println("prepareBusinessPartnerLine: " + acc);
        if (acc != null) {
            LedgerLine line = new LedgerLine();
            line.setTransdate(busdoc.getCreatedon());
            line.setAccount(acc);
            accdoc.addLedline(line);
            ledtable.put(line.getAccount().getAccountid(), line);
            evaluateLedgerLineValue(line, att, busdoc);
        }
    }

    public void prepareBusDocLedgerLine(AccDoc accdoc, Account acc, AccountTransactionType att) {
        LedgerLine line = ledtable.get(acc.getAccountid());
        if (line == null) {
            line = new LedgerLine();
            line.setTransdate(accdoc.getDocdate());
            line.setAccount(acc);
            line.setDescription(acc.getDescription());
            if (att.getTranstype().equalsIgnoreCase("Credit")) {
                line.setCredit(acc.getAmount());
            } else {
                line.setDebit(acc.getAmount());
            }
            accdoc.addLedline(line);
            ledtable.put(line.getAccount().getAccountid(), line);
        }
    }

    public void prepareBusDocLedgerLine(AccDoc accdoc, AccountTransactionType att, BusDoc busdoc) {
        LedgerLine line = ledtable.get(att.getAccountid().getAccountid());
        if (line == null) {
            line = new LedgerLine();
            line.setTransdate(accdoc.getDocdate());
            line.setAccount(att.getAccountid());
            accdoc.addLedline(line);
            ledtable.put(line.getAccount().getAccountid(), line);
        }
        evaluateLedgerLineValue(line, att, busdoc);
    }

    public void prepareProductLedgerLine(AccDoc accdoc, AccountTransactionType att, ProductTransaction pt) {
        Account ledaccount = getProductAccount(att.getAccountid(), pt.getProduct().getProdaccount());
        LedgerLine line = ledtable.get(ledaccount.getAccountid());
        if (line == null) {
            line = new LedgerLine();
            line.setTransdate(accdoc.getDocdate());
            //line.setAccount(att.getAccountid());
            line.setAccount(ledaccount);
            accdoc.addLedline(line);
            ledtable.put(line.getAccount().getAccountid(), line);
        }
        evaluateLedgerLineValue(line, att, pt);
    }

    public Account getProductAccount(Account internalAcc, ProductAccount pa) {
        if (internalAcc.getAccountid().equalsIgnoreCase("INT14")) { //default sales account
            if (pa != null) {
                return defaultController.resolveAccount(pa.getSalesAccount());
            }
            return defaultController.getDefaultAccount("DefSalesAccount");
        } else if (internalAcc.getAccountid().equalsIgnoreCase("INT15")) {  //default purchase account
            if (pa != null) {
                return defaultController.resolveAccount(pa.getPurchaseAccount());
            }
            return defaultController.getDefaultAccount("DefPurchaseAccount");
        }
        if (pa != null) {
            return defaultController.resolveAccount(pa.getConsumptionAccount());
        }
        return defaultController.getDefaultAccount("DefConsumptionAccount");
    }

    public void evaluateLedgerLineValue(LedgerLine line, AccountTransactionType att, BusDoc doc) {
        double value = 0;
        try {
            value = EvaluateExpression.evaluateExpression(doc, att.getFormula());
        } catch (SpelEvaluationException exception) {
            System.out.println("Invalid Formula: " + att.getFormula() + " " + doc.getDocno() + " " + att.getAccountid());
        }

        System.out.println("evaluateLedgerLineValue:busdoc:\t" + line.getAccount() + "\t\t" + att.getTranstype() + "\t" + att.getFormula() + "\t" + value);
        if (att.getTranstype().equalsIgnoreCase("Credit")) {
            line.setCredit(line.getCredit() + value);
        } else {
            line.setDebit(line.getDebit() + value);
        }
    }

    public void evaluateLedgerLineValue(LedgerLine line, AccountTransactionType att, ProductTransaction pt) {
        double value = 0;
        try {
            value = EvaluateExpression.evaluateExpression(pt, att.getFormula());
        } catch (SpelEvaluationException exception) {
            System.out.println("Invalid Formula: " + att.getFormula() + " " + pt.getProduct() + " " + att.getAccountid());
        }

        System.out.println("evaluateLedgerLineValue:prodtrans:\t" + line.getAccount() + "\t\t" + att.getTranstype() + "\t" + att.getFormula() + "\t" + value);
        if (att.getTranstype().equalsIgnoreCase("Credit")) {
            line.setCredit(line.getCredit() + value);
        } else {
            line.setDebit(line.getDebit() + value);
        }
    }
}
