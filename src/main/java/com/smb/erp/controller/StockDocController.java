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
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.PayTerms;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "stockDocController")
@ViewScoped
@ManagedBean
public class StockDocController extends AbstractController<BusDoc> implements ProductTransferable {

    BusDocRepository repo;

    @Autowired
    UserSession userSession;

    @Autowired
    SystemDefaultsController systemController;

    @Autowired
    BusDocInfoRepository docinfoRepo;

    @Autowired
    BusinessPartnerRepository partnerRepo;

    @Autowired
    ProductSearchController productSearchController;

    @Autowired
    ProductTransactionController productTransactionController;

    @Autowired
    TableKeyController keyCon;

    @Autowired
    BranchRepository branchRepo;

    @Autowired
    AccDocController accdocController;
    
    @Autowired
    PageAccessController pageController;

    DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    BusDocInfo docInfo;

    List<BusinessPartner> partnerList;

    private List<ProductTransaction> prodTransactions = new LinkedList<>();

    private ProductTransaction selectedTransaction;

    private boolean productTabDisabled = true;

    private List<ProductTransaction> salesPT;

    private List<ProductTransaction> purchasePT;

    private List<ProductTransaction> stockPT;

    private AccDoc accdoc;

    private Branch fromBranch;

    private Branch toBranch;

    //private Date docdate;
    @Autowired
    public StockDocController(BusDocRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusDoc.class, repo);
        this.repo = repo;
    }

    //http://localhost:8008/busdoc/busdoclist.xhtml?mode=l&docinfoid=1      ->test link
    @PostConstruct
    public void init() {
        //setSelected(new BusDoc());
        //getSelected().setCreatedon(new Date());

        systemController.getAsList("BusinessPartnerType");
        getProductSearchController().setProductTransferable(this);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");

        if (m != null) {
            if (m.equalsIgnoreCase("l")) {
                String docinfo = req.getParameter("docinfoid");
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                mode = DocumentTab.MODE.LIST;
            } else if (m.equalsIgnoreCase("n")) {   // new business document 
                String docinfo = req.getParameter("docinfoid");
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                BusDoc doc = new BusDoc();
                doc.setDocdate(new Date());
                doc.setCreatedon(doc.getDocdate());
                doc.setBusdocinfo(docInfo);
                setSelected(doc);
                mode = DocumentTab.MODE.NEW;
                doc.setProductTransactions(getProdTransactions());

                PayTerms pt = new PayTerms();
                doc.setPaytermsid(pt);

            } else {        //edit mode=e
                String docno = req.getParameter("docno");
                if (docno != null) {
                    setSelected(repo.getOne(docno));
                    prepareBusDocForEdit();
                }
                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
            }
            pageController.hasAccess(docInfo);
        }
    }

    public void prepareBusDocForEdit() {
        docInfo = getSelected().getBusdocinfo();
        salesPT = getSelected().getProductTransactions().stream().filter(t -> t.getLinesold() > 0).collect(Collectors.toList());
        if (salesPT != null && salesPT.size() > 0) {
            fromBranch = salesPT.get(0).getBranch();
        }
        setProdTransactions(salesPT);

        purchasePT = getSelected().getProductTransactions().stream().filter(t -> t.getLinereceived() > 0).collect(Collectors.toList());
        if (purchasePT != null && purchasePT.size() > 0) {
            toBranch = purchasePT.get(0).getBranch();
        }

        if (getSelected().getPaytermsid() == null) {
            PayTerms pt = new PayTerms();
            getSelected().setPaytermsid(pt);
        }
        getSelected().refreshTotal();
    }

    @Override
    public List<BusDoc> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            items = repo.findByBusDocByPrefix(docInfo.getPrefix());
        }
        return items;
    }

    public void refresh() {
        items = null;
        getItems();
    }

    //public Double getTotalAmount(){
    //    System.out.println("prodTransaction: getTotalAmount: " + getSelected().getProductTransactions());
    //    Double t = 0.0;
    //    if(getProdTransactions()!=null){
    //        t = getSelected().getTotalamount();
    //    }
    //    System.out.println("prodTransaction: getTotalAmount: " + t);
    //    return t;
    //}
    public void save() {
        getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            //getSelected().setEmp1();
            getSelected().setCreatedon(new Date());
        }
        getSelected().setProductTransactions(getProdTransactions());
        getSelected().setUpdatedon(new Date());
        //getSelected().setDocdate(getDocdate());
        for (ProductTransaction pt : getProdTransactions()) {
            pt.setTransdate(getSelected().getDocdate());
            pt.setCreatedon(getSelected().getCreatedon());
            pt.setUpdatedon(getSelected().getUpdatedon());
            pt.setBusdoc(getSelected());
            pt.setFcunitprice(pt.getLinefcunitprice());
            pt.setLineunitprice(pt.getLinecost());
            pt.setUnitprice(pt.getLinecost());
            pt.setTransactiontype(getSelected().getBusdocinfo().getTransactiontype());
            pt.setBranch(getSelected().getBranch());
            //pt.calculateActualQtyFromLineQty();
            pt.refreshTotals();
        }
        getSelected().refreshTotal();
        //getSelected().setBranch(userSession.getLoggedInBranch());      //to be changed later
        repo.save(getSelected());

        accdocController.createBusDocJV(getSelected());
        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
        mode = DocumentTab.MODE.EDIT;
    }

    public void saveStx() {
        getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            //getSelected().setEmp1();
            getSelected().setCreatedon(new Date());
        }

        //prepare product transactions for quantity received
        List<ProductTransaction> toTransactions = new LinkedList();

        //default all quantity is going to Sold (Out)
        for (ProductTransaction pt : getProdTransactions()) {
            pt.setTransdate(getSelected().getDocdate());
            pt.setCreatedon(getSelected().getCreatedon());
            pt.setUpdatedon(getSelected().getUpdatedon());
            pt.setBusdoc(getSelected());
            pt.setFcunitprice(pt.getLinefcunitprice());
            pt.setSold(pt.getLinesold());
            pt.setReceived(0.0);
            pt.setLinereceived(0.0);
            pt.setLineunitprice(pt.getLinecost());
            pt.setUnitprice(pt.getLinecost());
            pt.setCost(pt.getLinecost());
            pt.setTransactiontype(getSelected().getBusdocinfo().getTransactiontype());
            pt.setBranch(fromBranch);
            //pt.calculateActualQtyFromLineQty();
            pt.refreshTotals();

            ProductTransaction tpt = new ProductTransaction();
            tpt.setProduct(pt.getProduct());
            tpt.setUnit(pt.getProduct().getUnit());
            tpt.setCustomizedname(pt.getCustomizedname());
            tpt.setTransdate(pt.getTransdate());
            tpt.setCreatedon(pt.getCreatedon());
            tpt.setUpdatedon(pt.getUpdatedon());
            tpt.setBusdoc(getSelected());
            tpt.setLinereceived(pt.getLinesold());
            tpt.setReceived(pt.getLinesold());
            tpt.setSold(0.0);
            tpt.setLinesold(0.0);
            tpt.setFcunitprice(pt.getLinefcunitprice());
            tpt.setLineunitprice(pt.getLinecost());
            tpt.setUnitprice(pt.getLinecost());
            tpt.setCost(pt.getCost());
            tpt.setLinecost(pt.getLinecost());
            tpt.setTransactiontype(getSelected().getBusdocinfo().getTransactiontype());
            tpt.setBranch(toBranch);
            tpt.refreshTotals();
            toTransactions.add(tpt);
        }
        getProdTransactions().addAll(toTransactions);
        getSelected().setProductTransactions(getProdTransactions());
        getSelected().setUpdatedon(new Date());
        getSelected().refreshTotal();
        getSelected().setBranch(userSession.getLoggedInBranch());      //to be changed later
        repo.save(getSelected());

        accdocController.createBusDocJV(getSelected());
        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
        mode = DocumentTab.MODE.EDIT;
        prepareBusDocForEdit();
    }

    public void refreshTotal(ProductTransaction pt) {
        pt.refreshTotals();
        pt.getBusdoc().refreshTotal();
    }

    public void deleteTransactions() {
        if (getSelectedTransaction() != null) {
            getProdTransactions().remove(getSelectedTransaction());

            //getSelectedTransaction().removeAllFromprodtransaction();
            //getSelectedTransaction().removeAllToprodtransaction();
            setSelectedTransaction(null);
        }
        getSelected().refreshTotal();
    }

    @Override
    public void transfer(List<Product> products) {
        //System.out.println("Product_Transfer: " + products);
        if (products != null) {
            for (Product p : products) {
                getProdTransactions().add(convert(p, getSelected().getBusdocinfo().getDoctype(), getSelected().getBusdocinfo().getTransactiontype()));
            }
        }
    }

    public ProductTransaction convert(Product prod, String doctype, String transtype) {
        ProductTransaction pt = new ProductTransaction();
        pt.setProduct(prod);
        pt.setUnit(prod.getUnit());
        pt.setTransactiontype(transtype);
        pt.setCustomizedname(prod.getProductname());
        pt.setLineqty(1.0);
        pt.setBusdoc(getSelected());
        pt.setBranch(getSelected().getBranch());

        if (getSelected().getBusdocinfo().getPrefix().equalsIgnoreCase("SHS")) {
            pt.setLinefccost(0.0);
            pt.setLinefcunitprice(0.0);
            pt.setLinesold(1.0);
            pt.setLinereceived(0.0);
        } else {
            pt.setLinefccost(0.0);
            pt.setLinefcunitprice(0.0);
            pt.setLinesold(0.0);
            pt.setLinereceived(1.0);
        }
        return pt;
    }

    public String getTabTitle() {
        if (mode == DocumentTab.MODE.NEW) {
            return "New - " + getSelected().getBusdocinfo().getDocname();
        } else {
            return "Edit - " + getSelected().getDocno();
        }
    }

    public BusDocInfo getDocInfo() {
        return docInfo;
    }

    public List<String> findAsList(String propertyname) {
        return systemController.getAsList(propertyname);
    }

    public void refreshHistoryPanel() {
        salesPT = null;
        getSalesProductTransactions();
        purchasePT = null;
        getPurchaseProductTransactions();
    }

    public List<ProductTransaction> getSalesProductTransactions() {
        if (getSelectedTransaction() != null) {
            if (salesPT == null) {
                salesPT = productTransactionController.getSalesTransactions(getSelectedTransaction().getProduct().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid());
                salesPT.forEach(f -> f.setReference("Sales History"));
            }
        }
        return salesPT;
    }

    public List<ProductTransaction> getPurchaseProductTransactions() {
        if (getSelectedTransaction() != null) {
            if (purchasePT == null) {
                purchasePT = productTransactionController.getPurchaseTransactions(getSelectedTransaction().getProduct().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid());
                purchasePT.forEach(f -> f.setReference("Purchase History"));
            }
        }
        return purchasePT;
    }

    public List<ProductTransaction> getStockBalances() {
        if (getSelectedTransaction() != null) {
            if (stockPT == null) {
                stockPT = productTransactionController.getStockBalances(getSelectedTransaction().getProduct().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid());
                //purchasePT.forEach(f -> f.setReference("Purchase History"));
            }
        }
        return stockPT;
    }

    /**
     * @return the prodTransactions
     */
    public List<ProductTransaction> getProdTransactions() {
        return prodTransactions;
    }

    /**
     * @param prodTransactions the prodTransactions to set
     */
    public void setProdTransactions(List<ProductTransaction> prodTransactions) {
        this.prodTransactions = prodTransactions;
    }

    /**
     * @return the selectedTransaction
     */
    public ProductTransaction getSelectedTransaction() {
        return selectedTransaction;
    }

    /**
     * @param selectedTransaction the selectedTransaction to set
     */
    public void setSelectedTransaction(ProductTransaction selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    /**
     * @return the productSearchController
     */
    public ProductSearchController getProductSearchController() {
        return productSearchController;
    }

    public void docdateChange(SelectEvent event) {
        //FacesContext facesContext = FacesContext.getCurrentInstance();
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
        getSelected().setDocdate(DateUtil.setCurrentTime((Date) event.getObject()));
    }

    public void openJVViewer() {
        setAccdoc(accdocController.prepareJVViewwer(getSelected()));
        System.out.println("openJVViewer: " + getAccdoc());
    }

    public void print() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //facesContext.getExternalContext().getRequestMap().put("doc", getSelected());
        //Flash flash = facesContext.getExternalContext().getFlash();
        //flash.clear();
        //flash.setKeepMessages(true);
        //flash.setRedirect(true);
        //flash.put("doc", getSelected());
        //facesContext.getExternalContext().redirect("docviewer.xhtml?doc="+getSelected().getDocno());
        facesContext.getExternalContext().redirect(getReport());
    }

    public String getReport() {
        //reportGen.setBusdoc(getSelected());
        //reportGen.saveReport();
        //return reportGen.getReport();
        return "/viewer/doc/" + getSelected().getDocno();
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
     * @return the fromBranch
     */
    public Branch getFromBranch() {
        return fromBranch;
    }

    /**
     * @param fromBranch the fromBranch to set
     */
    public void setFromBranch(Branch fromBranch) {
        this.fromBranch = fromBranch;
    }

    /**
     * @return the toBranch
     */
    public Branch getToBranch() {
        return toBranch;
    }

    /**
     * @param toBranch the toBranch to set
     */
    public void setToBranch(Branch toBranch) {
        this.toBranch = toBranch;
    }
}
