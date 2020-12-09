/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.smb.erp.UserSession;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.util.JsfUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.dynamicreports.report.base.DRMargin;
import net.sf.dynamicreports.report.base.DRPage;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    private DefaultReportTheme reportTheme = new DefaultReportTheme(new ColorScheme());

    private BusDoc busdoc;

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
        System.out.println("DocumentReportGenerator_init(): " + getBusdoc());
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
                bis = prepareReport(getBusdoc());
            }
        }

        return new DefaultStreamedContent(bis, "application/pdf", busdoc.getDocno() + ".pdf");
    }

    @RequestMapping(value = "/doc/{fileName:.+}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable("fileName") String fileName) {

        //System.out.println("downloadRoport: " + fileName);
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
                bis = prepareReport(getBusdoc());
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "inline; filename=" + fileName + ".pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(new InputStreamResource(bis), headers, HttpStatus.OK);
        return response;
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

            IOUtils.copy(prepareReport(busdoc), new FileOutputStream(file));

            System.out.println("saveReport: " + file.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ByteArrayInputStream getReport() {
        return prepareReport(busdoc);
    }

    public ByteArrayInputStream prepareReport(BusDoc doc) {
        System.out.println("Preparing Report..." + doc);
        ByteArrayInputStream content = null;
        try {
            DRPage PAGE = new DRPage();
            PAGE.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
            PAGE.setMargin(new DRMargin(20));

            Report report = generateReport(doc);
            ReportGenerator gen = new ReportGenerator();
            JasperPrint print = gen.prepareReport(report, prepareTheme(), PAGE, doc).toJasperPrint();

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
        } catch (JRException ex) {
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            content = new ByteArrayInputStream(ex.getMessage().getBytes());
        } catch (DRException ex) {
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
            JasperPrint print = gen.prepareReport(report, prepareTheme(), PAGE, doc).toJasperPrint();

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
            content = new DefaultStreamedContent(new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print)), "application/pdf", busdoc.getDocno() + ".pdf");
        } catch (JRException ex) {
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            content = new DefaultStreamedContent(new ByteArrayInputStream(ex.getMessage().getBytes()), "application/pdf");
        } catch (DRException ex) {
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            content = new DefaultStreamedContent(new ByteArrayInputStream(ex.getMessage().getBytes()), "application/pdf");
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
        section.addReportField(new ReportField("", "businesspartner.companyname", false).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        section.addReportField(new ReportField("", "contactperson.contactpersonname", false).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        section.addReportField(new ReportField("", "businesspartner.addressLine1", false).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        section.addReportField(new ReportField("", "businesspartner.addressLine2", false).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        section.addReportField(new ReportField("", "businesspartner.country.countryname", false).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        header.add(section);

        section = new ReportSection();
        section.setGapBefore(10);
        section.setGapAfter(0);
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setTitle("DOCUMENT DETAILS");
        section.setTitleStyle("titleStyle1");
        section.addReportField(new ReportField("Vat No: ", "branch.company.vatno", true).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        section.addReportField(new ReportField("Doc No: ", "docno", true).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        section.addReportField(new ReportField("Doc Date: ", "docdate", true).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
        section.addReportField(new ReportField("LPO No: ", "refno", true).setLabelStyle("labelStyle1").setTextStyle("textStyle1"));
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
        section.addReportField(new ReportField("Total", "subtotal", true, ReportField.FieldPrintLayout.TOP_BOTTOM).setLabelStyle("labelStyle2").setTextStyle("textStyle2"));
        section.addReportField(new ReportField("Discount", "discount", true, ReportField.FieldPrintLayout.TOP_BOTTOM).setLabelStyle("labelStyle2").setTextStyle("textStyle2"));
        section.addReportField(new ReportField("Sub Total", "totalamount", true, ReportField.FieldPrintLayout.TOP_BOTTOM).setLabelStyle("labelStyle2").setTextStyle("textStyle2"));
        section.addReportField(new ReportField("Total VAT", "totalvat", true, ReportField.FieldPrintLayout.TOP_BOTTOM).setLabelStyle("labelStyle2").setTextStyle("textStyle2"));
        section.addReportField(new ReportField("Net Total", "grandtotal", true, ReportField.FieldPrintLayout.TOP_BOTTOM).setLabelStyle("labelStyle2").setTextStyle("textStyle2"));
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
        f.setText("Receiver Sign").setTextStyle("textStyle3");
        section.addReportField(f);
        footer.add(section);

        section = new ReportSection();
        section.setGapBefore(20);
        section.setGapAfter(0);
        section.setSectionWidth("30%");
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setTitle(null);
        f = new ReportField();
        f.setText("Receiver Name & Mobile").setTextStyle("textStyle3");
        section.addReportField(f);
        footer.add(section);

        section = new ReportSection();
        section.setGapBefore(20);
        section.setGapAfter(0);
        section.setSectionWidth("30%");
        section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
        section.setTitle(null);
        f = new ReportField();
        f.setText("Prepared By").setTextStyle("textStyle3");
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

    private Theme prepareTheme() {
        
        String schemename = "Maimoon Logo";
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
