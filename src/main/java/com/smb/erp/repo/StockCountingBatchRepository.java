/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.StockCountingBatch;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface StockCountingBatchRepository extends BaseRepository<StockCountingBatch, Long> {

    @Query("SELECT OBJECT(mov) FROM StockCountingBatch AS mov "
                + "WHERE mov.busdoc.docno=:docno AND mov.productid.productid=:productid "
                + "ORDER BY mov.countedon DESC")
    List<StockCountingBatch> findStockCountingBatchByDoc(@Param("docno") String docno, @Param("productid") long productid);
    
}
