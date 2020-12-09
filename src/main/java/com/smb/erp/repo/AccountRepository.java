/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Account;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface AccountRepository extends BaseRepository<Account, String> {

    @Query("SELECT a FROM Account as a WHERE (a.accountname LIKE %:criteria% OR a.accountid LIKE %:criteria%) AND a.nodetype='ACCOUNT' ORDER BY a.accountname")
    List<Account> findAccountLeafBySearchCriteria(@Param("criteria") String criteria);

    @Query("SELECT a FROM Account as a WHERE (a.accountname LIKE %:criteria% OR a.accountid LIKE %:criteria%) AND a.nodetype='GROUP' ORDER BY a.accountname")
    List<Account> findAccountGroupBySearchCriteria(@Param("criteria") String criteria);

    @Query("SELECT a FROM Account as a WHERE a.parentid.accountid=:parentid ORDER BY a.accountid")
    List<Account> findAccountByParent(@Param("parentid") String parentid);
    
    @Query("SELECT a FROM Account as a WHERE a.businesspartner.partnerid=:partnerid ORDER BY a.accountid")
    List<Account> findAccountByBusinessPartnerId(@Param("partnerid") Long partnerid);

    @Query("SELECT a FROM Account as a WHERE a.businesspartner.partnerid=:partnerid AND a.parentid.accountid=:parentid ORDER BY a.accountid")
    List<Account> findAccountByBusinessPartnerIdAndParent(@Param("partnerid") Long partnerid, @Param("parentid") String parentid);
}
