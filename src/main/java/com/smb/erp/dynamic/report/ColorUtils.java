/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.awt.Color;

/**
 *
 * @author Burhani152
 */
public class ColorUtils {
    //public static String convertToHTML(Color color) {
    //    return "#" + Integer.toHexString(color.getRGB()).substring(2);
    //}

    //Displays hex representation of displayed color
    public static String rgbToHex(Color color) {
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        while (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "Hex Code: #" + hex;
        return hex;
    }

    /**
     *
     * @param hex e.g. "#FFFFFF"
     * @return
     */
    public static Color hexToRgb(String hex) {
        //javafx.scene.paint.Color c = javafx.scene.paint.Color.valueOf(hex);
        //return new Color(c.getRed(), c.getGreen(), c.getBlue());
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
            case 8:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16));
        }
        return null;
    }
}
