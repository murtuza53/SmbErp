/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.AccDoc;
import com.smb.erp.entity.Account;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocExpense;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.Country;
import com.smb.erp.entity.PayTerms;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.entity.ProductTransactionExecution;
import com.smb.erp.entity.VatBusinessRegister;
import com.smb.erp.entity.VatCategory;
import com.smb.erp.entity.VatMapping;
import com.smb.erp.entity.VatSalesPurchaseType;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.CompanyRepository;
import com.smb.erp.repo.CountryRepository;
import com.smb.erp.repo.ProductTransactionExecutionRepository;
import com.smb.erp.repo.VatCategoryRepository;
import com.smb.erp.repo.VatMappingRepository;
import com.smb.erp.repo.VatSalesPurchaseTypeRepository;
import com.smb.erp.rest.JasperPrintReportGenerator;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.PrimeFaces;
import org.primefaces.component.filedownload.FileDownloadActionListener;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "foreignPurchaseController")
@ViewScoped
public class ForeignPurchaseController extends AbstractController<BusDoc> implements ProductTransferable {

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
    CountryRepository countryRepo;

    @Autowired
    ProductTransactionController productTransactionController;

    @Autowired
    TableKeyController keyCon;

    @Autowired
    CompanyRepository companyRepo;

    @Autowired
    BranchRepository branchRepo;

    @Autowired
    AccDocController accdocController;

    @Autowired
    ProductTransactionExecutionRepository pteRepo;

    @Autowired
    VatCategoryRepository vatcatRepo;

    @Autowired
    VatMappingRepository vatmappingRepo;

    @Autowired
    VatSalesPurchaseTypeRepository vatsalespurRepo;

    @Autowired
    VatSalesPurchaseTypeController vatsalespurchaseController;

    @Autowired
    CashRegisterController cashRegController;

    @Autowired
    JasperPrintReportGenerator reportGenerator;

    //@Autowired
    //BusDocExpenseController expenseCont;
    DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    BusDocInfo docInfo;

    List<BusinessPartner> partnerList;

    private List<ProductTransaction> prodTransactions = new LinkedList<>();

    private ProductTransaction selectedTransaction;

    private boolean productTabDisabled = true;

    private List<ProductTransaction> salesPT;

    private List<ProductTransaction> purchasePT;

    private List<ProductTransaction> stockPT;

    private BusDocInfo selectedConvertFromDocument;

    private List<BusDoc> convertFromDocumentList;

    private BusDoc selectedFromDocument;

    private List<ProductTransaction> selectedFromProductTransactions;

    private AccDoc accdoc;

    private HashMap<String, BusDoc> docmap = new HashMap<String, BusDoc>();     //key will be FPO_No, values are FPI

    private BusDocInfo fpiInfo;

    private BusDocExpense selectedExpense;

    private MenuModel printButtonModel;

    //private Date docdate;
    @Autowired
    public ForeignPurchaseController(BusDocRepository repo) {
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

                cashRegController.setCashRegister(getSelected().getBusdocinfo().getCashregiserid());
                //docdate = getSelected().getDocdate();
            } else {        //edit mode=e
                String docno = req.getParameter("docno");
                if (docno != null) {
                    setSelected(repo.getOne(docno));
                    docInfo = getSelected().getBusdocinfo();
                    setProdTransactions(getSelected().getProductTransactions());

                    //setup cash register for any document like cash memo
                    cashRegController.setupBusDoc(getSelected());

                    if (getSelected().getPaytermsid() == null) {
                        PayTerms pt = new PayTerms();
                        getSelected().setPaytermsid(pt);
                    }

                    //now get all FPI
                    List<BusDoc> docs = repo.findByBusDocByRefno(getSelected().getDocno());
                    for (BusDoc doc : docs) {
                        docmap.put(doc.getDocno(), doc);
                    }
                    reloadProductTransactions();
                    getSelected().refreshTotal();
                }
                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
                setupPrintMenu();
                getRatio();
            }
        }
        fpiInfo = docinfoRepo.getOne(13);
        System.out.println("PostConstruct_FPI_INFO: " + fpiInfo);
        //expenseCont.setBusdoc(getSelected());
        showGrowl();
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
    @Transactional
    public void save() {
        getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            //getSelected().setEmp1();
            getSelected().setCreatedon(new Date());
            getSelected().setEmp1(userSession.getLoggedInEmp());
        }

        if (getSelected().getEmp1() == null) {
            getSelected().setEmp1(userSession.getLoggedInEmp());
        }

        //getSelected().setProductTransactions(getProdTransactions());
        getSelected().setUpdatedon(new Date());
        if (cashRegController.calculateTotal() > 0) {
            getSelected().setAccounts(cashRegController.getCashRegisterAccounts());
        }
        //getSelected().setBranch(userSession.getLoggedInBranch());
        getSelected().setProductTransactions(new LinkedList<>());
        getSelected().setExtra11(getRatio());
        //first save FPC
        repo.save(getSelected());

        for (String key : docmap.keySet()) {
            BusDoc doc = docmap.get(key);
            doc.setRefno(getSelected().getDocno());
            if (doc.getDocno() == null) {
                doc.setDocno(keyCon.getDocNo(doc.getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            }
            doc.setDocdate(getSelected().getDocdate());
            doc.setCreatedon(getSelected().getCreatedon());
            doc.setUpdatedon(getSelected().getUpdatedon());
            doc.setBranch(getSelected().getBranch());
            doc.setCurrency(getSelected().getCurrency());
            doc.setRate(getSelected().getRate());
            doc.setEmp1(getSelected().getEmp1());
            doc.setExtra11(getRatio());
            for (ProductTransaction pt : doc.getProductTransactions()) {
                pt.setTransdate(getSelected().getDocdate());
                pt.setCreatedon(getSelected().getCreatedon());
                pt.setUpdatedon(getSelected().getUpdatedon());
                pt.setExchangerate(getRatio());
                pt.setBranch(getSelected().getBranch());
                pt.setFcunitprice(pt.getLinefcunitprice());
                pt.setLinefccost(pt.getLinefcunitprice());
                pt.setFccost(pt.getLinefcunitprice());
                pt.setLineunitprice(pt.getLinefcunitprice() * pt.getExchangerate());
                pt.setUnitprice(pt.getLineunitprice());
                pt.setCost(pt.getLineunitprice());
                pt.setLinecost(pt.getLineunitprice());
                pt.setTransactiontype(doc.getBusdocinfo().getTransactiontype());
                pt.refreshTotals();
                //pt.setBusdoc(doc);
                System.out.println("pt.getToprodtransaction: " + pt.getToprodtransaction());
                if (pt.getToprodtransaction() != null) {
                    for (ProductTransactionExecution pte : pt.getToprodtransaction()) {
                        if (pte.getToprodtransid().getProdtransid() == pt.getProdtransid()) {
                            pte.setExecutionqty(pt.getLineqty());
                            pte.setCreatedon(new Date());
                        }
                    }
                }
            }
            doc.refreshTotal();
            //doc.setBranch(userSession.getLoggedInBranch());      //to be changed later
            repo.save(doc);
            accdocController.createBusDocPurchaseJV(doc);
        }

        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
        mode = DocumentTab.MODE.EDIT;
        setupPrintMenu();
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("addedWithSuccess", "true");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
        } catch (IOException ex) {
            Logger.getLogger(BusDocController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    public void reloadProductTransactions() {
        prodTransactions.clear();
        for (String key : docmap.keySet()) {
            BusDoc doc = docmap.get(key);
            doc.refreshTotal();
            prodTransactions.addAll(doc.getProductTransactions());
        }
        getSelected().setProductTransactions(prodTransactions);
    }

    public List<BusDoc> getFpiList() {
        return new LinkedList(docmap.values());

    }

    public void refreshTotal(ProductTransaction pt) {
        pt.refreshTotals();
        pt.getBusdoc().refreshTotal();
    }

    public Double getRatio() {
        double invoiceTotalFc = 0.0;
        System.out.println("getRatio:isEmpty: " + docmap.isEmpty());
        if (!docmap.isEmpty()) {
            for (BusDoc doc : docmap.values()) {
                invoiceTotalFc = invoiceTotalFc + doc.getTotalamount();
            }
        }

        double totalExpenseFc = 0.0;
        double totalExpenseLc = 0.0;
        if (getSelected().getExpenses() != null) {
            for (BusDocExpense exp : getSelected().getExpenses()) {
                if (exp.getCountry().getDefcountry()) {
                    totalExpenseLc = totalExpenseLc + exp.getAmountlc();
                } else {
                    totalExpenseFc = totalExpenseFc + exp.getAmountfc();
                }
            }
        }

        double totalInvoicePlusExpFc = invoiceTotalFc + totalExpenseFc;
        double totalFcToLc = totalInvoicePlusExpFc * getSelected().getRate();
        double ratio = (totalFcToLc + totalExpenseLc) / invoiceTotalFc;     //Total Invoice & All Cost in LC / Material value in FC
        if (Double.isNaN(ratio) || Double.isInfinite(ratio)) {
            System.out.println("Ratio is NaN or Infinity");
            return getSelected().getRate();
        }
        return ratio;
    }

    public List<String> getCostingExpenses() {
        return systemController.getAsList("CostingExpense");
    }

    public Double getTotalExpenseInLc() {
        if (getSelected().getExpenses() == null || getSelected().getExpenses().isEmpty()) {
            return 0.0;
        }
        return getSelected().getExpenses().stream().mapToDouble(BusDocExpense::getTotalAmountInLC).sum();
    }

    public void newExpense() {
        BusDocExpense exp = new BusDocExpense();
        //System.out.println("newExpense: Step 1");
        Country defcon = countryRepo.findCountryDefault();
        //System.out.println("newExpense: Step 2");
        if (defcon == null) {
            defcon = getSelected().getCurrency();
        }
        String defexptype = getCostingExpenses().get(0);
        exp.setExpensetype(defexptype);
        exp.setCountry(defcon);
        exp.setRate(defcon.getRate());
        //System.out.println("newExpense: Step 3");
        exp.setAccount(systemController.getDefaultAccount("DefCostingExpense"));
        getSelected().addBusDocExpense(exp);
        //System.out.println("newExpense: Step 4");
    }

    public void deletedExpense() {
        if (selectedExpense == null) {
            JsfUtil.addErrorMessage("No Expense selected to delete");
            return;
        }
        getSelected().removeBusDocExpense(selectedExpense);
        JsfUtil.addSuccessMessage("Selected expense deleted");
    }

    public Double calculateExpense(String currency) {
        //System.out.println("calculateExpense: " + currency);
        if (currency == null || getSelected().getExpenses() == null) {
            return 0.0;
        }
        double total = 0.0;
        //for (BusDocExpense ex : getSelected().getExpenses()) {
        //    System.out.println(ex);
        //    if (currency.equalsIgnoreCase(ex.getCountry().getCurrencysym())) {
        //        total = total + ex.getAmount();
        //    }
        //}
        //double total = getSelected().getExpenses().stream().filter(o -> o.getCountry().getCountrysym().equalsIgnoreCase(currency)).mapToDouble(o -> o.getAmount()).sum();
        //System.out.println("calculateExpense_Total: " + total);
        return getSelected().getExpenses().stream().filter(o -> o.getCountry().getCurrencysym().equalsIgnoreCase(currency)).mapToDouble(o -> o.getAmount()).sum();
    }

    public void deleteTransactions() {
        if (getSelectedTransaction() != null) {
            getProdTransactions().remove(getSelectedTransaction());
            BusDoc doc = getSelectedTransaction().getBusdoc();
            doc.removeProductTransactions(selectedTransaction);

            setSelectedTransaction(null);
        }
        getSelected().refreshTotal();
    }

    public void new_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editbusdoc.xhtml?mode=n&docinfoid=" + docInfo.getBdinfoid());
    }

    public void edit_in_tab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No Document selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editbusdoc.xhtml?mode=e&docno=" + getSelected().getDocno());
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

    public void transferProductTransaction() {
        //if (selectedFromProductTransactions != null) {
        //    for (ProductTransaction pt : selectedFromProductTransactions) {
        //        ProductTransaction prodtra = cloneFrom(pt);
        //        prodtra.refreshTotals();
        //        getProdTransactions().add(prodtra);
        //    }
        //}
        //getSelected().refreshTotal();
        if (selectedFromProductTransactions == null || selectedFromProductTransactions.isEmpty()) {
            JsfUtil.addErrorMessage("No transactions selected");
            return;
        }
        for (ProductTransaction pt : selectedFromProductTransactions) {
            BusDoc doc = docmap.get(pt.getBusdoc().getDocno());
            if (doc == null) {
                doc = new BusDoc();
                doc.setDocdate(new Date());
                doc.setCreatedon(doc.getDocdate());
                doc.setBusinesspartner(pt.getBusdoc().getBusinesspartner());
                doc.setBusdocinfo(fpiInfo);
                //System.out.println("FPI_BusDocInfo: " + fpiInfo);
                PayTerms pay = new PayTerms();
                doc.setPaytermsid(pay);
                doc.setProductTransactions(new LinkedList());

                docmap.put(pt.getBusdoc().getDocno(), doc);
                System.out.println("Adding_New_FPI_For: " + pt.getBusdoc().getDocno());
            }
            ProductTransaction prodtra = cloneFrom(pt);
            //System.out.println("clone_getToprodtransaction" + prodtra.getToprodtransaction());
            prodtra.refreshTotals();
            doc.addProductTransactions(prodtra);
        }
        reloadProductTransactions();
        getSelected().refreshTotal();
        getRatio();
        JsfUtil.addSuccessMessage(selectedFromProductTransactions.size() + " trassactions added");
    }

    public List<BusinessPartner> getPartnerList() {
        if (partnerList == null) {
            if (docInfo.getDoctype().equalsIgnoreCase(BusDocType.SALES.getValue())) {
                partnerList = partnerRepo.findBusinessPartnerByTypeBySearchCriteria("", BusinessPartner.CUSTOMER);
            } else {
                partnerList = partnerRepo.findBusinessPartnerByTypeBySearchCriteria("", BusinessPartner.SUPPLIER);
            }
        }
        return partnerList;
    }

    public ProductTransaction convert(Product prod, String doctype, String transtype) {
        ProductTransaction pt = new ProductTransaction();
        pt.setProduct(prod);
        pt.setUnit(prod.getUnit());
        pt.setTransactiontype(transtype);
        pt.setCustomizedname(prod.getProductname());
        pt.setLineqty(1.0);
        pt.setBusdoc(getSelected());
        pt.setBranch(getSelected().getBranch());     //to be changed later

        //find vat sales or purchase type
        VatBusinessRegister vbr = getSelected().getBusinesspartner().getBusinessRegisters().get(0);
        List<VatMapping> vatmaps = vatmappingRepo.findByVatSalesPurchaseType(vbr.getVataccounttypeid().getVataccounttypeid(),
                vbr.getVatcategoryid().getVatcategoryid(), prod.getVatregisterid().getProducttype(), doctype);
        //System.out.println("VAT_MAPPING: " + vatmaps);
        if (vatmaps != null && vatmaps.size() > 0) {
            VatMapping vm = vatmaps.get(0);
            pt.setVatsptypeid(vm.getVatsptypeid());
        } else {
            if (doctype.equalsIgnoreCase("Sales")) {
                Optional<VatSalesPurchaseType> vt = vatsalespurRepo.findById(1l);
                if (vt.isPresent()) {
                    pt.setVatsptypeid(vt.get());
                }
            } else {

            }
        }

        if (prod.getVatregisterid() != null) {
            pt.setVatcategoryid(prod.getVatregisterid().getVatcategoryid());
        }
        if (doctype.equalsIgnoreCase("SALES")) {
            pt.setLinecost(0.0);
            pt.setLinefcunitprice(0.0);
            pt.setLinesold(1.0);
            pt.setLinereceived(0.0);
        } else {
            pt.setLinecost(0.0);
            pt.setLinefcunitprice(0.0);
            pt.setLinesold(0.0);
            pt.setLinereceived(1.0);
        }
        return pt;
    }

    public ProductTransaction cloneFrom(ProductTransaction pt) {
        ProductTransaction clone = new ProductTransaction();

        clone.setProduct(pt.getProduct());
        clone.setUnit(pt.getUnit());
        clone.setTransactiontype(pt.getTransactiontype());
        clone.setCustomizedname(pt.getCustomizedname());
        clone.setLineqty(pt.getBalance());
        clone.setBusdoc(getSelected());
        clone.setBranch(pt.getBranch());     //to be changed later
        clone.setVatsptypeid(pt.getVatsptypeid());

        clone.setVatcategoryid(pt.getVatcategoryid());

        clone.setLinecost(pt.getLinecost());
        clone.setLinefcunitprice(pt.getLinefcunitprice());
        clone.setLinesold(pt.getLinesold());
        clone.setLinereceived(pt.getLinereceived());
        clone.setLinecost(pt.getLinecost());

        ProductTransactionExecution pte = new ProductTransactionExecution();
        pte.setFromprodtransid(pt);
        pte.setCreatedon(new Date());
        pte.setToprodtransid(clone);
        pte.setExecutionqty(clone.getLineqty());

        clone.addToprodtransaction(pte);
        //pt.addToprodtransaction(pte);

        return clone;
    }

    public void businessPartnerSelected() {
        //System.out.println("businessPartnerSelected: " + getSelected());
        //System.out.println("businessPartnerSelected_BP: " + getSelected().getBusinesspartner());
        if (getSelected().getBusinesspartner() != null) {
            productTabDisabled = false;
        }
        refreshConvertFromDocumentList();
    }

    public void currencySelected() {
        if (getSelected().getCurrency() != null) {
            getSelected().setRate(getSelected().getCurrency().getRate());
        }
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

    /**
     * @return the productTabDisabled
     */
    public boolean isProductTabDisabled() {
        return false;
    }

    /**
     * @param productTabDisabled the productTabDisabled to set
     */
    public void setProductTabDisabled(boolean productTabDisabled) {
        this.productTabDisabled = productTabDisabled;
    }

    public List<VatCategory> getVatCatogories() {
        return vatcatRepo.findAll();
    }

    public List<VatSalesPurchaseType> getVatSalesPurchaseType() {
        return vatsalespurRepo.findByCategory(getSelected().getBusdocinfo().getDoctype());
    }

    public void updateVatCategory(ProductTransaction pt) {
        if (pt.getVatsptypeid().getTypename().equalsIgnoreCase("Zero Rated Domestic Sales")
                || pt.getVatsptypeid().getTypename().equalsIgnoreCase("Exempted Sales")
                || pt.getVatsptypeid().getTypename().equalsIgnoreCase("Zero Rated Domestic Purchase")
                || pt.getVatsptypeid().getTypename().equalsIgnoreCase("Out of Scope Purchase")) {
            pt.setVatcategoryid(getVatCatogories().get(0));
        } else {
            pt.setVatcategoryid(getVatCatogories().get(1));
        }
        refreshTotal(pt);
    }

    public void docdateChange(SelectEvent event) {
        //FacesContext facesContext = FacesContext.getCurrentInstance();
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
        getSelected().setDocdate(DateUtil.setCurrentTime((Date) event.getObject()));
    }

    public List<BusDocInfo> getConvertFromDocument() {
        return getSelected().getBusdocinfo().getConvertfrom();
    }

    public void openJVViewer() {
        accdoc = accdocController.prepareJVViewwer(getSelected());
        for (BusDoc doc : docmap.values()) {
            accdoc.getLedlines().addAll(accdocController.prepareBusDocJV(doc).getLedlines());
        }
    }

    public void refreshConvertFromDocumentList() {
        selectedConvertFromDocument = null;
        if (getConvertFromDocument() != null && getConvertFromDocument().size() > 0) {
            selectedConvertFromDocument = getConvertFromDocument().get(0);
        }

        //System.out.println(selectedConvertFromDocument.getPrefix() + " => refreshConvertFromDocumentList: " + getSelected().getBusinesspartner());
        //selectedFromDocument = null;
        if (selectedConvertFromDocument != null) {
            convertFromDocumentList = repo.findByBusDocByPrefix(selectedConvertFromDocument.getPrefix());
            System.out.println("refreshConvertFromDocumentList: " + convertFromDocumentList);
            if (convertFromDocumentList != null & convertFromDocumentList.size() > 0) {
                selectedFromDocument = convertFromDocumentList.get(0);
                refreshProdTransExe();
            }
        }
    }

    public void refreshProdTransExe() {
        if (getSelectedFromDocument() != null) {
            for (ProductTransaction pt : getSelectedFromDocument().getProductTransactions()) {
                if (pt.getLineqty() == 0) {
                    pt.setLinesold(pt.getSold());
                    pt.setLinereceived(pt.getReceived());
                }
                List<ProductTransactionExecution> frompte = pteRepo.findByFromProductTransaction(pt.getProdtransid());
                pt.setFromprodtransaction(frompte);
                if (pt.getBalance().doubleValue() == 0) {
                    //remove pt from document if flagged

                }
            }
        }
    }

    public void callDownloadReport(MenuActionEvent menuActionEvent) {
        //Create new action event
        final ActionEvent actionEvent = new ActionEvent(menuActionEvent.getComponent());

        //Create the value expression for the download listener
        //-> is executed when calling "processAction"!
        final FacesContext context = FacesContext.getCurrentInstance();
        final String exprStr = "#{foreignPurchaseController.exportPdf}";
        final ValueExpression valueExpr = context.getApplication()
                .getExpressionFactory()
                .createValueExpression(context.getELContext(), exprStr, StreamedContent.class);

        //Instantiate the download listener and indirectly call "downloadReport()"
        new FileDownloadActionListener(valueExpr, null, null)
                .processAction(actionEvent);
    }

    public StreamedContent getExportPdf() throws JRException {
        //System.out.println("BusDocController_getExportPdf: " + reportid);
        //PrintReport rep = null;
        //for (PrintReport report : getSelected().getBusdocinfo().getReportid()) {
        //    if (report.getReportid().toString().equalsIgnoreCase(reportid)) {
        //        rep = report;
        //    }
        //}
        //System.out.println("BusDocController_getExportPdf: " + rep);
        //getSelected().setCurrentPrintReport(rep);
        return reportGenerator.downloadFpcPdf(getSelected().getCurrentPrintReport(), getSelected(), getFpiList(), prodTransactions, getSelected().getReportTitle());
    }

    public void setupPrintMenu() {
        if (getSelected().getBusdocinfo().getReportid() != null
                && getSelected().getBusdocinfo().getReportid().size() > 0) {
            setPrintButtonModel(new DefaultMenuModel());
            getSelected().getBusdocinfo().getReportid().forEach((report) -> {
                DefaultMenuItem item = new DefaultMenuItem();
                item.setCommand("#{foreignPurchaseController.callDownloadReport}");
                item.setAjax(false);
                item.setValue(report.getReportname());
                getSelected().setCurrentPrintReport(report);
                //item.setUrl("../viewer/doc/" + report.getReportid() + "/" + getSelected().getDocno());
                getPrintButtonModel().getElements().add(item);
            });
        }
    }

    public boolean isPrintDisabled() {
        return getPrintButtonModel() == null || getPrintButtonModel().getElements().isEmpty();
    }

    /**
     * @return the selectedConvertFromDocument
     */
    public BusDocInfo getSelectedConvertFromDocument() {
        return selectedConvertFromDocument;
    }

    /**
     * @param selectedConvertFromDocument the selectedConvertFromDocument to set
     */
    public void setSelectedConvertFromDocument(BusDocInfo selectedConvertFromDocument) {
        this.selectedConvertFromDocument = selectedConvertFromDocument;
    }

    /**
     * @return the convertFromDocumentList
     */
    public List<BusDoc> getConvertFromDocumentList() {
        return convertFromDocumentList;
    }

    /**
     * @param convertFromDocumentList the convertFromDocumentList to set
     */
    public void setConvertFromDocumentList(List<BusDoc> convertFromDocumentList) {
        this.convertFromDocumentList = convertFromDocumentList;
    }

    /**
     * @return the selectedFromDocument
     */
    public BusDoc getSelectedFromDocument() {
        return selectedFromDocument;
    }

    /**
     * @param selectedFromDocument the selectedFromDocument to set
     */
    public void setSelectedFromDocument(BusDoc selectedFromDocument) {
        this.selectedFromDocument = selectedFromDocument;
    }

    /**
     * @return the selectedFromProductTransactions
     */
    public List<ProductTransaction> getSelectedFromProductTransactions() {
        return selectedFromProductTransactions;
    }

    /**
     * @param selectedFromProductTransactions the
     * selectedFromProductTransactions to set
     */
    public void setSelectedFromProductTransactions(List<ProductTransaction> selectedFromProductTransactions) {
        this.selectedFromProductTransactions = selectedFromProductTransactions;
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

    public List<Account> getCashRegisterAccounts() {
        return cashRegController.getCashRegisterAccounts();
    }

    public void refreshRegisterTotal() {
        cashRegController.calculateTotal();
    }

    /**
     * @return the selectedExpense
     */
    public BusDocExpense getSelectedExpense() {
        return selectedExpense;
    }

    /**
     * @param selectedExpense the selectedExpense to set
     */
    public void setSelectedExpense(BusDocExpense selectedExpense) {
        this.selectedExpense = selectedExpense;
    }

    /**
     * @return the printButtonModel
     */
    public MenuModel getPrintButtonModel() {
        return printButtonModel;
    }

    /**
     * @param printButtonModel the printButtonModel to set
     */
    public void setPrintButtonModel(MenuModel printButtonModel) {
        this.printButtonModel = printButtonModel;
    }

}
