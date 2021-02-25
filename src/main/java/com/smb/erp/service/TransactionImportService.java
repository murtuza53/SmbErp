/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import com.smb.erp.UserSession;
import com.smb.erp.controller.DataImportController;
import com.smb.erp.controller.SystemDefaultsController;
import static com.smb.erp.controller.DataImportController.saveUploadedFile;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.ReflectionUtil;
import com.smb.erp.util.Utils;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burhani152
 */
@Service
public class TransactionImportService implements Serializable {

    @Autowired
    SystemDefaultsController systemCon;

    @Autowired
    UserSession userSession;

    @Autowired
    ExcelImportService excelService;

    @Autowired
    private RepositoryService repoService;

    private UploadedFile file;

    private String progressMessage = new Date() + ": Choose File to upload";

    private String uploadDialogTitle = "Upload Data";

    private String selectedTable;

    private List<String> header;

    private List<List> items;

    private List<Class> headerClass;

    private Class clz;

    private int progress;

    private List transactionList = new LinkedList();

    private ImportTransferable importTransferable;
    
    public static SimpleDateFormat IMPORT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    //public static DateTimeFormatter IMPORT_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @PostConstruct
    public void init() {
        //setClz(ProductTransaction.class);
        //System.out.println("DataImportController_setupTable: " + getClz());
        IMPORT_DATE_FORMAT.setLenient(false);
    }

    public void handleFileUpload(FileUploadEvent event, Class cls) {
        setClz(cls);
        System.out.println("Processing Excel file for " + clz);
        //setFile(event.getFile());
        setFile(event.getFile());
        File f = saveUploadedFile(getFile());
        setProgressMessage(getProgressMessage() + "\n" + new Date() + ": " + f.getAbsolutePath() + " uploaded");
        //JsfUtil.addSuccessMessage("Succesful", getFile().getFileName() + " is uploaded.");
        //System.out.println(progressMessage);

        excelService.initFile(f);
        System.out.println("Header: " + excelService.getRowAsArray(0));
        setHeader(excelService.getRowAsArray(0));
        setupProperty();
        
        System.out.println("Found " + excelService.rowCount() + " records in File");
        //System.out.println("EXCEl_FILE_ROW_COUNT:" + excelService.rowCount());
        //System.out.println("HEADER: " + getHeader());
        setItems((List<List>) new LinkedList());
        setProgress(0);
        setProgressMessage("");
        for (int i = 1; i < excelService.rowCount() + 1; i++) {
            getItems().add(excelService.getRowAsArray(i));
            setProgress(i * 100 / excelService.rowCount());
            setProgressMessage(i + " / " + excelService.rowCount());
            //saveData(items.get(i - 1));
            //System.out.println("Row "+i+": " + excelService.getRowAsArray(i));
        }
        if (getItems() == null || getItems().size() == 0) {
            System.out.println("No data to process, select file to continue");
            JsfUtil.addErrorMessage("No data to process, select file to continue");
        } else {
            processData();
            System.out.println(getTransactionList().size() + " records processed for " + getClz().getSimpleName());
            JsfUtil.addSuccessMessage(getTransactionList().size() + " records processed for " + getClz().getSimpleName());
        }
    }

    public void processData() {
        if (items == null || items.size() == 0) {
            JsfUtil.addErrorMessage("No data to process, select file to continue");
            return;
        }
        int count = 1;
        progress = 0;
        progressMessage = "";
        transactionList = new LinkedList();
        for (List data : items) {
            System.out.println("Excel Row " + count + ": " + data);
            Object ret = saveData(data);
            progress = count * 100 / items.size();
            setProgressMessage(count + " / " + items.size());
            if (ret == null) {
                System.out.println(count++ + ". " + ret + " ERROR");
            } else {
                System.out.println(count++ + ". " + ret + " SAVED");
            }
        }
        //JsfUtil.addSuccessMessage(items.size() + " records saved in " + selectedTable);
    }

    public Object saveData(List data) {
        try {
            Object bean = repoService.getBeanValue(data.get(0).toString(), clz);
            if (bean == null) {
                bean = clz.newInstance();
            }
            //ConvertingWrapDynaBean wrapbean = new ConvertingWrapDynaBean(bean);
            for (int i = 0; i < header.size(); i++) {
                //System.out.println(headerClass.get(i) + "__" + Utils.isPrimitiveOrStringType(headerClass.get(i)));
                if (Utils.isPrimitiveOrStringType(headerClass.get(i))) {
                    //wrapbean.set(header.get(i), data.get(i).toString());
                    //org.apache.commons.beanutils.BeanUtils.setProperty(bean, header.get(i), Utils.convertValue(headerClass.get(i), data.get(i).toString()));
                    //System.out.println("Setting__" + Utils.convertValue(headerClass.get(i), data.get(i).toString()) + "__" + headerClass.get(i));
                    PropertyUtils.setNestedProperty(bean, header.get(i), Utils.convertValue(headerClass.get(i), data.get(i).toString()));
                } else if (headerClass.get(i) == Date.class) {
                    //org.apache.commons.beanutils.BeanUtils.setProperty(bean, header.get(i), SystemConfig.DATE_FORMAT.parse(data.get(i).toString()));
                    PropertyUtils.setNestedProperty(bean, header.get(i), IMPORT_DATE_FORMAT.parse(data.get(i).toString()));
                    //PropertyUtils.setNestedProperty(bean, header.get(i), converToDate(data.get(i).toString()));
                } else {
                    //System.out.println("Setting_Category: " + data.get(i) + "__" + headerClass.get(i) + "__" + repoService.getBeanValue(data.get(i).toString(), headerClass.get(i)));
                    //org.apache.commons.beanutils.BeanUtils.setProperty(bean, header.get(i), repoService.getBeanValue(data.get(i).toString(), headerClass.get(i)));
                    PropertyUtils.setNestedProperty(bean, header.get(i), repoService.getBeanValue(data.get(i).toString(), headerClass.get(i)));
                }
            }
            //System.out.println("saveData: " + bean);
            //return repoService.saveBean(bean, clz);
            transactionList.add(bean);
            System.out.println(bean);
            return bean;
        } catch (Exception ex) {
            Logger.getLogger(DataImportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setupProperty() {
        try {
            headerClass = new LinkedList();
            for (String h : header) {
                //headerClass.add(clz.getDeclaredField(h).getType());
                headerClass.add(ReflectionUtil.getPropertyType(clz, h));
                //excelService.addColumn(h, clz.getDeclaredField(h).getType());
                excelService.addColumn(h, ReflectionUtil.getPropertyType(clz, h));
            }
            System.out.println("HeaderClass: " + headerClass);
        } catch (Exception ex) {
            Logger.getLogger(DataImportController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Error setting up table properties " + ex.getMessage());
        }
    }

    public void transfer() {
        if (importTransferable != null) {
            importTransferable.transferData(transactionList);
        }
    }

    public String value(Object bean, String property) {
        try {
            //return ReflectionUtil.readProperty(bean, property).toString();
            return PropertyUtils.getNestedProperty(bean, property).toString();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TransactionImportService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TransactionImportService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TransactionImportService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TransactionImportService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    //public Date converToDate(String dateString){
        //LocalDate date = LocalDate.parse(dateString, IMPORT_DATE_FORMAT);
        //return DateUtil.createDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    //}

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
     * @return the progressMessage
     */
    public String getProgressMessage() {
        return progressMessage;
    }

    /**
     * @param progressMessage the progressMessage to set
     */
    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    /**
     * @return the uploadDialogTitle
     */
    public String getUploadDialogTitle() {
        return uploadDialogTitle;
    }

    /**
     * @param uploadDialogTitle the uploadDialogTitle to set
     */
    public void setUploadDialogTitle(String uploadDialogTitle) {
        this.uploadDialogTitle = uploadDialogTitle;
    }

    /**
     * @return the selectedTable
     */
    public String getSelectedTable() {
        return selectedTable;
    }

    /**
     * @param selectedTable the selectedTable to set
     */
    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    /**
     * @return the header
     */
    public List<String> getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(List<String> header) {
        this.header = header;
    }

    /**
     * @return the items
     */
    public List<List> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<List> items) {
        this.items = items;
    }

    /**
     * @return the headerClass
     */
    public List<Class> getHeaderClass() {
        return headerClass;
    }

    /**
     * @param headerClass the headerClass to set
     */
    public void setHeaderClass(List<Class> headerClass) {
        this.headerClass = headerClass;
    }

    /**
     * @return the clz
     */
    public Class getClz() {
        return clz;
    }

    /**
     * @param clz the clz to set
     */
    public void setClz(Class clz) {
        this.clz = clz;
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
     * @return the transactionList
     */
    public List getTransactionList() {
        return transactionList;
    }

    /**
     * @param transactionList the transactionList to set
     */
    public void setTransactionList(List transactionList) {
        this.transactionList = transactionList;
    }

    /**
     * @return the importTransferable
     */
    public ImportTransferable getImportTransferable() {
        return importTransferable;
    }

    /**
     * @param importTransferable the importTransferable to set
     */
    public void setImportTransferable(ImportTransferable importTransferable) {
        this.importTransferable = importTransferable;
    }
}
