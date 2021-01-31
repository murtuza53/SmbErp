/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.StockCountingBusDoc;
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
public interface StockCountingBusDocRepository extends BaseRepository<StockCountingBusDoc, String> {

    @Query("SELECT a FROM StockCountingBusDoc as a WHERE a.docno LIKE %:prefix% ORDER by a.docdate desc")
    List<StockCountingBusDoc> findByBusDocByPrefix(@Param("prefix") String prefix);

    @Query("SELECT a FROM StockCountingBusDoc as a WHERE a.refno=:refno ORDER by a.refno")
    List<StockCountingBusDoc> findByBusDocByRefno(@Param("refno") String refno);

    @Query("SELECT a FROM StockCountingBusDoc as a WHERE a.docno LIKE %:prefix% AND "
            + " (a.docdate>=:fromDate AND a.docdate<=:toDate) "
            + " ORDER by a.docdate desc")
    List<StockCountingBusDoc> findByBusDocByPrefix(@Param("prefix") String prefix, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT a FROM StockCountingBusDoc as a WHERE a.docstatus=:docstatus ORDER by a.docdate desc")
    List<StockCountingBusDoc> findByBusDocByDocstatus(@Param("docstatus") String docstatus);

    //@Query(value="SELECT * FROM busdoc as a WHERE a.docno LIKE '%:prefix%' AND a.partnerid=:partnerid ORDER by a.docdate desc", nativeQuery = true)
    //List<BusDoc> findByBusDocByPrefixAndBusinessPartner(@Param("prefix") String prefix, @Param("partnerid") int partnerid);
}
