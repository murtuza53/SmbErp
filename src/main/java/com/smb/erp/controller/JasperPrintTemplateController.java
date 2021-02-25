/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.rest.DocumentReportGenerator;
import com.smb.erp.dynamic.report.JasperReportFile;
import com.smb.erp.dynamic.report.ReportField;
import com.smb.erp.dynamic.report.ReportTable;
import com.smb.erp.dynamic.report.TableField;
import com.smb.erp.dynamic.report.Theme;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.PrintReport;
import com.smb.erp.entity.SystemDefaults;
import com.smb.erp.entity.Webpage;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.PrintReportRepository;
import com.smb.erp.repo.WebpageRepository;
import com.smb.erp.util.BeanField;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.ReflectionUtil;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;
import org.smberp.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author admin
 */
@Named(value = "jasperprintTemplateController")
@ViewScoped
public class JasperPrintTemplateController extends AbstractController<PrintReport> {

    PrintReportRepository repo;

    @Autowired
    SystemDefaultsController systemController;

    @Autowired
    BusDocInfoRepository bdRepo;

    @Autowired
    WebpageRepository pageRepo;

    @Autowired
    UserSession userSession;

    @Autowired
    TableKeyController keyCon;

    private BusDocInfo busdocInfo;

    private Webpage webpage;

    String folderLocation;

    private JasperReportFile dynamicReport;

    private ReportField selectedReportField;

    private BeanField reportTableField;

    private TableField selectedTransactionField;

    private String transactionFieldToAdd;

    private String sectionReportField;

    private UploadedFile file;

    private int progress = 0;

    @Autowired
    public JasperPrintTemplateController(PrintReportRepository repo) {
        super(PrintReport.class, repo);
        this.repo = repo;
        System.out.println("JasperPrintTemplateController");
    }

    @PostConstruct
    public void init() {
        SystemDefaults def = systemController.getByPropertyname("PrintTemplateLocation");
        folderLocation = def.getValue();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        //Map<String, String> req = facesContext.getExternalContext().getRequestParameterMap();
        System.out.println("JasperTemplateController_init: " + facesContext.getExternalContext().getRequestParameterMap());
        String bdid = req.getParameter("bdid");
        String pageid = req.getParameter("pageid");
        if (bdid != null) {
            setBusdocInfo(bdRepo.getOne(Integer.parseInt(bdid)));
            //invoke one field to load entity
            getBusdocInfo().getDocname();
        } else if (pageid != null) {
            setWebpage(pageRepo.getOne(Long.parseLong(pageid)));
            getWebpage().getPageurl();
        } else {
            JsfUtil.addErrorMessage("Document Setting not linked");
            return;
        }

        String m = req.getParameter("mode");
        System.out.println("JasperTemplateController_init2: " + m + "\tbdinfo: " + getBusdocInfo() + "\twebpage: " + getWebpage());
        if (m != null) {
            if (m.equalsIgnoreCase("n")) {   // new business document 
                setSelected(new PrintReport());
                getSelected().setJasper(true);
                getSelected().setBdinfoid(getBusdocInfo());
                getSelected().setPageid(webpage);
                getSelected().setEmpid(userSession.getLoggedInEmp());
                getSelected().setCreatedon(new Date());
                dynamicReport = new JasperReportFile();
                dynamicReport.setTitle(new ReportField("Report Title"));

                createReportTable();

                mode = DocumentTab.MODE.NEW;
            } else {        //edit mode=e
                Long docno = Long.parseLong(req.getParameter("reportid"));
                if (docno != null) {
                    setSelected(repo.getOne(docno));
                    getSelected().getFilename();
                    loadReport();
                    //if (dynamicReport.getReportTable() != null) {
                    //    reportTableField = new BeanField(dynamicReport.getReportTable().getDataPropertyName(), dynamicReport.getReportTable().getDataClass());
                    //}
                    //System.out.println("HEADER_SECTION: " + dynamicReport.getHeaderSection().size());
                }
                mode = DocumentTab.MODE.EDIT;
                //docdate = getSelected().getDocdate();
            }
        }
    }

    @Override
    public List<PrintReport> getItems() {
        if (busdocInfo != null) {
            return repo.findReportByBdinfoid(busdocInfo.getBdinfoid());
        } else if (webpage != null) {
            return repo.findReportByPageid(webpage.getPageid());
        }
        return repo.findAll();
    }

    @Override
    public void save() {
        try {
            if (getSelected().getReportid() > 10000) { //this is new report
                getSelected().setReportid(keyCon.getNextKey("report"));
                if (getSelected().getBdinfoid() != null) {
                    getSelected().getBdinfoid().addReportid(getSelected());
                }
                if (getSelected().getPageid() != null) {
                    getSelected().getPageid().addReportid(getSelected());
                }
            }

            getSelected().setReportname(getDynamicReport().getReportName());
            System.out.println("PRINT_REPORT: " + getSelected() + "\t" + getSelected().getReportname() + "\t" + getSelected().getFilename());

            if (getSelected().getFilename() == null || getSelected().getFilename().trim().length() == 0) {
                getSelected().setFilename(getSelected().getReportname() + "_" + new Date().getTime());
            }

            System.out.println("PRINT_REPORT2: " + getSelected() + "\t" + getSelected().getReportname() + "\t" + getSelected().getFilename());
            System.out.println("SavingReport: " + getSelected().getFilename() + ".rpt");
            File file = new File(folderLocation + getSelected().getFilename() + ".rpt");

            if (getSelected().getBdinfoid() != null) {
                bdRepo.save(getSelected().getBdinfoid());
            }
            if(getSelected().getPageid()!=null){
                pageRepo.save(getSelected().getPageid());
            }
            //IOUtils.copy(prepareReport(busdoc), new FileOutputStream(file));
            JsonUtils.writeJson(folderLocation + getSelected().getFilename() + ".rpt", getDynamicReport());
            //XmlUtils.writeToXmlFile(folderLocation + getSelected().getFilename() + ".rpt", getDynamicReport());

            System.out.println("saveReport: " + file.getAbsolutePath());

            JsfUtil.addSuccessMessage(getSelected().getReportname() + " saved successfuly");
            mode = DocumentTab.MODE.EDIT;
            //FacesContext facesContext = FacesContext.getCurrentInstance();
            //facesContext.getExternalContext().redirect("printdesigner.xhtml?mode=e&bdid=" + getSelected().getBdinfoid().getBdinfoid() + "&reportid=" + getSelected().getReportid());
        } catch (Exception ex) {
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean getDisableJasperUpload() {
        if (getSelected().getReportid() > 10000) {
            return true;
        }
        return false;
    }

    public String getTabTitle() {
        if (mode == DocumentTab.MODE.NEW) {
            return "New - Print Designer";
        } else {
            return "Edit - " + getSelected().getReportname();
        }
    }

    public void loadReport() {
        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Unable to load report: " + getSelected().getFilename() + ".rpt");
        }
        setDynamicReport(JsonUtils.readJson(folderLocation + getSelected().getFilename() + ".rpt", JasperReportFile.class));
        //setDynamicReport(XmlUtils.readFromXmlFile(folderLocation + getSelected().getFilename() + ".rpt", Report.class));
    }

    public ReportField.FieldPrintLayout[] fieldPrintLayouts() {
        return ReportField.FieldPrintLayout.values();
    }

    public TableField.ColumnFunction[] columnFunctions() {
        return TableField.ColumnFunction.values();
    }

    public TableField.FieldFunction[] fieldFunctions() {
        return TableField.FieldFunction.values();
    }

    public void print() {

    }

    public void createReportTable() {
        if (dynamicReport.getReportTable() == null) {
            dynamicReport.setReportTable(new ReportTable());
        }
    }

    public void updateReportFieldType(ReportField field) {
        field.setType(ReflectionUtil.getPropertyType(BusDoc.class, field.getPropertyName()));
    }

    public void addNewReportField() {
        System.out.println("addNewReportField: " + getSectionReportField());
        if (getSectionReportField() == null || getSectionReportField().isEmpty() || getSectionReportField().contains("Select...")) {
            setSectionReportField(null);
            JsfUtil.addErrorMessage("Invalid proptery name for Report Field");
            return;
        }
        ReportField field = new ReportField();
        field.setPropertyName(getSectionReportField());
        field.setType(ReflectionUtil.getPropertyType(BusDoc.class, getSectionReportField()));
        getDynamicReport().getParameters().add(field);
    }

    public void deleteReportField() {
        if (selectedReportField == null) {
            JsfUtil.addErrorMessage("No field selected to delete");
            return;
        }
        getDynamicReport().getParameters().remove(selectedReportField);
        JsfUtil.addSuccessMessage("Deleted Successfuly");
    }

    public void onRowSelect_ReportField(SelectEvent event) {
        System.out.println("onRowSelect_ReportField: " + selectedReportField);
    }

    public List<String> getThemeStyles() {
        return ReflectionUtil.getFields(Theme.class, 1)
                .stream().map(BeanField::getProperty)
                .collect(Collectors.toList());
    }

    public List<String> getFields() {
        return ReflectionUtil.getFields(BusDoc.class, 2)
                .stream().map(BeanField::getProperty)
                .collect(Collectors.toList());
    }

    public List<BeanField> getFields_ListType() {
        return ReflectionUtil.getFields_ListType(BusDoc.class);
    }

    public void onReportTableFieldSelectedListener(SelectEvent event) {
        if (getReportTableField() != null) {
            getDynamicReport().getReportTable().setDataPropertyName(getReportTableField().getProperty());
            getDynamicReport().getReportTable().setDataClass(getReportTableField().getType());
        }
    }

    //public List<BeanField> getTransactionFields() {
    //    return ReflectionUtil.getFields(getDynamicReport().getReportTable().getDataClass(), 2);
    //}
    public List<String> getTransactionFields() {
        if (getDynamicReport() == null) {
            return new LinkedList<>();
        }
        if (getDynamicReport().getReportTable() == null) {
            return new LinkedList<>();
        }
        if (getDynamicReport().getReportTable().getDataClass() == null) {
            return new LinkedList<>();
        }
        List<String> list = ReflectionUtil.getFields(getDynamicReport().getReportTable().getDataClass(), 2)
                .stream().map(BeanField::getProperty)
                .collect(Collectors.toList());
        //System.out.println("getTransactionFields: " + ret);
        //return ret;
        if (list == null) {
            return new LinkedList<>();
        }
        return list;
    }

    public void addNewTransactionField() {
        TableField field = new TableField();
        getDynamicReport().getReportTable().addTableField(field);
    }

    public void addTransactionField() {
        addTransactionField(null);
    }

    public void addTransactionField(SelectEvent event) {
        System.out.println("addTransactionField: " + transactionFieldToAdd);
        if (getDynamicReport().getReportTable() == null) {
            JsfUtil.addErrorMessage("Transaction table not initalized");
            return;
        }
        if (getDynamicReport().getReportTable().getDataClass() == null) {
            JsfUtil.addErrorMessage("Transaction Class still not selected");
            return;
        }
        if (transactionFieldToAdd != null) {
            TableField field = new TableField();
            field.setPropertyName(transactionFieldToAdd);
            field.setType(ReflectionUtil.getPropertyType(getDynamicReport().getReportTable().getDataClass(), transactionFieldToAdd));
            getDynamicReport().getReportTable().addTableField(field);
        }
    }

    public void deleteTransactionField() {
        if (selectedTransactionField == null) {
            JsfUtil.addErrorMessage("No transaction selected to delete");
            return;
        }
        getDynamicReport().getReportTable().removeTableField(selectedTransactionField);
        JsfUtil.addSuccessMessage("Deleted Successfuly");
    }

    public void handleJasperFileUpload() {
        //setFile(event.getFile());
        String fileName = folderLocation + "jrxml/" + getSelected().getFilename() + ".jrxml";

        System.out.println("handleJasperFileUpload: " + getFile().getSize() + " => " + fileName);

        File f = DataImportController.saveUploadedFile(getFile(), new File(fileName));

        //setDynamicReport(JsonUtils.readJson(f.getAbsolutePath(), Report.class));
        getSelected().setJasperfile(getSelected().getFilename());
        repo.save(getSelected());

        JsfUtil.addSuccessMessage("Jasper Report uploaded successfuly");
    }

    /**
     * @return the busdocInfo
     */
    public BusDocInfo getBusdocInfo() {
        return busdocInfo;
    }

    /**
     * @param busdocInfo the busdocInfo to set
     */
    public void setBusdocInfo(BusDocInfo busdocInfo) {
        this.busdocInfo = busdocInfo;
    }

    /**
     * @return the dynamicReport
     */
    public JasperReportFile getDynamicReport() {
        return dynamicReport;
    }

    /**
     * @param dynamicReport the dynamicReport to set
     */
    public void setDynamicReport(JasperReportFile dynamicReport) {
        this.dynamicReport = dynamicReport;
    }

    /**
     * @return the selectedReportField
     */
    public ReportField getSelectedReportField() {
        return selectedReportField;
    }

    /**
     * @param selectedReportField the selectedReportField to set
     */
    public void setSelectedReportField(ReportField selectedReportField) {
        this.selectedReportField = selectedReportField;
        System.out.println("setSelectedReportField: " + selectedReportField);
    }

    /**
     * @return the reportTableField
     */
    public BeanField getReportTableField() {
        return reportTableField;
    }

    /**
     * @param reportTableField the reportTableField to set
     */
    public void setReportTableField(BeanField reportTableField) {
        this.reportTableField = reportTableField;
    }

    /**
     * @return the selectedTransactionField
     */
    public TableField getSelectedTransactionField() {
        return selectedTransactionField;
    }

    /**
     * @param selectedTransactionField the selectedTransactionField to set
     */
    public void setSelectedTransactionField(TableField selectedTransactionField) {
        this.selectedTransactionField = selectedTransactionField;
    }

    /**
     * @return the transactionFieldToAdd
     */
    public String getTransactionFieldToAdd() {
        return transactionFieldToAdd;
    }

    /**
     * @param transactionFieldToAdd the transactionFieldToAdd to set
     */
    public void setTransactionFieldToAdd(String transactionFieldToAdd) {
        this.transactionFieldToAdd = transactionFieldToAdd;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * @return the sectionReportField
     */
    public String getSectionReportField() {
        return sectionReportField;
    }

    /**
     * @param sectionReportField the sectionReportField to set
     */
    public void setSectionReportField(String sectionReportField) {
        this.sectionReportField = sectionReportField;
    }

    /**
     * @return the webpage
     */
    public Webpage getWebpage() {
        return webpage;
    }

    /**
     * @param webpage the webpage to set
     */
    public void setWebpage(Webpage webpage) {
        this.webpage = webpage;
    }

}
