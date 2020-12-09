/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.smb.erp.util.SystemConfig;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.Format;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.DynamicReports;
import org.apache.commons.beanutils.PropertyUtils;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.datatype.DateType;
import net.sf.dynamicreports.report.builder.datatype.DoubleType;
import net.sf.dynamicreports.report.builder.datatype.LongType;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.SimpleStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import org.apache.commons.beanutils.NestedNullException;

/**
 *
 * @author Murtuza
 */
public class ReportUtils {

    //public static DecimalFormat NUMBER_FORMAT = new DecimalFormat(ClientConfiguration.NUMBER_FORMAT);
    ///public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat(ClientConfiguration.DECIMAL_FORMAT);
    //public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(ClientConfiguration.DATE_FORMAT);
    //public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(ClientConfiguration.TIME_FORMAT);
    public static final CurrencyType currencyType = new CurrencyType();
    public static final DecimalPatternType decimalPatternType = new DecimalPatternType(SystemConfig.DECIMAL_FORMAT_PATTERN);
    public static final NumberPatternType numberPatternType = new NumberPatternType(SystemConfig.INTEGER_FORMAT_PATTERN);
    public static final DatePatternType datePatternType = new DatePatternType(SystemConfig.DATE_FORMAT_PATTERN);
    public static final DatePatternType timePatternType = new DatePatternType(SystemConfig.TIME_FORMAT_PATTERN);

    public static Object getFieldValue(String fieldName, Object targetObject) {
        try {
            if (fieldName != null && targetObject != null) {
                return PropertyUtils.getNestedProperty(targetObject, fieldName);
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*try {
            System.out.println("TableProperty: " + fieldName);
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            return field.get(targetObject);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(TableField.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return null;
    }

    public static Object getFormattedFieldValue(ReportField field, Object targetObject) {
        if (field.getPropertyName() == null) {
            return field.getText();
        }

        Object value = getFieldValue(field, targetObject);

        if(value==null){
            return null;
        }
        try {
            Class type = PropertyUtils.getPropertyType(targetObject, field.getPropertyName());
            Format f = getValueFormatter(type);
            if (f != null) {
                return f.format(value);
            }
        } catch (NestedNullException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return value;
    }

    public static Object getFormattedFieldValue(String proptery, Object targetObject) {
        Object value = getFieldValue(proptery, targetObject);

        try {
            Class type = PropertyUtils.getPropertyType(targetObject, proptery);
            Format f = getValueFormatter(type);
            if (f != null) {
                return f.format(value);
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return value;
    }

    public static TextFieldBuilder getFormattedComponent(String property, Object targetObject) {
        Object value = getFieldValue(property, targetObject);

        try {
            //Class clz = PropertyUtils.getPropertyType(targetObject, property);
            Class clz = getPropertyType(targetObject, property);
            if (clz == Long.class || clz == long.class
                    || clz == Integer.class || clz == int.class) {
                return cmp.text((Number) value).setValueFormatter(createNummberPatternValueFormatter(SystemConfig.INTEGER_FORMAT_PATTERN));
            } else if (clz == Double.class || clz == double.class
                    || clz == Float.class || clz == float.class) {
                return cmp.text((Number) value).setValueFormatter(createNummberPatternValueFormatter(SystemConfig.DECIMAL_FORMAT_PATTERN));
            } else if (clz == Date.class) {
                return cmp.text((Date) value).setValueFormatter(createDatePatternValueFormatter(SystemConfig.DATE_FORMAT_PATTERN));
            } else if (clz == Time.class) {
                return cmp.text((Date) value).setValueFormatter(createDatePatternValueFormatter(SystemConfig.DATE_FORMAT_PATTERN));
            }
            return cmp.text(value.toString()).setDataType(type.stringType());
        } catch (Exception ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cmp.text(property);
    }

    public static Object getFieldValue(ReportField field, Object targetObject) {
        //System.out.println(field.getPropertyName());
        try {
            if (field.getPropertyName() != null && targetObject != null) {
                return PropertyUtils.getNestedProperty(targetObject, field.getPropertyName());
            }
        } catch (NestedNullException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return field.getText();
    }

    public static String getValueFormatPattern(String clz) {
        if (clz.equalsIgnoreCase("Long") || clz.equalsIgnoreCase("Integer")
                || clz.equalsIgnoreCase("int")) {
            return SystemConfig.INTEGER_FORMAT_PATTERN;
        } else if (clz.equalsIgnoreCase("Double") || clz.equalsIgnoreCase("Float")) {
            return SystemConfig.DECIMAL_FORMAT_PATTERN;
        } else if (clz.equalsIgnoreCase("Date")) {
            return SystemConfig.DATE_FORMAT_PATTERN;
        } else if (clz.equalsIgnoreCase("Time")) {
            return SystemConfig.TIME_FORMAT_PATTERN;
        }
        return null;
    }

    public static String getValueFormatPattern(Class clz) {
        if (clz == Long.class || clz == long.class
                || clz == Integer.class || clz == int.class) {
            return SystemConfig.INTEGER_FORMAT_PATTERN;
        } else if (clz == Double.class || clz == double.class
                || clz == Float.class || clz == float.class) {
            return SystemConfig.DECIMAL_FORMAT_PATTERN;
        } else if (clz == Date.class) {
            return SystemConfig.DATE_FORMAT_PATTERN;
        } else if (clz == Time.class) {
            return SystemConfig.TIME_FORMAT_PATTERN;
        }
        return null;
    }

    public static Format getValueFormatter(Class clz) {
        if (clz == Long.class || clz == long.class
                || clz == Integer.class || clz == int.class) {
            return SystemConfig.INTEGER_FORMAT;
        } else if (clz == Double.class || clz == double.class
                || clz == Float.class || clz == float.class) {
            return SystemConfig.DECIMAL_FORMAT;
        } else if (clz == Date.class) {
            return SystemConfig.DATE_FORMAT;
        } else if (clz == Time.class) {
            return SystemConfig.TIME_FORMAT;
        }
        return null;
    }

    public static AbstractDataType getDataType(Class clz) {
        if (clz == Long.class || clz == long.class
                || clz == Integer.class || clz == int.class) {
            return numberPatternType;
        } else if (clz == Double.class || clz == double.class
                || clz == Float.class || clz == float.class) {
            return decimalPatternType;
        } else if (clz == Date.class) {
            return datePatternType;
        } else if (clz == Time.class) {
            return timePatternType;
        }
        return type.stringType();
    }

    public static HorizontalTextAlignment getHorizonalAligment(Class clz) {
        if (clz == Long.class || clz == long.class
                || clz == Integer.class || clz == int.class) {
            return HorizontalTextAlignment.LEFT;
        } else if (clz == Double.class || clz == double.class
                || clz == Float.class || clz == float.class) {
            return HorizontalTextAlignment.RIGHT;
        } else if (clz == Date.class) {
            return HorizontalTextAlignment.CENTER;
        } else if (clz == Time.class) {
            return HorizontalTextAlignment.CENTER;
        }
        return HorizontalTextAlignment.LEFT;
    }

    /**
     *
     * @param bean
     * @param name
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException
     */
    public static Object getProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {

        Method method;

        if (null == bean) {
            throw new NullPointerException("Object Cannot be Null.");
        }
        if (null == name) {
            throw new NullPointerException("Field Name Cannot be Null.");
        }
        method = bean.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));

        return method.invoke(bean, (Object[]) null);

    }

    /**
     *
     * @param bean
     * @param name
     * @param value
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void setProperty(Object bean, String name, String value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (null == bean) {
            throw new NullPointerException("Object Cannot be Null.");
        }
        if (null == name) {
            throw new NullPointerException("Field Name Cannot be Null.");
        }
        Class[] parameterTypes = new Class[]{String.class};
        Object[] argumentsList = new Object[]{value};
        Method setterMethod = bean.getClass().getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), parameterTypes);
        setterMethod.invoke(bean, argumentsList);
    }

    public static Class getPropertyType(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {

        Method method;

        if (null == bean) {
            throw new NullPointerException("Object Cannot be Null.");
        }
        if (null == name) {
            throw new NullPointerException("Field Name Cannot be Null.");
        }
        method = bean.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));

        return method.getReturnType();

    }

    public static CurrencyValueFormatter createCurrencyValueFormatter(String pattern) {
        return new CurrencyValueFormatter(pattern);
    }

    public static class CurrencyType extends BigDecimalType {

        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return SystemConfig.CURRENCY_SYMBOL + " " + SystemConfig.DECIMAL_FORMAT_PATTERN;
        }
    }

    private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {

        private static final long serialVersionUID = 1L;

        private String label;

        public CurrencyValueFormatter(String label) {
            this.label = label;
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            return label + currencyType.valueToString(value, reportParameters.getLocale());
        }
    }

    public static NummberPatternValueFormatter createNummberPatternValueFormatter(String pattern) {
        return new NummberPatternValueFormatter(pattern);
    }

    public static class NumberPatternType extends LongType {

        private static final long serialVersionUID = 1L;

        private String pattern = SystemConfig.INTEGER_FORMAT_PATTERN;            //"#,##0.000";

        public NumberPatternType(String pattern) {
            if (pattern != null) {
                this.pattern = pattern;
            }
        }

        @Override
        public String getPattern() {
            return pattern;
        }
    }

    private static class NummberPatternValueFormatter extends AbstractValueFormatter<String, Number> {

        private static final long serialVersionUID = 1L;

        private NumberPatternType type;

        public NummberPatternValueFormatter(String pattern) {
            type = new NumberPatternType(pattern);
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            return type.valueToString(value, reportParameters.getLocale());
        }
    }

    public static DecimalPatternValueFormatter createDecimalPatternValueFormatter(String pattern) {
        return new DecimalPatternValueFormatter(pattern);
    }

    public static class DecimalPatternType extends DoubleType {

        private static final long serialVersionUID = 1L;

        private String pattern = SystemConfig.DECIMAL_FORMAT_PATTERN;            //"#,##0.000";

        public DecimalPatternType(String pattern) {
            if (pattern != null) {
                this.pattern = pattern;
            }
        }

        @Override
        public String getPattern() {
            return pattern;
        }
    }

    private static class DecimalPatternValueFormatter extends AbstractValueFormatter<String, Number> {

        private static final long serialVersionUID = 1L;

        private NumberPatternType type;

        public DecimalPatternValueFormatter(String pattern) {
            type = new NumberPatternType(pattern);
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            return type.valueToString(value, reportParameters.getLocale());
        }
    }

    public static DatePatternValueFormatter createDatePatternValueFormatter(String pattern) {
        return new DatePatternValueFormatter(pattern);
    }

    public static class DatePatternType extends DateType {

        private static final long serialVersionUID = 1L;

        private String pattern = SystemConfig.DATE_FORMAT_PATTERN;            //"#,##0.000";

        public DatePatternType(String pattern) {
            if (pattern != null) {
                this.pattern = pattern;
            }
        }

        @Override
        public String getPattern() {
            return pattern;
        }
    }

    private static class DatePatternValueFormatter extends AbstractValueFormatter<String, Date> {

        private static final long serialVersionUID = 1L;
        private DatePatternType type;

        public DatePatternValueFormatter(String pattern) {
            type = new DatePatternType(pattern);
        }

        @Override
        public String format(Date u, ReportParameters rp) {
            return type.valueToString(u, rp.getLocale());
        }

    }

    public static int convertMMtoPixel(double mm) {
        return (int) Math.round(mm * 2.8);
    }

    public static double convertPixeltoMM(int px) {
        return px / 2.8;
    }

    public static double convertPixeltoMM(double px) {
        return px / 2.8;
    }

    public static StyleBuilder createStyle(ReportElementStyle format, ColorScheme scheme) {
        StyleBuilder s = DynamicReports.stl.style();
        s.setFontName(format.getFontFamily());
        s.setFontSize(format.getFontSize());
        if (format.getHorizontalTextAlignment() != null) {
            s.setHorizontalTextAlignment(format.getHorizontalTextAlignment());
        }
        if (format.getVerticalTextAlignment() != null) {
            s.setVerticalTextAlignment(format.getVerticalTextAlignment());
        }
        s.setLeftPadding(format.getPaddingLeft());
        s.setRightPadding(format.getPaddingRight());
        s.setTopPadding(format.getPaddingTop());
        s.setBottomPadding(format.getPaddingBottom());
        s.setFont(stl.font().setBold(format.isFontBold())
                .setItalic(format.isFontItalic())
                .setUnderline(format.isFontUnderLine())
                .setStrikeThrough(format.isFontStrikeThrough())
                .setFontName(format.getFontFamily())
                .setFontSize(format.getFontSize()));
        //PenBuilder pen = stl.pen().setLineStyle(format.getLineStyle());
        if (format.getBackgroundColor() != null) {
            s.setBackgroundColor(ColorScheme.getColor(format.getBackgroundColor(), scheme));
        }
        if (format.getForegroundColor() == null) {
            s.setBackgroundColor(Color.BLACK);
        } else {
            s.setForegroundColor(ColorScheme.getColor(format.getForegroundColor(), scheme));
        }

        if (format.getTopBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            //stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getTopBorder());
            s.setTopBorder(p);
        }
        if (format.getRightBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            //PenBuilder p = stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getRightBorder());
            s.setRightBorder(p);
        }
        if (format.getBottomBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            //System.out.println("Created_Bottom_Border: " + p.getPen().getLineStyle() + " " + p.getPen().getLineWidth());
            //PenBuilder p = stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getBottomBorder());
            s.setBottomBorder(p);
        }
        if (format.getLeftBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            //PenBuilder p = stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getLeftBorder());
            s.setLeftBorder(p);
        }
        return s;
    }

    public static SimpleStyleBuilder createSimpleStyle(ReportElementStyle format, ColorScheme scheme) {
        SimpleStyleBuilder s = stl.simpleStyle();
        s.setFontName(format.getFontFamily());
        s.setFontSize(format.getFontSize());
        if (format.getHorizontalTextAlignment() != null) {
            s.setHorizontalTextAlignment(format.getHorizontalTextAlignment());
        }
        if (format.getVerticalTextAlignment() != null) {
            s.setVerticalTextAlignment(format.getVerticalTextAlignment());
        }
        s.setLeftPadding(format.getPaddingLeft());
        s.setRightPadding(format.getPaddingRight());
        s.setTopPadding(format.getPaddingTop());
        s.setBottomPadding(format.getPaddingBottom());
        s.setFont(stl.font().setBold(format.isFontBold())
                .setItalic(format.isFontItalic())
                .setUnderline(format.isFontUnderLine())
                .setStrikeThrough(format.isFontStrikeThrough())
                .setFontName(format.getFontFamily())
                .setFontSize(format.getFontSize()));
        //PenBuilder pen = stl.pen().setLineStyle(format.getLineStyle());
        if (format.getBackgroundColor() != null) {
            s.setBackgroundColor(ColorScheme.getColor(format.getBackgroundColor(), scheme));
        }
        if (format.getForegroundColor() == null) {
            s.setBackgroundColor(Color.BLACK);
        } else {
            s.setForegroundColor(ColorScheme.getColor(format.getForegroundColor(), scheme));
        }

        if (format.getTopBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            //stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getTopBorder());
            s.setTopBorder(p);
        }
        if (format.getRightBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            //PenBuilder p = stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getRightBorder());
            s.setRightBorder(p);
        }
        if (format.getBottomBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            System.out.println("Created_Bottom_Border: " + p.getPen().getLineStyle() + " " + p.getPen().getLineWidth());
            //PenBuilder p = stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getBottomBorder());
            s.setBottomBorder(p);
        }
        if (format.getLeftBorder() > 0) {
            PenBuilder p = createPen(format, scheme);
            //PenBuilder p = stl.pen().setLineStyle(format.getLineStyle());
            //p.setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
            //p.setLineWidth(format.getLeftBorder());
            s.setLeftBorder(p);
        }
        return s;
    }

    public static PenBuilder createPen(ReportElementStyle format, ColorScheme scheme) {
        return stl.pen().setLineWidth(format.getLineThickness()).setLineStyle(format.getLineStyle())
                .setLineColor(ColorScheme.getColor(format.getLineColor(), scheme));
    }
}
