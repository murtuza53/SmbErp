/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Product;
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
@Named(value = "documentController")
@ViewScoped
public class DocumentController implements Serializable{
    
    @Autowired
    ProductSearchController searchController;
    
    private List<Product> products = new LinkedList<Product>();
    private List<Product> selectedProducts;
    
    public DocumentController(){
        
    }
    
    @PostConstruct
    public void init(){
        //searchController.setDocController(this);
    }
    
    public void addProduct(List<Product> prods){
        if(prods!=null){
            getProducts().addAll(prods);
        }
    }

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * @return the selectedProducts
     */
    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    /**
     * @param selectedProducts the selectedProducts to set
     */
    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }
}
