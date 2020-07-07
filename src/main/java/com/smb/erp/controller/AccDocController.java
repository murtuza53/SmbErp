/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.AccDoc;
import com.smb.erp.entity.AccountTransactionType;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.LedgerLine;
import com.smb.erp.repo.AccDocRepository;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.EvaluateExpression;
import java.util.Hashtable;
import java.util.LinkedList;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

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

        }
    }

    public void createBusDocSalesJV(BusDoc busdoc) {
        BusDocInfo info = busdoc.getBusdocinfo();

        if (info.getTranstypeid() != null) {
            AccDoc accdoc = repo.findByRefno(busdoc.getDocno());
            if (accdoc == null) {
                accdoc = new AccDoc();
            }
            accdoc.setDocdate(busdoc.getCreatedon());
            accdoc.setBranch(busdoc.getBranch());
            accdoc.setEmp(busdoc.getEmp1());
            accdoc.setDocno(keyCon.getDocNo("JV", DateUtil.getYear(busdoc.getCreatedon())));
            if (accdoc.getLedlines() != null) {
                for (LedgerLine line : accdoc.getLedlines()) {
                    accdoc.removeLedline(line);
                }
            } else {
                accdoc.setLedlines(new LinkedList<>());
            }
            ledtable = new Hashtable<String, LedgerLine>();
            for (AccountTransactionType att : info.getTranstypeid()) {
                if (!att.getAccountid().getNodetype().equals("INTERNAL_ACCOUNT")) {
                    prepareBusDocLedgerLine(accdoc, att, busdoc);
                }
            }
        }
    }

    public void createBusDocPurchaseJV(BusDoc busdoc) {

    }

    public void prepareBusDocLedgerLine(AccDoc accdoc, AccountTransactionType att, BusDoc busdoc) {
        if (ledtable.get(att.getAccountid().getAccountid()) == null) {
            LedgerLine line = new LedgerLine();
            line.setTransdate(busdoc.getCreatedon());
            line.setAccount(att.getAccountid());
            accdoc.addLedline(line);
            evaluateLedgerLineValue(line, att, busdoc);
            ledtable.put(att.getAccountid().getAccountid(), line);
        }
    }

    public void evaluateLedgerLineValue(LedgerLine line, AccountTransactionType att, BusDoc doc) {
        double value = EvaluateExpression.evaluateExpression(doc, att.getFormula());
        if (att.getTranstype().equalsIgnoreCase("Credit")) {
            line.setCredit(value);
        } else {
            line.setDebit(value);
        }
    }
}
