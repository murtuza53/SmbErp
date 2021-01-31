/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.smb.erp.util.SystemConfig;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRPage;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.SimpleStyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder;
import net.sf.dynamicreports.report.constant.ComponentPositionType;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.ImageScale;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Burhani152
 */
public class ReportGenerator implements Serializable {

    public static final StationaryTemplate.CurrencyType currencyType;
    public static final StationaryTemplate.NumberPatternType numberPatternType;

    protected Report report;
    protected Theme theme;
    protected DRPage page;
    Object targetObject;
    private int reportWidth;
    private int reportHeight;

    static {
        currencyType = new StationaryTemplate.CurrencyType();
        numberPatternType = new StationaryTemplate.NumberPatternType(SystemConfig.DECIMAL_FORMAT_PATTERN);
    }

    public JasperReportBuilder prepareReport(Report report, Theme theme, DRPage page, Object targetObject) {
        System.out.println("ReportGenerator.start...0");
        this.report = report;
        this.theme = theme;
        this.page = page;
        this.targetObject = targetObject;

        reportWidth = page.getWidth() - page.getMargin().getLeft() - page.getMargin().getRight();
        reportHeight = page.getHeight() - page.getMargin().getTop() - page.getMargin().getBottom();

        System.out.println("ReportGenerator.start...1");
        ReportTemplateBuilder template = template()
                .setColumnStyle(theme.createStyle(report.getReportTable().getColumnStyle()))
                .setColumnHeaderStyle(theme.createStyle(report.getReportTable().getColumnTitleStyle()))
                .setColumnTitleStyle(theme.createStyle(report.getReportTable().getColumnTitleStyle()))
                //.setPageFooterStyle(createStyle(theme.getFooterSectionStyle(), theme.getColorScheme()))
                .setSubtotalStyle(theme.createStyle(report.getReportTable().getSubtotalStyle()));

        //if (theme.getDetailsEvenRowStyle() != null) {
        //    template.setDetailEvenRowStyle(createSimpleStyle(theme.getDetailsEvenRowStyle(), theme.getColorScheme()));
        //}
        System.out.println("ReportGenerator.start...2");
        JasperReportBuilder builder = report()
                .setTemplate(template)
                .setPageFormat(page.getWidth(), page.getHeight(), page.getOrientation())
                .setPageMargin(DynamicReports.margin().setTop(page.getMargin().getTop())
                        .setRight(page.getMargin().getRight())
                        .setBottom(page.getMargin().getBottom())
                        .setLeft(page.getMargin().getLeft()));
        System.out.println("ReportGenerator.start...3");

        builder.addPageHeader(createTitleComponent(report.getLetterHeadRequired(), report.getTitle()));
        System.out.println("ReportGenerator.start...3.1");
        builder.addPageHeader(createReportSection(report.getHeaderSection()));
        System.out.println("ReportGenerator.start...3.2");
        //add report summary is required
        if (report.getReportTable().getPrintSummary()) {
            List<ReportSectionContainer> rsclist = new LinkedList<>();
            ReportSectionContainer rsc = new ReportSectionContainer();
            rsc.setGapBefore(20);
            rsc.setGapAfter(0);
            rsc.setPrintLayout(ReportSectionContainer.ContainerPrintLayout.VERTICAL);
            rsclist.add(rsc);

            List<ReportSection> rslist = new LinkedList();
            ReportSection section = new ReportSection();
            section.setGapBefore(0);
            section.setGapAfter(0);
            section.setPrintLayout(ReportSection.SectionPrintLayout.HORIZONTAL);
            section.setReportFields(report.getReportTable().getSummaryList());
            rslist.add(section);
            rsc.setReportSection(rslist);

            System.out.println("ReportGenerator.start...3.5 .... " + rsclist.size() + "." + rsclist.get(0).getReportSection().size() + "." + rsclist.get(0).getReportSection().get(0).getReportFields().size());
            builder.addSummary(createReportSection(rsclist));
        }
        builder.addSummary(createReportSection(report.getFooterSection()));
        System.out.println("ReportGenerator.start...4");

        if (report.isPrintSignatures()) {
            if (report.getSignatureFields() != null && report.getSignatureFields().size() > 0) {
                List<ReportSectionContainer> rsclist = new LinkedList<>();
                ReportSectionContainer rsc = new ReportSectionContainer();
                rsc.setGapBefore(100);
                rsc.setGapAfter(0);
                rsc.setPrintLayout(ReportSectionContainer.ContainerPrintLayout.HORIZONTAL);
                rsclist.add(rsc);

                int percentage = 95 / report.getSignatureFields().size();
                int gapBefore = 0;
                List<ReportSection> rslist = new LinkedList<>();
                for (ReportField field : report.getSignatureFields()) {
                    ReportSection section = new ReportSection();
                    section.setGapBefore(gapBefore);
                    section.setGapAfter(0);
                    section.setSectionWidth(percentage + "%");
                    section.setPrintLayout(ReportSection.SectionPrintLayout.VERTICAL);
                    section.setTitle(null);
                    section.addReportField(field);
                    if (gapBefore == 0) {
                        gapBefore = 20;
                    }
                    rslist.add(section);
                }
                rsc.setReportSection(rslist);
                System.out.println("ReportGenerator.start...4.1");
                builder.addSummary(createReportSection(rsclist));
                System.out.println("ReportGenerator.start...4.2");
            }
        }

        createTable(builder);
        System.out.println("ReportGenerator.start...5");

        return builder;
    }

    public void createTable(JasperReportBuilder builder) {
        if (report.getReportTable() != null) {
            ReportTable rt = report.getReportTable();

            List list = (List) ReportUtils.getFieldValue(rt.getDataPropertyName(), targetObject);
            builder.setDataSource(new JRBeanCollectionDataSource(list));
            TableField lastField = null;
            if (rt.getFieldList() != null) {
                TextColumnBuilder srNo = col.reportRowNumberColumn("Sr.").setFixedWidth(30)
                        .setStyle(theme.createStyle(rt.getValueStyle())
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                        .setTitleStyle(theme.createStyle(rt.getColumnTitleStyle())
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
                //add serial number column
                builder.addColumn(srNo);

                if (report.getReportTable().hasSubtotal()) {
                    builder.addSubtotalAtSummary(sbt.text("", srNo));
                }

                for (TableField f : rt.getFieldList()) {
                    TextColumnBuilder column = col.column(f.getColumnTitle(), f.getPropertyName(), ReportUtils.getDataType(f.getType()));
                    System.out.println(f.getColumnTitle() + " => " + f.getPropertyName() + " => " + f.getType() + " => " + ReportUtils.getDataType(f.getType()));
                    column.setStyle(theme.createStyle(rt.getValueStyle()).setHorizontalTextAlignment(ReportUtils.getHorizonalAligment(f.getType())));
                    column.setTitleStyle(theme.createStyle(rt.getColumnTitleStyle()));
                    //column.setHorizontalAlignment(ReportUtils.getHorizonalAligment(f.getType()));
                    if (f.getWidth() > 0) {
                        column.setFixedWidth(f.getWidth());
                    }
                    builder.addColumn(column);

                    if (f.getColumnFunction() == TableField.ColumnFunction.SUM) {
                        SubtotalBuilder st = sbt.sum(column);
                        st.setStyle(theme.createStyle(report.getReportTable().getSubtotalStyle()).setHorizontalTextAlignment(ReportUtils.getHorizonalAligment(f.getType())));
                        //st.setLabel("Total ");
                        builder.addSubtotalAtSummary(st);
                    } else if (report.getReportTable().hasSubtotal()) {
                        SubtotalBuilder st = sbt.text("", column);
                        builder.addSubtotalAtSummary(st);
                    }
                }
                if (rt.getFieldList().size() > 0) {
                    lastField = rt.getFieldList().get(rt.getFieldList().size() - 1);
                }
                //r.setColumnTitleStyle(createStyle(currentTheme.getTableColumnTitleStyle()).setPadding(2).setHorizontalAlignment(HorizontalAlignment.CENTER));
                builder.setSubtotalStyle(theme.createStyle(report.getReportTable().getSubtotalStyle()));
            }

            /*VerticalListBuilder scomp = cmp.verticalList();
            if (rt.getSummaryList() != null) {
                int width = 0;
                if (lastField != null) {
                    width = lastField.getWidth();
                    System.out.println("LastField: " + lastField.getPropertyName() + ": " + lastField.getWidth());
                }
                if (width == 0) {
                    width = 60;
                }

                for (ReportField f : rt.getSummaryList()) {
                    String spell = "";
                    if (f.isSpellIt()) {
                        spell = Speller.spellAmount(ClientConfiguration.CURRENCY_SYMBOL, ((Number) f.getFieldValue(targetObject)).doubleValue());
                    }
                    HorizontalListBuilder hlist = cmp.horizontalList();
                    TextFieldBuilder summary = cmp.text(spell);
                    summary.setStyle(createStyle(theme.getTableRowStyle(), theme.getColorScheme()).setPadding(2).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT));
                    hlist.add(summary);

                    String label = "";
                    if (f.isPrintLabel()) {
                        label = f.getLabel();
                    }
                    summary = cmp.text(label);
                    summary.setStyle(createStyle(theme.getTableRowStyle(), theme.getColorScheme()).setPadding(2).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));
                    summary.setWidth(width);
                    hlist.add(summary);

                    summary = ReportUtils.getFormattedComponent(f.getPropertyName(), targetObject);
                    summary.setStyle(createStyle(theme.getTableRowStyle(), theme.getColorScheme()).setPadding(2).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));
                    summary.setFixedWidth(width);
                    hlist.add(summary);

                    //scomp.add(cmp.horizontalList().add(cmp.hListCell(hlist)));
                    scomp.add(hlist);
                }
                builder.summary(scomp);
            }*/

 /*if (report.getReportSummary() != null) {
                VerticalListBuilder vlb = cmp.verticalList();

                if (Utils.isValidString(report.getReportSummary().getLabel())
                        || Utils.isValidString(report.getReportSummary().getText())) {
                    vlb.add(cmp.verticalGap(GAP));
                }

                if (Utils.isValidString(report.getReportSummary().getLabel())) {
                    TextFieldBuilder title = cmp.text(report.getReportSummary().getLabel());
                    title.setStyle(createStyle(currentTheme.getHeaderLeftSectionTitleStyle()));
                    vlb.add(title);
                }

                String summary = "";
                if (report.getReportSummary().getPropertyName() != null) {
                    Object value = ReportUtils.getFormattedFieldValue(report.getReportSummary(), data);
                    if (value != null) {
                        summary = (String) value;
                    }
                } else if (Utils.isValidString(report.getReportSummary().getText())) {
                    summary = report.getReportSummary().getText();
                }

                TextFieldBuilder text = cmp.text(summary);
                text.setStyle(createStyle(currentTheme.getHeaderLeftFieldValueStyle()).setLeftPadding(10));
                vlb.add(text);

                scomp.add(vlb);

            }*/
        }

        if (report.getReportTable().getDetailsEvenStyle() != null) {
            System.out.println("EvenRowColor: " + report.getReportTable().getDetailsEvenStyle());
            SimpleStyleBuilder sb = theme.createSimpleStyle(report.getReportTable().getDetailsEvenStyle());
            //.setBackgroundColor(currentTheme.getTableEvenRowStyle().getBackgroundColor())
            //.setForegroundColor(currentTheme.getTableEvenRowStyle().getForegroundColor());
            builder.setDetailEvenRowStyle(sb);
            builder.highlightDetailEvenRows();
        }

        if (report.getReportTable().getDetailsOddStyle() != null) {
            System.out.println("OddRowColor: " + report.getReportTable().getDetailsOddStyle());
            SimpleStyleBuilder sb = theme.createSimpleStyle(report.getReportTable().getDetailsEvenStyle());        //stl.simpleStyle();
            //.setBackgroundColor(currentTheme.getTableOddRowStyle().getBackgroundColor())
            //.setForegroundColor(currentTheme.getTableOddRowStyle().getForegroundColor());
            builder.setDetailOddRowStyle(sb);
            builder.highlightDetailOddRows();
        }
    }

    /**
     * Creates custom component which is possible to add to any report band
     * component
     */
    public ComponentBuilder<?, ?> createTitleComponent(boolean letterheadRequired, ReportField title) {
        ComponentBuilder<?, ?> dynamicReportsComponent = cmp.verticalList();
        //PageFormat format = report.getPageFormat();
        if (letterheadRequired) {
            dynamicReportsComponent
                    = cmp.horizontalList(
                            cmp.image(SystemConfig.PRINT_LETTERHEAD_IMAGE)
                                    .setPositionType(ComponentPositionType.FLOAT)
                                    //.setMinWidth(page.getWidth())
                                    .setImageScale(ImageScale.FILL_FRAME));
        }
        //else {
        //    dynamicReportsComponent = cmp.verticalList(cmp.verticalGap(pageFormat.getPageHeaderSize()));
        //}
        return cmp.verticalList()
                .add(
                        dynamicReportsComponent,
                        cmp.text(title.getValue(targetObject).toString()).setStyle(theme.createStyle(report.getTitleStyle())));
        //.add(cmp.line());
        //.newRow()
        //.add(cmp.verticalGap(10));
    }

    public ComponentBuilder<?, ?> createReportSection(List<ReportSectionContainer> containers) {
        VerticalListBuilder build = cmp.verticalList();
        if (containers != null) {
            //System.out.println("createReportSection: " + containers.size() + "." + containers.get(0).getReportSection().size()
            //        + "." + containers.get(0).getReportSection().get(0).getReportFields().size());
            for (ReportSectionContainer con : containers) {
                if (con.getGapBefore() > 0) {
                    build.add(cmp.verticalGap(con.getGapBefore()));
                }
                if (con.getPrintLayout() == ReportSectionContainer.ContainerPrintLayout.HORIZONTAL) {
                    HorizontalListBuilder hb = cmp.horizontalList();
                    if (con.getReportSection() != null) {
                        for (ReportSection section : con.getReportSection()) {
                            if (section.getGapBefore() > 0) {
                                hb.add(cmp.horizontalGap(section.getGapBefore()));
                            }

                            hb.add(createReportSection(section));

                            if (section.getGapAfter() > 0) {
                                hb.add(cmp.horizontalGap(section.getGapAfter()));
                            }
                        }
                    }
                    build.add(hb);
                } else {
                    VerticalListBuilder vb = cmp.verticalList();
                    if (con.getReportSection() != null) {
                        for (ReportSection section : con.getReportSection()) {
                            if (section.getGapBefore() > 0) {
                                vb.add(cmp.verticalGap(section.getGapBefore()));
                            }

                            vb.add(createReportSection(section));

                            if (section.getGapAfter() > 0) {
                                vb.add(cmp.verticalGap(section.getGapAfter()));
                            }
                        }
                        build.add(vb);
                    }
                }
                if (con.getGapAfter() > 0) {
                    build.add(cmp.verticalGap(con.getGapAfter()));
                }
            }
        }

        return build;
    }

    public ComponentBuilder<?, ?> createReportSection(ReportSection section) {
        //System.out.println("createReportSection: sectionWidth: " + section.getSectionWith(reportWidth));
        ComponentBuilder<?, ?> build = cmp.verticalList();
        if (section != null) {
            if (section.getPrintLayout() == ReportSection.SectionPrintLayout.VERTICAL) {
                VerticalListBuilder fb = cmp.verticalList().setGap(1);
                if (section.getSectionWith(reportWidth) > 0) {
                    //System.out.println("createReportSection_VERTICAL: sectionWidth: " + section.getSectionWith(reportWidth));
                    fb.setFixedWidth(section.getSectionWith(reportWidth));
                }
                //if (section.getGapBefore() > 0) {
                //    fb.add(cmp.horizontalGap(section.getGapBefore()));
                //}
                if (section.getTitle() != null) {
                    fb.add(cmp.text(section.getTitle()).setStyle(theme.createStyle(section.getTitleStyle())));
                }
                for (ReportField field : section.getReportFields()) {
                    fb.add(createReportFieldComponent(field));
                }
                //if (section.getGapAfter() > 0) {
                //    fb.add(cmp.horizontalGap(section.getGapAfter()));
                //}

                build = fb;
            } else {
                HorizontalListBuilder hb = cmp.horizontalList();
                if (section.getSectionWith(reportWidth) > 0) {
                    //System.out.println("createReportSection_HORIZONTAL: sectionWidth: " + section.getSectionWith(reportWidth));
                    hb.setFixedWidth(section.getSectionWith(reportWidth));
                }
                //if (section.getGapBefore() > 0) {
                //    hb.add(cmp.horizontalGap(section.getGapBefore()));
                //}
                for (ReportField field : section.getReportFields()) {
                    hb.add(createReportFieldComponent(field));
                }
                //if (section.getGapAfter() > 0) {
                //    hb.add(cmp.horizontalGap(section.getGapAfter()));
                //}

                build = hb;
            }
        }
        return build;
    }

    public ComponentBuilder<?, ?> createReportFieldComponent(ReportField field) {

        if (field.getPrintLayout() == ReportField.FieldPrintLayout.TOP_BOTTOM) {
            if (field.isPrintLabel()) {
                return cmp.verticalList().add(cmp.text(field.getLabel()).setStyle(theme.createStyle(field.getLabelStyle())),
                        cmp.text(field.getValue(targetObject)).setStyle(theme.createStyle(field.getTextStyle())));
            }
            return cmp.verticalList().add(cmp.text(field.getValue(targetObject)).setStyle(theme.createStyle(field.getTextStyle())));
        } else {
            if (field.isPrintLabel()) {
                return cmp.horizontalList().add(cmp.text(field.getLabel()).setFixedColumns(8).setStyle(theme.createStyle(field.getLabelStyle())),
                        cmp.text(field.getValue(targetObject)).setStyle(theme.createStyle(field.getTextStyle())));
            }
            return cmp.horizontalList().add(cmp.text(field.getValue(targetObject)).setStyle(theme.createStyle(field.getTextStyle())));
        }
    }

    public ComponentBuilder<?, ?> createSectionTitleComponent(ReportSection section) {
        if (section.getPrintLayout() == ReportSection.SectionPrintLayout.VERTICAL) {
        }
        return cmp.verticalList();
    }

}
