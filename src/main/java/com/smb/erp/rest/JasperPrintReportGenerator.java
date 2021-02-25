/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.rest;

import com.smb.erp.UserSession;
import com.smb.erp.controller.SystemDefaultsController;
import com.smb.erp.dynamic.report.ColorScheme;
import com.smb.erp.dynamic.report.DefaultReportTheme;
import com.smb.erp.dynamic.report.JasperReportFile;
import com.smb.erp.dynamic.report.ReportField;
import com.smb.erp.entity.PrintReport;
import com.smb.erp.repo.PrintReportRepository;
import com.smb.erp.util.BeanPropertyUtil;
import com.smb.erp.util.EvaluateExpression;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.ReflectionUtil;
import com.smb.erp.util.Utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.smberp.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 *
 * @author admin
 */
//@RestController
//@RequestMapping("print")
@Named(value = "jasperPrintReportGenerator")
@ViewScoped
public class JasperPrintReportGenerator implements Serializable {

    @Autowired
    UserSession userSession;

    @Autowired
    PrintReportRepository printRepo;

    @Autowired
    SystemDefaultsController systemController;

    private DefaultReportTheme reportTheme = new DefaultReportTheme(new ColorScheme());

    private PrintReport printReport;

    String schemename = "Printed Stationary";

    String tempDir = System.getProperty("java.io.tmpdir");

    public JasperPrintReportGenerator() {

    }

    public StreamedContent downloadXlsx(PrintReport printReport, Object data, String fileName) throws FileNotFoundException, IOException, JRException {
        this.printReport = printReport;
        JasperPrint print = null;
        //System.out.println("downloadRoport: " + fileName);
        ByteArrayInputStream bis = null;
        String msg = "";
        if (data == null) {
            msg = "Invalid document to print";
            JsfUtil.addErrorMessage(msg);
            System.out.println(msg);
            bis = new ByteArrayInputStream(msg.getBytes());
        } else {
            if (printReport == null) {
                msg = "Unable to load Print Template";
                JsfUtil.addErrorMessage(msg);
                System.out.println(msg);
                bis = new ByteArrayInputStream(msg.getBytes());
            } else {
                //bis = generateReport(printReport, data);
                print = generateReport(printReport, data);
            }
        }

        System.out.println("BIS: " + bis);
        File file = new File(tempDir + fileName + ".xls");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file);
        if (bis == null) {
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(Boolean.FALSE);
            configuration.setDetectCellType(Boolean.TRUE);
            configuration.setWhitePageBackground(Boolean.FALSE);
            configuration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
            configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
            //configuration.setIgnoreGraphics(Boolean.TRUE);
            //configuration.setCollapseRowSpan(Boolean.TRUE);
            //configuration.setIgnoreCellBorder(Boolean.TRUE);
            //configuration.setMaxRowsPerSheet(0);
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setConfiguration(configuration);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
            exporter.exportReport();
            byteArrayOutputStream.writeTo(fos);
            byteArrayOutputStream.close();
            fos.close();
        }
        System.out.println("Exported_Excel_File: " + file.getAbsolutePath());
        final FileInputStream fbis = new FileInputStream(file);
        return DefaultStreamedContent.builder().name(fileName + ".xls")
                .contentEncoding("application/vnd.ms-excel")
                .stream(() -> fbis)
                .build();
    }

    public StreamedContent downloadPdf(PrintReport printReport, Object data, String fileName) throws JRException {
        this.printReport = printReport;
        JasperPrint print = null;
        //System.out.println("downloadRoport: " + fileName);
        ByteArrayInputStream bis = null;
        String msg = "";
        if (data == null) {
            msg = "Invalid document to print";
            JsfUtil.addErrorMessage(msg);
            System.out.println(msg);
            bis = new ByteArrayInputStream(msg.getBytes());
        } else {
            if (printReport == null) {
                msg = "Unable to load Print Template";
                JsfUtil.addErrorMessage(msg);
                System.out.println(msg);
                bis = new ByteArrayInputStream(msg.getBytes());
            } else {
                //bis = generateReport(printReport, data);
                print = generateReport(printReport, data);
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

        System.out.println("BIS: " + bis);
        if (bis == null) {
            bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print));
        }
        final ByteArrayInputStream fbis = bis;
        //ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(new InputStreamResource(bis), headers, HttpStatus.OK);
        //return response;
        return DefaultStreamedContent.builder().name(fileName + ".pdf")
                .contentEncoding("application/pdf")
                .stream(() -> fbis)
                .build();
    }

    public JasperPrint generateReport(PrintReport printReport, Object data) {
        //ByteArrayInputStream content = null;
        JasperPrint print = null;
        String folderLocation = systemController.getByPropertyname("PrintTemplateLocation").getValue();
        File file = new File(folderLocation + "jrxml/" + printReport.getJasperfile() + ".jrxml");
        System.out.println("prepareJasperReport: " + file.getAbsolutePath());
        JasperReportFile report = JsonUtils.readJson(folderLocation + printReport.getFilename() + ".rpt", JasperReportFile.class);

        //List rows = (List) EvaluateExpression.evaluateExpression(data, report.getReportTable().getDataPropertyName(), report.getReportTable().getDataClass());
        try {
            JasperDesign design = JRXmlLoader.load(file);
            JasperReport jreport = JasperCompileManager.compileReport(design);
            List<JRParameter> paramsList = getDefinedParameters(jreport);
            System.out.println("-------------PARAMS: " + report.getReportName() + "-------------");
            for (JRParameter p : paramsList) {
                System.out.println(p.getName() + "->" + p.getDescription());
            }

            List<JRField> fields = getDefinedFields(jreport);
            System.out.println("-------------FIELDS: " + report.getReportName() + "-------------");
            for (JRField f : fields) {
                System.out.println(f.getName() + "->" + f.getDescription());
            }

            List rows = new LinkedList();
            String transactionProperty = getTransactionProperty(paramsList);
            if (transactionProperty != null) {
                rows = (List) BeanPropertyUtil.getProperty(transactionProperty, data);
            }

            //JasperPrint print = JasperFillManager.fillReport(jreport, prepareParameters(data, paramsList),
            //        new JRTableModelDataSource(new JasperBeanTableModel2(report.getReportTable().getDataClass(),
            //                getDefinedFields(jreport), rows)));
            print = JasperFillManager.fillReport(jreport, prepareParameters(data, paramsList),
                    new JRBeanCollectionDataSource(rows));
            //content = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print));
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(DocumentReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return print;
        }
    }

    public Map<String, Object> prepareParameters(Object data, JasperReportFile report) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> params = new HashMap<>();
        for (ReportField field : report.getParameters()) {
            //params.put(field.getLabel(), Utils.formatValue(field.getType(), ReflectionUtil.readProperty(doc, field.getPropertyName())));
            params.put(field.getLabel(), Utils.formatValue(field.getType(), EvaluateExpression.evaluateExpression(data, field.getPropertyName(), field.getType())));
        }
        return params;
    }

    public Map<String, Object> prepareParameters(Object data, List<JRParameter> plist) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> params = new HashMap<>();
        for (JRParameter param : plist) {
            //params.put(field.getLabel(), Utils.formatValue(field.getType(), ReflectionUtil.readProperty(doc, field.getPropertyName())));
            if (param.getName().equalsIgnoreCase("TransactionProperty")) {
                params.put(param.getName(), null);
            } else if (param.getName().equalsIgnoreCase("headerImage")) {
                params.put(param.getName(), userSession.getSystemParamter(param.getName()));
            } else if (userSession.isSystemParameter(param.getName())) {
                if (ReflectionUtil.isNumberType(param.getValueClass())) {
                    params.put(param.getName(), userSession.getSystemParamter(param.getName()));
                } else {
                    params.put(param.getName(), userSession.getSystemParamterAsString(param.getName()));
                }
            } else {
                //params.put(param.getName(), Utils.formatValue(param.getValueClass(), EvaluateExpression.evaluateExpression(data, param.getName(), param.getValueClass())));
                params.put(param.getName(), EvaluateExpression.evaluateExpression(data, param.getName(), param.getValueClass()));
            }
        }
        return params;
    }

    public List<JRField> getDefinedFields(JasperReport jasperReport) {
        return Arrays.asList(jasperReport.getFields());
    }

    public List<JRParameter> getDefinedParameters(JasperReport jasperReport) {
        List<JRParameter> list = new LinkedList();
        JRParameter[] params = jasperReport.getParameters();
        for (JRParameter param : params) {
            if (!param.isSystemDefined() && !param.isForPrompting()) {
                //param.getName();
                //param.getDescription();
                //param.getDefaultValueExpression();
                //param.getNestedTypeName();
                //param.getClass();
                list.add(param);
            }
        }
        return list;
    }

    public String getTransactionProperty(List<JRParameter> params) {
        for (JRParameter param : params) {
            if (param.getName().equalsIgnoreCase("TransactionProperty")) {
                return param.getDescription();
            }
        }
        return null;
    }

}
