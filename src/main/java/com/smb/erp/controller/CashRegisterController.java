/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.CashRegister;
import com.smb.erp.repo.AccountRepository;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "cashRegisterController")
@ViewScoped
public class CashRegisterController implements Serializable {

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    AccDocController accDocCon;

    String cashAndBankLedgerNo = "16";

    private List<Account> cashRegisterAccounts;

    private Account total;

    public CashRegisterController() {

    }

    @PostConstruct
    public void init() {
        //setCashRegisterAccounts(accountRepo.findAccountByParent(cashAndBankLedgerNo));
        //getCashRegisterAccounts().add(total);
        resetTotal();
    }

    public void resetTotal() {
        total = new Account();
        total.setAccountid("Total");
        total.setAccountname("Total");
    }

    public void setupBusDoc(BusDoc doc) {
        if (doc.getBusdocinfo().hasCashRegister()) {
            cashRegisterAccounts = new LinkedList<Account>();
            resetTotal();
            for (CashRegister cr : doc.getBusdocinfo().getCashregiserid()) {
                cashRegisterAccounts.add(cr.getAccountid());
            }
            List<Account> accounts = accDocCon.getBusDocCashRegisterAccounts(doc.getDocno(), cashRegisterAccounts);
            for (Account acc : cashRegisterAccounts) {
                Account a = findAccount(accounts, acc.getAccountid());
                if (a != null) {
                    acc.setAmount(a.getAmount());
                    acc.setDescription(a.getDescription());
                }
            }
            calculateTotal();
            cashRegisterAccounts.add(total);
        }
    }

    public Account findAccount(List<Account> accounts, String accNo) {
        if (accounts != null) {
            for (Account acc : accounts) {
                if (acc.getAccountid().equalsIgnoreCase(accNo)) {
                    return acc;
                }
            }
        }
        return null;
    }

    public Account findAccountFromCashRegister(List<CashRegister> registers, String accNo) {
        if (registers != null) {
            for (CashRegister cr : registers) {
                if (cr.getAccountid().getAccountid().equalsIgnoreCase(accNo)) {
                    return cr.getAccountid();
                }
            }
        }
        return null;
    }

    /**
     * @return the cashRegisterAccounts
     */
    public List<Account> getCashRegisterAccounts() {
        return cashRegisterAccounts;
    }

    /**
     * @param cashRegisterAccounts the cashRegisterAccounts to set
     */
    public void setCashRegisterAccounts(List<Account> cashRegisterAccounts) {
        this.cashRegisterAccounts = cashRegisterAccounts;
    }

    public void setCashRegister(List<CashRegister> crList) {
        if (crList != null) {
            cashRegisterAccounts = new LinkedList();
            crList.forEach((cr) -> {
                cashRegisterAccounts.add(cr.getAccountid());
            });
        }
    }

    public double calculateTotal() {
        if (cashRegisterAccounts != null) {
            double t = 0.0;
            for (int i = 0; i < cashRegisterAccounts.size(); i++) {
                if (!cashRegisterAccounts.get(i).getAccountid().equalsIgnoreCase("Total")) {
                    t = t + cashRegisterAccounts.get(i).getAmount();
                }
            }
            total.setAmount(t);
        }
        return total.getAmount();
    }

    public List<Account> getFinalCashRegiserAccounts() {
        cashRegisterAccounts.remove(total);
        return cashRegisterAccounts;
    }

    public List<Account> getCashRegisterWithCashPaid(BusDocInfo info, double amount) {
        List<Account> accs = new LinkedList();
        Account account = findAccountFromCashRegister(info.getCashregiserid(), "1610");
        if (account != null) {
            account.setAmount(amount);
            accs.add(account);
            return accs;
        }
        return accs;
    }

}
