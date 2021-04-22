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
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PF;
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

    @Autowired
    PageAccessController pageController;

    private Date fromDate = DateUtil.startOfMonth(new Date());

    private Date toDate = new Date();

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
        pageController.hasAccess(docInfo);
    }

    @Override
    public List<AccDoc> getItems() {
        if (items == null) {
            //items = repo.findAll(Sort.by(Sort.Direction.ASC, "createdon"));
            //items = repo.findByAccDocByPrefix(docInfo.getPrefix());
            items = repo.findByAccDocByPrefix(docInfo.getPrefix(), fromDate, toDate);
            //System.out.println(docInfo.getPrefix() + " List: " + items.size());
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
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=n&docinfoid=" + docInfo.getBdinfoid());
    }

    public void edit_in_tab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No Document selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
    }

    public void makeCopy() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Document not selected to Copy");
            return;
        }

        BusDocInfo info = getSelected().getBusdocinfo();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=c&docno=" + getSelected().getDocno());
        //String url = facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=c&docno=" + getSelected().getDocno();
        //PF.current().executeScript("window.open('" + url + "', '_newtab')");
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

}
