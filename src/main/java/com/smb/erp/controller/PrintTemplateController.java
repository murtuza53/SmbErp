/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.rest.DocumentReportGenerator;
import com.smb.erp.dynamic.report.Report;
import com.smb.erp.dynamic.report.ReportField;
import com.smb.erp.dynamic.report.ReportSection;
import com.smb.erp.dynamic.report.ReportSection.SectionPrintLayout;
import com.smb.erp.dynamic.report.ReportSectionContainer;
import com.smb.erp.dynamic.report.ReportSectionContainer.ContainerPrintLayout;
import com.smb.erp.dynamic.report.ReportTable;
import com.smb.erp.dynamic.report.TableField;
import com.smb.erp.dynamic.report.TableField.ColumnFunction;
import com.smb.erp.dynamic.report.TableField.FieldFunction;
import com.smb.erp.dynamic.report.Theme;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.PrintReport;
import com.smb.erp.entity.SystemDefaults;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.util.BeanField;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.ReflectionUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import com.smb.erp.repo.PrintReportRepository;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import net.sf.dynamicreports.report.constant.PageOrientation;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;
import org.smberp.json.JsonUtils;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "printTemplateController")
@ViewScoped
public class PrintTemplateController extends AbstractController<PrintReport> {

    PrintReportRepository repo;

    @Autowired
    SystemDefaultsController systemController;

    @Autowired
    BusDocInfoRepository bdRepo;

    @Autowired
    UserSession userSession;

    @Autowired
    TableKeyController keyCon;

    private BusDocInfo busdocInfo;

    String folderLocation;

    private com.smb.erp.dynamic.report.Report dynamicReport;

    private ReportSectionContainer headerSectionContainer;

    private ReportSectionContainer footerSectionContainer;

    private ReportField selectedReportField;

    private BeanField reportTableField;

    private TableField selectedTransactionField;

    private ReportField selectedSummaryField;

    private String transactionFieldToAdd;

    private String sectionReportField;

    private ReportField selectedSignatureField;
    
    private UploadedFile file;
    
    private int progress = 0;
    
    @Autowired
    public PrintTemplateController(PrintReportRepository repo) {
        super(PrintReport.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        SystemDefaults def = systemController.getByPropertyname("PrintTemplateLocation");
        folderLocation = def.getValue();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String bdid = req.getParameter("bdid");
        System.out.println("PrintTemplateController: " + facesContext.getExternalContext().getRequestParameterMap());
        if (bdid != null) {
            setBusdocInfo(bdRepo.getOne(Integer.parseInt(bdid)));
            //invoke one field to load entity
            getBusdocInfo().getDocname();
        } else {
            JsfUtil.addErrorMessage("Document Setting not linked");
            return;
        }

        String m = req.getParameter("mode");
        if (m != null) {
            if (m.equalsIgnoreCase("n")) {   // new business document 
                setSelected(new PrintReport());
                getSelected().setJasper(false);
                getSelected().setBdinfoid(getBusdocInfo());
                getSelected().setEmpid(userSession.getLoggedInEmp());
                getSelected().setCreatedon(new Date());
                dynamicReport = new com.smb.erp.dynamic.report.Report();
                dynamicReport.setTitle(new ReportField("Report Title"));

                createHeaderSectionContainer();
                createFooterSectionContainer();
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
        }
        return repo.findAll();
    }

    public void save() {
        try {
            if (getSelected().getReportid() > 10000) { //this is new report
                getSelected().setReportid(keyCon.getNextKey("report"));
                getSelected().getBdinfoid().addReportid(getSelected());
            }

            getSelected().setReportname(getDynamicReport().getReportName());
            System.out.println("PRINT_REPORT: " + getSelected() + "\t" + getSelected().getReportname() + "\t" + getSelected().getFilename());

            if (getSelected().getFilename() == null || getSelected().getFilename().trim().length() == 0) {
                getSelected().setFilename(getSelected().getReportname() + "_" + new Date().getTime());
            }

            System.out.println("PRINT_REPORT2: " + getSelected() + "\t" + getSelected().getReportname() + "\t" + getSelected().getFilename());
            System.out.println("SavingReport: " + getSelected().getFilename() + ".rpt");
            File file = new File(folderLocation + getSelected().getFilename() + ".rpt");

            bdRepo.save(getSelected().getBdinfoid());
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
        setDynamicReport(JsonUtils.readJson(folderLocation + getSelected().getFilename() + ".rpt", Report.class));
        //setDynamicReport(XmlUtils.readFromXmlFile(folderLocation + getSelected().getFilename() + ".rpt", Report.class));
        if (getDynamicReport() != null) {
            if (getDynamicReport().getHeaderSection() != null && getDynamicReport().getHeaderSection().size() > 0) {
                headerSectionContainer = getDynamicReport().getHeaderSection().get(0);
            }
            if (getDynamicReport().getFooterSection() != null && getDynamicReport().getFooterSection().size() > 0) {
                footerSectionContainer = getDynamicReport().getFooterSection().get(0);
            }
        }
    }

    public ReportField.FieldPrintLayout[] fieldPrintLayouts() {
        return ReportField.FieldPrintLayout.values();
    }

    public ReportSectionContainer.ContainerPrintLayout[] containerPrintLayouts() {
        return ContainerPrintLayout.values();
    }

    public ReportSection.SectionPrintLayout[] sectionPrintLayouts() {
        return SectionPrintLayout.values();
    }

    public TableField.ColumnFunction[] columnFunctions() {
        return ColumnFunction.values();
    }

    public TableField.FieldFunction[] fieldFunctions() {
        return FieldFunction.values();
    }

    public com.smb.erp.dynamic.report.PageType[] pageTypes() {
        return com.smb.erp.dynamic.report.PageType.values();
    }

    public PageOrientation[] pageOrientations() {
        return PageOrientation.values();
    }

    public void print() {

    }

    public boolean getRenderHeaderSectionContainer() {
        return dynamicReport.getHeaderSection() != null && dynamicReport.getHeaderSection().size() > 0;
    }

    public boolean getRenderFooterSectionContainer() {
        return dynamicReport.getFooterSection() != null && dynamicReport.getFooterSection().size() > 0;
    }

    public void createHeaderSectionContainer() {
        if (headerSectionContainer == null) {
            if (dynamicReport.getHeaderSection() == null || dynamicReport.getHeaderSection().isEmpty()) {
                headerSectionContainer = new ReportSectionContainer();
                dynamicReport.addHeaderSection(headerSectionContainer);
            } else {
                headerSectionContainer = dynamicReport.getHeaderSection().get(0);
            }
        }
    }

    public void createFooterSectionContainer() {
        if (footerSectionContainer == null) {
            if (dynamicReport.getFooterSection() == null || dynamicReport.getFooterSection().isEmpty()) {
                footerSectionContainer = new ReportSectionContainer();
                dynamicReport.addFooterSection(footerSectionContainer);
            } else {
                footerSectionContainer = dynamicReport.getFooterSection().get(0);
            }
        }
    }

    public void createSignatureSection() {
        if (dynamicReport.getSignatureFields() == null) {
            dynamicReport.setSignatureFields(new LinkedList<>());
        }
    }

    public void createReportTable() {
        if (dynamicReport.getReportTable() == null) {
            dynamicReport.setReportTable(new ReportTable());
        }
    }

    public void createNewHeaderReportSection() {
        ReportSection sec = new ReportSection();
        headerSectionContainer.getReportSection().add(sec);
        System.out.println("headerSectionContainer.getReportSection:: " + headerSectionContainer.getReportSection().size());
    }

    public void deleteReportSection(ReportSection section){
        if(section!=null){
            headerSectionContainer.getReportSection().remove(section);
            JsfUtil.addSuccessMessage("Report Section deleted");
        }
    }
    
    public void addNewReportField(ReportSection sec) {
        System.out.println("addNewReportField: " + sec + " => " + getSectionReportField());
        if (getSectionReportField() == null || getSectionReportField().contains("Select...")) {
            JsfUtil.addErrorMessage("Invalid proptery name for Report Field");
            return;
        }
        ReportField field = new ReportField();
        field.setPropertyName(getSectionReportField());
        field.setType(ReflectionUtil.getPropertyType(BusDoc.class, getSectionReportField()));
        sec.addReportField(field);
    }

    public void deleteReportField(ReportSection sec) {
        if (selectedReportField == null) {
            JsfUtil.addErrorMessage("No field selected to delete");
            return;
        }
        sec.getReportFields().remove(selectedReportField);
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
        if(getDynamicReport()==null){
            return new LinkedList<>();
        }
        if(getDynamicReport().getReportTable()==null){
            return new LinkedList<>();
        }
        if(getDynamicReport().getReportTable().getDataClass()==null){
            return new LinkedList<>();
        }
        List<String> list = ReflectionUtil.getFields(getDynamicReport().getReportTable().getDataClass(), 2)
                .stream().map(BeanField::getProperty)
                .collect(Collectors.toList());
        //System.out.println("getTransactionFields: " + ret);
        //return ret;
        if(list==null){
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

    public void addSummaryField() {
        ReportField rf = new ReportField();
        rf.setPrintLabel(true);
        rf.setPrintLayout(ReportField.FieldPrintLayout.TOP_BOTTOM);
        getDynamicReport().getReportTable().addSummaryField(rf);
    }

    public void deletedSummaryField() {
        if (selectedSummaryField == null) {
            JsfUtil.addErrorMessage("No field selected to delete");
            return;
        }
        getDynamicReport().getReportTable().removeSummaryField(selectedSummaryField);
    }

    public void addSignatureField() {
        ReportField rf = new ReportField();
        getDynamicReport().addSignatureField(rf);
    }

    public void deleteSignatureField() {
        if (getSelectedSignatureField() == null) {
            JsfUtil.addErrorMessage("No signature selected to delete");
            return;
        }
        getDynamicReport().removeSignatureField(getSelectedSignatureField());
    }

    public void handleFileUpload() {
        //setFile(event.getFile());
        System.out.println("handleFileUpload: " + getFile());
        File f = DataImportController.saveUploadedFile(getFile());
        
        setDynamicReport(JsonUtils.readJson(f.getAbsolutePath(), Report.class));
        
        JsfUtil.addSuccessMessage("Report uploaded successfuly");
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
    public com.smb.erp.dynamic.report.Report getDynamicReport() {
        return dynamicReport;
    }

    /**
     * @param dynamicReport the dynamicReport to set
     */
    public void setDynamicReport(com.smb.erp.dynamic.report.Report dynamicReport) {
        this.dynamicReport = dynamicReport;
    }

    /**
     * @return the headerSectionContainer
     */
    public ReportSectionContainer getHeaderSectionContainer() {
        return headerSectionContainer;
    }

    /**
     * @param headerSectionContainer the headerSectionContainer to set
     */
    public void setHeaderSectionContainer(ReportSectionContainer headerSectionContainer) {
        this.headerSectionContainer = headerSectionContainer;
    }

    /**
     * @return the footerSectionContainer
     */
    public ReportSectionContainer getFooterSectionContainer() {
        return footerSectionContainer;
    }

    /**
     * @param footerSectionContainer the footerSectionContainer to set
     */
    public void setFooterSectionContainer(ReportSectionContainer footerSectionContainer) {
        this.footerSectionContainer = footerSectionContainer;
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
     * @return the selectedSummaryField
     */
    public ReportField getSelectedSummaryField() {
        return selectedSummaryField;
    }

    /**
     * @param selectedSummaryField the selectedSummaryField to set
     */
    public void setSelectedSummaryField(ReportField selectedSummaryField) {
        this.selectedSummaryField = selectedSummaryField;
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
        System.out.println("setSectionReportField: " + sectionReportField);
    }

    /**
     * @return the selectedSignatureField
     */
    public ReportField getSelectedSignatureField() {
        return selectedSignatureField;
    }

    /**
     * @param selectedSignatureField the selectedSignatureField to set
     */
    public void setSelectedSignatureField(ReportField selectedSignatureField) {
        this.selectedSignatureField = selectedSignatureField;
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
}
