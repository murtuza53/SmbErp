/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import com.smb.erp.util.ExcelFileReader;
import com.smb.erp.util.SystemConfig;
import com.smb.erp.util.Utils;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertingWrapDynaBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FatemaLaptop
 */
@Service
public class ExcelImportService implements Serializable {

    @Autowired
    SystemConfig systemConfig;

    @Autowired
    RepositoryService repoService;

    LinkedHashMap<String, Class> columnMapping = new LinkedHashMap();

    LinkedHashMap<String, Integer> indexMapping = new LinkedHashMap();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private List<Row> rows;

    public ExcelImportService() {
        //dateFormat.applyPattern(systemConfig.getDateFormatPattern());
    }

    public void addColumn(String propertyName, Class type) {
        if (columnMapping == null) {
            columnMapping = new LinkedHashMap();
        }
        columnMapping.put(propertyName, type);
    }

    public void addColumns(String[] properties, Class type[]) {
        if (properties == null) {
            return;
        }
        if (type == null) {
            return;
        }
        if (properties.length != type.length) {
            return;
        }

        for (int i = 0; i < properties.length; i++) {
            addColumn(properties[i], type[i]);
        }
    }

    //public void setDateFormat(String pattern) {
    //    dateFormat.applyPattern(pattern);
    //}
    public void initFile(File file) {
        rows = ExcelFileReader.readFile(file);
        if (rows != null) {
            List<Cell> cells = new LinkedList<Cell>();
            rows.get(0).cellIterator().forEachRemaining(cells::add);
            prepareIndexMap(cells);
        }

        System.out.println("columnMapping");
        for (String key : columnMapping.keySet()) {
            System.out.println(key + " => " + columnMapping.get(key));
        }
        System.out.println("\n\nindexMapping");
        for (String key : indexMapping.keySet()) {
            System.out.println(key + " => " + indexMapping.get(key));
        }
    }

    public boolean isValidFile() {
        if (columnMapping != null && indexMapping != null) {
            if (columnMapping.keySet().size() == indexMapping.keySet().size()) {
                return true;
            }
        }
        return false;
    }

    public int rowCount() {
        if (rows == null) {
            return 0;
        }
        return rows.size() - 1;
    }

    public int columnCount(){
        if(rows==null){
            return 0;
        }
        return rows.get(0).getLastCellNum();
    }
    
    public List getRowAsArray(int count) {
        Row row = getRow(count);
        
        List<String> ret = new LinkedList();
        for(int i=0; i<columnCount(); i++){
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (isCellEmpty(cell)) {
                ret.add("");
            } else {
                ret.add(cell.getStringCellValue());
            }
        }
        //row.cellIterator().forEachRemaining(cells::add);
        //System.out.println("getRowAsArray " + count + ": " + ret);
        //return cells.stream().map(Cell::getStringCellValue).collect(Collectors.toList());
        return ret;
    }

    public Row getRow(int count) {
        return rows.get(count);
    }

    public Object setRowValue(Object bean, int index) {
        try {
            Row row = rows.get(index);
            ConvertingWrapDynaBean wrapbean = new ConvertingWrapDynaBean(bean);
            List<Cell> cells = new LinkedList<>();
            row.cellIterator().forEachRemaining(cells::add);
            for (String key : columnMapping.keySet()) {
                //System.out.println("CELL ====> " + key + " ====> " + cells.get(indexMapping.get(key)).getStringCellValue());
                if (Utils.isPrimitiveOrStringType(columnMapping.get(key))) {
                    wrapbean.set(key, cells.get(indexMapping.get(key)).getStringCellValue());
                } else if (columnMapping.get(key) == Date.class) {
                    BeanUtils.setProperty(bean, key, dateFormat.parse(cells.get(indexMapping.get(key)).getStringCellValue()));
                } else {
                    BeanUtils.setProperty(bean, key, repoService.getBeanValue(cells.get(indexMapping.get(key)).getStringCellValue(), columnMapping.get(key)));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return bean;
        }
    }

    private void prepareIndexMap(List<Cell> cells) {
        List<String> list = new LinkedList();
        for (Cell c : cells) {
            list.add(c.getStringCellValue());
        }

        for (String key : columnMapping.keySet()) {
            int i = list.indexOf(key);
            if (i >= 0) {
                indexMapping.put(key, i);
            }
        }
    }

    /**
     * Checks if the value of a given {@link XSSFCell} is empty.
     *
     * @param cell The {@link XSSFCell}.
     * @return {@code true} if the {@link XSSFCell} is empty. {@code false}
     * otherwise.
     */
    public static boolean isCellEmpty(final Cell cell) {
        if (cell == null) { // use row.getCell(x, Row.CREATE_NULL_AS_BLANK) to avoid null cells
            return true;
        }

        if (cell.getCellType() == CellType.BLANK) {
            return true;
        }

        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) {
            return true;
        }

        return false;
    }

//    public Object getBeanValue(String value, Class clz) {
//        //System.out.println("getBeanValue.repoService:" + repoService);
//        BaseRepository repo = repoService.get(clz);
//        //System.out.println("getBeanValue.repo:" + repo);
//        Object bean = null;
//        if (repo != null) {
//            //System.out.println(clz.getName() + " Found Repo: " + repo.getOne(Integer.parseInt(value)));
//            try {
//                bean = repo.getOne(Integer.parseInt(value));
//            } catch (EntityNotFoundException ex) {
//                System.out.println(clz.getName() + " cannot find Data for " + value);
//            }
//        }
//        return bean;
//    }
//
//    public static void main(String[] args) {
//        File file = new File("C:\\Users\\FatemaLaptop\\Desktop\\EventList.xlsx");   //creating a new file instance 
//
//        ExcelImportService manager = new ExcelImportService();
//        manager.addColumns(new String[]{"eventNo", "eventname", "schemeno", "eventdate", "itsNo"},
//                new Class[]{Integer.class, String.class, QarzanScheme.class, Date.class, ItsMaster.class});
//        manager.initFile(file);
//
//        QarzanEvent event = new QarzanEvent();
//        manager.setRowValue(event, 1);
//        System.out.println(event + "\t" + event.getEventdate() + "\t" + event.getSchemeno() + "\t" + event.getItsNo());
//    }
//
//    public void test() {
//        File file = new File("C:\\Users\\FatemaLaptop\\Desktop\\EventList.xlsx");   //creating a new file instance 
//
//        ExcelImportService manager = new ExcelImportService();
//        manager.addColumns(new String[]{"eventNo", "eventname", "schemeno", "eventdate", "itsNo"},
//                new Class[]{Integer.class, String.class, QarzanScheme.class, Date.class, ItsMaster.class});
//        manager.initFile(file);
//
//        QarzanEvent event = new QarzanEvent();
//        manager.setRowValue(event, 1);
//        System.out.println(event + "\t" + event.getEventdate() + "\t" + event.getSchemeno() + "\t" + event.getItsNo());
//    }
}
