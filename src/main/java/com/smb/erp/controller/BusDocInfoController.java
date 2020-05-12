/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocTransactionType;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "busDocInfoController")
@ViewScoped
public class BusDocInfoController extends AbstractController<BusDocInfo> {

    BusDocInfoRepository repo;

    @Autowired
    public BusDocInfoController(BusDocInfoRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusDocInfo.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");
        if (m != null) {
            if (m.equalsIgnoreCase("0")) { // new business partner
                BusDocInfo bus = new BusDocInfo();
                setSelected(bus);
                mode = DocumentTab.MODE.NEW;
            } else {
                String bdid = req.getParameter("bdid");
                if (bdid != null) {
                    setSelected(repo.getOne(Integer.parseInt(bdid)));
                }
                mode = DocumentTab.MODE.EDIT;
            }
        }
    }

    @Override
    public List<BusDocInfo> getItems() {
        if (items == null) {
            items = repo.findAll(Sort.by(Sort.Direction.ASC, "docname"));
        }
        return items;
    }

    @Transactional
    public void save() {
        try {
            if (mode == DocumentTab.MODE.NEW) {
                getSelected().setCreatedon(new Date());
            }
            super.save();
            JsfUtil.addSuccessMessage("Business Document Definition saved");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "Could not save due to error");
        }
    }

    public String getTitle() {
        if (mode == DocumentTab.MODE.LIST) {
            return "Business Document List";
        } else if (mode == DocumentTab.MODE.NEW) {
            return "New Business Document";
        }
        if (getSelected() != null) {
            return "Edit Business Document";
        }
        return "Invalid";
    }

    public void refresh() {
        items = null;
        getItems();
    }

    public void new_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editbusdocinfo.xhtml?mode=0");
    }

    public void edit_in_tab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No Business Document selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editbusdocinfo.xhtml?mode=1&bdid=" + getSelected().getBdinfoid());
    }

    public List<String> getBusDocTypes() {
        return BusDocType.TYPES;
    }

    public List<String> getBusDocTransactionTypes() {
        return BusDocTransactionType.TYPES;
    }
}
