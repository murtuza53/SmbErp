/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Product;
import com.smb.erp.helper.StockBalanceHelper;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.report.NativeQueryReportObject;
import com.smb.erp.report.NativeQueryTableModel;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.LinkedList;
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

    @Autowired
    ProductTransactionController ptContoller;

    @Autowired
    UserSession userSession;

    private String criteria = "";

    private List<Product> productList;

    private List<Product> selectedProducts;

    private LayoutOptions layoutOptions;

    private ProductTransferable productTransferable;

    private List<StockBalanceHelper> stockList;
    
    private List<StockBalanceHelper> selectedProductStock;
    
    private List<String> branchList = new LinkedList<String>();

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

    public void transfer() {
        //System.out.println("Transfering: " + getSelectedProductStock());
        if(getSelectedProductStock()==null || getSelectedProductStock().size()==0){
            JsfUtil.addErrorMessage("Error", "No Products selected");
        } else if(productTransferable==null){
            JsfUtil.addErrorMessage("Error", "Document not set for transfer");
        } else {
            selectedProducts = new LinkedList<Product>();
            for(StockBalanceHelper sbh: getSelectedProductStock()){
                selectedProducts.add(sbh.getProduct());
            }
            productTransferable.transfer(getSelectedProducts());
        }

        //System.out.println("Transfer: " + stockList);
    }

    public void searchProducts() {
        //productList = prodRepo.findByCriteria(criteria.trim());
        //if(criteria==null){
        //    criteria = "";
        //}
        //tableModel = ptContoller.findStockBalances(criteria, userSession.getLoggedInCompany().getCompanyid(), new Date());
        productList = prodRepo.findByCriteria(criteria.trim());

        stockList = new LinkedList<StockBalanceHelper>();
        if (productList != null) {
            for (Product p : productList) {
                StockBalanceHelper sb = new StockBalanceHelper(p);
                NativeQueryTableModel model = ptContoller.findStockBalances(p.getProductid(), userSession.getLoggedInCompany().getCompanyid(), new Date());
                List<NativeQueryReportObject> ret = model.getData();
                if(ret!=null && ret.size()>0){
                    NativeQueryReportObject row = ret.get(0);
                    //System.out.println("Column Count: " + row.getColumnCount());
                    for(int i=0; i<row.getColumnCount(); i++){
                        //System.out.print(model.getColumns().get(i).getHeader()+":"+ Double.parseDouble(row.getField(i).toString())+"\t");
                        sb.putStock(model.getColumns().get(i).getHeader(), Double.parseDouble(row.getField(i).toString()));
                    }
                    //System.out.println();
                } else {
                    for(int i=0; i<model.getColumns().size(); i++){
                        //System.out.print(model.getColumns().get(i).getHeader()+":"+ Double.parseDouble(row.getField(i).toString())+"\t");
                        sb.putStock(model.getColumns().get(i).getHeader(), new Double(0));
                    }
                }
                stockList.add(sb);
            }
        }
        branchList = new LinkedList<String>();
        if(stockList.size()>0){
            branchList = stockList.get(0).getBranches();
        }
        //for(StockBalanceHelper sb: stockList){
        //    System.out.println(sb.getProduct() + "\t" + sb.getStockBalances());
        //}
    }

    public Product findByProductidOrSupplierCodeOrBarcodes(String criteria){
        productList = prodRepo.findByProductidOrSupplierCodeOrBarcodes(criteria);
        if(productList==null || productList.size()==0){
            return null;
        }
        return productList.get(0);
    }
    
    public List<StockBalanceHelper> getProductStocks(){
        return stockList;
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

    public void setProductTransferable(ProductTransferable transferable) {
        this.productTransferable = transferable;
    }

    /**
     * @return the selectedProductStock
     */
    public List<StockBalanceHelper> getSelectedProductStock() {
        return selectedProductStock;
    }

    /**
     * @param selectedProductStock the selectedProductStock to set
     */
    public void setSelectedProductStock(List<StockBalanceHelper> selectedProductStock) {
        this.selectedProductStock = selectedProductStock;
    }

    /**
     * @return the branchList
     */
    public List<String> getBranchList() {
        return branchList;
    }

    /**
     * @param branchList the branchList to set
     */
    public void setBranchList(List<String> branchList) {
        this.branchList = branchList;
    }

}
