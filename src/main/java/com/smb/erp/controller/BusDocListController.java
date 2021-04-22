/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.DocStatus;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PF;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "busDocListController")
@ViewScoped
public class BusDocListController extends AbstractController<BusDoc> {

    BusDocRepository repo;

    @Autowired
    BusDocInfoRepository docinfoRepo;

    @Autowired
    BusDocController busDocController;

    @Autowired
    BusinessPartnerRepository partnerRepo;

    @Autowired
    BusinessPartnerController partnerCon;

    @Autowired
    ProductTransactionExecutionController pteController;

    @Autowired
    CashRegisterController cashRegController;

    @Autowired
    PageAccessController pageController;

    @Autowired
    AccDocController accDocCon;

    //@Autowired
    //CashRegisterController cashRegController;
    DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    private BusDocInfo docInfo;

    List<BusinessPartner> partnerList;

    private Date fromDate = DateUtil.startOfMonth(new Date());

    private Date toDate = new Date();

    private BusinessPartner selectedBusinessPartner;
    
    private List<MenuItem> convertToMenuItems;

    @Autowired
    public BusDocListController(BusDocRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusDoc.class, repo);
        this.repo = repo;
    }

    //http://localhost:8008/busdoc/busdoclist.xhtml?mode=l&docinfoid=1      ->test link
    @PostConstruct
    public void init() {
        //setSelected(new BusDoc());
        //getSelected().setCreatedon(new Date());

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");

        if (m != null) {
            if (m.equalsIgnoreCase("l")) {
                String docinfo = req.getParameter("docinfoid");
                setDocInfo(docinfoRepo.getOne(Integer.parseInt(docinfo)));
                mode = DocumentTab.MODE.LIST;
            }
        }
        pageController.hasAccess(docInfo);
        selectedBusinessPartner = partnerCon.getAll();
        setupConvertToMenu();
    }

    @Override
    public List<BusDoc> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            //System.out.println("selectedBusinessPartner: " + selectedBusinessPartner);
            if (selectedBusinessPartner == null || selectedBusinessPartner.getPartnerid() == 0l) {
                items = repo.findByBusDocByPrefix(getDocInfo().getPrefix(), DateUtil.startOfDay(fromDate), DateUtil.endOfDay(toDate));
            } else {
                items = repo.findByBusDocByPrefixAndBusinessPartner(getDocInfo().getPrefix(), DateUtil.startOfDay(fromDate), DateUtil.endOfDay(toDate), selectedBusinessPartner.getPartnerid());
            }
            //System.out.println(docInfo.getPrefix() + " List: " + items.size());
            //for (BusDoc bd : items) {
            //System.out.println("----------" + bd.getDocno() + "----------");
            //for (ProductTransaction pt : bd.getProductTransactions()) {
            //System.out.println(pt.getProdtransid() + " => " + pt.getProduct() + " => FROM:" + pt.getFromprodtransaction() + " => TO:" + pt.getToprodtransaction());
            //}
            //System.out.println("\n");
            //}
            JsfUtil.addSuccessMessage(items.size() + " documents listed");
        }
        return items;
    }

    public void refresh() {
        items = null;
        getItems();
    }

    @Transactional
    public void saveAll() {
        if (getItems() == null) {
            JsfUtil.addErrorMessage("No document listed to save");
            return;
        }

        for (BusDoc doc : getItems()) {
            doc.refreshTotal();
            if (doc.getBusdocinfo().hasCashRegister()) {
                cashRegController.setupBusDoc(doc);
                for (Account ac : cashRegController.getCashRegisterAccounts()) {
                    if (ac.getAmount() > 0) {
                        System.out.println(ac.getAccountname() + " => " + ac.getAmount());
                        ac.setAmount(doc.getGrandtotal());
                        break;
                    }
                }
                if (cashRegController.calculateTotal() > 0) {
                    doc.setAccounts(cashRegController.getCashRegisterAccounts());
                }

            }
            busDocController.save(doc, false);
        }
        JsfUtil.addSuccessMessage(getItems().size() + " documents saved");
    }

    @Transactional
    public void saveAllUpdateCost() {
        if (getItems() == null) {
            JsfUtil.addErrorMessage("No document listed to save");
            return;
        }

        for (BusDoc doc : getItems()) {
            busDocController.save(doc, true);
        }
        JsfUtil.addSuccessMessage(getItems().size() + " documents saved");
    }

    @Transactional
    public void deleteAll() {
        int count = items.size();
        for (BusDoc doc : items) {
            System.out.print("DELETING: " + doc.getDocno() + "...");
            String errMsg = null;
            for (ProductTransaction pt : doc.getProductTransactions()) {
                if (pt.getFromprodtransaction() != null && pt.getFromprodtransaction().size() > 0) {
                    errMsg = "Cannot delete linked document";
                    break;
                }
            }

            if (errMsg != null) {
                System.out.println(errMsg);
                JsfUtil.addErrorMessage("Error", errMsg);
                continue;
            }

            for (ProductTransaction pt : doc.getProductTransactions()) {
                System.out.println("ToProdTransaction: " + pt.getToprodtransaction());
                if (pt.getToprodtransaction() != null) {
                    pteController.deleteToProductTransactionExe(pt.getToprodtransaction());
                    //for(ProductTransactionExecution pte: pt.getToprodtransaction()){
                    //    pt.removeToprodtransaction(pte);
                    //}
                    //pt.removeAllToprodtransaction();
                }
            }
            System.out.println("DONE");
            accDocCon.deleteBusDocVoucher(doc.getDocno());
            repo.delete(doc);
        }
        JsfUtil.addSuccessMessage("Success", +count + " documents deleted successfuly");
    }

    @Transactional
    public void delete() {
        System.out.println("Document Delete: " + getSelected());
        String errMsg = null;
        if (getSelected() != null) {

            for (ProductTransaction pt : getSelected().getProductTransactions()) {
                if (pt.getFromprodtransaction() != null && pt.getFromprodtransaction().size() > 0) {
                    errMsg = "Cannot delete linked document";
                    break;
                }
            }

            if (errMsg != null) {
                JsfUtil.addErrorMessage("Error", errMsg);
                return;
            }

            for (ProductTransaction pt : getSelected().getProductTransactions()) {
                System.out.println("ToProdTransaction: " + pt.getToprodtransaction());
                if (pt.getToprodtransaction() != null) {
                    pteController.deleteToProductTransactionExe(pt.getToprodtransaction());
                    //for(ProductTransactionExecution pte: pt.getToprodtransaction()){
                    //    pt.removeToprodtransaction(pte);
                    //}
                    //pt.removeAllToprodtransaction();
                }
            }
            accDocCon.deleteBusDocVoucher(getSelected().getDocno());
            repo.delete(getSelected());

            JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " deleted successfuly");
            setSelected(null);
            items = null;
        }
    }

    public void new_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=n&docinfoid=" + getDocInfo().getBdinfoid());
    }

    public void edit_in_tab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No Document selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
    }

    public List<BusinessPartner> getPartnerList() {
        if (partnerList == null) {
            if (getDocInfo().getDoctype().equalsIgnoreCase(BusDocType.SALES.getValue())) {
                partnerList = partnerRepo.findBusinessPartnerByTypeBySearchCriteria("", BusinessPartner.CUSTOMER);
            } else {
                partnerList = partnerRepo.findBusinessPartnerByTypeBySearchCriteria("", BusinessPartner.SUPPLIER);
            }
        }
        return partnerList;
    }

    public void makeCopy(){
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Document not selected to Copy");
            return;
        }
        
        BusDocInfo info = getSelected().getBusdocinfo();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=x&docinfoid=" + info.getBdinfoid()+ "&docno=" + getSelected().getDocno());
        String url = facesContext.getExternalContext().getRequestContextPath() + "/" + info.getDocediturl() + "?mode=c&docinfoid=" + info.getBdinfoid()+ "&docno=" + getSelected().getDocno();
        PF.current().executeScript("window.open('"+url+"', '_newtab')");
    }
    
    public void convertSelected(BusDocInfo info) throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Document not selected to convert");
            return;
        }
        String status = getSelected().getDocumentExecutionStatus();
        if(status.equalsIgnoreCase(DocStatus.COMPLETED.toString())){
            JsfUtil.addErrorMessage(getSelected().getDocno() + " is already converted");
            return;
        }
        if(status.equalsIgnoreCase(DocStatus.CANCELLED.toString()) || status.equalsIgnoreCase(DocStatus.RETURNED.toString())){
            JsfUtil.addErrorMessage(getSelected().getDocno() + " is " + status);
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        //facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=x&docinfoid=" + info.getBdinfoid()+ "&docno=" + getSelected().getDocno());
        String url = facesContext.getExternalContext().getRequestContextPath() + "/" + info.getDocediturl() + "?mode=x&docinfoid=" + info.getBdinfoid()+ "&docno=" + getSelected().getDocno();
        PF.current().executeScript("window.open('"+url+"', '_newtab')");
    }

    public List<MenuItem> setupConvertToMenu() {
        convertToMenuItems = new LinkedList<>();
        //System.out.println("setupConvertToMenu: ");
        if (getDocInfo() != null) {
            getDocInfo().getConvertto().forEach((info) -> {
                DefaultMenuItem item = new DefaultMenuItem();
                item.setCommand("#{busDocController.convertSelected}");
                item.setAjax(false);
                item.setTitle(info.getDocname());
                item.setValue(info);
                //item.setUrl("../viewer/doc/" + report.getReportid() + "/" + getSelected().getDocno());
                convertToMenuItems.add(item);
                //System.out.println("\t- " + item.getTitle());
            });
        }
        return convertToMenuItems;
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
     * @return the selectedBusinessPartner
     */
    public BusinessPartner getSelectedBusinessPartner() {
        return selectedBusinessPartner;
    }

    /**
     * @param selectedBusinessPartner the selectedBusinessPartner to set
     */
    public void setSelectedBusinessPartner(BusinessPartner selectedBusinessPartner) {
        this.selectedBusinessPartner = selectedBusinessPartner;
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
     * @return the convertToMenuItems
     */
    public List<MenuItem> getConvertToMenuItems() {
        return convertToMenuItems;
    }

    /**
     * @param convertToMenuItems the convertToMenuItems to set
     */
    public void setConvertToMenuItems(List<MenuItem> convertToMenuItems) {
        this.convertToMenuItems = convertToMenuItems;
    }

}
