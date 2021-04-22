/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.rest;

import com.smb.erp.entity.ProductCategory;
import com.smb.erp.repo.ProductCategoryRepository;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping({"/rest/inv"})
public class ProductInventoryRestController {
    
    @Autowired
    ProductCategoryRepository productCategoryRepo;
    
    @GetMapping("/lcat")
    List<ProductCategory> getCategoriesLeafNodes(){
        return productCategoryRepo.findCategoryLeafNodes();
    }
    
    @GetMapping("/pcat/{id}")
    List<ProductCategory> getCategories(@PathVariable long id){
        List list = productCategoryRepo.findByParentId(id);
        if(list==null){
            return new LinkedList<>();
        }
        return list;
    }

}
