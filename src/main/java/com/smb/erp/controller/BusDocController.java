/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.PayTerms;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.entity.VatBusinessRegister;
import com.smb.erp.entity.VatCategory;
import com.smb.erp.entity.VatMapping;
import com.smb.erp.entity.VatSalesPurchaseType;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.CompanyRepository;
import com.smb.erp.repo.VatCategoryRepository;
import com.smb.erp.repo.VatMappingRepository;
import com.smb.erp.repo.VatSalesPurchaseTypeRepository;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
@Named(value = "busDocController")
@ViewScoped
public class BusDocController extends AbstractController<BusDoc> implements ProductTransferable {

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
    CompanyRepository companyRepo;

    @Autowired
    BranchRepository branchRepo;

    @Autowired
    AccDocController accdocController;

    @Autowired
    VatCategoryRepository vatcatRepo;

    @Autowired
    VatMappingRepository vatmappingRepo;

    @Autowired
    VatSalesPurchaseTypeRepository vatsalespurRepo;

    @Autowired
    VatSalesPurchaseTypeController vatsalespurchaseController;

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

                //docdate = getSelected().getDocdate();
            } else {        //edit mode=e
                String docno = req.getParameter("docno");
                if (docno != null) {
                    setSelected(repo.getOne(docno));
                    docInfo = getSelected().getBusdocinfo();
                    setProdTransactions(getSelected().getProductTransactions());

                    if (getSelected().getPaytermsid() == null) {
                        PayTerms pt = new PayTerms();
                        getSelected().setPaytermsid(pt);
                    }
                    getSelected().refreshTotal();
                }
                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
            }
        }
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

    public void save() {
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getCreatedon())));
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
            pt.setUnitprice(pt.getLineunitprice());
            //pt.calculateActualQtyFromLineQty();
            pt.refreshTotals();
            //if (getSelected().getBusdocinfo().getDoctype().equalsIgnoreCase("Sales")) {
            //    pt.setLinesold(pt.getLineqty());
            //    pt.setSold(pt.getLinesold());
            //} else {
            //    pt.setLinereceived(pt.getLineqty());
            //    pt.setLinereceived(pt.getLinereceived());
            //}
        }
        getSelected().refreshTotal();
        //getSelected().setCompany(companyRepo.getOne(1));    //to be commented
        getSelected().setBranch(userSession.getLoggedInBranch());      //to be changed later
        repo.save(getSelected());

        accdocController.createBusDocJV(getSelected());
        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
        mode = DocumentTab.MODE.EDIT;
    }

    public void refreshTotal(ProductTransaction pt) {
        pt.refreshTotals();
        pt.getBusdoc().refreshTotal();
    }

    public void deleteTransactions() {
        if (getSelectedTransaction() != null) {
            getProdTransactions().remove(getSelectedTransaction());
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
        pt.setBranch(userSession.getLoggedInBranch());     //to be changed later

        //find vat sales or purchase type
        VatBusinessRegister vbr = getSelected().getBusinesspartner().getBusinessRegisters().get(0);
        List<VatMapping> vatmaps = vatmappingRepo.findByVatSalesPurchaseType(vbr.getVataccounttypeid().getVataccounttypeid(),
                vbr.getVatcategoryid().getVatcategoryid(), prod.getVatregisterid().getProducttype(), doctype);
        System.out.println("VAT_MAPPING: " + vatmaps);
        if (vatmaps != null && vatmaps.size() > 0) {
            VatMapping vm = vatmaps.get(0);
            pt.setVatsptypeid(vm.getVatsptypeid());
        } else {
            if (doctype.equalsIgnoreCase("Sales")) {
                Optional<VatSalesPurchaseType> vt = vatsalespurRepo.findById(1);
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

    public void businessPartnerSelected() {
        if (getSelected().getBusinesspartner() != null) {
            productTabDisabled = false;
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

    public List<VatSalesPurchaseType> getVatSalesPurchaseType(){
        return vatsalespurRepo.findByCategory(getSelected().getBusdocinfo().getDoctype());
    }
    
    public void docdateChange(SelectEvent event) {
        //FacesContext facesContext = FacesContext.getCurrentInstance();
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
        getSelected().setDocdate((Date) event.getObject());
    }

    public List<BusDocInfo> getConvertFromDocument() {
        return getSelected().getBusdocinfo().getConvertfrom();
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

}
