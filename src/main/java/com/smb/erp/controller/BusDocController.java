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
import com.smb.erp.entity.DocStatus;
import com.smb.erp.entity.PayTerms;
import com.smb.erp.entity.PrintReport;
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
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.repo.ProductTransactionExecutionRepository;
import com.smb.erp.repo.VatCategoryRepository;
import com.smb.erp.repo.VatMappingRepository;
import com.smb.erp.repo.VatSalesPurchaseTypeRepository;
import com.smb.erp.rest.JasperPrintReportGenerator;
import com.smb.erp.service.ImportTransferable;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.service.TransactionImportService;
import com.smb.erp.util.BeanPropertyUtil;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.ReflectionUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.primefaces.PrimeFaces;
import org.primefaces.component.filedownload.FileDownloadActionListener;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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
    ProductRepository prodRepo;

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
    TransactionImportService importService;

    @Autowired
    JasperPrintReportGenerator reportGenerator;

    @Autowired
    BusinessPartnerRepository businessRepo;

    @Autowired
    PageAccessController pageController;

    DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    BusDocInfo docInfo;

    List<BusinessPartner> partnerList;

    private List<ProductTransaction> prodTransactions = new LinkedList<>();

    private List<ProductTransaction> deletedTransactions = new LinkedList();

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

    private MenuModel convertToButtonModel;

    private String success = "0";

    private UploadedFile file;

    private String quickProductCode;

    private Integer progress;

    private List<VatCategory> vatCategories;

    private Product selectedProduct;
    
    private BusDoc copyDoc;

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
        vatCategories = vatcatRepo.findAll();

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
            } else if (m.equalsIgnoreCase("x")) {   // convert document
                String docno = req.getParameter("docno");
                if (docno != null) {
                    BusDoc doc = repo.getOne(docno);
                    doc.getBusdocinfo();

                    String docinfo = req.getParameter("docinfoid");
                    docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));

                    convertToDocument(doc, docInfo);
                }
            } else if (m.equalsIgnoreCase("c")) {   // convert document
                String docno = req.getParameter("docno");
                if (docno != null) {
                    BusDoc doc = repo.getOne(docno);
                    doc.getBusdocinfo();

                    String docinfo = req.getParameter("docinfoid");
                    docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));

                    copyDocument(doc);
                }
            } else if (m.equalsIgnoreCase("n")) {   // new business document 
                String docinfo = req.getParameter("docinfoid");
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                BusDoc doc = new BusDoc();
                doc.setDocdate(new Date());
                doc.setCreatedon(doc.getDocdate());
                doc.setBusdocinfo(docInfo);
                doc.setCurrency(countryCon.findCountryDefault());
                doc.setBranch(userSession.getLoggedInBranch());
                setSelected(doc);
                mode = DocumentTab.MODE.NEW;
                doc.setProductTransactions(getProdTransactions());

                PayTerms pt = new PayTerms();
                doc.setPaytermsid(pt);

                cashRegController.setCashRegister(getSelected().getBusdocinfo().getCashregiserid());
                //docdate = getSelected().getDocdate();
                //setupToolbar();

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
                setupToolbar();
                refreshConversionDocuments();
            }
            pageController.hasAccess(docInfo);
        }
        showGrowl();
    }

    public void setupToolbar() {
        setupPrintMenu();
        setupConvertToMenu();
    }

    public void copyDocument(){
        if(copyDoc==null){
            JsfUtil.addErrorMessage("Invalid copy document selection");
            return;
        }
        copyDocument(copyDoc, getDocInfo());
    }

    public void copyDocument(BusDoc fromDoc) {
        copyDocument(fromDoc, fromDoc.getBusdocinfo());
    }
    
    public void copyDocument(BusDoc fromDoc, BusDocInfo targetInfo){
        BusDoc newdoc = new BusDoc();
        setSelected(newdoc);
        newdoc.setDocdate(new Date());
        newdoc.setCreatedon(fromDoc.getDocdate());
        newdoc.setBusdocinfo(targetInfo);
        newdoc.setCurrency(fromDoc.getCurrency());
        newdoc.setBranch(userSession.getLoggedInBranch());
        newdoc.setBusinesspartner(fromDoc.getBusinesspartner());
        newdoc.setContactperson(fromDoc.getContactperson());
        newdoc.setDescription(fromDoc.getDescription());
        newdoc.setEmp1(fromDoc.getEmp1());
        newdoc.setEmp2(fromDoc.getEmp2());
        newdoc.setRate(fromDoc.getRate());
        mode = DocumentTab.MODE.NEW;

        for (ProductTransaction pt : fromDoc.getProductTransactions()) {
            ProductTransaction pt2 = convert(pt.getProduct(), newdoc.getBusdocinfo().getDoctype(), newdoc.getBusdocinfo().getTransactiontype());
            pt2.setLinefcunitprice(pt.getLinefcunitprice());
            pt2.setLineqty(pt.getBalance());
            pt2.setLinecost(pt.getLinecost());
            pt2.setLinefccost(pt.getLinefccost());
            pt2.setDiscount(pt.getDiscount());
            getProdTransactions().add(pt2);
        }

        newdoc.setProductTransactions(getProdTransactions());

        //setup cash register for any document like cash memo
        cashRegController.setupBusDoc(getSelected());

        if (getSelected().getPaytermsid() == null) {
            PayTerms pt = new PayTerms();
            getSelected().setPaytermsid(pt);
        }
        getSelected().refreshTotal();
    }

    public void makeCopy() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Document not selected to Copy");
            return;
        }

        BusDocInfo info = getSelected().getBusdocinfo();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + info.getDocediturl() + "?mode=c&docinfoid=" + info.getBdinfoid() + "&docno=" + getSelected().getDocno());
    }

    public void convertToDocument(BusDoc fromDoc, BusDocInfo toDocInfo) {
        BusDoc newdoc = new BusDoc();
        setSelected(newdoc);
        newdoc.setDocdate(new Date());
        newdoc.setCreatedon(fromDoc.getDocdate());
        newdoc.setBusdocinfo(toDocInfo);
        newdoc.setCurrency(fromDoc.getCurrency());
        newdoc.setBranch(userSession.getLoggedInBranch());
        newdoc.setBusinesspartner(fromDoc.getBusinesspartner());
        newdoc.setContactperson(fromDoc.getContactperson());
        newdoc.setDescription(fromDoc.getDescription());
        newdoc.setEmp1(fromDoc.getEmp1());
        newdoc.setEmp2(fromDoc.getEmp2());
        newdoc.setRate(fromDoc.getRate());
        mode = DocumentTab.MODE.NEW;

        setSelectedFromProductTransactions(fromDoc.getProductTransactions());
        transferProductTransaction();
        newdoc.setProductTransactions(getProdTransactions());

        //setup cash register for any document like cash memo
        cashRegController.setupBusDoc(getSelected());

        if (getSelected().getPaytermsid() == null) {
            PayTerms pt = new PayTerms();
            getSelected().setPaytermsid(pt);
        }
        getSelected().refreshTotal();
    }

    public void callDownloadReport(MenuActionEvent menuActionEvent) {
        //Create new action event
        final ActionEvent actionEvent = new ActionEvent(menuActionEvent.getComponent());

        PrintReport report = (PrintReport) menuActionEvent.getMenuItem().getValue();
        getSelected().setCurrentPrintReport(report);
        //Create the value expression for the download listener
        //-> is executed when calling "processAction"!
        final FacesContext context = FacesContext.getCurrentInstance();
        final String exprStr = "#{busDocController.exportPdf}";
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
        return reportGenerator.downloadPdf(getSelected().getCurrentPrintReport(), getSelected(), getSelected().getDocno());
    }

    public void setupPrintMenu() {
        if (getSelected().getBusdocinfo().getReportid() != null
                && getSelected().getBusdocinfo().getReportid().size() > 0) {
            setPrintButtonModel(new DefaultMenuModel());
            getSelected().getBusdocinfo().getReportid().forEach((report) -> {
                DefaultMenuItem item = new DefaultMenuItem();
                item.setCommand("#{busDocController.callDownloadReport}");
                item.setAjax(false);
                item.setTitle(report.getReportname());
                item.setValue(report);
                //item.setUrl("../viewer/doc/" + report.getReportid() + "/" + getSelected().getDocno());
                getPrintButtonModel().getElements().add(item);
            });
        }
    }

    public void setupConvertToMenu() {
        if (getSelected().getBusdocinfo().getConvertto() != null
                && getSelected().getBusdocinfo().getConvertto().size() > 0) {
            setConvertToButtonModel(new DefaultMenuModel());
            getDocInfo().getConvertto().forEach((info) -> {
                DefaultMenuItem item = new DefaultMenuItem();
                item.setCommand("#{busDocController.convertSelected}");
                item.setAjax(false);
                item.setTitle(info.getDocname());
                item.setValue(info);
                //item.setUrl("../viewer/doc/" + report.getReportid() + "/" + getSelected().getDocno());
                getConvertToButtonModel().getElements().add(item);
                //System.out.println("\t- " + item.getTitle());
            });
        }
    }

    public void convertSelected(MenuActionEvent menuActionEvent) throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Document not selected to convert");
            return;
        }
        String status = getSelected().getDocumentExecutionStatus();
        if (status.equalsIgnoreCase(DocStatus.COMPLETED.toString())) {
            JsfUtil.addErrorMessage(getSelected().getDocno() + " is already converted");
            return;
        }
        if (status.equalsIgnoreCase(DocStatus.CANCELLED.toString()) || status.equalsIgnoreCase(DocStatus.RETURNED.toString())) {
            JsfUtil.addErrorMessage(getSelected().getDocno() + " is " + status);
            return;
        }
        //Create new action event
        final ActionEvent actionEvent = new ActionEvent(menuActionEvent.getComponent());

        BusDocInfo info = (BusDocInfo) menuActionEvent.getMenuItem().getValue();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + info.getDocediturl() + "?mode=x&docinfoid=" + info.getBdinfoid() + "&docno=" + getSelected().getDocno());
        //String url = facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=x&docinfoid=" + info.getBdinfoid()+ "&docno=" + getSelected().getDocno();
        //PF.current().executeScript("window.open('"+url+"', '_newtab')");
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
    @Transactional
    public void save() {
        if (getSelected().getBusdocinfo().hasCashRegister()) {
            double paid = cashRegController.calculateTotal();
            double amount = getSelected().getGrandtotal();
            if (Math.abs(paid - amount) > 0.001) {
                JsfUtil.addErrorMessage("Payment doesn't match total value");
                return;
            }
        }
        //getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            //getSelected().setEmp1();
            getSelected().setCreatedon(new Date());
            getSelected().setEmp1(userSession.getLoggedInEmp());
        }

        if (getSelected().getEmp1() == null) {
            getSelected().setEmp1(userSession.getLoggedInEmp());
        }

        getSelected().setProductTransactions(getProdTransactions());
        getSelected().setUpdatedon(new Date());

        if (cashRegController.calculateTotal() > 0) {
            getSelected().setAccounts(cashRegController.getCashRegisterAccounts());
        }

        //first delete all ledgerline and their execution lines
        for (ProductTransaction pt : deletedTransactions) {
            productTransactionController.remove(pt);
        }

        //getSelected().setDocdate(getDocdate());
        for (ProductTransaction pt : getProdTransactions()) {
            pt.setTransdate(getSelected().getDocdate());
            pt.setCreatedon(getSelected().getCreatedon());
            pt.setUpdatedon(getSelected().getUpdatedon());
            pt.setBusdoc(getSelected());
            pt.setExchangerate(getSelected().getRate());
            //pt.setFcunitprice(pt.getLinefcunitprice());
            //pt.setUnitprice(pt.getLineunitprice());
            //pt.setTransactiontype(pt.getProduct().getProducttype().getName());
            pt.setTransactiontype(getSelected().getBusdocinfo().getTransactiontype());
            pt.refreshTotals();

            updateCost(pt);

            //pt.calculateActualQtyFromLineQty();
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
        setupToolbar();
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("addedWithSuccess", "true");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
        } catch (IOException ex) {
            Logger.getLogger(BusDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void save(BusDoc doc, boolean updateLineCost) {
        if (doc.getDocno() == null) {
            doc.setDocno(keyCon.getDocNo(doc.getBusdocinfo().getPrefix(), DateUtil.getYear(doc.getDocdate())));
        }

        //if (cashRegController.calculateTotal() > 0) {
        //    doc.setAccounts(cashRegController.getCashRegisterAccounts());
        //}
        //temporary for cashmemo without payment
        //if (doc.getBusdocinfo().hasCashRegister()) {
        //    doc.setAccounts(cashRegController.getCashRegisterWithCashPaid(docInfo, doc.getGrandtotal()));
        //}
        for (ProductTransaction pt : doc.getProductTransactions()) {
            pt.setTransdate(doc.getDocdate());
            pt.setCreatedon(doc.getCreatedon());
            pt.setUpdatedon(doc.getUpdatedon());
            pt.setBusdoc(doc);
            //pt.setFcunitprice(pt.getLinefcunitprice());
            //pt.setUnitprice(pt.getLineunitprice());
            //pt.setExchangerate(doc.getRate());
            //pt.setTransactiontype(pt.getProduct().getProducttype().getName());

            if (updateLineCost) {
                updateCost(pt);
            }

            //updateCost(pt, doc.getDocdate(), doc.getBusdocinfo().getDoctype());
            //pt.refreshTotals();
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
        doc.setBranch(userSession.getLoggedInBranch());      //to be changed later
        repo.save(doc);

        accdocController.createBusDocJV(doc);

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
        if (getSelected().getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.PURCHASE.getValue())) {
            pt.setFccost(pt.getFcunitprice());
            pt.setLinefccost(pt.getLinefcunitprice());
            pt.setCost(pt.getUnitprice());
            pt.setLinecost(pt.getLineunitprice());
            return;
        }
        if (pt.getFccost() == 0 || pt.getCost() == 0) {
            ProductTransaction p = productTransactionController.findLastCostPurchaseOrAdjustment(pt.getProduct().getProductid(), getSelected().getDocdate());
            if (p != null) {
                p.refreshTotals();
                pt.setFccost(p.getFccost());
                pt.setCost(p.getCost());
                pt.setLinefccost(p.getFccost());
                pt.setLinecost(p.getCost());
            }
        }
    }

    public void updateCost(ProductTransaction pt, Date date, String doctype) {
        if (doctype.equalsIgnoreCase(BusDocType.PURCHASE.getValue())) {
            pt.setFccost(pt.getFcunitprice());
            pt.setLinefccost(pt.getLinefcunitprice());
            pt.setCost(pt.getUnitprice());
            pt.setLinecost(pt.getLineunitprice());

            return;
        }
        if (pt.getFccost() == 0 || pt.getCost() == 0) {
            ProductTransaction p = productTransactionController.findLastCostPurchaseOrAdjustment(pt.getProduct().getProductid(), date);
            if (p != null) {
                pt.setFccost(p.getFccost());
                pt.setCost(p.getCost());
                pt.setLinefccost(p.getFccost() * pt.getQty());
                pt.setLinecost(p.getCost());
            }
        }
    }

    public void deleteTransactions() {
        if (getSelectedTransaction() != null) {
            //getSelectedTransaction().removeAllToprodtransaction();
            deletedTransactions.add(getSelectedTransaction());
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

    public void changeSearchedProduct(ProductTransaction pt) {
        pt.setCustomizedname(pt.getProduct().getProductname());
        setSelectedTransaction(pt);
    }

    public void quickSearchProductAndTranser() {
        if (selectedProduct == null) {
            JsfUtil.addErrorMessage("Invalid product selection");
            return;
        }
        ProductTransaction pt = convert(selectedProduct, getSelected().getBusdocinfo().getDoctype(), getSelected().getBusdocinfo().getTransactiontype());
        updateCost(pt);
        getProdTransactions().add(pt);
        selectedProduct = null;
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
        //ProductTransaction pt = convert(p, getSelected().getBusdocinfo().getDoctype(), p.getProducttype().getName());
        updateCost(pt);
        getProdTransactions().add(pt);
    }

    @Override
    public void transfer(List<Product> products) {
        //System.out.println("Product_Transfer: " + products);
        if (products != null) {
            for (Product p : products) {
                //ProductTransaction pt = convert(p, getSelected().getBusdocinfo().getDoctype(), p.getProducttype().getName());
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
        pt.setExchangerate(getSelected().getRate());
        pt.setQty(1.0);
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
        clone.setBranch(pt.getBranch());     //to be changed later
        clone.setExchangerate(pt.getExchangerate());
        clone.setVatsptypeid(pt.getVatsptypeid());

        clone.setVatcategoryid(pt.getVatcategoryid());

        clone.setLinefcunitprice(pt.getLinefcunitprice());
        //clone.setLinesold(pt.getLinesold());
        //clone.setLinereceived(pt.getLinereceived());
        clone.setLineqty(pt.getBalance());
        clone.setLinecost(pt.getLinecost());
        clone.setLinefccost(pt.getLinefccost());
        clone.setDiscount(pt.getDiscount());

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

    public void businessPartnerSelectedCM() {
        //System.out.println("businessPartnerSelected: " + getSelected());
        //System.out.println("businessPartnerSelected_BP: " + getSelected().getBusinesspartner());
        if (getSelected().getBusinesspartner() != null) {
            productTabDisabled = false;
            if (getSelected().getCurrency() == null) {
                getSelected().setCurrency(getSelected().getBusinesspartner().getCountry());
                getSelected().setRate(getSelected().getCurrency().getRate());
            }
            getSelected().setExtra4(getSelected().getBusinesspartner().getCurrentVatNo());
            getSelected().setExtra5(getSelected().getBusinesspartner().getFulladdress());
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
        //System.out.println("refreshHistoryPanel:start:" + new Date());
        salesPT = null;
        //getSalesProductTransactions();
        purchasePT = null;
        //getPurchaseProductTransactions();
        stockPT = null;
        //System.out.println("refreshHistoryPanel:end:" + new Date());
    }

    public List<ProductTransaction> getSalesProductTransactions() {
        //System.out.println("getSalesProductTransactions:start:" + new Date());
        if (getSelectedTransaction() != null) {
            if (salesPT == null) {
                salesPT = productTransactionController.getSalesTransactions(getSelectedTransaction().getProduct().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid());
                salesPT.forEach(f -> f.setReference("Sales History"));
            }
        }
        //System.out.println("getSalesProductTransactions:end:" + new Date());
        return salesPT;
    }

    public List<ProductTransaction> getPurchaseProductTransactions() {
        //System.out.println("getPurchaseProductTransactions:start:" + new Date());
        //System.out.println("getPurchaseProductTransactions: " + getSelectedTransaction() + " => " + purchasePT);
        if (getSelectedTransaction() != null) {
            if (purchasePT == null) {
                purchasePT = productTransactionController.getPurchaseTransactions(getSelectedTransaction().getProduct().getProductid(),
                        getSelected().getDocdate(), userSession.getLoggedInCompany().getCompanyid());
                purchasePT.forEach(f -> f.setReference("Purchase History"));
            }
        }

        /*if (purchasePT != null) {
            if(purchasePT.size()>0){
                JsonUtils.writeJson("D:\\ptlist.txt", purchasePT.get(0));
            }
        }*/
        //System.out.println("getPurchaseProductTransactions:end:" + new Date());
        return purchasePT;
    }

    public List<ProductTransaction> getStockBalances() {
        //System.out.println("getStockBalances:start:" + new Date());
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
        //System.out.println("getStockBalances:end:" + new Date());
        return stockPT;
    }

    public void currencySelected() {
        if (getSelected().getCurrency() != null) {
            getSelected().setRate(getSelected().getCurrency().getRate());
        }
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
        return vatCategories;
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
            //System.out.println("refreshConvertFromDocumentList: " + convertFromDocumentList);
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
                //System.out.println("refreshProdTransExe=>" + pt.getProdtransid() + " => " + frompte);
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
            pt.setTransactiontype(pt.getProduct().getProducttype().getName());
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

    public void handleFileUpload2(FileUploadEvent event) {
        getImportService().setClz(ProductTransaction.class);
        getImportService().setTransactionList(new LinkedList<>());
        //importService.IMPORT_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        getImportService().handleFileUpload(event, ProductTransaction.class);
        //importService.processData();
    }

    public void onComplete() {
    }

    public void importDocumentsWithTrans() {
        System.out.println("importDocumentsWithTrans:....");
        //importService.IMPORT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
        //importService.IMPORT_DATE_FORMAT.setLenient(false);
        //HashMap<String, Long> vatcat = new HashMap();
        //vatcat.put("VAT 5%", 2l);
        //vatcat.put("Vat 0%", 1l);

        //HashMap<String, Long> vatsalpur = new HashMap();
        //vatsalpur.put("VAT 5%", 1l);    //Standard Rated Domestic Sales
        //vatsalpur.put("Vat 0%", 2l);    //Zero Rated Domestic Sales
        HashMap<String, String> productReplace = new HashMap<>();
        productReplace.put("235460", "226146");
        productReplace.put("IMP003", "226148");
        productReplace.put("235807", "226154");
        productReplace.put("235753", "226147");
        productReplace.put("235961", "226159");
        productReplace.put("235971", "226160");
        productReplace.put("RENTMMS", "226161");
        productReplace.put("IMP001", "226149");
        productReplace.put("235337", "226155");
        productReplace.put("235981", "226164");
        productReplace.put("235980", "226165");
        productReplace.put("228923", "235257");
        productReplace.put("235982", "237015");
        productReplace.put("226165", "237016");
        productReplace.put("226148", "237017");
        productReplace.put("226146", "237018");
        productReplace.put("226164", "237019");
        productReplace.put("235979", "237026");
        productReplace.put("235820", "237038");
        productReplace.put("235983", "237039");
        productReplace.put("235909", "237040");
        productReplace.put("235914", "237041");
        productReplace.put("235978", "237025");
        productReplace.put("235977", "237024");
        productReplace.put("235976", "237023");
        productReplace.put("235975", "237022");
        productReplace.put("235974", "237021");
        productReplace.put("235973", "237020");
        productReplace.put("235962", "237006");
        productReplace.put("235963", "237007");
        productReplace.put("235964", "237008");
        productReplace.put("235965", "237009");
        productReplace.put("235966", "237010");
        productReplace.put("235967", "237011");
        productReplace.put("235968", "237012");

        Map<String, ProductTransaction> missingProd = Collections.synchronizedMap(new LinkedHashMap<>());

        Map<String, BusDoc> doctable = Collections.synchronizedMap(new LinkedHashMap<>());

        progress = 0;
        int count = 0;
        if (importService.getTransactionList() != null) {
            for (ProductTransaction trans : (List<ProductTransaction>) importService.getTransactionList()) {
                if (trans.getProduct() == null) {
                    String rep = productReplace.get(trans.getExtra1().trim());
                    if (rep == null) {
                        rep = "-1";
                    }
                    Optional<Product> prod = prodRepo.findById(Long.parseLong(rep));
                    if (prod.isPresent()) {
                        trans.setProduct(prod.get());
                        System.out.println("----------REPLACE PRODUCT----------");
                        System.out.println("   " + trans.getExtra1() + " => " + trans.getProduct().getProductid() + "   ");
                        System.out.println("------------------------------------");
                    } else {
                        System.out.println("----------INVALID PRODUCT----------");
                        System.out.println("          " + trans.getExtra1() + "          ");
                        System.out.println("------------------------------------");
                        missingProd.put(trans.getCode(), trans);
                        continue;
                    }
                }
                BusDoc doc = doctable.get(trans.getCode());
                if (doc == null) {
                    doc = new BusDoc();
                    doc.setBusdocinfo(docinfoRepo.getOne(Integer.parseInt(trans.getTransactiontype())));
                    Date d = DateUtil.addHours(trans.getTransdate(), 7, 0);
                    trans.setTransdate(d);
                    doc.setDocdate(trans.getTransdate());
                    doc.setCreatedon(trans.getTransdate());
                    doc.setUpdatedon(trans.getTransdate());
                    doc.setCurrency(countryCon.findCountryDefault());
                    doc.setBranch(trans.getBranch());
                    //doc.setProductTransactions(getProdTransactions());

                    PayTerms pt = new PayTerms();
                    doc.setPaytermsid(pt);
                    doc.setRefno(trans.getReference());
                    trans.setReference("");
                    doc.setExtra1(trans.getCode());
                    trans.setCode("");
                    doc.setBusinesspartner(businessRepo.getOne(Long.parseLong(trans.getPartnerId())));
                    //trans.setVatPercentage("");

                    //doc.setBranch(userSession.getLoggedInBranch());
                    doc.setEmp1(userSession.getLoggedInEmp());
                    doc.setDocstatus(DocStatus.PENDING.getValue());

                    doctable.put(doc.getExtra1(), doc);
                }
                trans.setBusdoc(doc);
                trans.setUnit(trans.getProduct().getUnit());
                trans.setTransactiontype(doc.getBusdocinfo().getTransactiontype());
                trans.setExchangerate(doc.getRate());
                trans.setLineqty(trans.getQty());
                //trans.setFcunitprice(trans.getLinefcunitprice());
                //trans.setUnitprice(trans.getLineunitprice());
                //trans.setBranch(doc.getBranch());
                //setSelected(doc);
                trans.refreshTotals();
                //if (trans.getFccost() == 0 || trans.getCost() == 0) {
                //System.out.println("Before_updateCost: " + trans.getProduct() + "\t" + getSelected().getDocdate());
                updateCost(trans, trans.getTransdate(), doc.getBusdocinfo().getDoctype());
                //}

                //System.out.println(doc);
                //getProdTransactions().add(trans);
                doc.addProductTransactions(trans);
                //System.out.println(doc.getExtra1() + "\t" + doc.getProductTransactions().size() + "\t" + trans.getLinereceived() + "\t" + trans.getLinefcunitprice());
                doc.refreshTotal();

            }

            int size = doctable.keySet().size();
            for (String key : doctable.keySet()) {
                BusDoc doc = doctable.get(key);
                mode = DocumentTab.MODE.NEW;
                if (doc.getBusdocinfo().hasCashRegister()) {
                    doc.setAccounts(cashRegController.getCashRegisterWithCashPaid(docInfo, doc.getGrandtotal()));
                }
                save(doc, false);
                count++;
                progress = count * 100 / size;
                System.out.println(doc.getDocno() + "\t" + doc.getExtra1() + "\t" + doc.getProductTransactions().size() + "\t" + doc.getTotalamount() + "\t" + doc.getTotalvat() + "\t" + doc.getGrandtotal() + "\t" + doc.getBusinesspartner());
            }
            //for saving
            //if (doc.getDocno() == null || doc.getDocno().trim().length() == 0) {
            //    doc.setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            //}
            progress = null;

            //display missing producttransactions
            System.out.println("---------------------MISSING PRODUCTS---------------------");
            System.out.println();
            System.out.println("docno, product, qty, unitprice");
            System.out.println();
            System.out.println("----------------------------------------------------------");
            for (String key : missingProd.keySet()) {
                ProductTransaction trans = missingProd.get(key);
                StringBuilder b = new StringBuilder(trans.getCode() + ",");
                b.append(trans.getExtra1() + ",");
                b.append(trans.getQty() + ",");
                b.append(trans.getLinefcunitprice());
                System.out.println(b.toString());
            }

            JsfUtil.addSuccessMessage(doctable.keySet().size() + " documents imported successfuly");
        }
    }

    public void handleFileUploadTransactions(FileUploadEvent event) {
        getImportService().handleFileUpload(event, ProductTransaction.class);
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
                //String transtype = getSelected().getBusdocinfo().getTransactiontype();
                String transtype = trans.getProduct().getProducttype().getName();
                String doctype = getSelected().getBusdocinfo().getDoctype();

                trans.setUnit(prod.getUnit());
                trans.setTransactiontype(transtype);
                trans.setCustomizedname(prod.getProductname());
                trans.setBusdoc(getSelected());
                if (trans.getBranch() == null) {
                    trans.setBranch(userSession.getLoggedInBranch());     //to be changed later
                }

                //find vat sales or purchase type
                VatBusinessRegister vbr = getSelected().getBusinesspartner().getBusinessRegisters().get(0);

                /* NOT REQUIRED AS IT SHOULD BE IN IMPORT FILE
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
                }*/
                if (prod.getVatregisterid() != null) {
                    trans.setVatcategoryid(prod.getVatregisterid().getVatcategoryid());
                }
                trans.refreshTotals();
                getProdTransactions().add(trans);
            }
            getSelected().refreshTotal();
        }
    }

    public void changeAll(String property) {
        if (getSelectedTransaction() == null) {
            JsfUtil.addErrorMessage("No Transaction selected to copy");
            return;
        }

        Object value = BeanPropertyUtil.getProperty(property, getSelectedTransaction());
        for (ProductTransaction pt2 : getProdTransactions()) {
            try {
                BeanUtilsBean2.getInstance().copyProperty(pt2, property, value);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(BusDocController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(BusDocController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void refreshConversionDocuments() {
        if (getProdTransactions() != null) {
            for (ProductTransaction pt : getSelected().getProductTransactions()) {
                getFromDocs(pt);
                getToDocs(pt);
            }
        }
    }

    public List<String> getFromDocs(ProductTransaction pt) {
        List<String> list = new LinkedList();
        //System.out.print("getFromDocs:");
        for (ProductTransactionExecution pte : pteRepo.findByFromProductTransaction(pt.getProdtransid())) {
            //build.append(pte.getFromprodtransid().getBusdoc().getDocno()).append(" ");
            //System.out.println("PTE_getFromDocs: " + pte);
            if (pte.getFromprodtransid() != null) {
                if (!pte.getFromprodtransid().getBusdoc().getDocno().equals(pt.getBusdoc().getDocno())) {
                    //System.out.print("[" + pte.getFromprodtransid().getBusdoc().getDocno() + ", " + pt.getBusdoc().getDocno() + "]");
                    list.add(pte.getFromprodtransid().getBusdoc().getDocno());
                }
            }
        }
        for (ProductTransactionExecution pte : pteRepo.findByToProductTransaction(pt.getProdtransid())) {
            //build.append(pte.getFromprodtransid().getBusdoc().getDocno()).append(" ");
            //System.out.println("PTE_getFromDocs: " + pte);
            if (pte.getFromprodtransid() != null) {
                if (!pte.getFromprodtransid().getBusdoc().getDocno().equals(pt.getBusdoc().getDocno())) {
                    //System.out.print("[" + pte.getFromprodtransid().getBusdoc().getDocno() + ", " + pt.getBusdoc().getDocno() + "]");
                    list.add(pte.getFromprodtransid().getBusdoc().getDocno());
                }
            }
        }
        //System.out.println();
        pt.setExtra2(list.toString().replaceAll("(^\\[|\\]$)", ""));
        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getToDocs(ProductTransaction pt) {
        List<String> list = new LinkedList();
        //System.out.print("getToDocs:");
        for (ProductTransactionExecution pte : pteRepo.findByFromProductTransaction(pt.getProdtransid())) {
            //build.append(pte.getFromprodtransid().getBusdoc().getDocno()).append(" ");
            //System.out.println("PTE_getFromDocs: " + pte);
            if (pte.getFromprodtransid() != null) {
                if (!pte.getToprodtransid().getBusdoc().getDocno().equals(pt.getBusdoc().getDocno())) {
                    //System.out.print("[" + pte.getToprodtransid().getBusdoc().getDocno() + ", " + pt.getBusdoc().getDocno() + "]");
                    list.add(pte.getToprodtransid().getBusdoc().getDocno());
                }
            }
        }
        for (ProductTransactionExecution pte : pteRepo.findByToProductTransaction(pt.getProdtransid())) {
            //build.append(pte.getFromprodtransid().getBusdoc().getDocno()).append(" ");
            //System.out.println("PTE_getFromDocs: " + pte);
            if (pte.getFromprodtransid() != null) {
                if (!pte.getToprodtransid().getBusdoc().getDocno().equals(pt.getBusdoc().getDocno())) {
                    //System.out.print("[" + pte.getToprodtransid().getBusdoc().getDocno() + ", " + pt.getBusdoc().getDocno() + "]");
                    list.add(pte.getToprodtransid().getBusdoc().getDocno());
                }
            }
        }
        //System.out.println();
        pt.setExtra3(list.toString().replaceAll("(^\\[|\\]$)", ""));
        return list.stream().distinct().collect(Collectors.toList());
    }

    public String getConversionDocs(ProductTransaction pt) {
        List<String> list = getFromDocs(pt);
        StringBuilder str = new StringBuilder();
        if (list.size() > 0) {
            str.append("Converted From:");
            str.append("<br/>");
            str.append(list.toString().replaceAll("(^\\[|\\]$)", ""));
        }

        list = getToDocs(pt);
        if (list.size() > 0) {
            str.append("Converted To:");
            str.append("<br/>");
            str.append(list.toString().replaceAll("(^\\[|\\]$)", ""));
        }
        return str.toString();
    }

    public List<BusDoc> completeFilterBusDoc(String criteria) {
        return repo.findByBusDocByPrefix(criteria, PageRequest.of(0, 10));
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

    /**
     * @return the progress
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
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
     * @return the convertToButtonModel
     */
    public MenuModel getConvertToButtonModel() {
        return convertToButtonModel;
    }

    /**
     * @param convertToButtonModel the convertToButtonModel to set
     */
    public void setConvertToButtonModel(MenuModel convertToButtonModel) {
        this.convertToButtonModel = convertToButtonModel;
    }

    /**
     * @return the copyDoc
     */
    public BusDoc getCopyDoc() {
        return copyDoc;
    }

    /**
     * @param copyDoc the copyDoc to set
     */
    public void setCopyDoc(BusDoc copyDoc) {
        this.copyDoc = copyDoc;
    }

}
