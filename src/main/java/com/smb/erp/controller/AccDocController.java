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
import com.smb.erp.entity.LedgerLine;
import com.smb.erp.entity.ProductAccount;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.repo.AccDocRepository;
import com.smb.erp.repo.VatMappingRepository;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.EvaluateExpression;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelEvaluationException;

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

    Hashtable<String, LedgerLine> ledtable = new Hashtable<String, LedgerLine>();

    @Autowired
    public AccDocController(AccDocRepository repo) {
        super(AccDoc.class, repo);
        this.repo = repo;
    }

    public void createBusDocJV(BusDoc busdoc) {
        if (busdoc.getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.SALES.getValue())) {
            createBusDocSalesJV(busdoc);
        } else {
            createBusDocSalesJV(busdoc);
        }
    }

    public void createBusDocSalesJV(BusDoc busdoc) {
        BusDocInfo info = busdoc.getBusdocinfo();

        if (info.getTranstypeid() != null) {
            AccDoc accdoc = repo.findByRefno(busdoc.getDocno());
            if (accdoc == null) {
                accdoc = new AccDoc();
                accdoc.setDocno(keyCon.getDocNo("JV", DateUtil.getYear(busdoc.getCreatedon())));
            }
            accdoc.setDocdate(busdoc.getCreatedon());
            accdoc.setBranch(busdoc.getBranch());
            accdoc.setEmp(busdoc.getEmp1());
            accdoc.setUpdatedon(new Date());
            accdoc.setRefno(busdoc.getDocno());
            if (accdoc.getLedlines() != null) {
                while (accdoc.getLedlines().size() > 0) {      //remove all current ledgerlines
                    LedgerLine line = accdoc.getLedlines().get(0);
                    accdoc.removeLedline(line);
                }
            } else {
                accdoc.setLedlines(new LinkedList<>());
            }
            ledtable = new Hashtable<String, LedgerLine>();
            for (AccountTransactionType att : info.getTranstypeid()) {
                if (att.getAccountid().getNodetype().equals("INTERNAL_ACCOUNT")) {  //internal accounts refer to products
                    for (ProductTransaction pt : busdoc.getProductTransactions()) {
                        prepareProductLedgerLine(accdoc, att, pt);
                    }
                } else {     //direct account reference
                    prepareBusDocLedgerLine(accdoc, att, busdoc);
                }
            }

            //ledgerline table is ready now but may contain ledgerline with credit & debit value
            //System.out.println("JV Entries for " + busdoc.getDocno());
            for (LedgerLine line : accdoc.getLedlines()) {
                line.setRefno(busdoc.getDocno());
                line.setReftype(busdoc.getBusdocinfo().getTransactiontype());
                line.setBranch(accdoc.getBranch());
                correctLedgerLine(line);
                System.out.println(line.getTransdate() + "\t" + line.getDebit() + "\t" + line.getCredit() + "\t" + line.getAccount().getAccountname());
            }

            //accdoc.setDocno(keyCon.getDocNo("JV", DateUtil.getYear(busdoc.getCreatedon())));
            repo.save(accdoc);
        }
    }

    public void createBusDocPurchaseJV(BusDoc busdoc) {

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

    public void prepareBusDocLedgerLine(AccDoc accdoc, AccountTransactionType att, BusDoc busdoc) {
        LedgerLine line = ledtable.get(att.getAccountid().getAccountid());
        if (line == null) {
            line = new LedgerLine();
            line.setTransdate(busdoc.getCreatedon());
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
        } else if (internalAcc.getAccountid().equalsIgnoreCase("INT15")) {
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

        if (att.getTranstype().equalsIgnoreCase("Credit")) {
            line.setCredit(line.getCredit() + value);
        } else {
            line.setDebit(line.getDebit() + value);
        }
    }
}
