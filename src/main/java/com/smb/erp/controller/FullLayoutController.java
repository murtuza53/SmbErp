/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ResizeEvent;
import org.primefaces.extensions.component.layout.LayoutPane;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 *
 * @author Burhani152
 */
@ViewScoped
public class FullLayoutController implements Serializable {

    private static final long serialVersionUID = 20120925L;

    private LayoutOptions layoutOptions;

    @PostConstruct
    protected void initialize() {
        layoutOptions = new LayoutOptions();

        // options for all panes
        final LayoutOptions panes = new LayoutOptions();
        panes.addOption("slidable", false);
        layoutOptions.setPanesOptions(panes);

        // north pane
        final LayoutOptions north = new LayoutOptions();
        north.addOption("resizable", false);
        north.addOption("closable", false);
        north.addOption("size", 60);
        north.addOption("spacing_open", 0);
        layoutOptions.setNorthOptions(north);

        final LayoutOptions center = new LayoutOptions();
        center.addOption("resizable", false);
        center.addOption("closable", false);
        center.addOption("minWidth", 200);
        center.addOption("minHeight", 60);
        layoutOptions.setCenterOptions(center);

        // set options for nested center layout
        //final LayoutOptions optionsNested = new LayoutOptions();
        //center.setChildOptions(optionsNested);

        // options for center-center pane
        //final LayoutOptions centerCenter = new LayoutOptions();
        //centerCenter.addOption("minHeight", 60);
        //optionsNested.setCenterOptions(centerCenter);

        // options for east pane
        final LayoutOptions east = new LayoutOptions();
        east.addOption("size", 200);
        layoutOptions.setEastOptions(east);

    }

    public LayoutOptions getLayoutOptions() {
        return layoutOptions;
    }

    public void handleClose(final CloseEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane closed",
                "Position:" + ((LayoutPane) event.getComponent()).getPosition());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void handleOpen(final OpenEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane opened",
                "Position:" + ((LayoutPane) event.getComponent()).getPosition());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void handleResize(final ResizeEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane resized",
                "Position:" + ((LayoutPane) event.getComponent()).getPosition() + ", new width = " + event.getWidth()
                + "px, new height = " + event.getHeight() + "px");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
