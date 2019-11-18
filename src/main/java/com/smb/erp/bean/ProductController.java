/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.bean;

import com.smb.erp.entity.Product;
import com.smb.erp.repo.ProductRepository;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author FatemaLaptop
 */
@Component
public class ProductController implements Serializable{
    
    @Autowired
    ProductRepository prodRepo;
    
    private Product product;
    
    public ProductController(){
        
    }
    
    public ProductController(Product prod){
        this.product = prod;
    }
    
    public void setProduct(Product prod){
        this.product = product;
    }
    
    public Product getProduct(){
        return product;
    }
    
}
