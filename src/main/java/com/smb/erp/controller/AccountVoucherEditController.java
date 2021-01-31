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
import com.smb.erp.entity.LedgerLine;
import com.smb.erp.repo.AccDocRepository;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.LedgerLineRepository;
import com.smb.erp.repo.PartialPaymentDetailRepository;
import com.smb.erp.util.DateUtil;
import com.smb.erp.util.JsfUtil;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
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
    UserSession userSession;

    @Autowired
    PartialPaymentDetailRepository ppRepo;

    @Autowired
    LedgerLineRepository ledRepo;

    @Autowired
    EntityManager em;

    private BusDocInfo docInfo;

    DocumentTab.MODE mode = DocumentTab.MODE.NEW;

    LinkedHashMap<String, LedgerLine> tabMap = new LinkedHashMap<>();

    private LedgerLine selectedLedgerLine;

    private List<Account> accountList;

    private List<Account> selectedAccountList;

    private String criteria = "";

    private Hashtable<String, AccountTab> tabtable = new Hashtable<>();

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

        if (m != null) {
            if (m.equalsIgnoreCase("n")) {   // new business document 
                String docinfo = req.getParameter("docinfoid");
                docInfo = docinfoRepo.getOne(Integer.parseInt(docinfo));
                AccDoc doc = new AccDoc();
                doc.setDocdate(new Date());
                doc.setUpdatedon(doc.getDocdate());
                doc.setBusdocinfo(docInfo);
                setSelected(doc);
                mode = DocumentTab.MODE.NEW;

                //docdate = getSelected().getDocdate();
            } else {        //edit mode=e
                String docno = req.getParameter("docno");
                if (docno != null) {
                    setSelected(repo.getOne(docno));
                    docInfo = getSelected().getBusdocinfo();
                }
                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
                for (LedgerLine ll : getSelected().getLedlines()) {
                    addTab(ll);
                }
            }
        }
    }

    @Transactional
    public void save() {
        getSelected().setDocdate(DateUtil.setCurrentTime(getSelected().getDocdate()));
        if (mode == DocumentTab.MODE.NEW) {
            getSelected().setDocno(keyCon.getDocNo(getSelected().getBusdocinfo().getPrefix(), DateUtil.getYear(getSelected().getDocdate())));
            getSelected().setCreatedon(new Date());
        }
        getSelected().setUpdatedon(new Date());
        getSelected().setBranch(userSession.getLoggedInBranch());
        getSelected().setBusdocinfo(docInfo);
        for (LedgerLine ll : getSelected().getLedlines()) {
            ll.setTransdate(getSelected().getDocdate());
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
        return docs;
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

}
