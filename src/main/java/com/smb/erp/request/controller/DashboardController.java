/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.request.controller;

import com.smb.erp.controller.AbstractController;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.helper.ChartHelper;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.ProductTransactionRepository;
import com.smb.erp.util.ColorUtil;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.SystemConfig;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author admin
 */
@Named(value = "dashboardController")
@ViewScoped
public class DashboardController extends AbstractController<ProductTransaction> {

    @Autowired
    EntityManager em;

    @Autowired
    BusDocInfoRepository bdInfoRepo;

    ProductTransactionRepository repo;

    private Integer completedQuotes = 0;
    private Integer completedSalesOrder = 0;
    private Integer completedDn = 0;
    private Integer pendingQuotes = 0;
    private Integer pendingSalesOrder = 0;
    private Integer pendingDn;

    private BarChartModel stackedChart;

    private LineChartModel yearlyLineChart;

    private List<Integer> years = Arrays.asList(new Integer[]{DateUtil.getYear(new Date()), DateUtil.getYear(new Date()) - 1});

    private Integer selectedYear = DateUtil.getYear(new Date());

    private List<String> docTypes;

    private String selectedDocType;

    @Autowired
    public DashboardController(ProductTransactionRepository repository) {
        this.repo = repository;
    }

    @PostConstruct
    public void init() {
        List<BusDocInfo> blist = bdInfoRepo.findBusDocInfoByDocAndTransactionType("Sales", "Inventory");
        if (blist != null) {
            setDocTypes(new LinkedList<>());
            for (BusDocInfo bd : blist) {
                getDocTypes().add(bd.getPrefix());
            }
        }
        if (getDocTypes() != null && getDocTypes().size() > 0) {
            selectedDocType = getDocTypes().get(0);
        }
        refresh();
    }

    public void refresh() {
        calculatePendingSalesDocuments();
        prepareSalesPurchaseByMonth(getDocTypes());
        prepareSalesPurchaseByYear(selectedDocType);
    }

    public void calculatePendingSalesDocuments() {
        completedQuotes = 0;
        pendingQuotes = 0;
        //List<Object[]> quote = ds.executeNativeQuery(preparePendingQuery("QTN"));
        List<Object[]> docs = em.createNativeQuery(preparePendingQuery("QTN")).getResultList();
        if (docs != null) {
            pendingQuotes = docs.size();
            /*for (Object[] ar : quote) {
                for (Object o : ar) {
                    System.out.print(o + "\t");
                }
                System.out.println();
            }*/
        }

        docs = em.createNativeQuery(prepareCompletedQuery("QTN")).getResultList();
        if (docs != null) {
            completedQuotes = docs.size();
        }

        docs = em.createNativeQuery(preparePendingQuery("SOR")).getResultList();
        if (docs != null) {
            pendingSalesOrder = docs.size();
        }
        docs = em.createNativeQuery(prepareCompletedQuery("SOR")).getResultList();
        if (docs != null) {
            completedSalesOrder = docs.size();
        }

        docs = em.createNativeQuery(preparePendingQuery("DN")).getResultList();
        if (docs != null) {
            pendingDn = docs.size();
        }
        docs = em.createNativeQuery(prepareCompletedQuery("DN")).getResultList();
        if (docs != null) {
            completedDn = docs.size();
        }
    }

    public String preparePendingQuery(String doctype) {
        return "SELECT docno, sum(qty), sum(exe), sum(qty-exe) as diff FROM "
                + "(SELECT pt.docno as docno, pt.prodtransid as transid, sum(pt.sold+pt.received) as qty, "
                + "COALESCE((select sum(pte.executionqty) from prodtransexecution pte where pte.fromprodtransid=pt.prodtransid),0) as exe FROM prodtransaction pt, busdoc doc "
                + "where pt.docno=doc.docno and pt.docno like '" + doctype + "%' and doc.docstatus NOT IN ('Draft', 'Counting',  'Completed', 'Paid', 'Cancelled', 'Returned') "
                + "group by pt.prodtransid order by pt.transdate) lvl1 "
                + "GROUP BY docno HAVING diff>0";
    }

    public String prepareCompletedQuery(String doctype) {
        return "SELECT docno, sum(qty), sum(exe), sum(qty-exe) as diff FROM "
                + "(SELECT pt.docno as docno, pt.prodtransid as transid, sum(pt.sold+pt.received) as qty, "
                + "COALESCE((select sum(pte.executionqty) from prodtransexecution pte where pte.fromprodtransid=pt.prodtransid),0) as exe FROM prodtransaction pt, busdoc doc "
                + "where pt.docno=doc.docno and pt.docno like '" + doctype + "%' and doc.docstatus NOT IN ('Draft', 'Counting',  'Completed', 'Paid', 'Cancelled', 'Returned') "
                + "group by pt.prodtransid order by pt.transdate) lvl1 "
                + "GROUP BY docno HAVING diff=0";
    }

    public void prepareSalesPurchaseByMonth() {
        prepareSalesPurchaseByMonth(getDocTypes());
    }

    public List<ChartHelper> prepareSalesPurchaseByMonth(List<String> doctypes) {
        stackedChart = new BarChartModel();

        if (doctypes == null || doctypes.isEmpty()) {
            JsfUtil.addErrorMessage("No Documents found for report");
            System.out.println("prepareSalesPurchaseByMonth: No Documents found for report");
            return new LinkedList<>();
        }

        Date fromDate = DateUtil.createDate(1, Calendar.JANUARY, selectedYear, 0, 1, 0, 0);
        Date toDate = DateUtil.createDate(1, Calendar.DECEMBER, selectedYear, 23, 59, 59, 0);

        String filter = null;
        for (String type : doctypes) {
            if (filter == null) {
                filter = "pt.docno LIKE '" + type + "%'";
            } else {
                filter = filter + " OR pt.docno LIKE '" + type + "%'";
            }
        }
        String query = "SELECT MONTH(ptdate) monno, MONTHNAME(ptdate) as mon, Year(ptdate) as yr, substring(docno, 1, locate('-', docno)-1) as dt, sum(price) as sp, sum(cost) as cst, sum(price-cost) as profit "
                + "FROM "
                + "(SELECT pt.docno as docno, pt.transdate as ptdate, pt.prodtransid as transid, (pt.sold+pt.received)*unitprice as price, (pt.sold+pt.received)*cost as cost "
                + "FROM prodtransaction as pt "
                + "WHERE (" + filter + ") AND (pt.transdate>='" + SystemConfig.MYSQL_DATE_FORMAT.format(fromDate) + "' AND pt.transdate<='" + SystemConfig.MYSQL_DATE_FORMAT.format(toDate) + "') ORDER BY pt.docno) as lvl1 "
                + "GROUP BY mon, dt ORDER BY monno, dt;";

        //System.out.println("prepareSalesPurchaseByMonth: " + query);
        List<Object[]> docs = em.createNativeQuery(query).getResultList();
        if (docs == null || docs.isEmpty()) {
            JsfUtil.addErrorMessage("No transaction found");
            System.out.println("prepareSalesPurchaseByMonth: No transaction found");
            return new LinkedList<>();
        }

        //for(Object[] data: docs){
        //    System.out.println("prepareSalesPurchaseByMonth: " + data);
        //}
        List<ChartHelper> list = new LinkedList<>();
        HashMap<String, List<ChartHelper>> map = new HashMap<>();
        List<String> labels = new LinkedList<>();
        docs.forEach(data -> {
            ChartHelper chart = new ChartHelper(data);
            list.add(chart);
            if (!labels.contains(chart.getMonthName())) {
                labels.add(chart.getMonthName());
            }
            if (map.containsKey(chart.getDocType())) {
                List<ChartHelper> cl = map.get(chart.getDocType());
                cl.add(chart);
            } else {
                List<ChartHelper> cl = new LinkedList<>();
                cl.add(chart);
                map.put(chart.getDocType(), cl);
            }
        });

        BarChartDataSet costSet = new BarChartDataSet();
        costSet.setLabel("Cost");
        costSet.setBackgroundColor(ColorUtil.RGB_COLORS.get(0));
        List<Number> costs = new LinkedList<>();

        BarChartDataSet profSet = new BarChartDataSet();
        profSet.setLabel("Profit");
        profSet.setBackgroundColor(ColorUtil.RGB_COLORS.get(1));
        List<Number> profs = new LinkedList<>();
        for (String key : map.keySet()) {
            for (ChartHelper ch : map.get(key)) {
                costs.add(ch.getCost().intValue());
                profs.add(ch.getProfit().intValue());
            }
        }
        costSet.setData(costs);
        profSet.setData(profs);

        ChartData data = new ChartData();
        data.addChartDataSet(costSet);
        data.addChartDataSet(profSet);
        data.setLabels(labels);

        stackedChart.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setStacked(true);
        linearAxes.setOffset(true);
        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);

        stackedChart.setOptions(options);
        return list;
    }

    public void prepareSalesPurchaseByYear() {
        prepareSalesPurchaseByYear(selectedDocType);
    }

    public List<ChartHelper> prepareSalesPurchaseByYear(String doctype) {
        yearlyLineChart = new LineChartModel();

        //if (doctypes == null || doctypes.isEmpty()) {
        if (doctype == null) {
            JsfUtil.addErrorMessage("No Documents found for report");
            System.out.println("prepareSalesPurchaseByYear: No Documents found for report");
            return new LinkedList<>();
        }

        Date fromDate = DateUtil.createDate(1, Calendar.JANUARY, DateUtil.getYear(new Date()) - 1, 0, 1, 0, 0);
        Date toDate = DateUtil.createDate(1, Calendar.DECEMBER, DateUtil.getYear(new Date()), 23, 59, 59, 0);

        String filter = null;
        //for (String type : doctypes) {
        //    if (filter == null) {
        filter = "pt.docno LIKE '" + doctype + "%'";
        //    } else {
        //        filter = filter + " OR pt.docno LIKE '" + type + "%'";
        //    }
        //}

        String query = "SELECT MONTH(ptdate) monno, MONTHNAME(ptdate) as mon, Year(ptdate) as yr, substring(docno, 1, locate('-', docno)-1) as dt, sum(price) as sp, sum(cost) as cst, sum(price-cost) as profit "
                + "FROM "
                + "(SELECT pt.docno as docno, pt.transdate as ptdate, pt.prodtransid as transid, (pt.sold+pt.received)*unitprice as price, (pt.sold+pt.received)*cost as cost "
                + "FROM prodtransaction as pt "
                + "WHERE (" + filter + ") AND (pt.transdate>='" + SystemConfig.MYSQL_DATE_FORMAT.format(fromDate) + "' AND pt.transdate<='" + SystemConfig.MYSQL_DATE_FORMAT.format(toDate) + "') ORDER BY pt.docno) as lvl1 "
                + "GROUP BY monno, dt ORDER BY monno, dt;";

        //System.out.println("prepareSalesPurchaseByMonth: " + query);
        List<Object[]> docs = em.createNativeQuery(query).getResultList();
        if (docs == null || docs.isEmpty()) {
            JsfUtil.addErrorMessage("No transaction found");
            System.out.println("prepareSalesPurchaseByYear: No transaction found");
            return new LinkedList<>();
        }

        //for(Object[] data: docs){
        //    System.out.println("prepareSalesPurchaseByMonth: " + data);
        //}
        List<ChartHelper> list = new LinkedList<>();
        List<String> labels = new LinkedList<>();
        HashMap<String, List<ChartHelper>> map = new HashMap<>();
        docs.forEach(data -> {
            ChartHelper chart = new ChartHelper(data);
            list.add(chart);
            if (!labels.contains(chart.getMonthName())) {
                labels.add(chart.getMonthName());
            }
            if (map.containsKey(chart.getMonthName())) {
                List<ChartHelper> cl = map.get(chart.getMonthName());
                cl.add(chart);
            } else {
                List<ChartHelper> cl = new LinkedList<>();
                cl.add(chart);
                map.put(chart.getMonthName(), cl);
            }
        });

        ChartData data = new ChartData();
        LineChartDataSet salesSet = new LineChartDataSet();
        salesSet.setLabel(selectedYear+"");
        salesSet.setBorderColor(ColorUtil.RGB_COLORS.get(docTypes.indexOf(doctype)));
        salesSet.setLineTension(0.1);
        List<Object> sales = new LinkedList<>();
        for (String key : map.keySet()) {
            for (ChartHelper ch : map.get(key)) {
                sales.add(ch.getPrice().intValue());
            }
        }
        salesSet.setData(sales);
        salesSet.setFill(false);
        salesSet.setLineTension(0.1);
        
        data.addChartDataSet(salesSet);
        data.setLabels(labels);
        yearlyLineChart.setData(data);

        return list;
    }

    /**
     * @return the pendingQuotes
     */
    public Integer getPendingQuotes() {
        return pendingQuotes;
    }

    /**
     * @param pendingQuotes the pendingQuotes to set
     */
    public void setPendingQuotes(Integer pendingQuotes) {
        this.pendingQuotes = pendingQuotes;
    }

    /**
     * @return the completedQuotes
     */
    public Integer getCompletedQuotes() {
        return completedQuotes;
    }

    /**
     * @param completedQuotes the completedQuotes to set
     */
    public void setCompletedQuotes(Integer completedQuotes) {
        this.completedQuotes = completedQuotes;
    }

    /**
     * @return the completedSalesOrder
     */
    public Integer getCompletedSalesOrder() {
        return completedSalesOrder;
    }

    /**
     * @param completedSalesOrder the completedSalesOrder to set
     */
    public void setCompletedSalesOrder(Integer completedSalesOrder) {
        this.completedSalesOrder = completedSalesOrder;
    }

    /**
     * @return the completedDn
     */
    public Integer getCompletedDn() {
        return completedDn;
    }

    /**
     * @param completedDn the completedDn to set
     */
    public void setCompletedDn(Integer completedDn) {
        this.completedDn = completedDn;
    }

    /**
     * @return the pendingSalesOrder
     */
    public Integer getPendingSalesOrder() {
        return pendingSalesOrder;
    }

    /**
     * @param pendingSalesOrder the pendingSalesOrder to set
     */
    public void setPendingSalesOrder(Integer pendingSalesOrder) {
        this.pendingSalesOrder = pendingSalesOrder;
    }

    /**
     * @return the pendingDn
     */
    public Integer getPendingDn() {
        return pendingDn;
    }

    /**
     * @param pendingDn the pendingDn to set
     */
    public void setPendingDn(Integer pendingDn) {
        this.pendingDn = pendingDn;
    }

    /**
     * @return the stackedChart
     */
    public BarChartModel getStackedChart() {
        return stackedChart;
    }

    /**
     * @param stackedChart the stackedChart to set
     */
    public void setStackedChart(BarChartModel stackedChart) {
        this.stackedChart = stackedChart;
    }

    /**
     * @return the years
     */
    public List<Integer> getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(List<Integer> years) {
        this.years = years;
    }

    /**
     * @return the selectedYear
     */
    public Integer getSelectedYear() {
        return selectedYear;
    }

    /**
     * @param selectedYear the selectedYear to set
     */
    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }

    /**
     * @return the selectedDocType
     */
    public String getSelectedDocType() {
        return selectedDocType;
    }

    /**
     * @param selectedDocType the selectedDocType to set
     */
    public void setSelectedDocType(String selectedDocType) {
        this.selectedDocType = selectedDocType;
    }

    /**
     * @return the docTypes
     */
    public List<String> getDocTypes() {
        return docTypes;
    }

    /**
     * @param docTypes the docTypes to set
     */
    public void setDocTypes(List<String> docTypes) {
        this.docTypes = docTypes;
    }

    /**
     * @return the yearlyLineChart
     */
    public LineChartModel getYearlyLineChart() {
        return yearlyLineChart;
    }

    /**
     * @param yearlyLineChart the yearlyLineChart to set
     */
    public void setYearlyLineChart(LineChartModel yearlyLineChart) {
        this.yearlyLineChart = yearlyLineChart;
    }

}
