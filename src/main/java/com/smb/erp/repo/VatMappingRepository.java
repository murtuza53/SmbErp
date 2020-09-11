/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.VatMapping;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface VatMappingRepository extends BaseRepository<VatMapping, Integer> {

    @Query("SELECT a FROM VatMapping as a WHERE a.vataccounttypeid.vataccounttypeid = :vataccounttypeid AND a.vatcategoryid.vatcategoryid = :vatcategoryid AND a.producttype = :producttype AND a.transactiontype = :transactiontype")
    List<VatMapping> findByVatSalesPurchaseType(@Param("vataccounttypeid") int vataccounttypeid, @Param("vatcategoryid") int vatcategoryid, @Param("producttype") String producttype, @Param("transactiontype") String transactiontype);

}
