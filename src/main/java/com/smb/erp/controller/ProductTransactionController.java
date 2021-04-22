/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Branch;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.Company;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.entity.ProductTransactionExecution;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.CompanyRepository;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.repo.ProductTransactionExecutionRepository;
import com.smb.erp.repo.ProductTransactionRepository;
import com.smb.erp.report.NativeQueryReportObject;
import com.smb.erp.report.NativeQueryTableModel;
import com.smb.erp.service.DatabaseService;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.Query;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "producttransactionController")
@ViewScoped
public class ProductTransactionController extends AbstractController<ProductTransaction> {

    ProductTransactionRepository repo;

    @Autowired
    DatabaseService ds;

    @Autowired
    BusDocInfoRepository docinfoRepo;

    @Autowired
    CompanyRepository companyRepo;
    
    @Autowired
    BranchController branchCon;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    ProductTransactionExecutionRepository pteRepo;

    @Autowired
    UserSession userSession;

    private NativeQueryTableModel nqModel = new NativeQueryTableModel();

    private Date fromDate = DateUtil.startOfYear(new Date());

    private Date toDate = DateUtil.endOfDay(new Date());

    private Product selectProduct;

    @Autowired
    public ProductTransactionController(ProductTransactionRepository repo) {
        super(ProductTransaction.class, repo);
        this.repo = repo;
    }

    @Override
    public List<ProductTransaction> getItems() {
        return items;
    }

    public void refresh() {
        items = null;
        //System.out.println("refresh: " + getSelectProduct());
        if (getSelectProduct() == null) {
            items = new LinkedList<ProductTransaction>();
        } else {
            items = repo.findStockMovement(getSelectProduct().getProductid(), DateUtil.startOfDay(fromDate), DateUtil.endOfDay(toDate));
            //System.out.println("FETCHED: " + getSelectProduct() + " => " + items);
            if (items != null) {
                Double bal = repo.findStockBalanceByCompany(getSelectProduct().getProductid(), userSession.getLoggedInCompany().getCompanyid(), DateUtil.startOfDay(fromDate));
                if (bal == null) {
                    bal = 0.0;
                }
                if (items.size() > 0) {
                    ProductTransaction pt = items.get(items.size() - 1);
                    bal = bal + pt.getReceived() - pt.getSold();
                    pt.setCumulative(bal);
                }
                for (int i = items.size() - 2; i >= 0; i--) {
                    ProductTransaction pt = items.get(i);
                    bal = bal + pt.getReceived() - pt.getSold();
                    pt.setCumulative(bal);
                }
                JsfUtil.addSuccessMessage(items.size() + " transactions listed");
                System.out.println(items.size() + " transactions listed from " + fromDate + " to " + toDate + ": " + getSelectProduct());
            }
        }
    }
    
    @Transactional
    public void remove(ProductTransaction pt){
        if(pt.getToprodtransaction()!=null){
            for(ProductTransactionExecution pte: pt.getToprodtransaction()){
                if(pte.getExecutionid()>0){
                    pte.setFromprodtransid(null);
                    pte.setToprodtransid(null);
                    pteRepo.delete(pte);
                }
            }
        }
        repo.delete(pt);
    }

    public void onItemSelect(SelectEvent event) {
        refresh();
    }

    public List<Product> completeFilter(String criteria) {
        //System.out.println("completeFilter: " + criteria);

        //return ejbFacade.findRange(0, 10, "firstName", "ASC",
        //        ejbFacade.createFilters(new String[]{"itsNo", "firstName"}, criteria));
        //Page<ItsMaster> page = itsFacade.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName")));
        return productRepo.findByCriteria(criteria);
    }

    public List<ProductTransaction> getSalesTransactions(long productid, Date toDate, long companyid) {
        List<ProductTransaction> ptlist = new LinkedList<>();

        List<BusDocInfo> bdiList = docinfoRepo.findBusDocInfoByDocType(BusDocType.SALES.getValue());

        /*System.out.println("getSalesTransactions:");
        bdiList.forEach((info) -> {
            System.out.print("\t=>" +info.getAbbreviation());
        });
        System.out.println("");*/
        for (BusDocInfo bdi : bdiList) {
            List<ProductTransaction> list = repo.findLastTransaction(productid, DateUtil.endOfDay(toDate), bdi.getPrefix(), PageRequest.of(0, 2));
            //System.out.println(bdi.getPrefix() + "\t" + list);
            if (list != null && list.size() > 0) {
                ptlist.addAll(list);
                //System.out.println(list.get(0));
            }
        }
        return ptlist;
    }

    public List<ProductTransaction> getPurchaseTransactions(long productid, Date toDate, long companyid) {
        List<ProductTransaction> ptlist = new LinkedList<>();

        List<BusDocInfo> bdiList = docinfoRepo.findBusDocInfoByDocType(BusDocType.PURCHASE.getValue());

        List<BusDocInfo> adjlist = docinfoRepo.findBusDocInfoByDocType(BusDocType.INVENTORY.getValue());
        if (adjlist != null) {
            bdiList.addAll(adjlist);
        }

        /*System.out.println("getPurchaseTransactions:");
        bdiList.forEach((info) -> {
            System.out.print("\t=>" +info.getAbbreviation());
        });
        System.out.println("");*/
        for (BusDocInfo bdi : bdiList) {
            List<ProductTransaction> list = repo.findLastTransaction(productid, DateUtil.endOfDay(toDate), bdi.getPrefix(), PageRequest.of(0, 2));
            //System.out.println(bdi.getPrefix() + "\t" + list);
            if (list != null && list.size() > 0) {
                ptlist.addAll(list);
                //System.out.println(list.get(0));
            }
        }
        return ptlist;
    }

    public List<ProductTransaction> getStockBalances(long productid, Date toDate, long companyid) {
        double total = 0.0;

        Company com = companyRepo.getOne(companyid);

        List<ProductTransaction> pts = new LinkedList<>();

        List<Branch> blist = branchCon.findBranchByCompanyId(com.getCompanyid());
        for (Branch br : blist) {
            Double bal = repo.findStockBalanceByBranch(productid, br.getBranchid(), toDate);
            //System.out.println("ProductTransactionController.findStockBalanceByBranch: " + productid + " => " + br.getBranchid() + " => " + bal);
            if (bal == null) {
                bal = 0.0;
            }
            ProductTransaction pt = new ProductTransaction();
            pt.setReference("Stock " + com.getAbbreviation());
            pt.setBranch(br);
            pt.setTransdate(toDate);
            pt.setCumulative(bal);
            total = total + bal;

            pts.add(pt);
        }
        ProductTransaction pt = new ProductTransaction();
        pt.setReference("Stock " + com.getAbbreviation());
        pt.setBranch(new Branch("TOTAL", "Total"));
        pt.setTransdate(toDate);
        pt.setCumulative(total);
        pts.add(pt);

        return pts;
    }

    public NativeQueryTableModel findStockBalances(String criteria, long companyid, Date todate) {
        Company com = companyRepo.getOne(companyid);
        //String query = "SELECT OBJECT(p) FROM Product as p "
        //        + "WHERE (TRIM(p.productid) LIKE %:criteria% OR p.productname LIKE %:criteria% OR "
        //        + "p.supplierscode LIKE %:criteria% OR p.stockid LIKE %:criteria%) AND p.inactive=0 "
        //        + "ORDER BY p.productname asc";

        nqModel.resetModel();
        nqModel.addColumns(new String[]{"Stock#", "Product Name", "Avg Price"},
                new Class[]{String.class, String.class, Double.class},
                new String[]{"", "", ""});

        String q = "SELECT pt.productid, p.productname, avg(pt.unitprice) as price,";

        String qtotal = null;
        List<Branch> blist = branchCon.findBranchByCompanyId(com.getCompanyid());
        for (Branch br : blist) {
            nqModel.addColumns(new String[]{br.getAbbreviation()}, new Class[]{Double.class}, new String[]{""});
            if (qtotal == null) {
                qtotal = "pt.branchid=" + br.getBranchid();
            } else {
                qtotal = qtotal + " OR pt.branchid=" + br.getBranchid();
            }
            q = q + "sum(if(pt.branchid=" + br.getBranchid() + ", pt.received-pt.sold, 0)) as " + br.getAbbreviation() + ",";
        }
        q = q + "sum(if(" + qtotal + ", pt.received-pt.sold, 0)) as TOTAL "
                + "FROM prodtransaction as pt, product as p "
                + "WHERE pt.productid=p.productid AND p.inactive=0 AND pt.transactiontype='Inventory' "
                + "AND (TRIM(p.productid) LIKE '%" + criteria + "%' OR p.productname LIKE '%" + criteria + "%' OR p.supplierscode LIKE '%" + criteria + "%' "
                + "OR p.stockid LIKE '%" + criteria + "%' OR p.barcode1 LIKE '%" + criteria + "%' OR p.barcode2 LIKE '%" + criteria + "%') "
                + " AND pt.transdate<=:toDate "
                + "GROUP BY pt.productid";
        nqModel.addColumns(new String[]{"TOTAL"}, new Class[]{Double.class}, new String[]{""});

        //System.out.println("findStockBalances: " + q);

        Query query = ds.createNativeQuery(q);
        query.setParameter("toDate", todate);
        nqModel.setData(NativeQueryReportObject.asReportObjectList(query.getResultList()));
        return nqModel;
    }

    public NativeQueryTableModel findStockBalances(long productid, long companyid, Date todate) {
        Company com = companyRepo.getOne(companyid);
        //String query = "SELECT OBJECT(p) FROM Product as p "
        //        + "WHERE (TRIM(p.productid) LIKE %:criteria% OR p.productname LIKE %:criteria% OR "
        //        + "p.supplierscode LIKE %:criteria% OR p.stockid LIKE %:criteria%) AND p.inactive=0 "
        //        + "ORDER BY p.productname asc";

        nqModel.resetModel();
        
        todate = DateUtil.endOfDay(todate);
        
        String q = "SELECT ";

        String qtotal = null;
        
        List<Branch> blist = branchCon.findBranchByCompanyId(com.getCompanyid());
        for (Branch br : blist) {
            nqModel.addColumns(new String[]{br.getAbbreviation()}, new Class[]{Double.class}, new String[]{""});
            if (qtotal == null) {
                qtotal = "pt.branchid=" + br.getBranchid();
            } else {
                qtotal = qtotal + " OR pt.branchid=" + br.getBranchid();
            }
            q = q + "sum(if(pt.branchid=" + br.getBranchid() + ", pt.received-pt.sold, 0)) as " + br.getAbbreviation() + ",";
        }
        q = q + "sum(if(" + qtotal + ", pt.received-pt.sold, 0)) as TOTAL "
                + "FROM prodtransaction as pt, product as p "
                + "WHERE pt.productid=p.productid AND p.inactive=0 AND pt.transactiontype='Inventory' "
                + "AND pt.transdate<=:toDate AND p.productid=:productid AND p.producttypeid=1 " //here producttype is 1 for Inventory
                + "GROUP BY pt.productid";
        nqModel.addColumns(new String[]{"TOTAL"}, new Class[]{Double.class}, new String[]{""});
        //System.out.println("findStockBalances: " + q);

        Query query = ds.createNativeQuery(q);
        query.setParameter("toDate", todate).setParameter("productid", productid);
        nqModel.setData(NativeQueryReportObject.asReportObjectList(query.getResultList()));
        return nqModel;
    }
    
    public NativeQueryTableModel findStockList(long companyid, Date todate){
        Company com = companyRepo.getOne(companyid);
        //String query = "SELECT OBJECT(p) FROM Product as p "
        //        + "WHERE (TRIM(p.productid) LIKE %:criteria% OR p.productname LIKE %:criteria% OR "
        //        + "p.supplierscode LIKE %:criteria% OR p.stockid LIKE %:criteria%) AND p.inactive=0 "
        //        + "ORDER BY p.productname asc";

        nqModel.resetModel();
        
        todate = DateUtil.endOfDay(todate);
        
        String q = "SELECT p.productid, p.productname, p.supplierscode, pc.catname, br.brandname, u.unitsym, ";

        String qtotal = null;
        nqModel.addColumns(new String[]{"Sotck#", "Description", "SupCode", "Category", "Brand", "Unit"}, 
                new Class[]{Long.class, String.class, String.class, String.class, String.class, String.class}, new String[]{"", "", "", "", "", ""});

        List<Branch> blist = branchCon.findBranchByCompanyId(com.getCompanyid());
        for (Branch br : blist) {
            nqModel.addColumns(new String[]{br.getAbbreviation()}, new Class[]{Double.class}, new String[]{""});
            if (qtotal == null) {
                qtotal = "pt.branchid=" + br.getBranchid();
            } else {
                qtotal = qtotal + " OR pt.branchid=" + br.getBranchid();
            }
            q = q + "sum(if(pt.branchid=" + br.getBranchid() + ", pt.received-pt.sold, 0)) as " + br.getAbbreviation() + ",";
        }
        q = q + "sum(if(" + qtotal + ", pt.received-pt.sold, 0)) as TOTAL "
                + "FROM prodtransaction as pt, product as p, unit as u, brand as br, prodcategory as pc "
                + "WHERE pt.productid=p.productid AND p.unitid=u.unitid AND p.brandid=br.brandid AND p.prodcatid=pc.prodcatId "
                + "AND p.inactive=0 AND pt.transactiontype='Inventory' "
                + "AND pt.transdate<=:toDate " //here producttype is 1 for Inventory
                + "GROUP BY pt.productid ORDER BY p.productname";
        nqModel.addColumns(new String[]{"TOTAL"}, new Class[]{Double.class}, new String[]{""});
        System.out.println("findStockBalances: " + q);

        Query query = ds.createNativeQuery(q);
        query.setParameter("toDate", todate);
        nqModel.setData(NativeQueryReportObject.asReportObjectList(query.getResultList()));
        return nqModel;
    }

    public Double findAveragePurchaseOrAdjustment(long productid) {
        Double cost = repo.findAveragePurchaseOrAdjustment(productid);
        if (cost == null) {
            return 0.0;
        }
        return cost;
    }

    public Double findAveragePurchaseOrAdjustment(long productid, Date toDate) {
        Double cost = repo.findAveragePurchaseOrAdjustment(productid, DateUtil.endOfDay(toDate));
        if (cost == null) {
            return 0.0;
        }
        return cost;
    }

    public double findLastCostPurchaseOrAdjustment(long productid) {
        double cost = 0.0;

        List<ProductTransaction> list = repo.findLastCostPurchaseOrAdjustment(productid, PageRequest.of(0, 1));
        //System.out.println("findLastCostPurchaseOrAdjustment: " + list.size() + " => " + list);
        if (list != null && list.size() > 0) {
            cost = list.get(0).getCost();
        }
        return cost;
    }

    public ProductTransaction findLastCostPurchaseOrAdjustment(long productid, Date toDate) {

        List<ProductTransaction> list = repo.findLastCostPurchaseOrAdjustment(productid, DateUtil.endOfDay(toDate), PageRequest.of(0, 1));
        //System.out.println("findLastCostPurchaseOrAdjustment: " + list.size() + " => " + list);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public double findLastCostPurchase(long productid) {
        double cost = 0.0;

        List<ProductTransaction> list = repo.findLastCostPurchase(productid, PageRequest.of(0, 1));
        if (list != null || list.size() > 0) {
            cost = list.get(0).getCost();
        }
        return cost;
    }

    public ProductTransaction findLastCostPurchase(long productid, Date toDate) {

        List<ProductTransaction> list = repo.findLastCostPurchase(productid, DateUtil.endOfDay(toDate), PageRequest.of(0, 1));
        if (list != null || list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public double findStockBalanceByBranch(long productid, long branchid, Date toDate) {
        Double bal = repo.findStockBalanceByBranch(productid, branchid, DateUtil.addHours(toDate, 0, -1));
        if (bal == null) {
            return 0;
        }
        return bal;
    }

    public double findStockBalanceByCompany(long productid, long companyid, Date toDate) {
        Double bal = repo.findStockBalanceByBranch(productid, companyid, DateUtil.addHours(toDate, 0, -1));
        if (bal == null) {
            return 0;
        }
        return bal;
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

    /**
     * @return the selectProduct
     */
    public Product getSelectProduct() {
        return selectProduct;
    }

    /**
     * @param selectProduct the selectProduct to set
     */
    public void setSelectProduct(Product selectProduct) {
        this.selectProduct = selectProduct;
    }
}
