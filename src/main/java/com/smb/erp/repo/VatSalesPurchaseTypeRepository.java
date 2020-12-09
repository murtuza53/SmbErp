/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.VatSalesPurchaseType;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface VatSalesPurchaseTypeRepository extends BaseRepository<VatSalesPurchaseType, Long> {

    @Query("SELECT a FROM VatSalesPurchaseType as a WHERE a.category = :category")
    List<VatSalesPurchaseType> findByCategory(@Param("category") String category);

}
