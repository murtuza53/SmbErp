/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Burhani152
 */
@Service
public class DatabaseService {

    @PersistenceContext
    EntityManager em;

    public Query createQuery(String query, Class T){
        return em.createQuery(query, T);
    }

    public Query createNativeQuery(String query){
        return em.createNativeQuery(query);
    }

    @Transactional
    public List<T> executeQuery(String query, Class T){
        return em.createQuery(query, T).getResultList();
    }

    @Transactional
    public List<T> executeNativeQuery(String query, Class T){
        return em.createNativeQuery(query, T).getResultList();
    }
    
    @Transactional
    public List<Object> executeCountNativeQuery(String query){
        return em.createNativeQuery(query).getResultList();
    }    
    
    @Transactional
    public List<Object[]> executeNativeQuery(String query){
        return em.createNativeQuery(query).getResultList();
    }

    @Transactional
    public List<Tuple> executeNativeQueryTuple(String query){
        return em.createNativeQuery(query).getResultList();
    }
    
}
