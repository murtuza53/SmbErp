/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.PayTerms;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.CompanyRepository;
import com.smb.erp.service.ProductTransferable;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.StringUtils;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
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
    SystemDefaultsController systemController;

    @Autowired
    BusDocInfoRepository docinfoRepo;

    @Autowired
    BusinessPartnerRepository partnerRepo;

    @Autowired
    private ProductSearchController productSearchController;

    @Autowired
    TableKeyController keyCon;

    @Autowired
    CompanyRepository companyRepo;

    DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    BusDocInfo docInfo;

    List<BusinessPartner> partnerList;

    private List<ProductTransaction> prodTransactions = new LinkedList<>();

    private ProductTransaction selectedTransaction;

    private boolean productTabDisabled = true;

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
                doc.setCreatedon(new Date());
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
                    docInfo = getSelected().getBusdocinfo();
                    setProdTransactions(getSelected().getProductTransactions());

                    if (getSelected().getPaytermsid() == null) {
                        PayTerms pt = new PayTerms();
                        getSelected().setPaytermsid(pt);
                    }
                    getSelected().refreshTotal();
                }
                mode = DocumentTab.MODE.EDIT;
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
        for (ProductTransaction pt : getProdTransactions()) {
            pt.setCeatedon(getSelected().getCreatedon());
            pt.setUpdatedon(getSelected().getUpdatedon());
            pt.setBusdoc(getSelected());
            pt.setUnitprice(pt.getLineunitprice());
            pt.calculateActualQtyFromLineQty();
            //if (getSelected().getBusdocinfo().getDoctype().equalsIgnoreCase("Sales")) {
            //    pt.setLinesold(pt.getLineqty());
            //    pt.setSold(pt.getLinesold());
            //} else {
            //    pt.setLinereceived(pt.getLineqty());
            //    pt.setLinereceived(pt.getLinereceived());
            //}
        }
        getSelected().setCompany(companyRepo.getOne(1));    //to be commented
        repo.save(getSelected());
        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
    }

    public void deleteTransactions() {
        if (getSelectedTransaction() != null) {
            getProdTransactions().remove(getSelectedTransaction());
            setSelectedTransaction(null);
        }
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    }

    public String getTabTitle() {
        if (mode == DocumentTab.MODE.NEW) {
            return "New - " + getSelected().getBusdocinfo().getDocname();
        } else {
            return "Edit - " + getSelected().getDocno();
        }
    }

    public List<String> findAsList(String propertyname) {
        return systemController.getAsList(propertyname);
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
}
