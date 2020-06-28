/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import org.springframework.util.FastByteArrayOutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of 
 * objects. Objects are first serialized and then deserialized. Error
 * checking is fairly minimal in this implementation. If an object is
 * encountered that cannot be serialized (or that references an object
 * that cannot be serialized) an error is printed to System.err and
 * null is returned. Depending on your specific application, it might
 * make more sense to have copy(...) re-throw the exception.
 */
public class DeepCopy {

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
    public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos = 
                    new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in. 
            ObjectInputStream in = 
                new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    /*public static Object copyDocument(MovementDocument doc){
        MovementDocument copy = (MovementDocument)copy(doc);
        
        copy.setDocNo(null);
        copy.setDocDate(new Date());
        copy.setEmployee(ClientConfiguration.LOGGED_IN_EMPLOYEE);
        copy.setBranch(ClientConfiguration.LOGGED_IN_BRANCH);
        
        if(copy.getMovement()!=null){
            for(ProductMovement mov: copy.getMovement()){
                mov.setMovementNo(0);
                mov.setMovDate(new Date());
                mov.setMovementDocument(copy);
                mov.setUnitPrice(0);
                mov.setFcUnitPrice(0);
                mov.setDiscount(0);
                mov.setCost(0);
            }
        }
        
        return copy;
    }
    
    public static Object copyDocument(PLMovementDocument doc){
        PLMovementDocument copy = (PLMovementDocument)copy(doc);
        
        copy.setDocNo(null);
        copy.setDocDate(new Date());
        copy.setEmployee(ClientConfiguration.LOGGED_IN_EMPLOYEE);
        copy.setBranch(ClientConfiguration.LOGGED_IN_BRANCH);
        
        if(copy.getProductLines()!=null){
            for(ProductLine mov: copy.getProductLines()){
                mov.setProdLineNo(0);
                mov.setLineDate(new Date());
                mov.setMovementDocument(copy);
                mov.setUnitPrice(0);
                mov.setFCUnitPrice(0);
                mov.setDiscount(0);
                mov.setProcessedRef(null);
                mov.setProcessedRefBack(null);
            }
        }
        
        return copy;
    }*/   
}