/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smberp.xml;

import com.smb.erp.dynamic.report.Report;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Murtuza
 */
public class XmlUtils {

    public static String writeToXmlString(Object report, Class cls) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(report, baos);
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Report readFromXmlString(String xmlString, Class cls) {
        if(xmlString==null){
            return null;
        }
        
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(cls);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Report lf = (Report) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));
            //System.out.println(lf);
            return lf;
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeToXmlFile(String fileName, Object report) {
        try {

            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(report.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(report, file);
            jaxbMarshaller.marshal(report, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public static Report readFromXmlFile(String fileName, Class cls) {
        try {

            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Report lf = (Report) jaxbUnmarshaller.unmarshal(file);
            //System.out.println(lf);
            return lf;
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

}
