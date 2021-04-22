/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.helper.StockBalanceHelper;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.report.NativeQueryReportObject;
import com.smb.erp.report.NativeQueryTableModel;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author admin
 */
@Named(value = "stockReportController")
@ViewScoped
public class StockReportController implements Serializable {

    @Autowired
    UserSession userSession;

    @Autowired
    ProductTransactionController ptCon;

    @Autowired
    ProductRepository prodRepo;

    private List<Product> productList;

    private List<StockBalanceHelper> stockList;

    private List<String> branchList = new LinkedList<String>();

    private String selectedGroupBy = "None";

    private Date toDate = new Date();

    private StockBalanceHelper totalRow = new StockBalanceHelper();

    public StockReportController() {

    }

    @PostConstruct
    public void init() {
    }

    public void refresh() {
        setProductList(prodRepo.findByCriteria(""));

        setStockList(new LinkedList<StockBalanceHelper>());

        setTotalRow(new StockBalanceHelper());

        if (getProductList() != null) {
            for (Product p : getProductList()) {
                StockBalanceHelper sb = new StockBalanceHelper(p);
                NativeQueryTableModel model = ptCon.findStockBalances(p.getProductid(), userSession.getLoggedInCompany().getCompanyid(), toDate);
                List<NativeQueryReportObject> ret = model.getData();
                if (ret != null && ret.size() > 0) {
                    NativeQueryReportObject row = ret.get(0);
                    //System.out.println("Column Count: " + row.getColumnCount());
                    for (int i = 0; i < row.getColumnCount(); i++) {
                        //System.out.print(model.getColumns().get(i).getHeader()+":"+ Double.parseDouble(row.getField(i).toString())+"\t");
                        sb.putStock(model.getColumns().get(i).getHeader(), Double.parseDouble(row.getField(i).toString()));
                    }
                    //System.out.println();
                } else {
                    for (int i = 0; i < model.getColumns().size(); i++) {
                        //System.out.print(model.getColumns().get(i).getHeader()+":"+ Double.parseDouble(row.getField(i).toString())+"\t");
                        sb.putStock(model.getColumns().get(i).getHeader(), new Double(0));
                    }
                }
                if (sb.getTotal() != 0) {
                    for (String br : sb.getBranches()) {
                        Double t = getTotalRow().findStock(br);
                        if (t == null) {
                            t = 0.0;
                        }
                        Double n = sb.findStock(br);
                        if (n == null) {
                            n = 0.0;
                        }
                        t = t + n;
                        getTotalRow().putStock(br, t);
                    }
                    System.out.println(sb.getStockBalances() + "\t" + getTotalRow().getStockBalances());
                    getStockList().add(sb);
                }
            }
        }
        setBranchList(new LinkedList<String>());
        if (getStockList().size() > 0) {
            setBranchList(getStockList().get(0).getBranches());
        }

        System.out.println("Total_Row: " + getTotalRow().getStockBalances());
    }

    public void refreshValue() {
        setProductList(prodRepo.findByCriteria(""));

        setStockList(new LinkedList<StockBalanceHelper>());

        setTotalRow(new StockBalanceHelper());

        if (getProductList() != null) {
            //for (Product p : getProductList()) {
            NativeQueryTableModel model = ptCon.findStockList(userSession.getLoggedInCompany().getCompanyid(), toDate);
            List<NativeQueryReportObject> ret = model.getData();
            if (ret != null && ret.size() > 0) {
                for (int n = 0; n < ret.size(); n++) {
                    StockBalanceHelper sb = new StockBalanceHelper();
                    NativeQueryReportObject row = ret.get(n);
                    //System.out.println("Column Count: " + row.getColumnCount());
                    //assign productid, productname, unit
                    sb.setProductid(Long.parseLong(row.getField(0).toString()));
                    sb.setProductname(row.getField(1).toString());
                    sb.setSupcode(row.getField(2).toString());
                    sb.setCategory(row.getField(3).toString());
                    sb.setBrand(row.getField(4).toString());
                    sb.setUnit(row.getField(5).toString());
                    for (int i = 6; i < row.getColumnCount(); i++) {
                        //System.out.print(model.getColumns().get(i).getHeader()+":"+ Double.parseDouble(row.getField(i).toString())+"\t");
                        sb.putStock(model.getColumns().get(i).getHeader(), Double.parseDouble(row.getField(i).toString()));
                    }
                    //System.out.println();
                    updateLandedPriceAndAdd(sb);
                }
                System.out.println(getTotalRow().getStockBalances());
            } else {    //all values with zero
                StockBalanceHelper sb = new StockBalanceHelper();
                NativeQueryReportObject row = ret.get(0);
                sb.setProductid(Long.parseLong(row.getField(0).toString()));
                sb.setProductname(row.getField(1).toString());
                sb.setSupcode(row.getField(2).toString());
                sb.setCategory(row.getField(3).toString());
                sb.setBrand(row.getField(4).toString());
                sb.setUnit(row.getField(5).toString());
                for (int i = 6; i < model.getColumns().size(); i++) {
                    //System.out.print(model.getColumns().get(i).getHeader()+":"+ Double.parseDouble(row.getField(i).toString())+"\t");
                    sb.putStock(model.getColumns().get(i).getHeader(), new Double(0));
                    getStockList().add(sb);
                }
            }
            //}
        }
        setBranchList(new LinkedList<String>());
        if (getStockList().size() > 0) {
            setBranchList(getStockList().get(0).getBranches());
        }

        System.out.println("Total_Row: " + getTotalRow().getStockBalances());
    }

    public void updateAveragePriceAndAdd(StockBalanceHelper sb) {
        if (sb.getTotal() != 0) {
            for (String br : sb.getBranches()) {
                Double t = getTotalRow().findStock(br);
                if (t == null) {
                    t = 0.0;
                }
                Double n = sb.findStock(br);
                if (n == null) {
                    n = 0.0;
                }
                t = t + n;
                getTotalRow().putStock(br, t);
            }
            Double cost = ptCon.findAveragePurchaseOrAdjustment(sb.getProductid(), toDate);
            if (cost != null) {
                cost = 0.0;
            }
            sb.setPrice(cost);
            getTotalRow().setPrice(cost);
            getStockList().add(sb);
        }
    }

    public void updateLandedPriceAndAdd(StockBalanceHelper sb) {
        if (sb.getTotal() != 0) {
            for (String br : sb.getBranches()) {
                Double t = getTotalRow().findStock(br);
                if (t == null) {
                    t = 0.0;
                }
                Double n = sb.findStock(br);
                if (n == null) {
                    n = 0.0;
                }
                t = t + n;
                getTotalRow().putStock(br, t);
            }
            ProductTransaction pt = ptCon.findLastCostPurchaseOrAdjustment(sb.getProductid(), toDate);
            if (pt != null) {
                sb.setPrice(pt.getCost());
                getTotalRow().setPrice(pt.getCost());
            }
            getStockList().add(sb);
        }
    }

    /**
     * @return the selectedGroupBy
     */
    public String getSelectedGroupBy() {
        return selectedGroupBy;
    }

    /**
     * @param selectedGroupBy the selectedGroupBy to set
     */
    public void setSelectedGroupBy(String selectedGroupBy) {
        this.selectedGroupBy = selectedGroupBy;
    }

    /**
     * @return the stockList
     */
    public List<StockBalanceHelper> getStockList() {
        return stockList;
    }

    /**
     * @param stockList the stockList to set
     */
    public void setStockList(List<StockBalanceHelper> stockList) {
        this.stockList = stockList;
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
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    /**
     * @return the totalRow
     */
    public StockBalanceHelper getTotalRow() {
        return totalRow;
    }

    /**
     * @param totalRow the totalRow to set
     */
    public void setTotalRow(StockBalanceHelper totalRow) {
        this.totalRow = totalRow;
    }

}
