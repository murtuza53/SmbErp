/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.repo.ProductRepository;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
//@Component
@Named(value = "productController")
@ViewScoped
public class ProductController extends AbstractController<Product> {

    //@Autowired
    ProductRepository prodRepo;

    //@Autowired
    //TabViewController tabCon;
    //DocumentTab<Product> docTab;
    @Autowired
    ProductTreeViewController ptvController;

    private Product selected;

    @Autowired
    public ProductController(ProductRepository repo) {
        this.prodRepo = repo;
    }

    @PostConstruct
    public void init() {
        //this.docTab = tabCon.getSelectedTab();
        //setSelected(docTab.getData());
        //System.out.println("TabController: " + tabCon);
        //System.out.println("Prod_Tab_id: " + docTab.getId());
        //System.out.println("ProductController->selected: " + selected);
    }

    //public ProductController(Product prod){
    //    this.selected = prod;
    //}
    public String getHeaderTitle() {
        if (selected == null || selected.getProductid() == null || selected.getProductid().longValue() == 0) {
            return "New Product";
        }
        return "Edit Product";
    }

    public void setSelected(Product prod) {
        this.selected = prod;
    }

    public Product getSelected() {
        return selected;
    }

    //public DocumentTab<Product> getTab(){
    //    return docTab;
    //}
    public void prepareCreateNew() {
        selected = new Product();
        selected.setProductid(0l);
        selected.setProductname("Testing");
        if (ptvController.getSelectedNode() != null) {
            if (ptvController.getSelectedNode().getData() != null) {
                selected.setProdcategry((ProductCategory)ptvController.getSelectedNode().getData());
            }
        }
    }

    public void prepareEdit() {
        selected = ptvController.getSelectedProduct();
    }
}
