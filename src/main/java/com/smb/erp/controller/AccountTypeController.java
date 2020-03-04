package com.smb.erp.controller;

import com.smb.erp.entity.AccountType;
import com.smb.erp.repo.AccountTypeRepository;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

@Named(value = "accounttypeController")
@ViewScoped
public class AccountTypeController extends AbstractController<AccountType> {

    private AccountTypeRepository facade;
    
    @Autowired
    public AccountTypeController(AccountTypeRepository repo) {
        // Inform the Abstract parent controller of the concrete Ledger Entity
        super(AccountType.class, repo);
        this.facade = repo;
    }

    @Override
    public List<AccountType> getItems(){
        if(items==null){
            items = facade.findAll(Sort.by(Sort.Direction.ASC, "name"));
        }
        return items;
    }
    
}
