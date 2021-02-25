/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.rest;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import com.smb.erp.UserSession;
import com.smb.erp.controller.SystemDefaultsController;
import com.smb.erp.dynamic.report.ColorScheme;
import com.smb.erp.dynamic.report.ColorSchemeManager;
import com.smb.erp.dynamic.report.DefaultReportTheme;
import com.smb.erp.dynamic.report.JasperReportFile;
import com.smb.erp.dynamic.report.Report;
import com.smb.erp.dynamic.report.ReportField;
import com.smb.erp.dynamic.report.ReportGenerator;
import com.smb.erp.dynamic.report.ReportSection;
import com.smb.erp.dynamic.report.ReportSectionContainer;
import com.smb.erp.dynamic.report.ReportTable;
import com.smb.erp.dynamic.report.TableField;
import com.smb.erp.dynamic.report.Theme;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.PrintReport;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.PrintReportRepository;
import com.smb.erp.report.JasperBeanTableModel;
import com.smb.erp.util.EvaluateExpression;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.Utils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import net.sf.dynamicreports.report.base.DRMargin;
import net.sf.dynamicreports.report.base.DRPage;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.smberp.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Burhani152
 */
@RestController
@RequestMapping("viewer")
public class DocumentReportGenerator implements Serializable {

    @Autowired
    UserSession userSession;

    @Autowired
    BusDocRepository busRepo;

    @Autowired
    PrintReportRepository printRepo;

    @Autowired
    SystemDefaultsController systemController;

    private DefaultReportTheme reportTheme = new DefaultReportTheme(new ColorScheme());

    private BusDoc busdoc;

    private PrintReport printReport;

    String schemename = "Printed Stationary";

    public DocumentReportGenerator() {
    }

    //@PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //Object obj = facesContext.getExternalContext().getRequestMap().get("doc");
        HttpServletRequest req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        System.out.println("DocumentReportGenerator_Init: " + req.getParameterNames());
        String obj = req.getParameter("doc");
        if (obj == null) {
            JsfUtil.addErrorMessage("No documents attached to view");
            System.out.println("No documents attached to view");
            Optional<BusDoc> doc = busRepo.findById("DN202014");
            if (doc.isPresent()) {
                System.out.println("Fetched: " + doc);
                setBusdoc(doc.get());
            } else {
                System.out.println("Invalid document " + obj);
                JsfUtil.addErrorMessage("Invalid document " + obj);
            }
        } else {
            Optional<BusDoc> doc = busRepo.findById(obj);
            if (doc.isPresent()) {
                System.out.println("Fetched: " + doc);
                setBusdoc(doc.get());
            } else {
                System.out.println("Invalid document " + obj);
                JsfUtil.addErrorMessage("Invalid document " + obj);
            }
        }
        schemename = systemController.getByPropertyname("DefPrintTheme").getValue();
        System.out.println("DocumentReportGenerator_init(): " + getBusdoc());
        System.out.println("Scehem Name: " + schemename);
    }

    @RequestMapping(value = "/doc2/{fileName:.+}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public StreamedContent downloadReport2(@PathVariable("fileName") String fileName) {

        System.out.println("downloadRoport: " + fileName);
        ByteArrayInputStream bis = null;
        String msg = "";
        if (fileName == null) {
            msg = "Invalid document " + fileName;
            JsfUtil.addErrorMessage(msg);
            System.out.println(msg);
            bis = new ByteArrayInputStream(msg.getBytes());
        } else {
            busdoc = loadBusDoc(fileName);
            if (busdoc == null) {
                msg = "Unable to load " + fileName;
                JsfUtil.addErrorMessage(msg);
                System.out.println(msg);
                bis = new ByteArrayInputStream(msg.getBytes());
            } else {
                setBusdoc(busdoc);
                bis = prepareReport(getBusdoc(), printReport);
            }
        }

        //return new DefaultStreamedContent(bis, "application/pdf", busdoc.getDocno() + ".pdf");
        final ByteArrayInputStream bis2 = bis;
        return new DefaultStreamedContent().builder()
                .name(busdoc.getDocno() + ".pdf")
                .contentType("application/pdf")
                .stream(() -> bis2)
                .build();

    }

    @RequestMapping(value = "/doc/{templateNo}/{docNo}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable("templateNo") String templateNo, @PathVariable("docNo") String docNo) {

        //System.out.println("downloadRoport: " + fileName);
        ByteArrayInputStream bis = null;
        String msg = "";
        if (docNo == null) {
            msg = "Invalid document " + docNo;
            JsfUtil.addErrorMessage(msg);
            System.out.println(msg);
            bis = new ByteArrayInputStream(msg.getBytes());
        } else {
            printReport = loadPrintTemplate(templateNo);
            if (printReport == null) {
                msg = "Unable to load Print Template " + templateNo;
                JsfUtil.addErrorMessage(msg);
                System.out.println(msg);
                bis = new ByteArrayInputStream(msg.getBytes());
            } else {
                busdoc = loadBusDoc(docNo);
                if (busdoc == null) {
                    msg = "Unable to load " + docNo;
                    JsfUtil.addErrorMessage(msg);
                    System.out.println(msg);
                    bis = new ByteArrayInputStream(msg.getBytes());
                } else {
                    setBusdoc(busdoc);
                    bis = generateReport(getBusdoc(), printReport);
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "inline; filename=" + docNo + ".pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        System.out.println("BIS: " + bis);

        ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(new InputStreamResource(bis), headers, HttpStatus.OK);
        return response;
    }

    public PrintReport loadPrintTemplate(String bdinfoid) {
        Optional<PrintReport> print = printRepo.findById(Long.parseLong(bdinfoid));
        if (print.isPresent()) {
            return print.get();
        }
        return null;
    }

    public BusDoc loadBusDoc(String docNo) {
        Optional<BusDoc> doc = busRepo.findById(docNo);
        if (doc.isPresent()) {
            System.out.println("Fetched: " + doc.get());
            return doc.get();
        }
        return null;
    }

    public void saveReport() {
        try {
            System.out.println("SavingReportFor: " + busdoc);
            File file = File.createTempFile(busdoc.getDocno(), ".pdf");

            //IOUtils.copy(prepareReport(busdoc), new FileOutputStream(file));
            System.out.println("saveReport: " + file.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ByteArrayInputStream getReport() {
        return prepareReport(busdoc, printReport);
    }

    public ByteArrayInputStream generateReport(BusDoc doc, PrintReport pr) {
        if (pr.getJasper()) {
            return prepareJasperReport(doc, pr);
        }
        return prepareReport(doc, pr);
    }

    public ByteArrayInputStream prepareJasperReport(BusDoc doc, PrintReport pr) {
        ByteArrayInputStream content = null;
        String folderLocation = systemController.getByPropertyname("PrintTemplateLocation").getValue();
        File file = new File(folderLocation + "jrxml/" + pr.getJasperfile() + ".jrxml");
        System.out.println("prepareJasperReport: " + file.getAbsolutePath());
        JasperReportFile report = JsonUtils.readJson(folderLocation + pr.getFilename() + ".rpt", JasperReportFile.class);

        try {
            JasperDesign design = JRXmlLoader.load(file);
            JasperReport jreport = JasperCompileManager.compileReport(design);
            JasperPrint print = JasperFillManager.fillReport(jreport, prepareParameters(doc, report),
                    new JRTableModelDataSource(new JasperBeanTableModel(report.getReportTable().getDataClass(), report.getReportTable().getFieldList(), doc.getProductTransactions())));
            content = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print));
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            content = new ByteArrayInputStream(ex.getMessage().getBytes());
        } finally {
            return content;
        }
    }

    public ByteArrayInputStream prepareDynamicJasperReport(BusDoc doc, PrintReport pr) {
        ByteArrayInputStream content = null;
        String folderLocation = systemController.getByPropertyname("PrintTemplateLocation").getValue();
        File file = new File(folderLocation + "jrxml/" + pr.getJasperfile() + ".jrxml");
        System.out.println("prepareJasperReport: " + file.getAbsolutePath());
        JasperReportFile report = JsonUtils.readJson(folderLocation + pr.getFilename() + ".rpt", JasperReportFile.class);
        try {
            FileInputStream is = new FileInputStream(file);
            JasperPrint print = report().setTemplateDesign(is)
                    //.parameters(prepareParameters(doc, report))
                    .setParameters(prepareParameters(doc, report))
                    .columns(prepareColumns(doc, report))
                    .setDataSource(doc.getProductTransactions())
                    .toJasperPrint();
            content = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print));
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            content = new ByteArrayInputStream(ex.getMessage().getBytes());
        } finally {
            return content;
        }
    }

    public Map<String, Object> prepareParameters(BusDoc doc, JasperReportFile report) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> params = new HashMap<>();
        for (ReportField field : report.getParameters()) {
            //params.put(field.getLabel(), Utils.formatValue(field.getType(), ReflectionUtil.readProperty(doc, field.getPropertyName())));
            params.put(field.getLabel(), Utils.formatValue(field.getType(), EvaluateExpression.evaluateExpression(doc, field.getPropertyName(), field.getType())));
        }
        return params;
    }

    public TextColumnBuilder[] prepareColumns(BusDoc doc, JasperReportFile reportFile) {
        TextColumnBuilder[] cols = new TextColumnBuilder[reportFile.getReportTable().getFieldList().size()];
        for (int i = 0; i < reportFile.getReportTable().getFieldList().size(); i++) {
            TableField field = reportFile.getReportTable().getFieldList().get(i);
            cols[i] = col.column(field.getColumnTitle(), field.getPropertyName(), field.getType());
        }
        return cols;
    }

    public ByteArrayInputStream prepareReport(BusDoc doc, PrintReport pr) {
        System.out.println("Preparing Report..." + doc);
        ByteArrayInputStream content = null;
        try {
            DRPage PAGE = new DRPage();
            PAGE.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
            PAGE.setMargin(new DRMargin(20));

            String folderLocation = systemController.getByPropertyname("PrintTemplateLocation").getValue();

            Report report = JsonUtils.readJson(folderLocation + pr.getFilename() + ".rpt", Report.class);
            //Report report = generateReport(doc);
            //JsonUtils.writeJson("C:/Users/Burhani152/Documents/dpi.rpt", report);
            ReportGenerator gen = new ReportGenerator();
            JasperPrint print = gen.prepareReport(report, prepareTheme(schemename), report.getPage(), doc).toJasperPrint();
            //JasperPrint print = gen.prepareReport(report, prepareTheme(), PAGE, doc).toJasperPrint();
            System.out.println("REPORTED_GENERATED: " + print.getName());
            ////final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ////JRPdfExporter exporter = new JRPdfExporter();
            ////exporter.setExporterInput(new SimpleExporterInput(print));
            ////exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            ////SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            ////configuration.setMetadataAuthor("SMB ERP");  //why not set some config as we like
            ////exporter.setConfiguration(configuration);
            ////exporter.exportReport();
            //File file = File.createTempFile(busdoc.getDocno(), ".pdf");
            //final FileOutputStream out = new FileOutputStream(file);
            //JasperExportManager.exportReportToPdfStream(print, out);
            //out.close();
            //System.out.println("ReportFile: " + file.getAbsolutePath());
            content = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print));
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            content = new ByteArrayInputStream(ex.getMessage().getBytes());
        } finally {
            return content;
        }
    }

    public StreamedContent prepareReportStreamedContent(BusDoc doc) {
        System.out.println("Preparing Report..." + doc);
        StreamedContent content = null;
        try {
            DRPage PAGE = new DRPage();
            PAGE.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
            PAGE.setMargin(new DRMargin(20));

            Report report = generateReport(doc);
            ReportGenerator gen = new ReportGenerator();
            //JasperPrint print = gen.prepareReport(report, prepareTheme(), PAGE, doc).toJasperPrint();
            JasperPrint print = gen.prepareReport(report, prepareTheme(schemename), PAGE, doc).toJasperPrint();

            //final ByteArrayOutputStream out = new ByteArrayOutputStream();
            //JRPdfExporter exporter = new JRPdfExporter();
            //exporter.setExporterInput(new SimpleExporterInput(print));
            //exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            //SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            //configuration.setMetadataAuthor("SMB ERP");  //why not set some config as we like
            //exporter.setConfiguration(configuration);
            //exporter.exportReport();
            File file = File.createTempFile(busdoc.getDocno(), ".pdf");
            final FileOutputStream out = new FileOutputStream(file);
            JasperExportManager.exportReportToPdfStream(print, out);
            out.close();
            System.out.println("ReportFile: " + file.getAbsolutePath());
            //content = new DefaultStreamedContent(new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print)), "application/pdf", busdoc.getDocno() + ".pdf");
            final ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print));
            content = new DefaultStreamedContent().builder()
                    .name(busdoc.getDocno() + ".pdf")
                    .contentType("application/pdf")
                    .stream(() -> bis)
                    .build();
        } catch (JRException | DRException ex) {
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            content = new DefaultStreamedContent().builder()
                    .name(busdoc.getDocno() + ".pdf")
                    .contentType("application/pdf")
                    .stream(() -> new ByteArrayInputStream(ex.toString().getBytes()))
                    .build();
        } finally {
            return content;
        }
    }

    private Report generateReport(BusDoc doc) {
        Report report = new Report();
        report.setLetterHeadRequired(true);
        report.setTitle(new ReportField("SALES TAX INVOICE"));
        report.setTitleStyle("reportTitleStyle");

        ReportSectionContainer con = new ReportSectionContainer();
        con.setPrintLayout(ReportSectionContainer.ContainerPrintLayout.HORIZONTAL);
        con.setGapBefore(20);
        con.setGapAfter(20);

        List<ReportSection> header = new LinkedList();
        ReportSection section = new ReportSection();
        section.setGapBefore(0);
        section.setGapAfter(0);
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setSectionWidth("50%");
        section.setTitle("BILLED TO");
        section.setTitleStyle("titleStyle1");
        section.addReportField(new ReportField("", "businesspartner.companyname", false, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        section.addReportField(new ReportField("", "contactperson.contactpersonname", false, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        section.addReportField(new ReportField("", "businesspartner.addressLine1", false, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        section.addReportField(new ReportField("", "businesspartner.addressLine2", false, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        section.addReportField(new ReportField("", "businesspartner.country.countryname", false, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        header.add(section);

        section = new ReportSection();
        section.setGapBefore(10);
        section.setGapAfter(0);
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setTitle("DOCUMENT DETAILS");
        section.setTitleStyle("titleStyle1");
        section.addReportField(new ReportField("Vat No: ", "branch.company.vatno", true, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        section.addReportField(new ReportField("Doc No: ", "docno", true, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        section.addReportField(new ReportField("Doc Date: ", "docdate", true, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        section.addReportField(new ReportField("LPO No: ", "refno", true, ReportField.FieldPrintLayout.LEFT_RIGHT, "labelStyle1", "textStyle1"));
        header.add(section);

        con.setReportSection(header);

        List<ReportSectionContainer> sc = new LinkedList<>();
        sc.add(con);
        report.setHeaderSection(sc);

        con = new ReportSectionContainer();
        con.setGapBefore(20);
        con.setGapAfter(0);
        con.setPrintLayout(ReportSectionContainer.ContainerPrintLayout.VERTICAL);

        List<ReportSection> footer = new LinkedList();
        sc = new LinkedList<>();

        section = new ReportSection();
        section.setGapBefore(0);
        section.setGapAfter(0);
        section.setPrintLayout(ReportSection.SectionPrintLayout.HORIZONTAL);
        section.addReportField(new ReportField("Total", "subtotal", true, ReportField.FieldPrintLayout.TOP_BOTTOM, "labelStyle2", "textStyle2"));
        section.addReportField(new ReportField("Discount", "discount", true, ReportField.FieldPrintLayout.TOP_BOTTOM, "labelStyle2", "textStyle2"));
        section.addReportField(new ReportField("Sub Total", "totalamount", true, ReportField.FieldPrintLayout.TOP_BOTTOM, "labelStyle2", "textStyle2"));
        section.addReportField(new ReportField("Total VAT", "totalvat", true, ReportField.FieldPrintLayout.TOP_BOTTOM, "labelStyle2", "textStyle2"));
        section.addReportField(new ReportField("Net Total", "grandtotal", true, ReportField.FieldPrintLayout.TOP_BOTTOM, "labelStyle2", "textStyle2"));
        footer.add(section);
        con.setReportSection(footer);
        sc.add(con);

        con = new ReportSectionContainer();
        con.setGapBefore(100);
        con.setGapAfter(0);
        con.setPrintLayout(ReportSectionContainer.ContainerPrintLayout.HORIZONTAL);

        footer = new LinkedList();

        section = new ReportSection();
        section.setGapBefore(0);
        section.setGapAfter(0);
        section.setSectionWidth("30%");
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setTitle(null);
        ReportField f = new ReportField();
        f.setText("Receiver Sign");
        f.setTextStyle("textStyle3");
        section.addReportField(f);
        footer.add(section);

        section = new ReportSection();
        section.setGapBefore(20);
        section.setGapAfter(0);
        section.setSectionWidth("30%");
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setTitle(null);
        f = new ReportField();
        f.setText("Receiver Name & Mobile");
        f.setTextStyle("textStyle3");
        section.addReportField(f);
        footer.add(section);

        section = new ReportSection();
        section.setGapBefore(20);
        section.setGapAfter(0);
        section.setSectionWidth("30%");
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setTitle(null);
        f = new ReportField();
        f.setText("Prepared By");
        f.setTextStyle("textStyle3");
        section.addReportField(f);
        footer.add(section);

        con.setReportSection(footer);
        sc.add(con);
        report.setFooterSection(sc);

        ReportTable table = new ReportTable();
        table.setColumnStyle("columnStyle");
        //table.setColumnTitleStyle("titleStyle3");
        table.setColumnTitleStyle("columnTitleStyle");
        table.setTitleStyle("columnHeaderStyle");
        table.setValueStyle("tableRowStyle");
        table.setSubtotalStyle("subtotalStyle");
        table.setDetailsEvenStyle("detailsEvenRowStyle");
        table.setDetailsOddStyle(null);
        table.setTitle("Transactions");
        table.setDataPropertyName("productTransactions");
        table.addColumn("Stock#", "product.productid", Long.class, 70);
        table.addColumn("Description", "product.productname", String.class);
        table.addColumn("Qty", "lineqty", Double.class, 50);
        table.addColumn("Price", "unitprice", Double.class, 50);
        table.addColumn("SubTotal", "subtotal", Double.class, 50, TableField.ColumnFunction.SUM);
        table.addColumn("VAT", "vatamount", Double.class, 50, TableField.ColumnFunction.SUM);
        table.addColumn("Total", "grandtotal", Double.class, 50, TableField.ColumnFunction.SUM);

        report.setReportTable(table);
        return report;
    }

    private Theme prepareTheme(String schemename) {

        reportTheme = new DefaultReportTheme(ColorSchemeManager.themes.get(schemename));

        Theme theme = new Theme(schemename, ColorSchemeManager.themes.get(schemename));

        theme.setDefaultStyle(reportTheme.getVerticalSectionTextStyle());
        theme.setColumnHeaderStyle(reportTheme.getColumnHeaderStyle().setFontBold(true));
        theme.setColumnStyle(reportTheme.getColumnStyle());
        theme.setColumnTitleStyle(reportTheme.getColumnTitleStyle().setFontSize(11));
        theme.setSubtotalStyle(reportTheme.getColumnTitleStyle().setFontSize(9));
        theme.setTableRowStyle(reportTheme.getDefaultFormat().clone().setFontSize(9).setPaddingBottom(2).setPaddingTop(2));
        theme.setDetailsEvenRowStyle(reportTheme.getDefaultFormat().clone().setFontSize(9).setBackgroundColor("secondaryColor").setHorizontalTextAlignment(null));
        //theme.setDetailsEvenRowStyle(null);
        theme.setDetailsOddRowStyle(null);
        theme.setFooterSectionStyle(reportTheme.getPageFooterStyle());
        theme.setLabelStyle1(reportTheme.getHorizontalSectionLabelStyle());
        theme.setTextStyle1(reportTheme.getHorizontalSectionTextStyle());
        theme.setTitleStyle1(reportTheme.getHorizontalSectionTitleStyle().clone().setFontBold(true));
        theme.setReportTitleStyle(reportTheme.getReportTitleStyle());
        theme.setLabelStyle2(reportTheme.getVerticalSectionLabelStyle());
        theme.setTextStyle2(reportTheme.getVerticalSectionTextStyle());
        theme.setTitleStyle2(reportTheme.getVerticalSectionTitleStyle());
        theme.setLabelStyle3(reportTheme.getHorizontalSectionLabelStyle());
        theme.setTextStyle3(reportTheme.getHorizontalSectionTextStyle().setPadding(0).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setTopBorder(1.0f).setLineThickness(1.0f).setLineColor("primaryColor"));
        theme.setTitleStyle3(reportTheme.getHorizontalSectionTitleStyle().setBackgroundColor("#ffffff")
                .setForegroundColor("textPrimaryColor").setBottomBorder(0.5f).setTopBorder(0.5f)
                .setLineThickness(2.0f).setLineStyle(LineStyle.DOUBLE)
                .setLineColor("primaryColor"));

        return theme;
    }

    private Theme prepareThemeForStationary(String schemename) {

        reportTheme = new DefaultReportTheme(ColorSchemeManager.themes.get(schemename));

        Theme theme = new Theme(schemename, ColorSchemeManager.themes.get(schemename));

        theme.setDefaultStyle(reportTheme.getVerticalSectionTextStyle());
        theme.setColumnHeaderStyle(reportTheme.getColumnHeaderStyle().setFontBold(true));
        theme.setColumnStyle(reportTheme.getColumnStyle());
        theme.setColumnTitleStyle(reportTheme.getColumnTitleStyle().setFontSize(10)
                .setBackgroundColor("secondaryColor").setForegroundColor("textPrimaryColor")
                .setTopBorder(1.0f).setLineColor("textPrimaryColor").setLineStyle(LineStyle.DASHED));
        theme.setSubtotalStyle(reportTheme.getColumnTitleStyle().setFontSize(9)
                .setBackgroundColor("secondaryColor").setForegroundColor("textPrimaryColor")
                .setTopBorder(1.0f).setLineColor("textPrimaryColor").setLineStyle(LineStyle.DASHED));
        theme.setTableRowStyle(reportTheme.getDefaultFormat().clone().setFontSize(9).setPaddingBottom(2).setPaddingTop(2));
        theme.setDetailsEvenRowStyle(reportTheme.getDefaultFormat().clone().setFontSize(9).setBackgroundColor("#ffffff").setHorizontalTextAlignment(null));
        //theme.setDetailsEvenRowStyle(null);
        theme.setDetailsOddRowStyle(null);
        theme.setFooterSectionStyle(reportTheme.getPageFooterStyle());
        theme.setLabelStyle1(reportTheme.getHorizontalSectionLabelStyle());
        theme.setTextStyle1(reportTheme.getHorizontalSectionTextStyle());
        theme.setTitleStyle1(reportTheme.getHorizontalSectionTitleStyle().clone().setFontBold(true)
                .setBackgroundColor("secondaryColor").setForegroundColor("textPrimaryColor")
                .setBottomBorder(1.0f).setLineColor("textPrimaryColor").setLineStyle(LineStyle.DASHED));
        theme.setReportTitleStyle(reportTheme.getReportTitleStyle());
        theme.setLabelStyle2(reportTheme.getVerticalSectionLabelStyle());
        theme.setTextStyle2(reportTheme.getVerticalSectionTextStyle());
        theme.setTitleStyle2(reportTheme.getVerticalSectionTitleStyle());
        theme.setLabelStyle3(reportTheme.getHorizontalSectionLabelStyle());
        theme.setTextStyle3(reportTheme.getHorizontalSectionTextStyle().setPadding(0)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setTopBorder(1.0f).setLineStyle(LineStyle.DASHED)
                .setLineColor("primaryColor"));
        theme.setTitleStyle3(reportTheme.getHorizontalSectionTitleStyle().setBackgroundColor("#ffffff")
                .setForegroundColor("textPrimaryColor").setBottomBorder(0.5f).setTopBorder(0.5f)
                .setLineStyle(LineStyle.DASHED).setLineColor("primaryColor"));

        return theme;
    }

    /**
     * @return the busdoc
     */
    public BusDoc getBusdoc() {
        return busdoc;
    }

    /**
     * @param busdoc the busdoc to set
     */
    public void setBusdoc(BusDoc busdoc) {
        this.busdoc = busdoc;
    }

}
