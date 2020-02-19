/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.BusDoc;
import com.smb.erp.repo.BusDocRepository;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "busDocController")
@ViewScoped
public class BusDocController extends AbstractController<BusDoc> {

    BusDocRepository repo;

    @Autowired
    public BusDocController(BusDocRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(BusDoc.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init(){
        setSelected(new BusDoc());
        getSelected().setCreatedon(new Date());
    }
}
