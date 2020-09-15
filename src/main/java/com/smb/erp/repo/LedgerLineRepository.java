/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.LedgerLine;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface LedgerLineRepository extends BaseRepository<LedgerLine, Integer> {

    @Query("SELECT a FROM LedgerLine as a WHERE a.account.accountid LIKE :accountid% AND a.transdate>=:fromdate AND a.transdate<=:todate ORDER BY a.transdate")
    List<LedgerLine> findByAccountIdBetweenDate(@Param("accountid") String accountid, @Param("fromdate") Date fromdate, @Param("todate") Date todate);

    @Query("SELECT sum(a.debit-a.credit) as balance FROM LedgerLine as a WHERE a.account.accountid LIKE :accountid% AND a.transdate<=:todate")
    Double findBalanceByAccountByDate(@Param("accountid") String accountid, @Param("todate") Date todate);

}
