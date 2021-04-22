/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.AccDoc;
import com.smb.erp.entity.Branch;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.DocStatus;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.entity.StockCountingBatch;
import com.smb.erp.entity.StockCountingBusDoc;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.StockCountingBusDocRepository;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.service.TransactionImportService;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.ReflectionUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "stockCountingBusDocController")
@ViewScoped
public class StockCountingBusDocController extends AbstractController<StockCountingBusDoc> implements ProductTransferable {

    StockCountingBusDocRepository repo;

    @Autowired
    UserSession userSession;

    @Autowired
    JVViewerController jvViewer;

    @Autowired
    BusDocInfoRepository docinfoRepo;

    @Autowired
    BusDocRepository docRepo;

    @Autowired
    SystemDefaultsController systemController;

    @Autowired
    AccDocController accdocController;

    @Autowired
    ProductTransactionController productTransactionController;

    @Autowired
    ProductSearchController productSearchController;

    @Autowired
    TransactionImportService importService;

    @Autowired
    PageAccessController pageController;

    @Autowired
    TableKeyController keyCon;

    private List<StockCountingBatch> countingBatch = new LinkedList<>();

    private BusDocInfo docInfo;

    private BusDoc processedDoc;

    private Date fromDate = DateUtil.startOfYear(new Date());

    private Date toDate = new Date();

    private StockCountingBatch selectedTransaction;

    private ProductTransaction selectedProcessedTransaction;

    private List<ProductTransaction> salesPT;

    private List<ProductTransaction> purchasePT;

    private List<ProductTransaction> stockPT;

    private List<StockCountingBatch> countedRepo = new LinkedList();

    private AccDoc accdoc;

    private String success = "0";

    private Branch selectedBranch;

    @Autowired
    public StockCountingBusDocController(StockCountingBusDocRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(StockCountingBusDoc.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        //setSelected(new BusDoc());
        //getSelected().setCreatedon(new Date());
        getProductSearchController().setProductTransferable(this);
        importService.setClz(ProductTransaction.class);
        importService.setTransactionList(new LinkedList<>());

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");

        System.out.println("StockCountingBusDocController: " + m);
        if (m != null) {
            if (m.equalsIgnoreCase("l")) {
                String docinfo = req.getParameter("docinfoid");
                setDocInfo(docinfoRepo.getOne(Integer.parseInt(docinfo)));
                mode = DocumentTab.MODE.LIST;
            } else if (m.equalsIgnoreCase("n")) {   // new business document 
                String docinfo = req.getParameter("docinfoid");
                setDocInfo(docinfoRepo.getOne(Integer.parseInt(docinfo)));
                StockCountingBusDoc doc = new StockCountingBusDoc();
                doc.setDocdate(new Date());
                doc.setProcessdate(doc.getDocdate());
                doc.setCreatedon(doc.getDocdate());
                doc.setBusdocinfo(getDocInfo());
                doc.setBranch(userSession.getLoggedInBranch());
                doc.setEmp(userSession.getLoggedInEmp());
                setSelected(doc);
                mode = DocumentTab.MODE.NEW;
                doc.setStockCountingBatch(getCountingBatch());
                setSelectedBranch(userSession.getLoggedInBranch());
            } else {        //edit mode=e
                String docno = req.getParameter("docno");
                if (docno != null) {
                    setSelected(repo.getOne(docno));
                    System.out.println("EditMode_branch: " + getSelected().getBranch());
                    setSelectedBranch(getSelected().getBranch());
                    setDocInfo(getSelected().getBusdocinfo());
                    setCountingBatch(getSelected().getStockCountingBatch());

                    Optional<BusDoc> op = docRepo.findById(docno);
                    if (op.isPresent()) {
                        setProcessedDoc(op.get());
                        getProcessedDoc().getProductTransactions();
                        //System.out.println("Processed_Doc: " + getProcessedDoc().getProductTransactions());
                        getProcessedDoc().refreshTotal();
                    }
                }
                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
                prepareCountedReport();
            }
            pageController.hasAccess(docInfo);
        }
        showGrowl();
    }

    public void showGrowl() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String labelAddedWithSuccess = (String) ec.getSessionMap().remove("addedWithSuccess");
        //System.out.println("showGrowl: " + labelAddedWithSuccess);
        //if the flag on context is true show the growl message
        if (labelAddedWithSuccess != null && labelAddedWithSuccess.equals("true")) {
            JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
            PrimeFaces.current().ajax().update(":growl");
        }
    }

    @Override
    public List<StockCountingBusDoc> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            items = repo.findByBusDocByPrefix(getDocInfo().getPrefix());
        }
        return items;
    }

    public void refresh() {
        items = null;
        getItems();
    }

    public void save() {
        getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            //getSelected().setEmp1();
            getSelected().setCreatedon(new Date());
        }
        getSelected().setUpdatedon(new Date());
        getSelected().setBranch(getSelectedBranch());
        //getSelected().setDocdate(getDocdate());
        for (StockCountingBatch pt : getCountingBatch()) {
            //pt.setCurrentstock(productTransactionController.findStockBalanceByBranch(pt.getProductid().getProductid(), userSession.getLoggedInBranch().getBranchid(), new Date()));
            if (pt.getCost() == 0) {
                updateCost(pt);
            }
            if (pt.getCurrentstock() == 0) {
                pt.setCurrentstock(findStockBalance(pt.getProductid()));
            }
            pt.setEnteredon(getSelected().getDocdate());
            pt.setCountedon(getSelected().getCreatedon());
            pt.setBusdoc(getSelected());
            pt.setCountyear(getSelected().getCountyear());
            pt.setCountedbyempno(userSession.getLoggedInEmp());
            pt.setEnteredbyempno(userSession.getLoggedInEmp());
            pt.setCountedinbranchno(getSelectedBranch());
            pt.setCountedunitno(pt.getProductid().getUnit());
        }
        getSelected().setStockCountingBatch(countingBatch);
        System.out.println("StockCounting_Save: " + getSelected().getDocdate() + " => " + getSelected().getProcessdate());
        repo.save(getSelected());

        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
        mode = DocumentTab.MODE.EDIT;
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("addedWithSuccess", "true");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
        } catch (IOException ex) {
            Logger.getLogger(BusDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stockcounting_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("stockcounting.xhtml");
    }

    public void new_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(getDocInfo().getDocediturl() + "?mode=n&docinfoid=" + getDocInfo().getBdinfoid());
    }

    public void edit_in_tab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No Document selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
    }

    @Override
    public void transfer(List<Product> products) {
        if (products != null) {
            for (Product p : products) {
                getCountingBatch().add(convert(p, getSelected().getBusdocinfo().getDoctype(), getSelected().getBusdocinfo().getTransactiontype()));
            }
        }
    }

    public String getTabTitle() {
        if (mode == DocumentTab.MODE.NEW) {
            return "New - " + getSelected().getBusdocinfo().getDocname();
        } else {
            return "Edit - " + getSelected().getDocno();
        }
    }

    public void deleteTransactions() {
        if (getSelectedTransaction() != null) {
            getCountingBatch().remove(getSelectedTransaction());

            //getSelectedTransaction().removeAllFromprodtransaction();
            //getSelectedTransaction().removeAllToprodtransaction();
            setSelectedTransaction(null);
            prepareCountedReport();
        }
        //getSelected().refreshTotal();
    }

    @Transactional
    public void processCounting() {
        //delete the document if it already exists
        System.out.println("processCounting: " + getProcessedDoc());
        if (getProcessedDoc() != null) {
            docRepo.deleteById(getSelected().getDocno());
            //accdocController.deleteBusDocVoucher(getSelected().getDocno());
        }

        //System.out.println("processCounting...");
        BusDoc doc = new BusDoc();
        doc.setBusdocinfo(getSelected().getBusdocinfo());
        doc.setBranch(getSelectedBranch());
        doc.setCreatedon(getSelected().getProcessdate());
        doc.setUpdatedon(getSelected().getProcessdate());
        doc.setDocdate(getSelected().getProcessdate());
        doc.setDescription(getSelected().getDescription());
        doc.setDocno(getSelected().getDocno());
        doc.setDocstatus(DocStatus.COMPLETED.getValue());
        doc.setEmp1(getSelected().getEmp());
        doc.setProductTransactions(new LinkedList<>());
        if (getCountedRepo() != null) {
            for (StockCountingBatch b : getCountedRepo()) {
                ProductTransaction pt = new ProductTransaction();
                pt.setTransdate(getSelected().getProcessdate());
                pt.setUpdatedon(doc.getUpdatedon());
                pt.setCreatedon(doc.getCreatedon());
                pt.setProduct(b.getProductid());
                pt.setUnit(b.getProductid().getUnit());
                pt.setTransactiontype(doc.getBusdocinfo().getTransactiontype());
                pt.setCustomizedname(b.getProductid().getProductname());
                pt.setBusdoc(doc);
                pt.setBranch(getSelectedBranch());
                pt.setFccost(b.getCost());
                pt.setFcunitprice(b.getCost());
                pt.setLinefccost(b.getCost());
                pt.setLinefcunitprice(b.getCost());
                pt.setUnitprice(b.getCost());
                pt.setLinecost(b.getCost());
                pt.setCost(b.getCost());
                if (b.getDifference() > 0) {
                    pt.setReceived(b.getDifference());
                    pt.setSold(0.0);
                    pt.setLinereceived(b.getDifference());
                    pt.setLinesold(0.0);
                } else if (b.getDifference() < 0) {
                    pt.setSold(b.getDifference());
                    pt.setReceived(0.0);
                    pt.setLinesold(b.getDifference());
                    pt.setLinereceived(0.0);
                }
                doc.addProductTransactions(pt);
            }
        }

        System.out.println("SaveCounting: " + doc.getBranch());
        docRepo.save(doc);

        System.out.println("UpdateDocStatus: " + getSelected().getBranch());
        getSelected().setDocstatus(DocStatus.COMPLETED.getValue());
        repo.save(getSelected());
        //System.out.println("processCounting...saved");
        setProcessedDoc(doc);
        getProcessedDoc().refreshTotal();

        accdocController.createBusDocJV(doc);
        JsfUtil.addSuccessMessage(getSelected().getDocno() + " is processed");
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("addedWithSuccess", "true");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
        } catch (IOException ex) {
            Logger.getLogger(BusDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer getSpinnerMinYear() {
        return DateUtil.getYear(new Date()) - 1;
    }

    public Integer getSpinnerMaxYear() {
        return DateUtil.getYear(new Date());
    }

    public StockCountingBatch convert(Product prod, String doctype, String transtype) {
        StockCountingBatch pt = new StockCountingBatch();
        pt.setProductid(prod);
        pt.setCountedunitno(prod.getUnit());
        pt.setCount(1.0);
        pt.setBusdoc(getSelected());
        pt.setCountedinbranchno(getSelected().getBranch());     //to be changed later
        updateCost(pt);
        return pt;
    }

    public void updateCost(StockCountingBatch pt) {
        ProductTransaction p = productTransactionController.findLastCostPurchaseOrAdjustment(pt.getProductid().getProductid(), getSelected().getProcessdate());
        if (p != null) {
            pt.setCost(p.getCost());
        }
    }

    public void refreshTotal(StockCountingBatch pt) {
        pt.getBusdoc().refreshTotal();
    }

    public void refreshHistoryPanel() {
        setSalesPT(null);
        getSalesProductTransactions();
        setPurchasePT(null);
        getPurchaseProductTransactions();
    }

    public List<ProductTransaction> getSalesProductTransactions() {
        if (getSelectedTransaction() != null) {
            if (getSalesPT() == null) {
                setSalesPT(productTransactionController.getSalesTransactions(getSelectedTransaction().getProductid().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid()));
                getSalesPT().forEach(f -> f.setReference("Sales History"));
            }
        }
        return getSalesPT();
    }

    public List<ProductTransaction> getPurchaseProductTransactions() {
        if (getSelectedTransaction() != null) {
            if (getPurchasePT() == null) {
                setPurchasePT(productTransactionController.getPurchaseTransactions(getSelectedTransaction().getProductid().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid()));
                getPurchasePT().forEach(f -> f.setReference("Purchase History"));
            }
        }
        return getPurchasePT();
    }

    public List<ProductTransaction> getStockBalances() {
        if (getSelectedTransaction() != null) {
            if (getStockPT() == null) {
                setStockPT(productTransactionController.getStockBalances(getSelectedTransaction().getProductid().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid()));
                //purchasePT.forEach(f -> f.setReference("Purchase History"));
            }
        }
        return getStockPT();
    }

    public Double findStockBalance(Product product) {
        return productTransactionController.findStockBalanceByBranch(product.getProductid(), getSelected().getBranch().getBranchid(), getSelected().getDocdate());
    }

    public Double findTotalCount(Product product) {
        return getSelected().getStockCountingBatch().stream().filter(o -> o.getProductid().getProductid() == product.getProductid()).mapToDouble(o -> o.getCount()).sum();
    }

    public void prepareCountedReport() {
        List<StockCountingBatch> dlist = findDistinctByProduct();
        countedRepo = new LinkedList<>();
        if (dlist != null) {
            for (StockCountingBatch scb : dlist) {
                StockCountingBatch scb2 = new StockCountingBatch();
                scb2.setProductid(scb.getProductid());
                scb2.setCountedunitno(scb.getCountedunitno());
                scb2.setCount(findTotalCount(scb2.getProductid()));
                scb2.setCurrentstock(findStockBalance(scb2.getProductid()));
                scb2.setDifference(scb2.getCount() - scb2.getCurrentstock());
                if (scb.getCost() > 0.0) {
                    scb2.setCost(scb.getCost());
                } else {
                    scb2.setCost(productTransactionController.findLastCostPurchaseOrAdjustment(scb2.getProductid().getProductid()));
                }
                countedRepo.add(scb2);
            }
        }
        dlist = null;
    }

    public List<StockCountingBatch> findDistinctByProduct() {
        return getSelected().getStockCountingBatch().stream().filter(distinctByProductId(p -> p.getProductid())).collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByProductId(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        importService.handleFileUpload(event, StockCountingBatch.class);
        //importService.processData();
    }

    public String value(Object bean, String property) {
        try {
            return ReflectionUtil.readProperty(bean, property).toString();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TransactionImportService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TransactionImportService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TransactionImportService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void importTransactions() {
        if (getTransferList() != null) {
            for (ProductTransaction trans : getTransferList()) {
                StockCountingBatch sb = convert(trans.getProduct(), getSelected().getBusdocinfo().getDoctype(), getSelected().getBusdocinfo().getTransactiontype());
                sb.setCount(trans.getLinereceived());
                sb.setCost(trans.getLinecost());
                getCountingBatch().add(sb);
            }
        }
    }

    public void openJVViewer() {
        if (getProcessedDoc() == null) {
            JsfUtil.addErrorMessage("Counting needs to be processed to view JV");
            return;
        }
        setAccdoc(accdocController.prepareJVViewwer(getProcessedDoc()));
        jvViewer.setAccdoc(getAccdoc());
    }

    /**
     * @return the productSearchController
     */
    public ProductSearchController getProductSearchController() {
        return productSearchController;
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
     * @return the docInfo
     */
    public BusDocInfo getDocInfo() {
        return docInfo;
    }

    /**
     * @param docInfo the docInfo to set
     */
    public void setDocInfo(BusDocInfo docInfo) {
        this.docInfo = docInfo;
    }

    /**
     * @return the selectedTransaction
     */
    public StockCountingBatch getSelectedTransaction() {
        return selectedTransaction;
    }

    /**
     * @param selectedTransaction the selectedTransaction to set
     */
    public void setSelectedTransaction(StockCountingBatch selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    /**
     * @return the salesPT
     */
    public List<ProductTransaction> getSalesPT() {
        return salesPT;
    }

    /**
     * @param salesPT the salesPT to set
     */
    public void setSalesPT(List<ProductTransaction> salesPT) {
        this.salesPT = salesPT;
    }

    /**
     * @return the purchasePT
     */
    public List<ProductTransaction> getPurchasePT() {
        return purchasePT;
    }

    /**
     * @param purchasePT the purchasePT to set
     */
    public void setPurchasePT(List<ProductTransaction> purchasePT) {
        this.purchasePT = purchasePT;
    }

    /**
     * @return the stockPT
     */
    public List<ProductTransaction> getStockPT() {
        return stockPT;
    }

    /**
     * @param stockPT the stockPT to set
     */
    public void setStockPT(List<ProductTransaction> stockPT) {
        this.stockPT = stockPT;
    }

    /**
     * @return the countedRepo
     */
    public List<StockCountingBatch> getCountedRepo() {
        return countedRepo;
    }

    /**
     * @param countedRepo the countedRepo to set
     */
    public void setCountedRepo(List<StockCountingBatch> countedRepo) {
        this.countedRepo = countedRepo;
    }

    /**
     * @return the processedDoc
     */
    public BusDoc getProcessedDoc() {
        return processedDoc;
    }

    /**
     * @param processedDoc the processedDoc to set
     */
    public void setProcessedDoc(BusDoc processedDoc) {
        this.processedDoc = processedDoc;
    }

    /**
     * @return the selectedProcessedTransaction
     */
    public ProductTransaction getSelectedProcessedTransaction() {
        return selectedProcessedTransaction;
    }

    /**
     * @param selectedProcessedTransaction the selectedProcessedTransaction to
     * set
     */
    public void setSelectedProcessedTransaction(ProductTransaction selectedProcessedTransaction) {
        this.selectedProcessedTransaction = selectedProcessedTransaction;
    }

    public List<ProductTransaction> getTransferList() {
        //System.out.println("getTransferList: " + importService.getTransactionList());
        return importService.getTransactionList();
    }

    public List<String> getTransferHeader() {
        return importService.getHeader();
    }

    public Class getTransClass() {
        return ProductTransaction.class;
    }

    /**
     * @return the accdoc
     */
    public AccDoc getAccdoc() {
        return accdoc;
    }

    /**
     * @param accdoc the accdoc to set
     */
    public void setAccdoc(AccDoc accdoc) {
        this.accdoc = accdoc;
    }

    /**
     * @return the selectedBranch
     */
    public Branch getSelectedBranch() {
        return selectedBranch;
    }

    /**
     * @param selectedBranch the selectedBranch to set
     */
    public void setSelectedBranch(Branch selectedBranch) {
        //System.out.println("setSelectedBranch: " + selectedBranch);
        if (selectedBranch != null) {
            this.selectedBranch = selectedBranch;
        }
    }
}
