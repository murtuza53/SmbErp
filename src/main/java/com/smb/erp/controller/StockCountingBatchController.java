/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.StockCountingBatch;
import com.smb.erp.entity.StockCountingBusDoc;
import com.smb.erp.repo.StockCountingBatchRepository;
import com.smb.erp.repo.StockCountingBusDocRepository;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "stockCountingBatchController")
@ViewScoped
public class StockCountingBatchController extends AbstractController<StockCountingBatch> {

    StockCountingBatchRepository repo;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    StockCountingBusDocRepository scbRepo;

    @Autowired
    ProductSearchController searchCon;

    @Autowired
    UserSession userSession;

    @Autowired
    ProductTransactionController ptCon;

    private List<StockCountingBusDoc> busdocList;

    private List<StockCountingBatch> countingBatch;

    private StockCountingBusDoc selectedDoc;

    private StockCountingBatch selectedStockCounting;

    private String productCode = "";

    private Product selectedProduct;

    private Double count = 1.0;

    private String productName = "";

    private String unitDetails = "";

    private String brandCatDetails = "";

    private Double totalCount = 0.0;

    private Double totalAmount = 0.0;

    @Autowired
    public StockCountingBatchController(StockCountingBatchRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(StockCountingBatch.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        setBusdocList(scbRepo.findByBusDocByDocstatus("Counting"));
        if (getBusdocList() != null && getBusdocList().size() > 0) {
            selectedDoc = getBusdocList().get(0);
        }
    }

    public void refresh() {

    }

    public void checkCount() {
        if (selectedDoc == null) {
            JsfUtil.addErrorMessage("Document Selection Required");
            return;
        }

        if (count <= 0) {
            JsfUtil.addErrorMessage("Count must be greater than 0");
            return;
        }

        if (selectedProduct == null) {
            if (productCode != null) {
                selectedProduct = searchCon.findByProductidOrSupplierCodeOrBarcodes(productCode);
                if (selectedProduct == null) {
                    JsfUtil.addErrorMessage("Invalid product criteria");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Must select product or enter product code");
                return;
            }
        }
        countingBatch = repo.findStockCountingBatchByDoc(selectedDoc.getDocno(), selectedProduct.getProductid());

        totalCount = 0.0;
        totalAmount = 0.0;
        if (countingBatch != null) {
            for (StockCountingBatch s : countingBatch) {
                totalCount = totalCount + s.getCount();
                totalAmount = totalAmount + s.getTotalAmount();
            }
        }

        //System.out.println("CountingBatch: " + countingBatch);
        productName = selectedProduct.toString();
        unitDetails = selectedProduct.getPunit().getUnitsym() + " = " + selectedProduct.getPackqty() + " " + selectedProduct.getUnit().getUnitsym();
        brandCatDetails = selectedProduct.getBrand().getBrandname() + "   " + selectedProduct.getProdcategory().getCatname();
    }

    public void addCount() {
        if (selectedDoc == null) {
            JsfUtil.addErrorMessage("Document Selection Required");
            return;
        }

        if (count <= 0) {
            JsfUtil.addErrorMessage("Count must be greater than 0");
            return;
        }

        if (selectedProduct == null) {
            if (productCode != null) {
                selectedProduct = searchCon.findByProductidOrSupplierCodeOrBarcodes(productCode);
                if (selectedProduct == null) {
                    JsfUtil.addErrorMessage("Invalid product criteria");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Must select product or enter product code");
                return;
            }
        }

        StockCountingBatch pt = new StockCountingBatch();
        pt.setProductid(selectedProduct);
        pt.setCount(count);
        pt.setCurrentstock(ptCon.findStockBalanceByBranch(selectedProduct.getProductid(), userSession.getLoggedInBranch().getBranchid(), new Date()));
        pt.setCost(ptCon.findLastCostPurchaseOrAdjustment(selectedProduct.getProductid()));
        pt.setEnteredon(new Date());
        pt.setCountedon(pt.getEnteredon());
        pt.setBusdoc(getSelectedDoc());
        pt.setCountyear(getSelectedDoc().getCountyear());
        pt.setCountedbyempno(userSession.getLoggedInEmp());
        pt.setEnteredbyempno(userSession.getLoggedInEmp());
        pt.setCountedinbranchno(userSession.getLoggedInBranch());
        pt.setCountedunitno(pt.getProductid().getUnit());

        getSelectedDoc().getStockCountingBatch().add(pt);

        repo.save(pt);

        JsfUtil.addSuccessMessage(selectedProduct.toString() + " was added with count " + count);

        countingBatch = repo.findStockCountingBatchByDoc(selectedDoc.getDocno(), selectedProduct.getProductid());

        totalCount = 0.0;
        totalAmount = 0.0;
        if (countingBatch != null) {
            for (StockCountingBatch s : countingBatch) {
                totalCount = totalCount + s.getCount();
                totalAmount = totalAmount + s.getTotalAmount();
            }
        }

        //System.out.println("CountingBatch: " + countingBatch);
        productName = selectedProduct.toString();
        unitDetails = selectedProduct.getPunit().getUnitsym() + " = " + selectedProduct.getPackqty() + " " + selectedProduct.getUnit().getUnitsym();
        brandCatDetails = selectedProduct.getBrand().getBrandname() + "   " + selectedProduct.getProdcategory().getCatname();

        //System.out.println(productName + "\n" + unitDetails + "\n" + brandCatDetails);
        selectedProduct = null;
        productCode = "";
        count = 1.0;

    }

    @Transactional
    public void deleteTransactions() {
        if (selectedStockCounting == null) {
            JsfUtil.addErrorMessage("Invalid Transaction selection");
            return;
        }

        //getSelectedDoc().getStockCountingBatch().remove(selectedStockCounting);
        //selectedStockCounting.setBusdoc(null);
        //repo.delete(selectedStockCounting);
        
        int i = em.createNativeQuery("delete from scbatch where countingno=" + selectedStockCounting.getCountingno()).executeUpdate();
        if(i>0){
            JsfUtil.addSuccessMessage("Transaction was deleted successfuly");
        } else{
            JsfUtil.addErrorMessage("Could not delete transaction, contact administrator");
        }
        checkCount();
        selectedStockCounting = null;
    }

    /**
     * @return the busdocList
     */
    public List<StockCountingBusDoc> getBusdocList() {
        return busdocList;
    }

    /**
     * @param busdocList the busdocList to set
     */
    public void setBusdocList(List<StockCountingBusDoc> busdocList) {
        this.busdocList = busdocList;
    }

    /**
     * @return the selectedDoc
     */
    public StockCountingBusDoc getSelectedDoc() {
        return selectedDoc;
    }

    /**
     * @param selectedDoc the selectedDoc to set
     */
    public void setSelectedDoc(StockCountingBusDoc selectedDoc) {
        this.selectedDoc = selectedDoc;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the countingBatch
     */
    public List<StockCountingBatch> getCountingBatch() {
        return countingBatch;
    }

    /**
     * @param countingBatch the countingBatch to set
     */
    public void setCountingBatch(List<StockCountingBatch> countingBatch) {
        this.countingBatch = countingBatch;
    }

    /**
     * @return the selectedStockCounting
     */
    public StockCountingBatch getSelectedStockCounting() {
        return selectedStockCounting;
    }

    /**
     * @param selectedStockCounting the selectedStockCounting to set
     */
    public void setSelectedStockCounting(StockCountingBatch selectedStockCounting) {
        this.selectedStockCounting = selectedStockCounting;
    }

    /**
     * @return the selectedProduct
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /**
     * @return the count
     */
    public Double getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Double count) {
        this.count = count;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the unitDetails
     */
    public String getUnitDetails() {
        return unitDetails;
    }

    /**
     * @param unitDetails the unitDetails to set
     */
    public void setUnitDetails(String unitDetails) {
        this.unitDetails = unitDetails;
    }

    /**
     * @return the brandCatDetails
     */
    public String getBrandCatDetails() {
        return brandCatDetails;
    }

    /**
     * @param brandCatDetails the brandCatDetails to set
     */
    public void setBrandCatDetails(String brandCatDetails) {
        this.brandCatDetails = brandCatDetails;
    }

    /**
     * @return the totalCount
     */
    public Double getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(Double totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the totalAmount
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
