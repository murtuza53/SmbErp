/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.BusDocInfo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface BusDocInfoRepository extends BaseRepository<BusDocInfo, Integer> {
    
    @Query("SELECT b FROM BusDocInfo as b WHERE b.abbreviation=:abbreviation ORDER BY b.docname")
    List<BusDocInfo> findBusDocInfoByAbbreviation(@Param("abbreviation") String abbreviation);

    @Query("SELECT b FROM BusDocInfo as b WHERE b.doctype=:doctype ORDER BY b.docname")
    List<BusDocInfo> findBusDocInfoByDocType(@Param("doctype") String doctype);
    
    @Query("SELECT b FROM BusDocInfo as b WHERE b.accounttype=:accounttype ORDER BY b.docname")
    List<BusDocInfo> findBusDocInfoByTransactionType(@Param("accounttype") String accounttype);

    @Query("SELECT b FROM BusDocInfo as b WHERE b.doctype=:doctype AND b.transactiontype=:transactiontype ORDER BY b.docname")
    List<BusDocInfo> findBusDocInfoByDocAndTransactionType(@Param("doctype") String doctype, @Param("transactiontype") String transactiontype);
}
