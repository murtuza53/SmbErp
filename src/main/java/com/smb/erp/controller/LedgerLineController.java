/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.entity.LedgerLine;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.LedgerLineRepository;
import com.smb.erp.util.DateUtil;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "ledgerlineController")
@ViewScoped
public class LedgerLineController extends AbstractController<LedgerLine> {

    LedgerLineRepository repo;

    @Autowired
    AccountRepository accountRepo;

    private Date fromDate = DateUtil.startOfYear(new Date());

    private Date toDate = DateUtil.endOfDay(new Date());

    private List<Account> selectedAccounts;

    private List<Account> accountList;

    @Autowired
    public LedgerLineController(LedgerLineRepository repo) {
        super(LedgerLine.class, repo);
        this.repo = repo;
    }

    @Override
    public List<LedgerLine> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            if (getSelectedAccounts() != null && getSelectedAccounts().size() > 0) {
                items = new LinkedList<LedgerLine>();
                if (getSelectedAccounts().get(0).getAccountname().equalsIgnoreCase("All")) {
                    for (int i = 1; i < getAccountList().size(); i++) {
                        Account a = getAccountList().get(i);
                        List<LedgerLine> list = findAccountLedgerLines(a);
                        if (list != null && list.size() > 0) {
                            items.addAll(list);
                        }
                    }
                } else {
                    for (Account a : getSelectedAccounts()) {
                        List<LedgerLine> list = findAccountLedgerLines(a);
                        if (list != null && list.size() > 0) {
                            items.addAll(list);
                        }
                    }
                }
            }
        }
        return items;
    }

    public List<LedgerLine> findAccountLedgerLines(Account a) {
        List<LedgerLine> list = repo.findByAccountIdBetweenDate(a.getAccountid(), getFromDate(), getToDate());
        if (list != null && list.size() > 0) {
            LedgerLine opening = new LedgerLine();
            opening.setLlno(0);
            opening.setAccount(a);
            opening.setTransdate(fromDate);
            opening.setDescription("Opening Balance");
            Double bal = repo.findBalanceByAccountByDate(a.getAccountid(), DateUtil.startOfDay(fromDate));
            if (bal == null) {
                bal = 0.0;
            }
            if (bal >= 0) {
                opening.setDebit(bal);
            } else {
                opening.setCredit(bal * -1);
            }
            list.add(0, opening);

            bal = 0.0;
            Double dt = 0.0;
            Double ct = 0.0;
            for (LedgerLine line : list) {
                bal = bal + line.getDebit() - line.getCredit();
                line.setCumulative(bal);

                dt = dt + line.getDebit();
                ct = ct + line.getCredit();
            }

            LedgerLine closing = new LedgerLine();
            closing.setLlno(0);
            closing.setAccount(a);
            closing.setTransdate(toDate);
            closing.setDescription("Closing Balance");
            closing.setDebit(dt);
            closing.setCredit(ct);
            closing.setCumulative(bal);
            list.add(list.size(), closing);
            return list;
        }
        return null;
    }

    public void refresh() {
        items = null;
    }

    /**
     * @return the accountList
     */
    public List<Account> getAccountList() {
        if (accountList == null) {
            accountList = accountRepo.findAccountLeafBySearchCriteria("");
            Account all = new Account();
            all.setAccountid("All");
            all.setAccountname("All");
            accountList.add(0, all);
        }
        return accountList;
    }

    public Double findAccountBalance(String accountNo, Date toDate) {
        return repo.findAccountBalance(accountNo, DateUtil.endOfDay(toDate));
    }

    public Double findAccountBalance(String accountNo, Date fromDate, Date toDate) {
        return repo.findAccountBalance(accountNo, DateUtil.endOfDay(new Date(DateUtil.previousDay(fromDate.getTime()))),
                                        DateUtil.endOfDay(toDate));
    }

    public Double findAccountLikeBalance(String accountNo, Date toDate) {
        return repo.findAccountBalance(accountNo + "%", DateUtil.endOfDay(toDate));
    }

    public Double findAccountLikeBalance(String accountNo, Date fromDate, Date toDate) {
        return repo.findAccountBalance(accountNo + "%", DateUtil.endOfDay(new Date(DateUtil.previousDay(fromDate.getTime()))),
                                        DateUtil.endOfDay(toDate));
    }

    public List<Double> findAccountLikeBalanceDrCr(String accountNo, Date toDate) {
        List<Double[]> res = repo.findAccountBalanceDrCr(accountNo + "%", DateUtil.endOfDay(toDate));
        //System.out.println("findAccountLikeBalanceDrCr2: " + accountNo + " => " + res.size() + " => " + ((Double[])res.get(0))[0] + "," + ((Double[])res.get(0))[1]);

        List<Double> ret = new LinkedList<>();

        if (res == null) {
            ret.add(new Double(0.0));
            ret.add(new Double(0.0));
        } else {
            ret = new LinkedList(Arrays.asList(res.get(0)));
            if (ret.get(0) == null) {
                ret.remove(0);
                ret.add(0, new Double(0.0));
            }
            if (ret.get(1) == null) {
                ret.remove(1);
                ret.add(1, new Double(0.0));
            }
        }
        if (ret.size() < 2) {
            ret.clear();
            ret.add(new Double(0.0));
            ret.add(new Double(0.0));
        }
        return ret;
    }

    public List<Double> findAccountLikeBalanceDrCr(String accountNo, Date fromDate, Date toDate) {
        List<Double[]> res = repo.findAccountBalanceDrCr(accountNo + "%", DateUtil.endOfDay(new Date(DateUtil.previousDay(fromDate.getTime()))),
                                                    DateUtil.endOfDay(toDate));
        //System.out.println("findAccountLikeBalanceDrCr2: " + accountNo + " => " + res.size() + " => " + ((Double[])res.get(0))[0] + "," + ((Double[])res.get(0))[1]);

        List<Double> ret = new LinkedList<>();

        if (res == null) {
            ret.add(new Double(0.0));
            ret.add(new Double(0.0));
        } else {
            ret = new LinkedList(Arrays.asList(res.get(0)));
            if (ret.get(0) == null) {
                ret.remove(0);
                ret.add(0, new Double(0.0));
            }
            if (ret.get(1) == null) {
                ret.remove(1);
                ret.add(1, new Double(0.0));
            }
        }
        if (ret.size() < 2) {
            ret.clear();
            ret.add(new Double(0.0));
            ret.add(new Double(0.0));
        }
        return ret;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the selectedAccounts
     */
    public List<Account> getSelectedAccounts() {
        return selectedAccounts;
    }

    /**
     * @param selectedAccounts the selectedAccounts to set
     */
    public void setSelectedAccounts(List<Account> selectedAccounts) {
        this.selectedAccounts = selectedAccounts;
    }

}
