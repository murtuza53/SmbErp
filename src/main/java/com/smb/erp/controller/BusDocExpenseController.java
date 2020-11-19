/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocExpense;
import com.smb.erp.repo.BusDocExpenseRepository;
import java.util.LinkedList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "busDocExpenseController")
@ViewScoped
public class BusDocExpenseController extends AbstractController<BusDocExpense> {
    
    BusDocExpenseRepository repo;
    
    private BusDoc busdoc;
    
    @Autowired
    public BusDocExpenseController(BusDocExpenseRepository repo){
        this.repo = repo;
    }
    
    @Override
    public List<BusDocExpense> getItems(){
        if(items==null){
            items = repo.findAll(Sort.by(Sort.Direction.ASC, "expenseid"));
        }
        return items;
    }

    /**
     * @return the busdoc
     */
    public BusDoc getBusdoc() {
        return busdoc;
    }

    /**
     * @param busdoc the busdoc to set
     */
    public void setBusdoc(BusDoc busdoc) {
        this.busdoc = busdoc;
    }
    
    public List<BusDocExpense> getBusDocExpens(){
        if(busdoc!=null){
            if(busdoc.getExpenses()!=null){
                return busdoc.getExpenses();
            } else {
                busdoc.setExpenses(new LinkedList<>());
            }
        }
        return null;
    }
    
}
