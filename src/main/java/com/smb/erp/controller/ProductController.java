/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Product;
import com.smb.erp.repo.ProductRepository;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author FatemaLaptop
 */
//@Component
@Named
@ViewScoped
public class ProductController implements Serializable{
    
    @Autowired
    ProductRepository prodRepo;
    
    @Autowired
    TabViewController tabCon;
    
    DocumentTab<Product> docTab;
    
    private Product selected;
    
    public ProductController(){
    }
    
    @PostConstruct
    public void init() {
        this.docTab = tabCon.getSelectedTab();
        setSelected(docTab.getData());
        System.out.println("TabController: " + tabCon);
        System.out.println("Prod_Tab_id: " + docTab.getId());
        System.out.println("ProductController->selected: " + selected);
    }
    
    //public ProductController(Product prod){
    //    this.selected = prod;
    //}
    
    public void setSelected(Product prod){
        this.selected = prod;
    }
    
    public Product getSelected(){
        return selected;
    }
    
    public DocumentTab<Product> getTab(){
        return docTab;
    }
}
