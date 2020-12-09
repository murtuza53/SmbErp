/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author Murtuza
 */
@XmlRootElement
public class ReportElementStyle implements Serializable {

    private String fontFamily = "Arial";
    private int fontSize = 10;
    private boolean fontBold;
    private boolean fontItalic;
    private boolean fontUnderLine;
    private boolean fontStrikeThrough;
    private float leftBorder;
    private float rightBorder;
    private float topBorder;
    private float bottomBorder;
    private LineStyle lineStyle = LineStyle.SOLID;
    private String lineColor = "secondaryColor";             //"#363636";
    private String backgroundColor = "#ffffff";
    private String foregroundColor = "textPrimaryColor";       //"#000000";
    private float lineThickness = 0.5f;
    private HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
    private VerticalTextAlignment verticalTextAlignment = VerticalTextAlignment.MIDDLE;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    
    public ReportElementStyle() {

    }

    public ReportElementStyle(String fontFamily, int size) {
        this.fontFamily = fontFamily;
        this.fontSize = size;
    }

    public ReportElementStyle(String fontFamily, int size, boolean italic, boolean bold) {
        this.fontFamily = fontFamily;
        this.fontSize = size;
        this.fontItalic = italic;
        this.fontBold = bold;
    }

    /**
     * @return the fontFamily
     */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
     * @param fontFamily the fontFamily to set
     */
    @XmlElement
    public ReportElementStyle setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    @Override
    public String toString() {
        return "BasicFormat{" + "fontFamily=" + fontFamily + ", fontSize=" + fontSize + ", fontBold=" + fontBold + ", fontItalic=" + fontItalic + ", fontUnderLine=" + fontUnderLine + ", fontStrikeThrough=" + fontStrikeThrough + ", leftBorder=" + leftBorder + ", rightBorder=" + rightBorder + ", topBorder=" + topBorder + ", bottomBorder=" + bottomBorder + ", lineStyle=" + lineStyle + ", lineColor=" + lineColor + ", backgroundColor=" + backgroundColor + ", foregroundColor=" + foregroundColor + ", lineThickness=" + lineThickness + ", textAlignment=" + horizontalTextAlignment + '}';
    }

    /**
     * @return the leftBorder
     */
    public float getLeftBorder() {
        return leftBorder;
    }

    /**
     * @param leftBorder the leftBorder to set
     */
    @XmlElement
    public ReportElementStyle setLeftBorder(float leftBorder) {
        this.leftBorder = leftBorder;
        return this;
    }

    /**
     * @return the rightBorder
     */
    public float getRightBorder() {
        return rightBorder;
    }

    /**
     * @param rightBorder the rightBorder to set
     */
    @XmlElement
    public ReportElementStyle setRightBorder(float rightBorder) {
        this.rightBorder = rightBorder;
        return this;
    }

    /**
     * @return the topBorder
     */
    public float getTopBorder() {
        return topBorder;
    }

    /**
     * @param topBorder the topBorder to set
     */
    @XmlElement
    public ReportElementStyle setTopBorder(float topBorder) {
        this.topBorder = topBorder;
        return this;
    }

    /**
     * @return the bottomBorder
     */
    public float getBottomBorder() {
        return bottomBorder;
    }

    /**
     * @param bottomBorder the bottomBorder to set
     */
    @XmlElement
    public ReportElementStyle setBottomBorder(float bottomBorder) {
        this.bottomBorder = bottomBorder;
        return this;
    }

    /**
     * @return the lineStyle
     */
    public LineStyle getLineStyle() {
        return lineStyle;
    }

    /**
     * @param lineStyle the lineStyle to set
     */
    @XmlElement
    public ReportElementStyle setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
        return this;
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
    @XmlElement
    public ReportElementStyle setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * @return the forgroundColor
     */
    public String getForegroundColor() {
        return foregroundColor;
    }

    /**
     * @param forgroundColor the forgroundColor to set
     */
    @XmlElement
    public ReportElementStyle setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
        return this;
    }

    /**
     * @return the lineColor
     */
    public String getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    @XmlElement
    public ReportElementStyle setLineColor(String lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    /**
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    @XmlElement
    public ReportElementStyle setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * @return the fontBold
     */
    public boolean isFontBold() {
        return fontBold;
    }

    /**
     * @param fontBold the fontBold to set
     */
    @XmlElement
    public ReportElementStyle setFontBold(boolean fontBold) {
        this.fontBold = fontBold;
        return this;
    }

    /**
     * @return the fontItalic
     */
    public boolean isFontItalic() {
        return fontItalic;
    }

    /**
     * @param fontItalic the fontItalic to set
     */
    @XmlElement
    public ReportElementStyle setFontItalic(boolean fontItalic) {
        this.fontItalic = fontItalic;
        return this;
    }

    /**
     * @return the fontUnderLine
     */
    public boolean isFontUnderLine() {
        return fontUnderLine;
    }

    /**
     * @param fontUnderLine the fontUnderLine to set
     */
    @XmlElement
    public ReportElementStyle setFontUnderLine(boolean fontUnderLine) {
        this.fontUnderLine = fontUnderLine;
        return this;
    }

    /**
     * @return the fontStrikeThrough
     */
    public boolean isFontStrikeThrough() {
        return fontStrikeThrough;
    }

    /**
     * @param fontStrikeThrough the fontStrikeThrough to set
     */
    @XmlElement
    public ReportElementStyle setFontStrikeThrough(boolean fontStrikeThrough) {
        this.fontStrikeThrough = fontStrikeThrough;
        return this;
    }

    /**
     * @return the lineThickness
     */
    public float getLineThickness() {
        return lineThickness;
    }

    /**
     * @param lineThickness the lineThickness to set
     */
    @XmlElement
    public ReportElementStyle setLineThickness(float lineThickness) {
        this.lineThickness = lineThickness;
        return this;
    }

    /**
     * @return the paddingLeft
     */
    public int getPaddingLeft() {
        return paddingLeft;
    }

    /**
     * @param paddingLeft the paddingLeft to set
     */
    public ReportElementStyle setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    /**
     * @return the paddingRight
     */
    public int getPaddingRight() {
        return paddingRight;
    }

    /**
     * @param paddingRight the paddingRight to set
     */
    public ReportElementStyle setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    /**
     * @return the paddingTop
     */
    public int getPaddingTop() {
        return paddingTop;
    }

    /**
     * @param paddingTop the paddingTop to set
     */
    public ReportElementStyle setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    /**
     * @return the paddingBottom
     */
    public int getPaddingBottom() {
        return paddingBottom;
    }

    /**
     * @param paddingBottom the paddingBottom to set
     */
    public ReportElementStyle setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    /**
     * @return the horizontalTextAlignment
     */
    public HorizontalTextAlignment getHorizontalTextAlignment() {
        return horizontalTextAlignment;
    }

    /**
     * @param horizontalTextAlignment the horizontalTextAlignment to set
     */
    public ReportElementStyle setHorizontalTextAlignment(HorizontalTextAlignment horizontalTextAlignment) {
        this.horizontalTextAlignment = horizontalTextAlignment;
        return this;
    }

    /**
     * @return the verticalTextAlignment
     */
    public VerticalTextAlignment getVerticalTextAlignment() {
        return verticalTextAlignment;
    }

    /**
     * @param verticalTextAlignment the verticalTextAlignment to set
     */
    public ReportElementStyle setVerticalTextAlignment(VerticalTextAlignment verticalTextAlignment) {
        this.verticalTextAlignment = verticalTextAlignment;
        return this;
    }
    
    public ReportElementStyle setPadding(int padding){
        this.paddingTop = padding;
        this.paddingBottom = padding;
        this.paddingLeft = padding;
        this.paddingRight = padding;
        return this;
    }
    
    @Override
    public ReportElementStyle clone() {
        /*ReportElementStyle bs = new ReportElementStyle(fontFamily, fontSize, fontItalic, fontBold);
        bs.setBackgroundColor(backgroundColor);
        bs.setBottomBorder(bottomBorder);
        bs.setFontStrikeThrough(fontStrikeThrough);
        bs.setFontUnderLine(fontUnderLine);
        bs.setForegroundColor(foregroundColor);
        bs.setLeftBorder(leftBorder);
        bs.setLineColor(lineColor);
        bs.setLineStyle(lineStyle);
        bs.setRightBorder(rightBorder);
        bs.setTopBorder(topBorder);
        bs.setPaddingTop(paddingTop);
        bs.setPaddingRight(paddingRight);
        bs.setPaddingBottom(paddingBottom);
        bs.setPaddingLeft(paddingLeft);
        bs.setHorizontalTextAlignment(horizontalTextAlignment);
        bs.setVerticalTextAlignment(verticalTextAlignment);
        
        return bs;*/
        return SerializationUtils.clone(this);
    }
    
    public static ReportElementStyle applyTheme(ColorScheme scheme, ReportElementStyle bf){
        bf.setBackgroundColor(scheme.getInversePrimaryColor());
        bf.setFontFamily(scheme.getFontName());
        bf.setForegroundColor(scheme.getPrimaryColor());
        bf.setLineColor(scheme.getSecondaryColor());
        
        return bf;
    }

}
