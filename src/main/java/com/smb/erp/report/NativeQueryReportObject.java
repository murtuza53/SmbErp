/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.report;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Burhani152
 */
public class NativeQueryReportObject implements Serializable {

    private List<Object> list;

    public static List<NativeQueryReportObject> asReportObjectList(List<Object[]> data) {
        if (data != null) {
            if (data.size() > 0) {
                //System.out.println("asReportObjectList Rows:" + data.size() + " Columns:" + data.get(0).length);
            } else {
                //System.out.println("asReportObjectList Rows:" + data.size());
            }
            List<NativeQueryReportObject> roList = new LinkedList<NativeQueryReportObject>();
            for (Object[] o : data) {
                roList.add(new NativeQueryReportObject(o));
            }
            return roList;
        }
        return new LinkedList<NativeQueryReportObject>();
    }

    public NativeQueryReportObject(Object[] obj) {
        if (obj != null) {
            list = Arrays.asList(obj);
            //System.out.println("NativeQueryReportObject: " + list.get(0) + " => " + list);
        } else {
            list = new LinkedList();
        }
    }

    public int getColumnCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public Object getField(int index) {
        if (index < list.size()) {
            return list.get(index);
        }
        return null;
    }
    
    public Object getKeyValue(){
        if(list!=null){
            return list.get(0);
        }
        return null;
    }
        
}
