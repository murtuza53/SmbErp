/**
 * Number To Word Convertor.
 * This Class convert numbers to words up to a billion.
 * Copyright (c) 2001 BuddySoft(tm). All Rights Reserved.
 * Commercial use is subject to license terms.
 * Free for non commercial use!
 *
 * @author plamen_matanski@yahoo.com
 * @version 	1.2, Dec. 9 2001
 *
 * http://java,dir.bg
 */
package com.smb.erp.util;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Speller extends Object {
    
    public static String spellAmount(String currency, double amount) {
        amount = Math.abs(amount);
        DecimalFormat format = new DecimalFormat("#,##0.000");
        try {
            amount = ((Number)format.parse(format.format(amount))).doubleValue();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        int intVal = (int)amount;
        int fils = (int)Math.ceil((amount-intVal)*1000);
        //fils = (int)(fils/10) *10;
        
        String spelling = currency;
        if(intVal>0) {
            spelling = spelling + intName(intVal);
            if(fils>0)
                spelling = spelling + " And ";
        }
        if(fils>0)
            spelling = spelling + "Fils " + fils;
        
        return spelling + " Only";
    }
    
    /**
     * Main convertor finction
     */
    public static String intName( int n ) {
        String result = "";
        int c = n;
        
        if (c >= 1000000000) {
            result = intName(c / 1000000000) + " Billion";
            c = c % 1000000000;
        }
        
        if (c >= 1000000) {
            result = result + " " + intName(c / 1000000) + " Million";
            c = c % 1000000;
        }
        
        if (c >= 1000) {
            result = result + " " + intName(c / 1000) + " Thousand";
            c = c % 1000;
        }
        
        if (c >= 100) {
            result = result + " " + digitName(c / 100) + " Hundred";
            c = c % 100;
        }
        
        if (c >= 20) {
            result = result + " " + tensName(c / 10);
            c = c % 10;
        } else if (c >=10) {
            result = result + " " + teenName(c);
            c = 0;
        }
        if (c >= 0)
            
            result = result + " " + digitName(c);
        
        return result;
    }
    
    /**
     * Tens from 20 to 90.
     */
    public static String tensName(int n) {
        String tensName;
        
        switch(n){
            
            case(2): tensName = "Twenty"; break;
            case(3): tensName = "Thirty"; break;
            case(4): tensName = "Forty"; break;
            case(5): tensName = "Fifty"; break;
            case(6): tensName = "Sixty"; break;
            case(7): tensName = "Seventy"; break;
            case(8): tensName = "Eighty"; break;
            case(9): tensName = "Ninety"; break;
            
            default: tensName = "";
        }
        return tensName;
    }
    
    /**
     * Tens from 10 to 19.
     */
    public static String teenName(int n) {
        String teenName;
        
        switch(n) {
            case(10): teenName = "Ten"; break;
            case(11): teenName = "Eleven"; break;
            case(12): teenName = "Twelve"; break;
            case(13): teenName = "Thirteen"; break;
            case(14): teenName = "Fourteen"; break;
            case(15): teenName = "Fifteen"; break;
            case(16): teenName = "Sixteen"; break;
            case(17): teenName = "Seventeen"; break;
            case(18): teenName = "Eighteen"; break;
            case(19): teenName = "Nineteen"; break;
            
            default: teenName = "";
        }
        return teenName;
    }
    
    /**
     * Digit
     */
    public static String digitName(int n) {
        String digitName;
        
        switch(n) {
            case(1): digitName = "One"; break;
            case(2): digitName = "Two"; break;
            case(3): digitName = "Three"; break;
            case(4): digitName = "Four"; break;
            case(5): digitName = "Five"; break;
            case(6): digitName = "Six"; break;
            case(7): digitName = "Seven"; break;
            case(8): digitName = "Eight"; break;
            case(9): digitName = "Nine"; break;
            
            default: digitName = "";
        }
        return digitName;
    }

    public static double round(double amount, double precision){
        
        //System.out.println("Working Rounding now...");
        long temp = (long)amount;
        
        double decimal = amount - temp;
        //System.out.println("Decimal part: " + decimal);
        if(decimal==0)
            return amount;
        
        long temp2 = (long)(decimal * 100000);
        //System.out.println("Amount Decimal part: " + temp2);
        long temppre = (long)(precision * 100000);
        //System.out.println("Precision Decimal part: " + temppre);
        
        double div = Math.ceil((double)temp2/(double)temppre);
        //System.out.println("Ceil part: " + div);
        
        div = div*temppre;
        //System.out.println("Ceil multiply part: " + div +"\n\n");
        return (double)temp + div/100000;
    }

}