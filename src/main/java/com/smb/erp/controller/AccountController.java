/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.repo.AccountRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "accountController")
@ViewScoped
public class AccountController extends AbstractController<Account> {

    @PersistenceContext
    private EntityManager em;

    AccountRepository repo;

    protected final static char[] alpha = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    @Autowired
    public AccountController(AccountRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(Account.class, repo);
        this.repo = repo;
    }

    public List<Account> completeFilterLeaf(String criteria) {
        System.out.println("completeFilterLeaf_Account: " + criteria);

        //return Utils.sublist(itsFacade.findItsMasterByCriteria(criteria), 10);
        return repo.findAccountLeafBySearchCriteria(criteria);
    }

    public List findAccountsInLedger(String parentNo) {
        //return em.createNamedQuery("SELECT OBJECT(o) FROM Account o WHERE o.parent.accountNo=:parentNo ORDER BY o.accountName ASC").setParameter("parentNo", parentNo).getResultList();
        return repo.findAccountByParentBySearchCriteria(parentNo);
    }

    public Account findLastAccountInLedger(String parentNo) {
        List<Account> accounts = null;

        //Account parent = repo.getOne(parentNo);   //findAccountByNo(parentNo);
        //System.out.println("findLastAccountInLedger: " + parentNo);

        if (parentNo != null) {
            accounts = em.createQuery(
                    "SELECT OBJECT(a) FROM Account AS a WHERE a.parentid.accountid=" + parentNo
                    + " ORDER BY a.accountid DESC").setMaxResults(1).getResultList();

            //accounts = em.createNamedQuery("SELECT * FROM account where parentid=" + parentNo + " order by accountid desc limit 1").getResultList();
            //accounts = repo.findAccountByParentBySearchCriteria(parentNo);
        }
        if (accounts == null) {
            System.out.println("LAST ACCOUNTNO: null");
            return null;
        } else if (accounts.isEmpty()) {
            return null;
        }
        System.out.println("LAST ACCOUNTNO: " + accounts.get(accounts.size()-1));
        return accounts.get(accounts.size()-1);
    }

    public String getAccountNextNo(Account parent) {

        String returnValue = "10";

        String accountNo = "";
        //String lower = "";

        if (parent != null) {
            accountNo = parent.getAccountid();
            //lower = Long.parseLong(accountNo) * 100;
            //lower = accountNo.substring(accountNo.length() - 2);
        }
        //long upper = Long.parseLong(accountNo + 99);
        Account account = null;
        if (parent == null) {
            account = findLastAccountInLedger(null);
        } else {
            account = findLastAccountInLedger(parent.getAccountid());
        }

        if (account == null) {
            returnValue = accountNo + "10";
        } else {
            String acc = account.getAccountid();
            //long nextValue = Long.parseLong(acc.substring(acc.length() - 2)) + 1;
            int len = 0;
            if (parent != null) {
                len = parent.getAccountid().length();
            }
            String nextValue = "10";
            if (account != null) {
                nextValue = getNextCount(account.getAccountid().substring(len));
            }
            if (parent == null) {
                returnValue = nextValue;
            } else {
                //returnValue = acc.substring(0, acc.length() - 2).concat(
                //        nextValue + "");
                returnValue = parent.getAccountid() + nextValue;
            }
        }
        //LAST_NO = returnValue;
        System.out.println("parent:" + parent + "\t" + "last:" + account + "\t" + "newid:" + returnValue);
        return returnValue;
    }

    private String nextValue(List<Account> accounts) {
        String value = "10";
        for (Account account : accounts) {
            String current = account.getAccountid().substring(account.getAccountid().length() - 2);
            if (value.compareTo(current) < 0) {
                value = current;
            }
        }
        return getNextCount(value);
    }

    private String getNextCount(String count) {
        String retVal = "10";
        if (count == null) {
            return retVal;
        } else if (count.length() == 0) {
            return retVal;
        }

        if (count.equalsIgnoreCase("ZZ")) {
            return "1010";
        }
        if (count.length() > 2 && count.length() <= 4) {
            return getNextCountOf4Digit(count);
        }

        char second = getNextChar(count.charAt(1));
        char first = count.charAt(0);

        if (new Character(second).equals(alpha[0])) {
            first = getNextChar(first);
        }
        retVal = first + "" + second;
        return retVal;
    }

    private String getNextCountOf4Digit(String count) {
        String retVal = "1000";

        char fourth = getNextChar(count.charAt(3));
        char third = count.charAt(2);
        char second = count.charAt(1);
        char first = count.charAt(0);

        if (new Character(fourth).equals(alpha[0])) {
            third = getNextChar(third);
            if (new Character(third).equals(alpha[0])) {
                second = getNextChar(second);
                if (new Character(second).equals(alpha[0])) {
                    first = getNextChar(first);
                }
            }
        }
        retVal = first + "" + second + third + fourth;
        return retVal;
    }

    private char getNextChar(char c) {

        for (int i = 0; i < alpha.length; i++) {
            if (new Character(c).equals(alpha[i])) {
                int n = (i + 1) % alpha.length;
                //System.out.println("mod: " + n);
                return alpha[n];
            }
        }
        return alpha[0];
    }

}
