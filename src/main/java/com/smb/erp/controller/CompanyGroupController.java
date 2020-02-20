/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.CompanyGroup;
import com.smb.erp.repo.CompanyGroupRepository;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named(value = "companygroupController")
@ViewScoped
public class CompanyGroupController extends AbstractController<CompanyGroup> {

    CompanyGroupRepository repo;

    @Autowired
    public CompanyGroupController(CompanyGroupRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(CompanyGroup.class, repo);
        this.repo = repo;
    }

}
