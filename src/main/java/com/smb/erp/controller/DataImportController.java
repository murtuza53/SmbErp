/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.service.ExcelImportService;
import com.smb.erp.service.RepositoryService;
import com.smb.erp.util.BeanUtils;
import com.smb.erp.util.JsfUtil;
import com.smb.erp.util.SystemConfig;
import com.smb.erp.util.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.ConvertingWrapDynaBean;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Named(value = "dataImportController")
@ViewScoped
public class DataImportController implements Serializable {

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

    final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));

    private List<String> availableTables;

    private String selectedTable;

    private List<String> header;

    private List<List> items;

    private List<Class> headerClass;

    private Class clz;
    
    private int progress;

    @PostConstruct
    public void init() {
        setAvailableTables(systemCon.getAsList("DataImportTable"));
        if (getAvailableTables().size() > 0) {
            setSelectedTable(getAvailableTables().get(0));
        }
    }

    public void setupTable() {
        try {
            clz = Class.forName("com.smb.erp.entity." + selectedTable);
            System.out.println("DataImportController_setupTable: " + clz);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataImportController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Error setting up table class");
        }
    }

    public void handleFileUpload() {
        //setFile(event.getFile());
        File f = saveUploadedFile(getFile());
        setProgressMessage(getProgressMessage() + "\n" + new Date() + ": " + f.getAbsolutePath() + " uploaded");
        //JsfUtil.addSuccessMessage("Succesful", getFile().getFileName() + " is uploaded.");
        //System.out.println(progressMessage);

        excelService.initFile(f);
        setHeader(excelService.getRowAsArray(0));
        setupProperty();

        //System.out.println("EXCEl_FILE_ROW_COUNT:" + excelService.rowCount());
        //System.out.println("HEADER: " + getHeader());
        items = new LinkedList();
        progress = 0;
        progressMessage = "";
        for (int i = 1; i < excelService.rowCount() + 1; i++) {
            items.add(excelService.getRowAsArray(i));
            progress = i*100/excelService.rowCount();
            setProgressMessage(i + " / " + excelService.rowCount());
            //saveData(items.get(i - 1));
            //System.out.println("Row "+i+": " + excelService.getRowAsArray(i));
        }
        if (items == null || items.size() == 0) {
            System.out.println("No data to process, select file to continue");
            JsfUtil.addErrorMessage("No data to process, select file to continue");
        } else {
            System.out.println(items.size() + " records processed for " + selectedTable);
            JsfUtil.addSuccessMessage(items.size() + " records processed for " + selectedTable);
        }
        //excelService.addColumns(new String[]{"eventNo", "eventname", "schemeno", "eventdate", "itsNo"},
        //        new Class[]{Integer.class, String.class, QarzanScheme.class, Date.class, ItsMaster.class});
        //excelService.initFile(f);
        //if (!excelService.isValidFile()) {
        //    JsfUtil.addErrorMessage("File data invalid to import Schemes");
        //    progressMessage = progressMessage + "\n" + new Date() + ": ERROR! File format data to import Schemes";
        //} else {
        //    items = new LinkedList<>();
        //first row is header
        //    for (int i = 1; i <= excelService.rowCount(); i++) {
        //        QarzanEvent qe = new QarzanEvent();
        //        excelService.setRowValue(qe, i);
        //        qe.setStatus("DATA OK");
        //        items.add(qe);
        //    }
        //}
    }

    public void processData() {
        if (items == null || items.size() == 0) {
            JsfUtil.addErrorMessage("No data to process, select file to continue");
            return;
        }
        int count = 1;
        progress = 0;
        progressMessage = "";
        for (List data : items) {
            Object ret = saveData(data);
            progress = count*100/items.size();
            setProgressMessage(count + " / " + items.size());
            if(ret==null){
                System.out.println(count++ + ". " + data + " ERROR");
            } else {
                System.out.println(count++ + ". " + data + " SAVED");
            }
        }
        JsfUtil.addSuccessMessage(items.size() + " records saved in " + selectedTable);
    }

    public File saveUploadedFile(UploadedFile file) {
        File f = new File(sysTempDir + System.getProperty("file.separator") + file.getFileName());
        System.out.println("Saving Imported file: " + f.getAbsolutePath());
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(f);
            int read = 0;
            byte[] bytes = new byte[1024];
            InputStream inputStream = file.getInputstream();
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            return f;
        }
    }

    public void setupProperty() {
        try {
            headerClass = new LinkedList();
            for (String h : header) {
                headerClass.add(clz.getDeclaredField(h).getType());
                excelService.addColumn(h, clz.getDeclaredField(h).getType());
            }
            System.out.println("HeaderClass: " + headerClass);
        } catch (Exception ex) {
            Logger.getLogger(DataImportController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Error setting up table properties " + ex.getMessage());
        }
    }

    public Object saveData(List data) {
        try {
            Object bean = repoService.getBeanValue(data.get(0).toString(), clz);
            if (bean == null) {
                bean = clz.newInstance();
            }
            ConvertingWrapDynaBean wrapbean = new ConvertingWrapDynaBean(bean);
            for (int i = 0; i < header.size(); i++) {
                //System.out.println(headerClass.get(i) + "__" + Utils.isPrimitiveOrStringType(headerClass.get(i)));
                if (Utils.isPrimitiveOrStringType(headerClass.get(i))) {
                    //wrapbean.set(header.get(i), data.get(i).toString());
                    org.apache.commons.beanutils.BeanUtils.setProperty(bean, header.get(i), Utils.convertValue(headerClass.get(i), data.get(i).toString()));
                    //System.out.println("Setting__" + Utils.convertValue(headerClass.get(i), data.get(i).toString()) + "__" + headerClass.get(i));
                } else if (headerClass.get(i) == Date.class) {
                    org.apache.commons.beanutils.BeanUtils.setProperty(bean, header.get(i), SystemConfig.DATE_FORMAT.parse(data.get(i).toString()));
                } else {
                    //System.out.println("Setting_Category: " + data.get(i) + "__" + headerClass.get(i) + "__" + repoService.getBeanValue(data.get(i).toString(), headerClass.get(i)));
                    org.apache.commons.beanutils.BeanUtils.setProperty(bean, header.get(i), repoService.getBeanValue(data.get(i).toString(), headerClass.get(i)));
                }
            }
            //System.out.println("saveData: " + bean);
            return repoService.saveBean(bean, clz);
        } catch (Exception ex) {
            Logger.getLogger(DataImportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Transactional
    public void saveDataOld(List data) {
        String insertQuery = "INSER INTO " + selectedTable + " (";
        boolean first = true;
        for (Object h : getHeader()) {
            if (first) {
                insertQuery = insertQuery + "'" + h + "'";
                first = false;
            } else {
                insertQuery = insertQuery + "," + "'" + h + "'";
            }
        }
        insertQuery = insertQuery + ") VALUES (";
        first = true;
        for (Object h : data) {
            if (first) {
                first = false;
                insertQuery = insertQuery + "'" + h + "'";
            } else {
                insertQuery = insertQuery + "," + "'" + h + "'";
            }
        }
        insertQuery = insertQuery + ")";
        //int count = em.createNativeQuery(insertQuery).executeUpdate();
        //System.out.println("QUERY_SAVE_DATA: " + insertQuery + " ==> UPDATED_" + count);
    }

    /*public void saveData() {
        for (QarzanEvent qe : getItems()) {
            qe.setEventNo((int) new Date().getTime());
            repo.save(qe);
            qe.setStatus("SAVED");
        }
        progressMessage = progressMessage + "\n" + new Date() + "Total " + items.size() + " saved";
        JsfUtil.addSuccessMessage("Total " + items.size() + " saved");
    }*/
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
     * @return the availableTables
     */
    public List<String> getAvailableTables() {
        return availableTables;
    }

    /**
     * @param availableTables the availableTables to set
     */
    public void setAvailableTables(List<String> availableTables) {
        this.availableTables = availableTables;
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
     * @return the items
     */
    public List getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List items) {
        this.items = items;
    }

    /**
     * @return the header
     */
    public List getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(List header) {
        this.header = header;
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

}
