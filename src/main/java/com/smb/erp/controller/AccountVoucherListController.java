/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.AccDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.repo.AccDocRepository;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "accountVoucherListController")
@ViewScoped
public class AccountVoucherListController extends AbstractController<AccDoc> {

    AccDocRepository repo;

    @Autowired
    TableKeyController keyCon;

    @Autowired
    SystemDefaultsController defaultController;

    @Autowired
    BusDocInfoRepository docinfoRepo;

    private BusDocInfo docInfo;

    @Autowired
    public AccountVoucherListController(AccDocRepository repo) {
        super(AccDoc.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String docinfo = req.getParameter("docinfoid");
        setDocInfo(docinfoRepo.getOne(Integer.parseInt(docinfo)));

    }

    @Override
    public List<AccDoc> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            items = repo.findByAccDocByPrefix(docInfo.getPrefix());
            System.out.println(docInfo.getPrefix() + " List: " + items.size());
        }
        return items;
    }

    public void refresh() {
        items = null;
        getItems();
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

    public void new_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editvoucher.xhtml?mode=n&docinfoid=" + docInfo.getBdinfoid());
    }

    public void edit_in_tab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No Document selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editvoucher.xhtml?mode=e&docno=" + getSelected().getDocno());
    }

}
