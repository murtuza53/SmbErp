/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import com.smb.erp.entity.Product;
import java.util.List;

/**
 *
 * @author FatemaLaptop
 */
public interface ProductTransferable {
    
    public void transfer(List<Product> products);
}
