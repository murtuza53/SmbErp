/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.ContactPerson;
import com.smb.erp.repo.ContactPersonRepository;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burhani152
 */
@Named(value = "contactPersonController")
@ViewScoped
@Service
public class ContactPersonController extends AbstractController<ContactPerson> {

    ContactPersonRepository repo;

    @Autowired
    public ContactPersonController(ContactPersonRepository repo) {
        // Inform the Abstract parent controller of the concrete ItsMaster Entity
        super(ContactPerson.class, repo);
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
    }

}
