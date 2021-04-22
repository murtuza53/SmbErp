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
import com.smb.erp.entity.BusDocTransactionType;
import com.smb.erp.entity.Country;
import com.smb.erp.entity.LedgerLine;
import com.smb.erp.entity.PaymentMethod;
import com.smb.erp.repo.AccDocRepository;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.LedgerLineRepository;
import com.smb.erp.repo.PartialPaymentDetailRepository;
import com.smb.erp.rest.JasperPrintReportGenerator;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.PrimeFaces;
import org.primefaces.component.filedownload.FileDownloadActionListener;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.smberp.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "accountVoucherEditController")
@ViewScoped
public class AccountVoucherEditController extends AbstractController<AccDoc> {

    AccDocRepository repo;

    @Autowired
    TableKeyController keyCon;

    @Autowired
    SystemDefaultsController defaultController;

    @Autowired
    BusDocInfoRepository docinfoRepo;

    @Autowired
    AccountRepository accRepo;

    @Autowired
    CountryController countryCon;

    @Autowired
    UserSession userSession;

    @Autowired
    PartialPaymentDetailRepository ppRepo;

    @Autowired
    LedgerLineRepository ledRepo;

    @Autowired
    PageAccessController pageController;

    @Autowired
    EntityManager em;

    @Autowired
    JasperPrintReportGenerator reportGenerator;

    private BusDocInfo docInfo;

    DocumentTab.MODE mode = DocumentTab.MODE.NEW;

    LinkedHashMap<String, LedgerLine> tabMap = new LinkedHashMap<>();

    private LedgerLine selectedLedgerLine;

    private List<Account> accountList;

    private List<Account> selectedAccountList;

    private String criteria = "";

    private Hashtable<String, AccountTab> tabtable = new Hashtable<>();

    private Account defaultModeAccount;

    private MenuModel printButtonModel;

    private List<Country> currencyList;
    
    private AccDoc copydoc;

    //private List<AccountTab> tabs = new LinkedList<>();
    @Autowired
    public AccountVoucherEditController(AccDocRepository repo) {
        super(AccDoc.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String m = req.getParameter("mode");

        setCurrencyList(countryCon.getCurrenyForAccountVoucher());

        if (m != null) {
            if (m.equalsIgnoreCase("n")) {   // new business document 
                String docinfo = req.getParameter("docinfoid");
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                AccDoc doc = new AccDoc();
                doc.setDocdate(new Date());
                doc.setUpdatedon(doc.getDocdate());
                doc.setBusdocinfo(docInfo);
                doc.setEmp(userSession.getLoggedInEmp());
                setSelected(doc);

                getSelected().setPaymentMethod(new PaymentMethod());
                getSelected().getPaymentMethod().setPaydate(new Date());
                getSelected().setCurrency(countryCon.findCountryDefault());

                if (doc.getBusdocinfo().isAccountDebitMode()) { //receipt
                    setDefaultModeAccount(doc.getBusdocinfo().getDebitaccountid());
                }
                if (doc.getBusdocinfo().isAccountCreditMode()) {    //payment
                    setDefaultModeAccount(doc.getBusdocinfo().getCreditaccountid());
                }

                mode = DocumentTab.MODE.NEW;

                //docdate = getSelected().getDocdate();
            } else if (m.equalsIgnoreCase("c")) {
                String docno = req.getParameter("docno");
                if (docno != null) {
                    AccDoc doc = repo.getOne(docno);
                    docInfo = doc.getBusdocinfo();
                    copyDocument(doc);
                }

                if (getSelected().getPaymentMethod() == null) {
                    getSelected().setPaymentMethod(new PaymentMethod());
                    getSelected().getPaymentMethod().setPaydate(new Date());
                }

                //remove items if Debit or Credit Mode
                if (getSelected().getBusdocinfo().isAccountDebitMode()) { //payment
                    getSelected().getLedlines().removeIf(s -> s.getDebit() > 0);
                    setDefaultModeAccount(getSelected().getBusdocinfo().getDebitaccountid());
                }
                if (getSelected().getBusdocinfo().isAccountCreditMode()) {    //receipt
                    getSelected().getLedlines().removeIf(s -> s.getCredit() > 0);
                    setDefaultModeAccount(getSelected().getBusdocinfo().getCreditaccountid());
                }

                mode = DocumentTab.MODE.NEW;
                //docdate = getSelected().getDocdate();
                for (LedgerLine ll : getSelected().getLedlines()) {
                    addTab(ll);
                }

                //docdate = getSelected().getDocdate();
            } else {        //edit mode=e
                String docno = req.getParameter("docno");
                if (docno != null) {
                    setSelected(repo.getOne(docno));
                    docInfo = getSelected().getBusdocinfo();
                }

                if (getSelected().getPaymentMethod() == null) {
                    getSelected().setPaymentMethod(new PaymentMethod());
                    getSelected().getPaymentMethod().setPaydate(new Date());
                }

                //remove items if Debit or Credit Mode
                if (getSelected().getBusdocinfo().isAccountDebitMode()) { //payment
                    getSelected().getLedlines().removeIf(s -> s.getDebit() > 0);
                    setDefaultModeAccount(getSelected().getBusdocinfo().getDebitaccountid());
                }
                if (getSelected().getBusdocinfo().isAccountCreditMode()) {    //receipt
                    getSelected().getLedlines().removeIf(s -> s.getCredit() > 0);
                    setDefaultModeAccount(getSelected().getBusdocinfo().getCreditaccountid());
                }

                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
                for (LedgerLine ll : getSelected().getLedlines()) {
                    addTab(ll);
                }
                setupPrintMenu();
            }
            pageController.hasAccess(docInfo);
        }
        showGrowl();
    }

    public void copyDocument(){
        if(copydoc==null){
           JsfUtil.addErrorMessage("Invalid document to copy");
            return;
        }
        copyDocument(copydoc, docInfo);
    }
    
    public void copyDocument(AccDoc doc){
        copyDocument(doc, doc.getBusdocinfo());
    }
    
    public void copyDocument(AccDoc doc, BusDocInfo targetInfo) {
        if(doc==null){
            JsfUtil.addErrorMessage("Invalid document to copy");
            return;
        }
        AccDoc newdoc = AccDoc.clone(doc);
        newdoc.setBusdocinfo(targetInfo);
        setSelected(newdoc);
        //System.out.println("CopyDocument: " + getSelected().getDescription());
        if (newdoc.getPaymentMethod() == null) {
            newdoc.setPaymentMethod(new PaymentMethod());
            newdoc.getPaymentMethod().setPaydate(new Date());
        }

        //remove items if Debit or Credit Mode
        if (newdoc.getBusdocinfo().isAccountDebitMode()) { //payment
            newdoc.getLedlines().removeIf(s -> s.getDebit() > 0);
            setDefaultModeAccount(newdoc.getBusdocinfo().getDebitaccountid());
        }
        if (newdoc.getBusdocinfo().isAccountCreditMode()) {    //receipt
            newdoc.getLedlines().removeIf(s -> s.getCredit() > 0);
            setDefaultModeAccount(newdoc.getBusdocinfo().getCreditaccountid());
        }

        mode = DocumentTab.MODE.NEW;
        //docdate = getSelected().getDocdate();
        for (LedgerLine ll : getSelected().getLedlines()) {
            addTab(ll);
        }
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
    
    public List<AccDoc> completeFilterBusDoc(String criteria) {
        return repo.findByAccDocByPrefix(criteria);
    }


    @Transactional
    public void save() {
        if(getSelected().getLedlines()==null || getSelected().getLedlines().isEmpty()){
            JsfUtil.addErrorMessage("No Transactions found");
            return;
        }
        
        Double diff = Math.abs(getSelected().getTotalDebit()-getSelected().getTotalCredit());
        String accuracy = defaultController.getSystemPropertyValue("AccountTransactionAccuracy");
        if(accuracy == null){
            accuracy = "0.0001";
        }
        if(diff>Double.parseDouble(accuracy)){
            JsfUtil.addErrorMessage("Total Credit doesn't match with Total Debit");
            return;
        }
        //if(!Objects.equals(getSelected().getTotalDebit(), getSelected().getTotalCredit())){
        //    JsfUtil.addErrorMessage("Total Credit doesn't match with Total Debit");
        //    return;
        //}
        
        getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            getSelected().setCreatedon(new Date());
        }
        getSelected().setUpdatedon(new Date());
        if (getSelected().getBranch() == null) {
            getSelected().setBranch(userSession.getLoggedInBranch());
        }
        if (getSelected().getEmp() == null) {
            getSelected().setEmp(userSession.getLoggedInEmp());
        }
        getSelected().setBusdocinfo(docInfo);
        for (LedgerLine ll : getSelected().getLedlines()) {
            ll.setTransdate(getSelected().getDocdate());
        }

        //update ledger lines if Debit or Credit mode
        if (getSelected().getBusdocinfo().isAccountModeVisible()) {
            LedgerLine ll = new LedgerLine();
            ll.setAccdoc(getSelected());
            ll.setAccount(defaultModeAccount);
            ll.setBranch(userSession.getLoggedInBranch());
            ll.setTransdate(getSelected().getDocdate());

            if (getSelected().getBusdocinfo().isAccountDebitMode()) { //receipt
                ll.setFcdebit(getFcCreditTotal());
                ll.setDebit(getCreditTotal() * ll.getRate());
            }
            if (getSelected().getBusdocinfo().isAccountCreditMode()) {    //payment
                ll.setFccredit(getFcDebitTotal());
                ll.setCredit(getDebitTotal() * ll.getRate());
            }

            getSelected().addLedline(ll);
        }

        repo.save(getSelected());

        for (AccountTab tab : tabtable.values()) {
            if (tab.getDeletedPP() != null) {
                tab.getDeletedPP().forEach((pp) -> {
                    //System.out.println("Deleting_PP: " + pp.getPplineno());
                    //ppRepo.deleteById(pp.getPplineno());
                    //ppRepo.flush();
                    em.createQuery("delete from PartialPaymentDetail as p where p.pplineno = :id")
                            .setParameter("id", pp.getPplineno())
                            .executeUpdate();
                });
            }
        }

        JsfUtil.addSuccessMessage("Success", getSelected().getDocno() + " saved successfuly");
        mode = DocumentTab.MODE.EDIT;
        
        //JsonUtils.writeJson("D:\\brt.json", getSelected());

        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("addedWithSuccess", "true");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/" + getDocInfo().getDocediturl() + "?mode=e&docno=" + getSelected().getDocno());
        } catch (IOException ex) {
            Logger.getLogger(BusDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Double getDebitTotal() {
        if (getSelected().getLedlines() == null) {
            return 0.0;
        }
        return getSelected().getLedlines().stream().mapToDouble(a -> a.getDebit()).sum();
    }

    public Double getFcDebitTotal() {
        if (getSelected().getLedlines() == null) {
            return 0.0;
        }
        return getSelected().getLedlines().stream().mapToDouble(a -> a.getFcdebit()).sum();
    }

    public Double getCreditTotal() {
        if (getSelected().getLedlines() == null) {
            return 0.0;
        }
        return getSelected().getLedlines().stream().mapToDouble(a -> a.getCredit()).sum();
    }

    public Double getFcCreditTotal() {
        if (getSelected().getLedlines() == null) {
            return 0.0;
        }
        return getSelected().getLedlines().stream().mapToDouble(a -> a.getFccredit()).sum();
    }

    public void addTab(LedgerLine line) {
        if (line.getAccount().getAccounttype().getName().equalsIgnoreCase("Debtors")
                || line.getAccount().getAccounttype().getName().equalsIgnoreCase("Creditors")) {
            AccountTab tab = new AccountTab(line, line.getAccount().getParentid().getAccountname() + " - " + line.getAccount().getAccountname(), "", DocumentTab.MODE.NONE);
            tab.setPendingDocs(findPendingDocuments(line.getAccount()));
            //getTabs().add(tab);
            if (!tabtable.contains(line.getAccount().getAccountid())) {
                tabtable.put(line.getAccount().getAccountid(), tab);
            }
        }
    }

    @Transactional
    public void deleteTransactions() {
        if (getSelectedLedgerLine() != null) {
            getSelected().removeLedline(getSelectedLedgerLine());
            tabtable.remove(getSelectedLedgerLine().getAccount().getAccountid());
            ledRepo.delete(selectedLedgerLine);
        }
    }

    public void updateCurrency(LedgerLine led) {
        //System.out.println("updateCurrency: " + getSelected().getCurrency());
        //getSelected().setRate(getSelected().getCurrency().getRate());
        led.setRate(led.getCurrency().getRate());
    }

    public void docdateChange(SelectEvent event) {
        getSelected().setDocdate(DateUtil.setCurrentTime((Date) event.getObject()));
    }

    public String getTabTitle() {
        if (mode == DocumentTab.MODE.NEW) {
            return "New " + docInfo.getDocname();
        }
        return "Edit " + docInfo.getDocname();
    }

    public void transfer() {
        if (selectedAccountList == null || selectedAccountList.isEmpty()) {
            JsfUtil.addErrorMessage("No Accounts selected to add in document");
            return;
        }
        for (Account acc : selectedAccountList) {
            LedgerLine ll = new LedgerLine();
            ll.setAccdoc(getSelected());
            ll.setAccount(acc);
            ll.setBranch(userSession.getLoggedInBranch());
            ll.setTransdate(getSelected().getDocdate());
            ll.setCurrency(getSelected().getCurrency());
            getSelected().addLedline(ll);

            addTab(ll);
        }
    }

    public void searchAccounts() {
        if (criteria == null) {
            criteria = "";
        }
        accountList = accRepo.findAccountLeafBySearchCriteria(criteria);
    }

    public List<BusDoc> findPendingDocuments(Account acc) {
        List<BusDocInfo> doctypes = null;
        if (acc.getAccounttype().getName().equalsIgnoreCase("Debtors")) {
            //gather all sales receivable type document;
            doctypes = docinfoRepo.findBusDocInfoByTransactionType(BusDocTransactionType.ACCOUNTS_RECEIVABLE.getValue());
        } else {
            //gather all purchase payable type document
            doctypes = docinfoRepo.findBusDocInfoByTransactionType(BusDocTransactionType.ACCOUNTS_PAYABLE.getValue());
        }

        List<BusDoc> docs = null;

        String query = null;
        for (BusDocInfo info : doctypes) {
            if (query == null) {
                query = "a.docno LIKE '%" + info.getPrefix() + "%'";
            } else {
                query = query + " OR a.docno LIKE '%" + info.getPrefix() + "%'";
            }
        }
        if (query == null) {
            docs = new LinkedList();
        } else {
            docs = em.createQuery("SELECT a FROM BusDoc as a WHERE (" + query
                    + ") AND a.businesspartner.partnerid=" + acc.getBusinesspartner().getPartnerid()
                    + " ORDER by a.docdate asc")
                    .getResultList();
        }
        //remove all docs whose pending value is zero
        docs.removeIf(d -> d.getTotalPending() == 0);
        return docs;
    }

    public void callDownloadReport(MenuActionEvent menuActionEvent) {
        //Create new action event
        final ActionEvent actionEvent = new ActionEvent(menuActionEvent.getComponent());

        //Create the value expression for the download listener
        //-> is executed when calling "processAction"!
        final FacesContext context = FacesContext.getCurrentInstance();
        final String exprStr = "#{accountVoucherEditController.exportPdf}";
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
        AccDoc doc = repo.getOne(getSelected().getDocno());
        doc.getBusdocinfo().getAbbreviation();
        doc.preparePaymentDetailsForPrinting();
        return reportGenerator.downloadPdf(getSelected().getCurrentPrintReport(), doc, getSelected().getReportTitle());
    }

    public void setupPrintMenu() {
        if (getSelected().getBusdocinfo().getReportid() != null
                && getSelected().getBusdocinfo().getReportid().size() > 0) {
            setPrintButtonModel(new DefaultMenuModel());
            getSelected().getBusdocinfo().getReportid().forEach((report) -> {
                DefaultMenuItem item = new DefaultMenuItem();
                item.setCommand("#{accountVoucherEditController.callDownloadReport}");
                item.setAjax(false);
                item.setValue(report.getReportname());
                getSelected().setCurrentPrintReport(report);
                //item.setUrl("../viewer/doc/" + report.getReportid() + "/" + getSelected().getDocno());
                getPrintButtonModel().getElements().add(item);
            });
        }
    }

    public boolean isPrintDisabled() {
        return getPrintButtonModel() == null || getPrintButtonModel().getElements().isEmpty();
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

    /**
     * @return the docInfo
     */
    public BusDocInfo getDocInfo() {
        return docInfo;
    }

    /**
     * @return the selectedLedgerLine
     */
    public LedgerLine getSelectedLedgerLine() {
        return selectedLedgerLine;
    }

    /**
     * @param selectedLedgerLine the selectedLedgerLine to set
     */
    public void setSelectedLedgerLine(LedgerLine selectedLedgerLine) {
        this.selectedLedgerLine = selectedLedgerLine;
    }

    /**
     * @return the accountList
     */
    public List<Account> getAccountList() {
        return accountList;
    }

    /**
     * @param accountList the accountList to set
     */
    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the selectedAccountList
     */
    public List<Account> getSelectedAccountList() {
        return selectedAccountList;
    }

    /**
     * @param selectedAccountList the selectedAccountList to set
     */
    public void setSelectedAccountList(List<Account> selectedAccountList) {
        this.selectedAccountList = selectedAccountList;
    }

    /**
     * @return the tabs
     */
    public List<AccountTab> getTabs() {
        return new LinkedList<AccountTab>(tabtable.values());
    }

    /**
     * @return the defaultModeAccount
     */
    public Account getDefaultModeAccount() {
        return defaultModeAccount;
    }

    /**
     * @param defaultModeAccount the defaultModeAccount to set
     */
    public void setDefaultModeAccount(Account defaultModeAccount) {
        this.defaultModeAccount = defaultModeAccount;
    }

    /**
     * @return the currencyList
     */
    public List<Country> getCurrencyList() {
        return currencyList;
    }

    /**
     * @param currencyList the currencyList to set
     */
    public void setCurrencyList(List<Country> currencyList) {
        this.currencyList = currencyList;
    }

    /**
     * @return the copydoc
     */
    public AccDoc getCopydoc() {
        return copydoc;
    }

    /**
     * @param copydoc the copydoc to set
     */
    public void setCopydoc(AccDoc copydoc) {
        this.copydoc = copydoc;
    }

}
