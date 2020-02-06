/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.util.JsfUtil;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FatemaLaptop
 */
//@Component
@Named(value = "productController")
@Service
@ViewScoped
public class ProductController extends AbstractController<Product> {

    //@Autowired
    ProductRepository prodRepo;

    //@Autowired
    //TabViewController tabCon;
    //DocumentTab<Product> docTab;
    @Autowired
    ProductTreeViewController ptvController;
    //private Product selected;
    private ProductCategory prodCategory;

    private String mode;

    private String title = "New Product";

    @Autowired
    public ProductController(ProductRepository repo) {
        super(Product.class, repo);
        this.prodRepo = repo;
    }

    @PostConstruct
    public void init() {
        //this.docTab = tabCon.getSelectedTab();
        //setSelected(docTab.getData());
        //System.out.println("TabController: " + tabCon);
        //System.out.println("Prod_Tab_id: " + docTab.getId());
        //System.out.println("ProductController->selected: " + selected);
        
        //FacesContext facesContext = FacesContext.getCurrentInstance();
        //Flash flash = facesContext.getExternalContext().getFlash();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        String windowId = params.get("windowid").toString();

        DocumentTab<Product> tab = ptvController.getDocumentTab(windowId);
        if (tab == null) {
            JsfUtil.addErrorMessage("Error", "This page could not be loaded");
            return;
        }
        setSelected(tab.getData());
        setProdCategory(getSelected().getProdcategry());
        System.out.println("ProductController:Init: " + getSelected() + "\tCategory: " + getSelected().getProdcategry());
        //mode = flash.get("mode").toString();
        //flash.put("mode", mode);
        //if (getSelected().getProductid() != null || getSelected().getProductid() > 0) {          //(mode.equalsIgnoreCase("new")) {
            //prodCategory = (ProductCategory) flash.get("category");
            //flash.put("category", prodCategory);
            //} else {
            //setSelected((Product) flash.get("product"));
            setTitle("Edit - " + tab.getTitle());
            //flash.put("product", getSelected());
        //}
        //flash.put("windowid", windowId);
        //flash.setKeepMessages(true);
    }

    //public ProductController(Product prod){
    //    this.selected = prod;
    //}
    public String getHeaderTitle() {
        if (getSelected() == null || getSelected().getProductid() == null || getSelected().getProductid().longValue() == 0) {
            return "New Product";
        }
        return "Edit Product";
    }

    //public DocumentTab<Product> getTab(){
    //    return docTab;
    //}
    /*public void prepareCreateNew() {
        Product p = new Product();
        p.setProductid(0l);
        p.setProductname("Testing");
        if (ptvController.getSelectedNode() != null) {
            if (ptvController.getSelectedNode().getData() != null) {
                selected.setProdcategry((ProductCategory) ptvController.getSelectedNode().getData());
            }
        }
    }

    public void prepareEdit() {
        selected = ptvController.getSelectedProduct();
    }*/
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the prodCategory
     */
    public ProductCategory getProdCategory() {
        System.out.println("getProdCategory: " + prodCategory);
        return prodCategory;
    }

    /**
     * @param prodCategory the prodCategory to set
     */
    public void setProdCategory(ProductCategory prodCategory) {
        this.prodCategory = prodCategory;
    }
}
