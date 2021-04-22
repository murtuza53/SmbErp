/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.request.controller;

import com.smb.erp.controller.AbstractController;
import com.smb.erp.controller.SystemDefaultsController;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.helper.ChartHelper;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.ProductTransactionRepository;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.SystemConfig;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.primefaces.model.charts.bar.BarChartModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author admin
 */
@Named(value = "vatDashboardController")
@ViewScoped
public class VatDashboardController extends AbstractController<ProductTransaction> {

    @Autowired
    EntityManager em;

    @Autowired
    BusDocInfoRepository bdInfoRepo;

    @Autowired
    SystemDefaultsController sysCon;

    ProductTransactionRepository repo;

    private List<Integer> years = Arrays.asList(new Integer[]{DateUtil.getYear(new Date()), DateUtil.getYear(new Date()) - 1});

    private Integer selectedYear = DateUtil.getYear(new Date());

    private List<BusDocInfo> salesTypes;

    private List<BusDocInfo> purchaseTypes;

    private Date fromDate = DateUtil.startOfYear(new Date());

    private Date toDate = new Date();

    @Autowired
    public VatDashboardController(ProductTransactionRepository repository) {
        this.repo = repository;
    }

    @PostConstruct
    public void init() {
        //BusDocInfo = bdInfoRepo.findBusDocInfoByDocAndTransactionType("Sales", "Inventory");
        //BusDocInfo = bdInfoRepo.findBusDocInfoByDocAndTransactionType("Purchase", "Inventory");

        List<Integer> sales = sysCon.getAsListInteger("SalesVatOutputDocuments");
        salesTypes = bdInfoRepo.findAllById(sales);

        List<Integer> purs = sysCon.getAsListInteger("PurchaseVatInputDocuments");
        purchaseTypes = bdInfoRepo.findAllById(purs);

        refresh();
    }

    public void refresh() {
    }

    public void prepareSalesOutputVat(List<BusDocInfo> doctypes) {

        if (doctypes == null || doctypes.isEmpty()) {
            JsfUtil.addErrorMessage("No Documents found for report");
            System.out.println("prepareSalesOutputVat: No Documents found for report");
            return;
        }

        fromDate = DateUtil.startOfDay(fromDate);
        toDate = DateUtil.endOfDay(toDate);

        String filter = null;
        for (BusDocInfo type : doctypes) {
            if (filter == null) {
                filter = "pt.docno LIKE '" + type.getDoctype() + "%'";
            } else {
                filter = filter + " OR pt.docno LIKE '" + type.getDoctype() + "%'";
            }
        }
        String query = "SELECT MONTH(ptdate) monno, MONTHNAME(ptdate) as mon, Year(ptdate) as yr, ptdate, transid, docno, "
                + "substring(docno, 1, locate('-', docno)-1) as dt, sum(price) as sp, sum(cost) as cst, sum(price-cost) as profit, vatamt, vatname "
                + "FROM "
                + "(SELECT pt.docno as docno, pt.transdate as ptdate, pt.prodtransid as transid, (pt.sold+pt.received)*unitprice as price, "
                + "(pt.sold+pt.received)*cost as cost, vatamount as vatamt, vatsp.typename as vatname "
                + "FROM prodtransaction as pt, vatsalespurchasetype as vatsp "
                + "WHERE pt.vatsptypeid=vatsp.vatsptypeid AND (" + filter + ") AND (pt.transdate>='" + 
                SystemConfig.MYSQL_DATE_FORMAT.format(fromDate) + "' AND pt.transdate<='" + 
                SystemConfig.MYSQL_DATE_FORMAT.format(toDate) + "') ORDER BY pt.docno) as lvl1 "
                + "GROUP BY transid;";

        //System.out.println("prepareSalesPurchaseByMonth: " + query);
        List<Object[]> docs = em.createNativeQuery(query).getResultList();
        if (docs == null || docs.isEmpty()) {
            JsfUtil.addErrorMessage("No transaction found");
            System.out.println("prepareSalesOutputVat: No transaction found");
            return;
        }
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
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

}
