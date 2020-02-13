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
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "accountController")
@ViewScoped
public class AccountController extends AbstractController<Account> {

    AccountRepository repo;

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

}
