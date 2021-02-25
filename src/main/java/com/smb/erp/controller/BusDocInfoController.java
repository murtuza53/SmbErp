/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.entity.AccountTransactionType;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusDocTransactionType;
import com.smb.erp.entity.BusDocType;
import com.smb.erp.entity.CashRegister;
import com.smb.erp.entity.DefaultModeAccount;
import com.smb.erp.entity.PrintReport;
import com.smb.erp.entity.Webpage;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.WebpageRepository;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.ReflectionUtil;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
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

    @Autowired
    WebpageRepository webRepo;

    BusDocInfoRepository repo;

    private AccountTransactionType selectedTransaction;

    private CashRegister selectedCashRegister;
    
    private DefaultModeAccount selectedDefaultAccount;

    private BusDocInfo selectedConvertFrom;

    private PrintReport selectedPrintReport;
    
    private String modeDebitOrCredit = "None";

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
                bus.setDocname("New Document");
                setSelected(bus);
                mode = DocumentTab.MODE.NEW;
            } else {
                String bdid = req.getParameter("bdid");
                if (bdid != null) {
                    setSelected(repo.getOne(Integer.parseInt(bdid)));
                }
                
                if(getSelected().getDebitaccountid()!=null){
                    setModeDebitOrCredit("Debit");
                }
                if(getSelected().getCreditaccountid()!=null){
                    setModeDebitOrCredit("Credit");
                }
                
                mode = DocumentTab.MODE.EDIT;
            }
            if (getSelected().getPageid() == null) {
                getSelected().setPageid(new Webpage());
                getSelected().getPageid().setCreatedon(new Date());
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
            
            if(getModeDebitOrCredit().equalsIgnoreCase("None")){
                getSelected().setDebitaccountid(null);
                getSelected().setCreditaccountid(null);
            } else if(getModeDebitOrCredit().equalsIgnoreCase("Debit")){
                getSelected().setCreditaccountid(null);
            } else if(getModeDebitOrCredit().equalsIgnoreCase("Credit")){
                getSelected().setDebitaccountid(null);
            }
            
            if(getSelected().getDefaultaccid()!=null){
                for(DefaultModeAccount def: getSelected().getDefaultaccid()){
                    def.setTranstype(modeDebitOrCredit);
                }
            }
            //if(getSelected().getPageid().getPageid()==null){
            //    webRepo.save(getSelected().getPageid());
            //}
            super.save();
            //reload the busdocinfo 
            setSelected(repo.getOne(getSelected().getBdinfoid()));
            getSelected().getPageid().setListurl(getSelected().getDoclisturl());
            getSelected().getPageid().setPageurl(getSelected().getDocediturl());
            super.save();
            //}

            JsfUtil.addSuccessMessage(getSelected().getDocname() + " Definition saved");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "Could not save due to error");
        }
    }

    public void createNewAccountTransaction() {
        selectedTransaction = new AccountTransactionType();
        selectedTransaction.setTranstypeid(0);
        if (getSelected().getTranstypeid() == null) {
            getSelected().setTranstypeid(new LinkedList<AccountTransactionType>());
        }
        getSelected().addTranstypeid(selectedTransaction);
    }

    public void deleteAccountTransaction() {
        if (selectedTransaction == null) {
            JsfUtil.addErrorMessage("No Account Selected to Delete");
            return;
        }
        getSelected().removeTranstypeid(selectedTransaction);
        selectedTransaction = null;
        JsfUtil.addSuccessMessage("Deleted Successfuly");
    }

    public void createNewCashRegister() {
        selectedCashRegister = new CashRegister();
        selectedCashRegister.setCashregisterid(0);
        if (getSelected().getCashregiserid() == null) {
            getSelected().setCashregiserid(new LinkedList<CashRegister>());
        }
        getSelected().addCashregisterid(selectedCashRegister);
    }

    public void deleteCashRegister() {
        if (selectedCashRegister == null) {
            JsfUtil.addErrorMessage("No Account Selected to Delete");
            return;
        }
        getSelected().removeCashregisterid(selectedCashRegister);
        selectedCashRegister = null;
        JsfUtil.addSuccessMessage("Deleted Successfuly");
    }

    public void createNewDefaultModeAccount() {
        setSelectedDefaultAccount(new DefaultModeAccount());
        getSelectedDefaultAccount().setDefaultaccid(0);
        getSelectedDefaultAccount().setTranstype(modeDebitOrCredit);
        if (getSelected().getDefaultaccid()== null) {
            getSelected().setDefaultaccid(new LinkedList<DefaultModeAccount>());
        }
        getSelected().addDefaultaccid(getSelectedDefaultAccount());
    }

    public void deleteDefaultModeAccount() {
        if (getSelectedDefaultAccount() == null) {
            JsfUtil.addErrorMessage("No Account Selected to Delete");
            return;
        }
        getSelected().removeDefaultaccid(getSelectedDefaultAccount());
        setSelectedDefaultAccount(null);
        JsfUtil.addSuccessMessage("Deleted Successfuly");
    }

    public void createNewPrintReport() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("../report/printdesigner.xhtml?mode=n&bdid=" + getSelected().getBdinfoid());
    }

    public void createNewJasperReport() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //facesContext.getExternalContext().getRequestMap().put("mode", "n");
        //facesContext.getExternalContext().getRequestMap().put("bdid", "" + getSelected().getBdinfoid());
        facesContext.getExternalContext().redirect("../report/jasperdesigner.xhtml?mode=n&bdid=" + getSelected().getBdinfoid());
    }

    public void editPrintReport() throws IOException {
        if (getSelectedPrintReport() == null) {
            JsfUtil.addErrorMessage("Error", "No Print Template selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("../report/printdesigner.xhtml?mode=e&bdid=" + getSelected().getBdinfoid() + "&reportid=" + getSelectedPrintReport().getReportid());
    }

    public void editJasperReport() throws IOException {
        if (getSelectedPrintReport() == null) {
            JsfUtil.addErrorMessage("Error", "No Print Template selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("../report/jasperdesigner.xhtml?mode=e&bdid=" + getSelected().getBdinfoid() + "&reportid=" + getSelectedPrintReport().getReportid());
    }

    public void deletePrintReport() {
        if (getSelectedPrintReport() == null) {
            JsfUtil.addErrorMessage("No Print Template selected to Delete");
            return;
        }
        getSelected().removeReportid(selectedPrintReport);
        selectedPrintReport = null;
        JsfUtil.addSuccessMessage("Deleted Successfuly");
    }

    public String getTitle() {
        if (mode == DocumentTab.MODE.LIST) {
            return "BusDocInfo List";
        } else if (mode == DocumentTab.MODE.NEW) {
            return "New " + getSelected().getDocname();
        }
        if (getSelected() != null) {
            return "Edit " + getSelected().getDocname();
        }
        return "Invalid";
    }

    public List<String> getNumberFields() {
        return ReflectionUtil.getNumberFields(BusDoc.class);
    }

    public void refresh() {
        items = null;
        getItems();
    }

    public void findDocumentListLink() throws IOException {
        String link = getSelected().getDoclisturl() + "?mode=l&docinfoid=" + getSelected().getBdinfoid();
        if (getSelected().getDoctype().equalsIgnoreCase(BusDocType.ACCOUNT.toString())) {
            link = getSelected().getDoclisturl() + "?docinfoid=" + getSelected().getBdinfoid();
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(link);
    }

    public void new_in_tab() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editbusdocinfo.xhtml?mode=0");
    }

    public void edit_in_tab() throws IOException {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Error", "No " + getSelected().getDocname() + " selected to edit");
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("editbusdocinfo.xhtml?mode=1&bdid=" + getSelected().getBdinfoid());
    }

    public void createNewConvertFrom() {
        //selectedConvertTo = new BusDocInfo();
        //selectedConvertTo.setCreatedon(new Date());
        //getSelected().addConvertTo(selectedConvertFrom);
        //System.out.println("createNewConvertTo: " + getSelected().getConvertto());
    }

    public void addNewConvertTo() {
        getSelected().addConvertFrom(getSelectedConvertFrom());
        //System.out.println("addNewConvertTo: " + getSelected().getConvertto());
    }

    public void deleteConvertFrom() {

    }

    public List<String> getBusDocTypes() {
        return BusDocType.TYPES;
    }

    public List<String> getBusDocTransactionTypes() {
        return BusDocTransactionType.BUSDOC_INVENTORY_TYPES;
    }

    public List<String> getBusDocAccountTypes() {
        return BusDocTransactionType.BUSDOC_ACCOUNT_TYPES;
    }

    /**
     * @return the selectedTransaction
     */
    public AccountTransactionType getSelectedTransaction() {
        return selectedTransaction;
    }

    /**
     * @param selectedTransaction the selectedTransaction to set
     */
    public void setSelectedTransaction(AccountTransactionType selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    /**
     * @return the selectedConvertFrom
     */
    public BusDocInfo getSelectedConvertFrom() {
        return selectedConvertFrom;
    }

    /**
     * @param selectedConvertFrom the selectedConvertFrom to set
     */
    public void setSelectedConvertFrom(BusDocInfo selectedConvertFrom) {
        this.selectedConvertFrom = selectedConvertFrom;
    }

    /**
     * @return the selectedCashRegister
     */
    public CashRegister getSelectedCashRegister() {
        return selectedCashRegister;
    }

    /**
     * @param selectedCashRegister the selectedCashRegister to set
     */
    public void setSelectedCashRegister(CashRegister selectedCashRegister) {
        this.selectedCashRegister = selectedCashRegister;
    }

    /**
     * @return the selectedPrintReport
     */
    public PrintReport getSelectedPrintReport() {
        return selectedPrintReport;
    }

    /**
     * @param selectedPrintReport the selectedPrintReport to set
     */
    public void setSelectedPrintReport(PrintReport selectedPrintReport) {
        this.selectedPrintReport = selectedPrintReport;
    }

    /**
     * @return the modeDebitOrCredit
     */
    public String getModeDebitOrCredit() {
        return modeDebitOrCredit;
    }

    /**
     * @param modeDebitOrCredit the modeDebitOrCredit to set
     */
    public void setModeDebitOrCredit(String modeDebitOrCredit) {
        this.modeDebitOrCredit = modeDebitOrCredit;
    }

    /**
     * @return the selectedDefaultAccount
     */
    public DefaultModeAccount getSelectedDefaultAccount() {
        return selectedDefaultAccount;
    }

    /**
     * @param selectedDefaultAccount the selectedDefaultAccount to set
     */
    public void setSelectedDefaultAccount(DefaultModeAccount selectedDefaultAccount) {
        this.selectedDefaultAccount = selectedDefaultAccount;
    }

}
