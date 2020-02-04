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
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
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
    //@Autowired
    //ProductTreeViewController ptvController;
    //private Product selected;
    private ProductCategory prodCategory;

    private String mode;

    private String title = "New Product";

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

        if (mode == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();

            mode = flash.get("mode").toString();
            //flash.put("mode", mode);
            if (mode.equalsIgnoreCase("new")) {
                prodCategory = (ProductCategory) flash.get("category");
                //flash.put("category", prodCategory);
            } else {
                setSelected((Product) flash.get("product"));
                setTitle("Edit - " + getSelected().getProductname());
                //flash.put("product", getSelected());
            }
            //flash.setKeepMessages(true);
        }
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
}
