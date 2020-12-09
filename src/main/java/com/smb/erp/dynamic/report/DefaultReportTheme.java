/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

/**
 *
 * @author Burhani152
 */
public class DefaultReportTheme extends ReportTheme {

    public DefaultReportTheme(ColorScheme scheme) {
        super(scheme);
    }

    @Override
    public ReportElementStyle getReportTitleStyle() {
        return getDefaultFormat().clone()
                .setFontBold(true).setFontSize(getColorScheme().getH2FontSize())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setForegroundColor(getColorScheme().getPrimaryColor());
    }

    @Override
    public ReportElementStyle getHorizontalSectionTitleStyle() {
        return getDefaultFormat().clone()
                .setFontBold(true).setFontSize(getColorScheme().getH4FontSize())
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                .setBackgroundColor(getColorScheme().getPrimaryColor())
                .setForegroundColor(getColorScheme().getInversePrimaryColor());
    }

    @Override
    public ReportElementStyle getHorizontalSectionLabelStyle() {
        return getDefaultFormat().clone().setFontSize(getColorScheme().getNormalFontSize())
                //.setBackgroundColor(colorScheme.getInversePrimaryColor()))
                .setForegroundColor(getColorScheme().getTextPrimaryColor())
                .setPaddingLeft(10);
    }

    @Override
    public ReportElementStyle getHorizontalSectionTextStyle() {
        return getDefaultFormat().clone().setFontSize(getColorScheme().getNormalFontSize())
                //.setBackgroundColor(colorScheme.getInversePrimaryColor()))
                .setForegroundColor(getColorScheme().getTextPrimaryColor())
                .setPaddingLeft(10);
    }

    @Override
    public ReportElementStyle getVerticalSectionTitleStyle() {
        return getDefaultFormat().clone()
                .setFontBold(true).setFontSize(getColorScheme().getH4FontSize())
                .setPaddingLeft(3)
                .setBackgroundColor(getColorScheme().getPrimaryColor())
                .setForegroundColor(getColorScheme().getInversePrimaryColor());
    }

    @Override
    public ReportElementStyle getVerticalSectionLabelStyle() {
        return getDefaultFormat().clone()
                .setFontSize(getColorScheme().getH4FontSize())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setFontBold(true)
                .setTopBorder(0.5f)
                .setBottomBorder(0.5f)
                .setLeftBorder(0.5f)
                .setRightBorder(0.5f)
                .setBackgroundColor(getColorScheme().getPrimaryColor())
                .setForegroundColor(getColorScheme().getInversePrimaryColor());
    }

    @Override
    public ReportElementStyle getVerticalSectionTextStyle() {
        return getDefaultFormat().clone()
                .setFontSize(getColorScheme().getNormalFontSize())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setTopBorder(0.5f)
                .setBottomBorder(0.5f)
                .setLeftBorder(0.5f)
                .setRightBorder(0.5f)
                //.setBackgroundColor(colorScheme.getInversePrimaryColor()))
                .setForegroundColor(getColorScheme().getTextPrimaryColor());
    }

    @Override
    public ReportElementStyle getColumnHeaderStyle() {
        return getVerticalSectionLabelStyle().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    }

    @Override
    public ReportElementStyle getColumnTitleStyle() {
        return getVerticalSectionLabelStyle().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    }

    @Override
    public ReportElementStyle getColumnStyle() {
        return getHorizontalSectionTextStyle();
    }

    @Override
    public ReportElementStyle getDetailEvenRowStyle() {
        return getDefaultFormat().clone()
                .setFontBold(true).setFontSize(getColorScheme().getNormalFontSize())
                .setBackgroundColor(getColorScheme().getInversePrimaryColor())
                .setForegroundColor(getColorScheme().getTextPrimaryColor());
    }

    @Override
    public ReportElementStyle getPageFooterStyle(){
        return getHorizontalSectionTextStyle().setFontItalic(true);
    }

}
