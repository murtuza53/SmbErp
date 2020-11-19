/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.LedgerLine;
import com.smb.erp.entity.PartialPaymentDetail;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Burhani152
 * @param <LedgerLine>
 */
public class AccountTab extends DocumentTab<LedgerLine> {

    private List<BusDoc> pendingDocs;

    private List<PartialPaymentDetail> paymentList = new LinkedList<>();

    private List<BusDoc> selectedPendingDocs;

    private PartialPaymentDetail selectedPaymentDetails;

    private List<PartialPaymentDetail> deletedPP = new LinkedList();

    public AccountTab(String title, String page, boolean closable) {
        super(title, page, closable);
    }

    public AccountTab(LedgerLine line, String title, String page, MODE mode) {
        super(line, title, page, mode);
        if (line.getPpdetails() != null) {
            this.paymentList = new LinkedList(line.getPpdetails());
        }
    }

    /**
     * @return the pendingDocs
     */
    public List<BusDoc> getPendingDocs() {
        return pendingDocs;
    }

    /**
     * @param pendingDocs the pendingDocs to set
     */
    public void setPendingDocs(List<BusDoc> pendingDocs) {
        this.pendingDocs = pendingDocs;
    }

    /**
     * @return the paymentList
     */
    public List<PartialPaymentDetail> getPaymentList() {
        return paymentList;
    }

    /**
     * @param paymentList the paymentList to set
     */
    public void setPaymentList(List<PartialPaymentDetail> paymentList) {
        this.paymentList = paymentList;
    }

    public void transferSelectedPendingInvoice() {
        if (selectedPendingDocs != null) {
            for (BusDoc doc : selectedPendingDocs) {
                if (!containsBusDoc(doc)) {
                    PartialPaymentDetail ppd = new PartialPaymentDetail();
                    ppd.setTransdate(getData().getTransdate());
                    ppd.setAmount(doc.getTotalPending());
                    getData().addPpdetail(ppd);
                    doc.addPpdetail(ppd);

                    paymentList.add(ppd);
                }
            }
            updateLedgerValue();
        }
        System.out.println("transferSelectedPendingInvoice: " + paymentList.size());
    }

    public void deleteSelectedInvoice() {
        if (getSelectedPaymentDetails() != null) {
            selectedPaymentDetails.getBusdoc().removePpdetail(selectedPaymentDetails);
            getData().removePpdetail(selectedPaymentDetails);
            paymentList.remove(getSelectedPaymentDetails());
            //System.out.println("deleteSelectedInvoice: " + getSelectedPaymentDetails().getPplineno() + " => " + selectedPaymentDetails.getBusdoc() + " => " + selectedPaymentDetails.getLedline());
            deletedPP.add(selectedPaymentDetails);
        }
    }

    public boolean containsBusDoc(BusDoc doc) {
        for (PartialPaymentDetail ppd : paymentList) {
            if (ppd.getBusdoc().getDocno().equalsIgnoreCase(doc.getDocno())) {
                return true;
            }
        }
        return false;
    }

    public void addBusDoc(BusDoc busdoc) {
        PartialPaymentDetail ppd = new PartialPaymentDetail();
        ppd.setTransdate(getData().getTransdate());
        ppd.setBusdoc(busdoc);
        ppd.setAmount(busdoc.getTotalPending());
        getData().addPpdetail(ppd);
        paymentList.add(ppd);
    }

    public void updateLedgerValue() {
        if (getData().getAccount().getAccounttype().getName().equalsIgnoreCase("Debtors")) {
            getData().setCredit(getTotalAmount_Paid());
        } else if (getData().getAccount().getAccounttype().getName().equalsIgnoreCase("Creditors")) {
            getData().setDebit(getTotalAmount_Paid());
        }
    }

    /**
     * @return the selectedPendingDocs
     */
    public List<BusDoc> getSelectedPendingDocs() {
        return selectedPendingDocs;
    }

    /**
     * @param selectedPendingDocs the selectedPendingDocs to set
     */
    public void setSelectedPendingDocs(List<BusDoc> selectedPendingDocs) {
        this.selectedPendingDocs = selectedPendingDocs;
    }

    /**
     * @return the selectedPaymentDetails
     */
    public PartialPaymentDetail getSelectedPaymentDetails() {
        return selectedPaymentDetails;
    }

    /**
     * @param selectedPaymentDetails the selectedPaymentDetails to set
     */
    public void setSelectedPaymentDetails(PartialPaymentDetail selectedPaymentDetails) {
        this.selectedPaymentDetails = selectedPaymentDetails;
    }

    public Double getTotalInvoiced_Pending() {
        if (getPendingDocs() == null || getPendingDocs().isEmpty()) {
            return 0.0;
        }
        return getPendingDocs().stream().mapToDouble(o -> o.getGrandtotal()).sum();
    }

    public Double getTotalPaid_Pending() {
        if (getPendingDocs() == null || getPendingDocs().isEmpty()) {
            return 0.0;
        }
        return getPendingDocs().stream().mapToDouble(o -> o.getTotalPaid()).sum();
    }

    public Double getTotalPending_Pending() {
        if (getPendingDocs() == null || getPendingDocs().isEmpty()) {
            return 0.0;
        }
        return getPendingDocs().stream().mapToDouble(o -> o.getTotalPending()).sum();
    }

    public Double getTotalPending_Paid() {
        if (getPaymentList() == null || getPaymentList().isEmpty()) {
            return 0.0;
        }
        return getPaymentList().stream().mapToDouble(o -> o.getPendingAmount()).sum();
    }

    public Double getTotalAmount_Paid() {
        if (getPaymentList() == null || getPaymentList().isEmpty()) {
            return 0.0;
        }
        return getPaymentList().stream().mapToDouble(o -> o.getAmount()).sum();
    }

    public Double getBalanceAmount_Paid() {
        if (getPaymentList() == null || getPaymentList().isEmpty()) {
            return 0.0;
        }
        return getPaymentList().stream().mapToDouble(o -> o.getBalanceAmount()).sum();
    }

    /**
     * @return the deletedPP
     */
    public List<PartialPaymentDetail> getDeletedPP() {
        return deletedPP;
    }

    /**
     * @param deletedPP the deletedPP to set
     */
    public void setDeletedPP(List<PartialPaymentDetail> deletedPP) {
        this.deletedPP = deletedPP;
    }
}
