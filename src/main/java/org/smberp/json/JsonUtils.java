/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smberp.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Burhani152
 */
public class JsonUtils {

    /*public static <T> T readGenson(String fileName, Class cls) {
        Reader reader = null;
        try {
            File file = new File(fileName);
            reader = Files.newBufferedReader(file.toPath());

            Genson genson = new GensonBuilder()
                    .useClassMetadata(true)
                    .useRuntimeType(true)
                    .useIndentation(true)
                    .create();

            return (T) genson.deserialize(reader, cls);

        } catch (IOException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static void writeGenson(String fileName, Object bean) {
        Writer writer = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }   //create a writer
            writer = Files.newBufferedWriter(file.toPath());

            Genson genson = new GensonBuilder()
                    .useClassMetadata(true)
                    .useRuntimeType(true)
                    .create();

            genson.serialize(bean, writer);

            System.out.println("Json Write Complete: " + file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/

    public static <T> T readJson(String fileName, Class cls) {
        Reader reader = null;
        try {
            File file = new File(fileName);
            reader = Files.newBufferedReader(file.toPath());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new ClassTypeAdapterFactory())
                    .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                    .create();

            return (T) gson.fromJson(reader, cls);

        } catch (IOException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static void writeJson(String fileName, Object bean) {
        Writer writer = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }   //create a writer
            writer = Files.newBufferedWriter(file.toPath());

            //conver map to json file
            Gson gson = new GsonBuilder()
                    //.serializeNulls()
                    .registerTypeAdapterFactory(new ClassTypeAdapterFactory())
                    .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                    .setPrettyPrinting().create();

            gson.toJson(bean, writer);

            System.out.println("Json Write Complete: " + file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /*public static void main(String[] args) {
        //testReport();
        
        Properties PREFIX = new Properties();
        PREFIX.setProperty("Jamaat", "JM");
        PREFIX.setProperty("Fmb", "FMB");
        PREFIX.setProperty("Qarzan", "QZ");
        System.out.println(PREFIX);
    }

    public static void testReport(){
        String fileName = "C:\\Users\\Burhani152\\Documents\\salesinvoice.json";
        String themeName = "C:\\Users\\Burhani152\\Documents\\basictheme.json";

        Theme theme = (Theme) readJson(themeName, Theme.class);
        
        TestDynamicReport test = new TestDynamicReport();
        writeJson(fileName, test.prepareReport());
        Report report = readJson(fileName, Report.class);
        //System.out.println(report.getTitle());

        PAGE = new DRPage();
        PAGE.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
        PAGE.setMargin(new DRMargin(20));
        Invoice data = new InvoiceData().createInvoice();

        try {
            ReportGenerator gen = new ReportGenerator();
            JasperReportBuilder jasper = gen.prepareReport(report, test.prepareTheme(), PAGE, data);
            jasper.show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }
    
    public static void testTheme() {
        TestDynamicReport test = new TestDynamicReport();

        String fileName = "C:\\Users\\Burhani152\\Documents\\basictheme.json";
        //writeJson(fileName, formats);
        //formats = Arrays.asList(readJson(fileName, ReportElementStyle[].class));
        //for (ReportElementStyle f : formats) {
        //    System.out.println(f);
        //}

        //writeJson(fileName, test.prepareTheme());
        System.out.println((Theme) readJson(fileName, Theme.class));
    }*/
}
