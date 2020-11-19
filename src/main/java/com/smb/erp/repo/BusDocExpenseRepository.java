/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.BusDocExpense;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface BusDocExpenseRepository extends BaseRepository<BusDocExpense, Integer>{
    List<BusDocExpense> findByOrderByExpenseidAsc();
}
