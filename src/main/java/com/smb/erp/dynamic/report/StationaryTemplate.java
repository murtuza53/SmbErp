/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.smb.erp.util.SystemConfig;
import java.awt.Color;
import java.util.Locale;
import net.sf.dynamicreports.report.base.DRMargin;
import net.sf.dynamicreports.report.base.DRPage;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.datatype.DoubleType;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.ImageScale;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 *
 * @author Burhani152
 */
public class StationaryTemplate {

    public static final StyleBuilder rootStyle;
    public static final StyleBuilder boldStyle;
    public static final StyleBuilder bold12Style;
    public static final StyleBuilder bold18Style;
    public static final StyleBuilder bold22Style;
    public static final StyleBuilder italicStyle;
    public static final StyleBuilder normalCenteredStyle;
    public static final StyleBuilder boldCenteredStyle;
    public static final StyleBuilder bold12CenteredStyle;
    public static final StyleBuilder bold18CenteredStyle;
    public static final StyleBuilder bold22CenteredStyle;

    public static StyleBuilder columnStyle;
    public static StyleBuilder columnTitleStyle;
    public static StyleBuilder groupStyle;
    public static StyleBuilder subtotalStyle;
    public static StyleBuilder receiverStyle;
    public static StyleBuilder columnTitleDottedStyle;
    public static StyleBuilder tableDataStyle;
    public static StyleBuilder summaryStyle;

    public static PenBuilder defaultPen;

    public static final ReportTemplateBuilder reportTemplate;
    public static final CurrencyType currencyType;
    public static final NumberPatternType numberPatternType;
    public static ComponentBuilder<?, ?> dynamicReportsComponent;
    public static ComponentBuilder<?, ?> footerComponent;
    public static DRPage PAGE;

    static {
        
        PAGE = new DRPage();
        PAGE.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
        PAGE.setMargin(new DRMargin(20));
        
        defaultPen = stl.penDotted();
        rootStyle = stl.style().setPadding(2);
        boldStyle = stl.style(rootStyle).bold();
        italicStyle = stl.style(rootStyle).italic();
        normalCenteredStyle = stl.style().setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
        boldCenteredStyle = stl.style(boldStyle)
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
        bold12CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(12);
        bold18CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(18);
        bold22CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(22);
        bold12Style = stl.style(boldStyle)
                .setFontSize(12);
        bold18Style = stl.style(boldStyle)
                .setFontSize(18);
        bold22Style = stl.style(boldStyle)
                .setFontSize(22);
        columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
        columnTitleStyle = stl.style(columnStyle)
                .setBorder(stl.penThin())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setBackgroundColor(Color.LIGHT_GRAY)
                .bold();
        groupStyle = stl.style(boldStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
        subtotalStyle = stl.style(boldStyle)
                .setTopBorder(stl.penThin());
        receiverStyle = stl.style(boldStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setTopBorder(stl.penDotted());
        summaryStyle = stl.style(boldStyle);
        //.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
        //.setBorder(stl.penDotted());
        columnTitleDottedStyle = stl.style(boldStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setBottomBorder(stl.penDotted())
                .setTopBorder(stl.penDotted());
        tableDataStyle = stl.style()
                //.setRightBorder(stl.penDotted())
                //.setLeftBorder(stl.penDotted())
                .setLeftPadding(2)
                .setRightPadding(2);

        StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
        StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(170, 170, 170));
        StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(140, 140, 140));
        StyleBuilder crosstabCellStyle = stl.style(columnStyle)
                .setBorder(stl.penThin());

        TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
                .setHeadingStyle(0, stl.style(rootStyle).bold());

        reportTemplate = template()
                .setLocale(Locale.ENGLISH)
                .setColumnStyle(columnStyle)
                .setColumnTitleStyle(columnTitleStyle)
                .setGroupStyle(groupStyle)
                .setGroupTitleStyle(groupStyle)
                .setSubtotalStyle(subtotalStyle)
                .setPageMargin(margin().setBottom(10).setTop(0).setRight(10).setLeft(10))
                //.setPageMargin(margin().setLeft(PAGE.getMargin().getLeft()).setRight(PAGE.getMargin().getRight()).setTop(PAGE.getMargin().getTop()).setBottom(PAGE.getMargin().getBottom()))
                //.highlightDetailEvenRows()
                //.setDetailEvenRowStyle(detailEvenRowStyle)
                .crosstabHighlightEvenRows()
                .setCrosstabGroupStyle(crosstabGroupStyle)
                .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
                .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
                .setCrosstabCellStyle(crosstabCellStyle)
                .setTableOfContentsCustomizer(tableOfContentsCustomizer);

        currencyType = new CurrencyType();
        numberPatternType = new NumberPatternType(SystemConfig.DECIMAL_FORMAT_PATTERN);

        HyperLinkBuilder link = hyperLink("http://www.maimoontrading.com");
        dynamicReportsComponent
                = cmp.verticalList(
                        cmp.image(SystemConfig.PRINT_LETTERHEAD_IMAGE)
                                .setImageScale(ImageScale.FILL_FRAME));
                                //.setFixedDimension(PAGE_WIDTH - PAGE_LEFT_MARGN - PAGE_RIGHT_MARGIN,
                                //        PAGE_HEADER_MARGIN));
        //cmp.verticalList(
        //	cmp.text("DynamicReports").setStyle(bold22CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
        //	cmp.text("http://www.dynamicreports.org").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(300);

        footerComponent = cmp.verticalList()
                /*.add(cmp.horizontalList()
                 .add(cmp.verticalList().add(cmp.text("Murtuza Vohra").setHorizontalAlignment(HorizontalAlignment.CENTER), cmp.text("Ordered By").setStyle(receiverStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)))
                 .add(cmp.horizontalGap(10))
                 .add(cmp.verticalList().add(cmp.text(""), cmp.text("Receiver Name & Mobile").setStyle(receiverStyle)))
                 .add(cmp.horizontalGap(10))
                 .add(cmp.verticalList().add(cmp.text(""), cmp.text("Receiver Sign").setStyle(receiverStyle)))
                 .add(cmp.horizontalGap(10))
                 .add(cmp.verticalList().add(cmp.text(""), cmp.text("Prepared By").setStyle(receiverStyle))))*/
                .add(cmp.pageXofY()
                        .setStyle(stl.style(normalCenteredStyle)));
                //.add(cmp.verticalGap(PAGE_FOOTER_MARGIN));
    }

    /**
     * Creates custom component which is possible to add to any report band
     * component
     */
    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
        return cmp.verticalList()
                .add(
                        dynamicReportsComponent,
                        cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
        //.newRow();
        //.add(cmp.line());
        //.newRow()
        //.add(cmp.verticalGap(10));
    }

    public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
        return new CurrencyValueFormatter(label);
    }

    public static class CurrencyType extends BigDecimalType {

        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return "BD #,##0.000";
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

    public static NummberPatternValueFormatter createNummberPatternValueFormatter(String label) {
        return new NummberPatternValueFormatter(label);
    }

    public static class NumberPatternType extends DoubleType {

        private static final long serialVersionUID = 1L;

        private String pattern = SystemConfig.DECIMAL_FORMAT_PATTERN;            //"#,##0.000";

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

        private String label;

        public NummberPatternValueFormatter(String label) {
            this.label = label;
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            return label + numberPatternType.valueToString(value, reportParameters.getLocale());
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

}
