/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.ProductTransactionExecution;
import com.smb.erp.repo.ProductTransactionExecutionRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "productTransactionExecutionController")
@ViewScoped
public class ProductTransactionExecutionController extends AbstractController<ProductTransactionExecution> {

    ProductTransactionExecutionRepository repo;

    @Autowired
    public ProductTransactionExecutionController(ProductTransactionExecutionRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(ProductTransactionExecution.class, repo);
        this.repo = repo;
    }
    
    @Transactional
    public void deleteToProductTransactionExe(List<ProductTransactionExecution> ptes){
        System.out.println("deleteToProductTransactionExe: " + ptes);
        if(ptes!=null){
            for(ProductTransactionExecution pte: ptes){
                //pte.setToprodtransid(null);
                //pte.setFromprodtransid(null);
                //repo.save(pte);
                System.out.println("deleteToProductTransactionExe: " + pte);
                repo.deleteToProductTransations(pte.getToprodtransid().getProdtransid());
            }
        }
    }

}
