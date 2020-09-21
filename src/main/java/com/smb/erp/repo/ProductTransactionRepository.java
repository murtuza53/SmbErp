package com.smb.erp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smb.erp.entity.ProductTransaction;
import java.util.Date;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductTransactionRepository extends BaseRepository<ProductTransaction, Integer> {
    //@Query("SELECT a FROM Author a WHERE firstName = ?1 AND lastName = ?2")
    //List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    //@Query(value = "SELECT * FROM author WHERE first_name = :firstName", nativeQuery = true)
    //List<Author> findAuthorsByFirstName(@Param("firstName") String firstName);
    @Query(value = "SELECT sum(received)-sum(sold) as balance from prodtransaction "
            + "where productid=:productid and branchid=:branchid and transdate<=:toDate ", nativeQuery = true)
    Double findStockBalanceByBranch(@Param("productid") long productid, @Param("branchid") int branchid, @Param("toDate") Date toDate);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov "
                + "WHERE mov.product.productid=:productid AND mov.transdate>=:fromDate AND mov.transdate<=:toDate AND mov.transactiontype=:transactiontype "
                + "ORDER BY mov.transdate DESC")
    List<ProductTransaction> findStockMovement(@Param("productid") long productid, 
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transactiontype") String transactiontype);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov "
                + "WHERE mov.product.productid=:productid AND mov.transdate>=:fromDate AND mov.transdate<=:toDate AND "
                + "(mov.transactiontype=:transactiontype1 OR mov.transactiontype=:transactiontype2)"
                + "ORDER BY mov.transdate DESC")
    List<ProductTransaction> findStockMovement(@Param("productid") long productid, 
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transactiontype1") String transactiontype1, 
            @Param("transactiontype2") String transactiontype2);
    
    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov WHERE mov.busdoc.docno LIKE %:doctype% " 
            + "AND mov.product.productid=:productid AND mov.transdate<=:toDate ORDER BY mov.transdate")
    List<ProductTransaction> findLastTransaction(@Param("productid") long productid, 
            @Param("toDate") Date toDate, @Param("doctype") String doctype, Pageable pageable);
}
