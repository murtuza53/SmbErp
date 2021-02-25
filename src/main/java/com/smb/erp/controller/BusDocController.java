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
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.BusinessPartner;
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
import com.smb.erp.repo.ProductTransactionExecutionRepository;
import com.smb.erp.repo.VatCategoryRepository;
import com.smb.erp.repo.VatMappingRepository;
import com.smb.erp.repo.VatSalesPurchaseTypeRepository;
import com.smb.erp.service.ImportTransferable;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "busDocController")
@ViewScoped
@ManagedBean
public class BusDocController extends AbstractController<BusDoc> implements ProductTransferable, ImportTransferable {

    BusDocRepository repo;

    @Autowired
    UserSession userSession;

    @Autowired
    JVViewerController jvViewer;

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
    CompanyRepository companyRepo;

    @Autowired
    BranchRepository branchRepo;

    @Autowired
    CountryController countryCon;

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
    private TransactionImportService importService;

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

    private MenuModel printButtonModel;

    private String success = "0";

    private UploadedFile file;

    private String quickProductCode;

    //private Date docdate;
    @Autowired
    public BusDocController(BusDocRepository repo) {
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
        getImportService().setClz(ProductTransaction.class);
        getImportService().setTransactionList(new LinkedList<>());

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");

        if (m != null) {
            if (m.equalsIgnoreCase("l")) {
                String docinfo = req.getParameter("docinfoid");
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                mode = DocumentTab.MODE.LIST;
                getImportService().setClz(BusDoc.class);
                getImportService().setTransactionList(new LinkedList<>());
            } else if (m.equalsIgnoreCase("n")) {   // new business document 
                String docinfo = req.getParameter("docinfoid");
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                BusDoc doc = new BusDoc();
                doc.setDocdate(new Date());
                doc.setCreatedon(doc.getDocdate());
                doc.setBusdocinfo(docInfo);
                doc.setCurrency(countryCon.findCountryDefault());
                setSelected(doc);
                mode = DocumentTab.MODE.NEW;
                doc.setProductTransactions(getProdTransactions());

                PayTerms pt = new PayTerms();
                doc.setPaytermsid(pt);

                cashRegController.setCashRegister(getSelected().getBusdocinfo().getCashregiserid());
                //docdate = getSelected().getDocdate();
                //setupPrintMenu();

                //set default businesspartner
                doc.setBusinesspartner(docInfo.getBusinesspartner());

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
                    getSelected().refreshTotal();
                }
                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
                setupPrintMenu();
            }
        }
        showGrowl();
    }

    public void setupPrintMenu() {
        if (getSelected().getBusdocinfo().getReportid() != null
                && getSelected().getBusdocinfo().getReportid().size() > 0) {
            setPrintButtonModel(new DefaultMenuModel());
            getSelected().getBusdocinfo().getReportid().forEach((report) -> {
                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(report.getReportname());
                item.setUrl("../viewer/doc/" + report.getReportid() + "/" + getSelected().getDocno());
                getPrintButtonModel().getElements().add(item);
            });
        }
    }

    public boolean isPrintDisabled() {
        return getPrintButtonModel() == null || getPrintButtonModel().getElements().isEmpty();
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
        if (cashRegController.calculateTotal() > 0) {
            getSelected().setAccounts(cashRegController.getCashRegisterAccounts());
        }
        //getSelected().setDocdate(getDocdate());
        for (ProductTransaction pt : getProdTransactions()) {
            pt.setTransdate(getSelected().getDocdate());
            pt.setCreatedon(getSelected().getCreatedon());
            pt.setUpdatedon(getSelected().getUpdatedon());
            pt.setBusdoc(getSelected());
            pt.setFcunitprice(pt.getLinefcunitprice());
            pt.setUnitprice(pt.getLineunitprice());
            pt.setExchangerate(getSelected().getRate());
            pt.setTransactiontype(getSelected().getBusdocinfo().getTransactiontype());
            if (pt.getFccost() == 0 || pt.getCost() == 0) {
                updateCost(pt);
            }
            //pt.calculateActualQtyFromLineQty();
            pt.refreshTotals();
            //if (getSelected().getBusdocinfo().getDoctype().equalsIgnoreCase("Sales")) {
            //    pt.setLinesold(pt.getLineqty());
            //    pt.setSold(pt.getLinesold());
            //} else {
            //    pt.setLinereceived(pt.getLineqty());
            //    pt.setLinereceived(pt.getLinereceived());
            //}

            if (pt.getToprodtransaction() != null) {
                for (ProductTransactionExecution pte : pt.getToprodtransaction()) {
                    if (pte.getToprodtransid().getProdtransid() == pt.getProdtransid()) {
                        pte.setExecutionqty(pt.getLineqty());
                        pte.setCreatedon(new Date());
                    }
                }
            }
        }
        getSelected().refreshTotal();
        //getSelected().setCompany(companyRepo.getOne(1));    //to be commented
        getSelected().setBranch(userSession.getLoggedInBranch());      //to be changed later
        repo.save(getSelected());

        accdocController.createBusDocJV(getSelected());
        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
        mode = DocumentTab.MODE.EDIT;
        //init();
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

    public void refreshTotal(ProductTransaction pt) {
        pt.refreshTotals();
        pt.getBusdoc().refreshTotal();
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

    public void updateCost(List<ProductTransaction> ptlist) {
        if (ptlist != null) {
            for (ProductTransaction pt : ptlist) {
                updateCost(pt);
            }
        }
    }

    public void updateCost(ProductTransaction pt) {
        ProductTransaction p = productTransactionController.findLastCostPurchaseOrAdjustment(pt.getProduct().getProductid(), getSelected().getDocdate());
        if (p != null) {
            pt.setCost(p.getCost());
            pt.setFccost(p.getFccost());
        }
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

    public void new_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(getDocInfo().getDocediturl() + "?mode=n&docinfoid=" + docInfo.getBdinfoid());
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
    public void transferData(List list) {

    }

    public void searchAndTransfer() {
        if (quickProductCode == null || quickProductCode.trim().isEmpty()) {
            JsfUtil.addErrorMessage("Quick Scan cannot be empty");
            return;
        }
        Product p = productSearchController.findByProductidOrSupplierCodeOrBarcodes(quickProductCode);
        if (p == null) {
            JsfUtil.addErrorMessage("No Product found for " + quickProductCode);
            return;
        }
        ProductTransaction pt = convert(p, getSelected().getBusdocinfo().getDoctype(), getSelected().getBusdocinfo().getTransactiontype());
        updateCost(pt);
        getProdTransactions().add(pt);
    }

    @Override
    public void transfer(List<Product> products) {
        //System.out.println("Product_Transfer: " + products);
        if (products != null) {
            for (Product p : products) {
                ProductTransaction pt = convert(p, getSelected().getBusdocinfo().getDoctype(), getSelected().getBusdocinfo().getTransactiontype());
                updateCost(pt);
                getProdTransactions().add(pt);
            }
        }
    }

    public void transferProductTransaction() {
        if (selectedFromProductTransactions != null) {
            for (ProductTransaction pt : selectedFromProductTransactions) {
                ProductTransaction prodtra = cloneFrom(pt);
                prodtra.refreshTotals();
                getProdTransactions().add(prodtra);
            }
        }
        getSelected().refreshTotal();
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
        pt.setLineqty(getSelected().getRate());
        pt.setBusdoc(getSelected());
        pt.setBranch(userSession.getLoggedInBranch());     //to be changed later

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

    public ProductTransaction cloneFrom(ProductTransaction pt) {
        ProductTransaction clone = new ProductTransaction();

        clone.setProduct(pt.getProduct());
        clone.setUnit(pt.getUnit());
        clone.setTransactiontype(pt.getTransactiontype());
        clone.setCustomizedname(pt.getCustomizedname());
        clone.setLineqty(pt.getBalance());
        clone.setBusdoc(getSelected());
        clone.setBranch(userSession.getLoggedInBranch());     //to be changed later
        clone.setVatsptypeid(pt.getVatsptypeid());

        clone.setVatcategoryid(pt.getVatcategoryid());

        clone.setLinecost(pt.getLinecost());
        clone.setLinefcunitprice(pt.getLinefcunitprice());
        clone.setLinesold(pt.getLinesold());
        clone.setLinereceived(pt.getLinereceived());
        clone.setLinecost(pt.getLinecost());
        clone.setLinefccost(pt.getLinefccost());

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
            if (getSelected().getCurrency() == null) {
                getSelected().setCurrency(getSelected().getBusinesspartner().getCountry());
                getSelected().setRate(getSelected().getCurrency().getRate());
            }
        }
        refreshConvertFromDocumentList();
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
        //getSalesProductTransactions();
        purchasePT = null;
        //getPurchaseProductTransactions();
        stockPT = null;
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
        //System.out.println("getPurchaseProductTransactions: " + getSelectedTransaction() + " => " + purchasePT);
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
                        DateUtil.addHours(getSelected().getDocdate(), 0, -1), userSession.getLoggedInCompany().getCompanyid());
                //purchasePT.forEach(f -> f.setReference("Purchase History"));
                /*System.out.print("getStockBalances: ");
                for (ProductTransaction pt : stockPT) {
                    System.out.print(pt.getBranch().getAbbreviation() + "=>" + pt.getCumulative()+ "\t");
                }
                System.out.println("");*/
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
        businessPartnerSelected();
        return productTabDisabled;
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
        jvViewer.setAccdoc(accdoc);
    }

    public void refreshConvertFromDocumentList() {
        selectedConvertFromDocument = null;
        if (getConvertFromDocument() != null && getConvertFromDocument().size() > 0) {
            selectedConvertFromDocument = getConvertFromDocument().get(0);
        }

        //System.out.println(selectedConvertFromDocument.getPrefix() + " => refreshConvertFromDocumentList: " + getSelected().getBusinesspartner());
        //selectedFromDocument = null;
        if (getSelected().getBusinesspartner() != null && selectedConvertFromDocument != null) {
            convertFromDocumentList = repo.findByBusDocByPrefixAndBusinessPartner(selectedConvertFromDocument.getPrefix(), getSelected().getBusinesspartner().getPartnerid());
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
                List<ProductTransactionExecution> frompte = pteRepo.findByFromProductTransaction(pt.getProdtransid());
                pt.setFromprodtransaction(frompte);
                if (pt.getBalance().doubleValue() == 0) {
                    //remove pt from document if flagged

                }
            }
        }
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

    public void saveDocument() {
        getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            //getSelected().setEmp1();
            getSelected().setCreatedon(new Date());
        }
        getSelected().setProductTransactions(getProdTransactions());
        getSelected().setUpdatedon(new Date());
        if (cashRegController.calculateTotal() > 0) {
            getSelected().setAccounts(cashRegController.getCashRegisterAccounts());
        }
        //getSelected().setDocdate(getDocdate());
        for (ProductTransaction pt : getProdTransactions()) {
            pt.setTransdate(getSelected().getDocdate());
            pt.setCreatedon(getSelected().getCreatedon());
            pt.setUpdatedon(getSelected().getUpdatedon());
            pt.setBusdoc(getSelected());
            pt.setFcunitprice(pt.getLinefcunitprice());
            pt.setUnitprice(pt.getLineunitprice());
            pt.setExchangerate(getSelected().getRate());
            pt.setTransactiontype(getSelected().getBusdocinfo().getTransactiontype());
            if (pt.getFccost() == 0 || pt.getCost() == 0) {
                updateCost(pt);
            }
            //pt.calculateActualQtyFromLineQty();
            pt.refreshTotals();
            //if (getSelected().getBusdocinfo().getDoctype().equalsIgnoreCase("Sales")) {
            //    pt.setLinesold(pt.getLineqty());
            //    pt.setSold(pt.getLinesold());
            //} else {
            //    pt.setLinereceived(pt.getLineqty());
            //    pt.setLinereceived(pt.getLinereceived());
            //}

            if (pt.getToprodtransaction() != null) {
                for (ProductTransactionExecution pte : pt.getToprodtransaction()) {
                    if (pte.getToprodtransid().getProdtransid() == pt.getProdtransid()) {
                        pte.setExecutionqty(pt.getLineqty());
                        pte.setCreatedon(new Date());
                    }
                }
            }
        }
        getSelected().refreshTotal();
        //getSelected().setCompany(companyRepo.getOne(1));    //to be commented
        getSelected().setBranch(userSession.getLoggedInBranch());      //to be changed later
        repo.save(getSelected());

        accdocController.createBusDocJV(getSelected());
    }

    public void importDocuments() {
        System.out.println("importDocuments:....");
        //importService.IMPORT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
        //importService.IMPORT_DATE_FORMAT.setLenient(false);
        if (importService.getTransactionList() != null) {
            for (BusDoc doc : (List<BusDoc>) importService.getTransactionList()) {
                setSelected(doc);
                doc.setBusdocinfo(docInfo);
                if (doc.getDocno() == null || doc.getDocno().trim().length() == 0) {
                    doc.setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
                }
                //doc.setPaytermsid(new PayTerms());
                doc.setBusdocinfo(getDocInfo());
                setProdTransactions(new LinkedList<>());
                //System.out.println(doc);
                Product p = productSearchController.findByProductidOrSupplierCodeOrBarcodes(doc.getExtra1());
                ProductTransaction pt = convert(p, getSelected().getBusdocinfo().getDoctype(), getSelected().getBusdocinfo().getTransactiontype());
                pt.setLineqty(doc.getRate());
                pt.setLinefccost(doc.getExtra11());
                pt.setLinefcunitprice(doc.getExtra11());
                pt.calculateActualQtyFromLineQty();
                getProdTransactions().add(pt);
                doc.refreshTotal();
                saveDocument();
            }
            JsfUtil.addSuccessMessage(importService.getTransactionList().size() + " documents imported successfuly");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        //importService.IMPORT_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        getImportService().handleFileUpload(event, BusDoc.class);
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
                Product prod = trans.getProduct();
                String transtype = getSelected().getBusdocinfo().getTransactiontype();
                String doctype = getSelected().getBusdocinfo().getDoctype();

                trans.setUnit(prod.getUnit());
                trans.setTransactiontype(transtype);
                trans.setCustomizedname(prod.getProductname());
                trans.setBusdoc(getSelected());
                trans.setBranch(userSession.getLoggedInBranch());     //to be changed later

                //find vat sales or purchase type
                VatBusinessRegister vbr = getSelected().getBusinesspartner().getBusinessRegisters().get(0);
                List<VatMapping> vatmaps = vatmappingRepo.findByVatSalesPurchaseType(vbr.getVataccounttypeid().getVataccounttypeid(),
                        vbr.getVatcategoryid().getVatcategoryid(), prod.getVatregisterid().getProducttype(), doctype);
                //System.out.println("VAT_MAPPING: " + vatmaps);
                if (vatmaps != null && vatmaps.size() > 0) {
                    VatMapping vm = vatmaps.get(0);
                    trans.setVatsptypeid(vm.getVatsptypeid());
                } else {
                    if (doctype.equalsIgnoreCase("Sales")) {
                        Optional<VatSalesPurchaseType> vt = vatsalespurRepo.findById(1l);
                        if (vt.isPresent()) {
                            trans.setVatsptypeid(vt.get());
                        }
                    } else {

                    }
                }

                if (prod.getVatregisterid() != null) {
                    trans.setVatcategoryid(prod.getVatregisterid().getVatcategoryid());
                }
                getProdTransactions().add(trans);
            }
        }
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

    public List<ProductTransaction> getTransferList() {
        //System.out.println("getTransferList: " + importService.getTransactionList());
        return getImportService().getTransactionList();
    }

    public List<String> getTransferHeader() {
        return getImportService().getHeader();
    }

    public Class getTransClass() {
        return ProductTransaction.class;
    }

    /**
     * @return the importService
     */
    public TransactionImportService getImportService() {
        return importService;
    }

    /**
     * @param importService the importService to set
     */
    public void setImportService(TransactionImportService importService) {
        this.importService = importService;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * @return the quickProductCode
     */
    public String getQuickProductCode() {
        return quickProductCode;
    }

    /**
     * @param quickProductCode the quickProductCode to set
     */
    public void setQuickProductCode(String quickProductCode) {
        this.quickProductCode = quickProductCode;
    }

}
