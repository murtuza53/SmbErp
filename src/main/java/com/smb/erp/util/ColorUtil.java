/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author admin
 */
public class ColorUtil {
    
    public static final List<String> RGB_COLORS = new LinkedList();
    public static final List<String> RGB_TRANSPARENT_COLORS = new LinkedList();
    
    static{
        RGB_COLORS.add("rgb(255, 99, 132)");
        RGB_COLORS.add("rgb(255, 159, 64)");
        RGB_COLORS.add("rgb(75, 192, 192)");
        RGB_COLORS.add("rgb(255, 205, 86)");
        RGB_COLORS.add("rgb(54, 162, 235)");
        RGB_COLORS.add("rgb(153, 102, 255)");
        RGB_COLORS.add("rgb(201, 203, 207)");
        
        RGB_TRANSPARENT_COLORS.add("rgba(255, 99, 132, 0.2)");
        RGB_TRANSPARENT_COLORS.add("rgba(255, 159, 64, 0.2)");
        RGB_TRANSPARENT_COLORS.add("rgba(75, 192, 192, 0.2)");
        RGB_TRANSPARENT_COLORS.add("rgba(255, 205, 86, 0.2)");
        RGB_TRANSPARENT_COLORS.add("rgba(54, 162, 235, 0.2)");
        RGB_TRANSPARENT_COLORS.add("rgba(153, 102, 255, 0.2)");
        RGB_TRANSPARENT_COLORS.add("rgba(201, 203, 207, 0.2)");
    };
}
