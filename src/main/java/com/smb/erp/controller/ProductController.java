/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductAccount;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.entity.VatProductRegister;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.repo.ProductTypeRepository;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
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
    
    @Autowired
    VatCategoryController vatcatController;

    @Autowired
    SystemDefaultsController systemController;
    
    @Autowired
    TableKeyController keyController;
    
    @Autowired
    ProductTypeRepository ptypeRepo;

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
        //FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        String windowId = params.get("windowid").toString();

        DocumentTab<Product> tab = ptvController.getDocumentTab(windowId);
        if (tab == null) {
            JsfUtil.addErrorMessage("Error", "This page could not be loaded");
            return;
        }
        setSelected(tab.getData());
        setProdCategory(getSelected().getProdcategory());
        System.out.println("ProductController:Init: " + getSelected() + "\tCategory: " + getSelected().getProdcategory());
        //mode = flash.get("mode").toString();
        //flash.put("mode", mode);
        //if (getSelected().getProductid() != null || getSelected().getProductid() > 0) {          //(mode.equalsIgnoreCase("new")) {
        //prodCategory = (ProductCategory) flash.get("category");
        //flash.put("category", prodCategory);
        //} else {
        //setSelected((Product) flash.get("product"));
        setTitle(tab.getTitle());
        //flash.put("product", getSelected());
        //}
        //flash.put("windowid", windowId);
        //flash.setKeepMessages(true);

        //load default accounts
        if (getSelected().getProdaccount() == null) {
            ProductAccount pa = new ProductAccount();
            pa.setProdaccountid(0l);
            getSelected().setProdaccount(pa);
            pa.setSalesAccount(systemController.getDefaultAccount("DefSalesAccount"));
            pa.setPurchaseAccount(systemController.getDefaultAccount("DefPurchaseAccount"));
            pa.setConsumptionAccount(systemController.getDefaultAccount("DefConsumptionAccount"));
        }
        
        if(getSelected().getVatregisterid()==null){
            VatProductRegister vpr = new VatProductRegister();
            vpr.setVatregisterid(0l);
            vpr.setWef(new Date());
            vpr.setVatcategoryid(vatcatController.getItems().get(1));
            getSelected().setVatregisterid(vpr);
        }
        
        if(getSelected().getProducttype()==null){
            getSelected().setProducttype(ptypeRepo.findAll().get(0));
        }
    }
    
    //public ProductController(Product prod){
    //    this.selected = prod;
    //}
    
    @Override
    public void save(){
        if(getSelected().getProductid()==null || getSelected().getProductid()==0){
            getSelected().setProductid(keyController.getProductNextId());
        }
        super.save();
        setSelected(prodRepo.getOne(getSelected().getProductid()));
        JsfUtil.addSuccessMessage(getSelected() + " saved successfuly");
    }
    
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
        //System.out.println("getProdCategory: " + prodCategory);
        return prodCategory;
    }

    /**
     * @param prodCategory the prodCategory to set
     */
    public void setProdCategory(ProductCategory prodCategory) {
        this.prodCategory = prodCategory;
    }

    public void savepage() {
        System.out.println("ProductController: savepage");
    }
}
