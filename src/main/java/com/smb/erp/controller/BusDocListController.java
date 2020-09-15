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
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.entity.ProductTransactionExecution;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
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
    BusinessPartnerRepository partnerRepo;

    @Autowired
    ProductTransactionExecutionController pteController;

    DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    BusDocInfo docInfo;

    List<BusinessPartner> partnerList;

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
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                mode = DocumentTab.MODE.LIST;
            }
        }
    }

    @Override
    public List<BusDoc> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            items = repo.findByBusDocByPrefix(docInfo.getPrefix());
            System.out.println(docInfo.getPrefix() + " List: " + items.size());
            for (BusDoc bd : items) {
                System.out.println("----------" + bd.getDocno() + "----------");
                for (ProductTransaction pt : bd.getProductTransactions()) {
                    System.out.println(pt.getProdtransid() + " => " + pt.getProduct() + " => FROM:" + pt.getFromprodtransaction() + " => TO:" + pt.getToprodtransaction());
                }
                System.out.println("\n");
            }
        }
        return items;
    }

    public void refresh() {
        items = null;
        getItems();
    }

    @Transactional
    public void delete() {
        System.out.println("Document Delete: " + getSelected());
        String errMsg = null;
        if (getSelected() != null) {

            for (ProductTransaction pt : getSelected().getProductTransactions()) {
                if (pt.getFromprodtransaction() != null && pt.getFromprodtransaction().size()>0) {
                    errMsg = "Cannot delete linked document";
                    break;
                }
            }

            if(errMsg!=null){
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
            repo.delete(getSelected());

            JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " deleted successfuly");
            setSelected(null);
            items = null;
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

}
