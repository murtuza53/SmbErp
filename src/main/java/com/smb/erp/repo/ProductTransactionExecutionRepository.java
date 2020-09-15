/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.ProductTransactionExecution;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface ProductTransactionExecutionRepository extends BaseRepository<ProductTransactionExecution, Integer> {

    @Query("SELECT a FROM ProductTransactionExecution as a WHERE a.fromprodtransid.prodtransid = :prodtransid ORDER by a.createdon")
    List<ProductTransactionExecution> findByFromProductTransaction(@Param("prodtransid") int prodtransid);

    @Query("SELECT a FROM ProductTransactionExecution as a WHERE a.toprodtransid.prodtransid = :prodtransid ORDER by a.createdon")
    List<ProductTransactionExecution> findByToProductTransaction(@Param("prodtransid") int prodtransid);

    @Modifying
    @Query("DELETE FROM ProductTransactionExecution a where a.toprodtransid.prodtransid=:prodtransid")
    void deleteToProductTransations(@Param("prodtransid") int prodtransid);
}
