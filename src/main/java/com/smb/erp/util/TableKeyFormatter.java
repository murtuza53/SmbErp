/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smb.erp.util;

import java.text.DecimalFormat;

/**
 *
 * @author Murtuza
 */
public class TableKeyFormatter {

    public static DecimalFormat NUMBER_FORMAT = new DecimalFormat("0000");
    
    public static DecimalFormat YEAR2_FORMAT = new DecimalFormat("00");

    public static DecimalFormat YEAR4_FORMAT = new DecimalFormat("0000");

    /*public static String formatTableKey(String prefix, long number){
        return formatTableKey(prefix, DateUtil.getYear(new Date()), number);
    }*/
    
    public static String formatTableKey(String prefix, long number, int year){
        return formatTableKey(prefix, NUMBER_FORMAT.format(number), year);
    }
    
    public static String formatTableKey(String prefix, String number, int year){
        return prefix + year + number;
    }
}
