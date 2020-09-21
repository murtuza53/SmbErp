/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Branch;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocTransactionType;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.Company;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.CompanyRepository;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.repo.ProductTransactionRepository;
import com.smb.erp.report.NativeQueryReportObject;
import com.smb.erp.report.NativeQueryTableModel;
import com.smb.erp.service.DatabaseService;
import com.smb.erp.util.DateUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

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
    ProductRepository productRepo;

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
        if (items == null) {
            if (getSelectProduct() == null) {
                items = new LinkedList<ProductTransaction>();
            } else {
                items = repo.findStockMovement(getSelectProduct().getProductid(), getFromDate(), getToDate(), BusDocTransactionType.INVENTORY_ACCOUNT.getValue());
            }
        }
        return items;
    }

    public void refresh() {
        items = null;
        getItems();
    }

    public List<Product> completeFilter(String criteria) {
        System.out.println("completeFilter: " + criteria);

        //return ejbFacade.findRange(0, 10, "firstName", "ASC",
        //        ejbFacade.createFilters(new String[]{"itsNo", "firstName"}, criteria));
        //Page<ItsMaster> page = itsFacade.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName")));
        return productRepo.findByCriteria(criteria);
    }

    public List<ProductTransaction> getSalesTransactions(long productid, Date toDate, int companyid) {
        List<ProductTransaction> ptlist = new LinkedList<>();

        List<BusDocInfo> bdiList = docinfoRepo.findBusDocInfoByDocType(BusDocType.SALES.getValue());

        //System.out.println("getSalesTransactions:");
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

    public List<ProductTransaction> getPurchaseTransactions(long productid, Date toDate, int companyid) {
        List<ProductTransaction> ptlist = new LinkedList<>();

        List<BusDocInfo> bdiList = docinfoRepo.findBusDocInfoByDocType(BusDocType.PURCHASE.getValue());

        //System.out.println("getPurchaseTransactions:");
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

    public List<ProductTransaction> getStockBalances(long productid, Date toDate, int companyid) {
        double total = 0.0;

        Company com = companyRepo.getOne(companyid);

        List<ProductTransaction> pts = new LinkedList<>();

        for (Branch br : com.getBranches()) {
            Double bal = repo.findStockBalanceByBranch(productid, br.getBranchid(), toDate);
            if (bal == null) {
                bal = 0.0;
            }
            ProductTransaction pt = new ProductTransaction();
            pt.setReference("Stock " + com.getAbbreviation());
            pt.setBranch(br);
            pt.setTransdate(toDate);
            pt.setBalance(bal);
            total = total + bal;

            pts.add(pt);
        }
        ProductTransaction pt = new ProductTransaction();
        pt.setReference("Stock " + com.getAbbreviation());
        pt.setBranch(new Branch("TOTAL", "Total"));
        pt.setTransdate(toDate);
        pt.setBalance(total);
        pts.add(pt);

        return pts;
    }

    public NativeQueryTableModel findStockBalances(String criteria, int companyid, Date todate) {
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
        for (Branch br : com.getBranches()) {
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
                + "WHERE pt.productid=p.productid AND p.inactive=0 AND (pt.transactiontype='Inventory_Account' or pt.transactiontype = 'Inventory_Only') "
                + "AND (TRIM(p.productid) LIKE '%" + criteria + "%' OR p.productname LIKE '%" + criteria + "%' OR p.supplierscode LIKE '%" + criteria + "%' "
                + "OR p.stockid LIKE '%" + criteria + "%' OR p.barcode1 LIKE '%" + criteria + "%' OR p.barcode2 LIKE '%" + criteria + "%') "
                + " AND pt.transdate<=:toDate "
                + "GROUP BY pt.productid";
        nqModel.addColumns(new String[]{"TOTAL"}, new Class[]{Double.class}, new String[]{""});

        System.out.println("findStockBalances: " + q);

        Query query = ds.createNativeQuery(q);
        query.setParameter("toDate", todate);
        nqModel.setData(NativeQueryReportObject.asReportObjectList(query.getResultList()));
        return nqModel;
    }

    public NativeQueryTableModel findStockBalances(long productid, int companyid, Date todate) {
        Company com = companyRepo.getOne(companyid);
        //String query = "SELECT OBJECT(p) FROM Product as p "
        //        + "WHERE (TRIM(p.productid) LIKE %:criteria% OR p.productname LIKE %:criteria% OR "
        //        + "p.supplierscode LIKE %:criteria% OR p.stockid LIKE %:criteria%) AND p.inactive=0 "
        //        + "ORDER BY p.productname asc";

        nqModel.resetModel();

        String q = "SELECT ";

        String qtotal = null;
        for (Branch br : com.getBranches()) {
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
                + "WHERE pt.productid=p.productid AND p.inactive=0 AND (pt.transactiontype='Inventory_Account' or pt.transactiontype = 'Inventory_Only') "
                + "AND pt.transdate<=:toDate AND p.productid=:productid "
                + "GROUP BY pt.productid";
        nqModel.addColumns(new String[]{"TOTAL"}, new Class[]{Double.class}, new String[]{""});
        //System.out.println("findStockBalances: " + q);

        Query query = ds.createNativeQuery(q);
        query.setParameter("toDate", todate).setParameter("productid", productid);
        nqModel.setData(NativeQueryReportObject.asReportObjectList(query.getResultList()));
        return nqModel;
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
