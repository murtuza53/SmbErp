/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.io.Serializable;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

/**
 *
 * @author Burhani152
 */
public abstract class ReportTheme implements Serializable{
    
    private ReportElementStyle defaultFormat;
    private ColorScheme colorScheme;
    
    public ReportTheme(ColorScheme colorScheme){
        this.colorScheme = colorScheme;
        defaultFormat = new ReportElementStyle().setFontFamily(colorScheme.getFontName())
                            .setFontSize(colorScheme.getNormalFontSize()).setFontBold(false)
                            .setFontItalic(false).setFontStrikeThrough(false).setFontUnderLine(false)
                            .setTopBorder(0).setRightBorder(0).setLeftBorder(0).setBottomBorder(0)
                            .setPadding(0).setBackgroundColor("#ffffff").setForegroundColor("textPrimaryColor")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                            .setLineColor("secondaryColor").setLineStyle(LineStyle.SOLID);
    }
    
    public abstract ReportElementStyle getReportTitleStyle();
    
    public abstract ReportElementStyle getHorizontalSectionTitleStyle();
    
    public abstract ReportElementStyle getHorizontalSectionLabelStyle();

    public abstract ReportElementStyle getHorizontalSectionTextStyle();
    
    public abstract ReportElementStyle getVerticalSectionTitleStyle();

    public abstract ReportElementStyle getVerticalSectionLabelStyle();
    
    public abstract ReportElementStyle getVerticalSectionTextStyle();
    
    public abstract ReportElementStyle getColumnHeaderStyle();

    public abstract ReportElementStyle getColumnTitleStyle();
    
    public abstract ReportElementStyle getColumnStyle();
    
    public abstract ReportElementStyle getDetailEvenRowStyle();
    
    public abstract ReportElementStyle getPageFooterStyle();

    /**
     * @return the defaultFormat
     */
    public ReportElementStyle getDefaultFormat() {
        return defaultFormat;
    }

    /**
     * @param defaultFormat the defaultFormat to set
     */
    public void setDefaultFormat(ReportElementStyle defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    /**
     * @return the colorScheme
     */
    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    /**
     * @param colorScheme the colorScheme to set
     */
    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

}
