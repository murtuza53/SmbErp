/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Product;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.util.JsfUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "productSearchController")
@ViewScoped
public class ProductSearchController extends AbstractController<Product> {

    //@Autowired
    ProductRepository prodRepo;

    private String criteria = "";

    private List<Product> productList;
    
    private List<Product> selectedProducts;

    private LayoutOptions layoutOptions;
    
    private ProductTransferable productTransferable;

    @Autowired
    public ProductSearchController(ProductRepository repo) {
        this.prodRepo = repo;
    }

    @PostConstruct
    public void init() {
        layoutOptions = new LayoutOptions();

        final LayoutOptions center = new LayoutOptions();
        layoutOptions.setCenterOptions(center);

        final LayoutOptions west = new LayoutOptions();
        west.addOption("size", "25%");
        layoutOptions.setWestOptions(west);

        // options for nested center layout
        LayoutOptions childCenterOptions = new LayoutOptions();
        center.setChildOptions(childCenterOptions);

    }
    
    public void transfer(){
        System.out.println("Transfer: " + getSelectedProducts());
        if(getSelectedProducts()==null || getSelectedProducts().size()==0){
            JsfUtil.addErrorMessage("Error", "No Products selected");
        } else if(productTransferable==null){
            JsfUtil.addErrorMessage("Error", "Document not set for transfer");
        } else {
            productTransferable.transfer(getSelectedProducts());
        }
    }

    public void searchProducts() {
        productList = prodRepo.findByCriteria(criteria.trim());
    }

    public LayoutOptions getLayoutOptions() {
        return layoutOptions;
    }

    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the productList
     */
    public List<Product> getProductList() {
        return productList;
    }

    /**
     * @param productList the productList to set
     */
    public void setProductList(List<Product> productList) {
        this.productList = productList;
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

    public void setProductTransferable(ProductTransferable transferable){
        this.productTransferable = transferable;
    }
    
}
