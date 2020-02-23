/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Burhani152
 */
public class StringUtils {
   
    
    public static boolean isEmpty(String value){
        if(value==null){
            return true;
        } else if(value.trim().length()==0){
            return true;
        }
        return false;
    }
    
    public static List<String> tokensToList(String commaDelimitedString){
        StringTokenizer tokens = new StringTokenizer(commaDelimitedString, ",");
        List<String> list = new LinkedList<>();
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.length() > 0) {
                list.add(token);
            }
        }
        return list;
    }
    
    public static List<String> tokensToList(String commaDelimitedString, String firstToken){
        StringTokenizer tokens = new StringTokenizer(commaDelimitedString, ",");
        List<String> list = new LinkedList<>();
        list.add(firstToken);
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.length() > 0) {
                list.add(token);
            }
        }
        return list;
    }
}
