/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.report;

import java.text.Format;

/**
 *
 * @author Burhani152
 */
public class ColumnStyle {
    
    private String backgroundColor;     //hexcode
    private String fontSize;            //int size
    private String fontWeight;          //bold, bolder, inherit, lighter, normal
    private Format format;              //number/decimal/dateformat
    private String textAlign = "inherit";
        
    public ColumnStyle(){
        
    }
    
    public ColumnStyle(String bgcolor){
        this.backgroundColor = bgcolor;
    }

    public ColumnStyle(String backgroundColor, Format format) {
        this.backgroundColor = backgroundColor;
        this.format = format;
    }

    public ColumnStyle(String backgroundColor, String fontSize, String fontWeight, Format format) {
        this.backgroundColor = backgroundColor;
        this.fontSize = fontSize;
        this.fontWeight = fontWeight;
        this.format = format;
    }
    
    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the fontSize
     */
    public String getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the fontWeight
     */
    public String getFontWeight() {
        return fontWeight;
    }

    /**
     * @param fontWeight the fontWeight to set
     */
    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    /**
     * @return the format
     */
    public Format getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * @return the textAlign
     */
    public String getTextAlign() {
        return textAlign;
    }

    /**
     * @param textAlign the textAlign to set
     */
    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }
}
