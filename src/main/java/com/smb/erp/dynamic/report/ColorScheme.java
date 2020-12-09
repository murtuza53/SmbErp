/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.awt.Color;
import java.util.Objects;

/**
 *
 * @author Burhani152
 */
public class ColorScheme {
    
    private String schemeName = "Default Scheme";
    private String primaryColor = "#112f91";      //"#363636";
    private String secondaryColor = "#1b7fbd";    //"#99d8d0";
    private String inversePrimaryColor = "#f6ef98";           //"#b7efcd";
    private String textPrimaryColor = "#000000";                  //"#363636";
    
    private String fontName = "Carlito";        //Georgia, Calibri, Arial, Courier, Tahoma, Carlito
   
    private int normalFontSize = 12;
    private int h1FontSize = 22;
    private int h2FontSize = 18;
    private int h3FontSize = 14;
    private int h4FontSize = 12;
    
    public ColorScheme(){
        
    }

    /**
     * @return the schemeName
     */
    public String getSchemeName() {
        return schemeName;
    }

    /**
     * @param schemeName the schemeName to set
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    /**
     * @return the primaryColor
     */
    public String getPrimaryColor() {
        return primaryColor;
    }

    /**
     * @param primaryColor the primaryColor to set
     */
    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     * @return the secondaryColor
     */
    public String getSecondaryColor() {
        return secondaryColor;
    }

    /**
     * @param secondaryColor the secondaryColor to set
     */
    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    /**
     * @return the inversePrimaryColor
     */
    public String getInversePrimaryColor() {
        return inversePrimaryColor;
    }

    /**
     * @param inversePrimaryColor the inversePrimaryColor to set
     */
    public void setInversePrimaryColor(String inversePrimaryColor) {
        this.inversePrimaryColor = inversePrimaryColor;
    }

    /**
     * @return the textPrimaryColor
     */
    public String getTextPrimaryColor() {
        return textPrimaryColor;
    }

    /**
     * @param textPrimaryColor the textPrimaryColor to set
     */
    public void setTextPrimaryColor(String textPrimaryColor) {
        this.textPrimaryColor = textPrimaryColor;
    }

    /**
     * @return the fontName
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * @param fontName the fontName to set
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * @return the normalFontSize
     */
    public int getNormalFontSize() {
        return normalFontSize;
    }

    /**
     * @param normalFontSize the normalFontSize to set
     */
    public void setNormalFontSize(int normalFontSize) {
        this.normalFontSize = normalFontSize;
    }

    /**
     * @return the h1FontSize
     */
    public int getH1FontSize() {
        return h1FontSize;
    }

    /**
     * @param h1FontSize the h1FontSize to set
     */
    public void setH1FontSize(int h1FontSize) {
        this.h1FontSize = h1FontSize;
    }

    /**
     * @return the h2FontSize
     */
    public int getH2FontSize() {
        return h2FontSize;
    }

    /**
     * @param h2FontSize the h2FontSize to set
     */
    public void setH2FontSize(int h2FontSize) {
        this.h2FontSize = h2FontSize;
    }

    /**
     * @return the h3FontSize
     */
    public int getH3FontSize() {
        return h3FontSize;
    }

    /**
     * @param h3FontSize the h3FontSize to set
     */
    public void setH3FontSize(int h3FontSize) {
        this.h3FontSize = h3FontSize;
    }

    /**
     * @return the h4FontSize
     */
    public int getH4FontSize() {
        return h4FontSize;
    }

    /**
     * @param h4FontSize the h4FontSize to set
     */
    public void setH4FontSize(int h4FontSize) {
        this.h4FontSize = h4FontSize;
    }

    @Override
    public String toString() {
        return "ColorScheme{" + "schemeName=" + schemeName + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.schemeName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColorScheme other = (ColorScheme) obj;
        if (!Objects.equals(this.schemeName, other.schemeName)) {
            return false;
        }
        return true;
    }
    
    public static Color getColor(String colorOrProperty, ColorScheme scheme){
        if(colorOrProperty.startsWith("#")){
            return ColorUtils.hexToRgb(colorOrProperty);
        } else {
            String hex = ReportUtils.getFieldValue(colorOrProperty, scheme).toString();
            return ColorUtils.hexToRgb(hex);
        }
    }
}
