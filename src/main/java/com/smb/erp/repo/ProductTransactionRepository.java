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
            + "where productid=:productid and branchid=:branchid and transdate<=:toDate and transactiontype='Inventory' ", nativeQuery = true)
    Double findStockBalanceByBranch(@Param("productid") long productid, @Param("branchid") long branchid, @Param("toDate") Date toDate);

    @Query(value = "SELECT sum(line.received)-sum(line.sold) as balance from ProductTransaction as line "
            + "where line.product.productid=:productid and line.busdoc.branch.company.companyid=:companyid and line.transdate<=:toDate and transactiontype='Inventory'")
    Double findStockBalanceByCompany(@Param("productid") long productid, @Param("companyid") long companyid, @Param("toDate") Date toDate);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov "
                + "WHERE mov.product.productid=:productid AND mov.transdate>=:fromDate AND mov.transdate<=:toDate AND mov.transactiontype='Inventory' "
                + "ORDER BY mov.transdate DESC")
    List<ProductTransaction> findStockMovement(@Param("productid") long productid, 
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov "
                + "WHERE mov.product.productid=:productid AND mov.transdate>=:fromDate AND mov.transdate<=:toDate AND mov.transactiontype=:transactiontype "
                + "ORDER BY mov.transdate DESC")
    List<ProductTransaction> findStockMovement(@Param("productid") long productid, 
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transactiontype") String transactiontype);

    /*@Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov "
                + "WHERE mov.product.productid=:productid AND mov.transdate>=:fromDate AND mov.transdate<=:toDate AND mov.transactiontype!=:not_transactiontype "
                + "ORDER BY mov.transdate DESC")
    List<ProductTransaction> findStockMovementAllInventory(@Param("productid") long productid, 
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("not_transactiontype") String not_transactiontype);*/

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

    @Query("SELECT avg(mov.cost) FROM ProductTransaction AS mov WHERE (mov.busdoc.busdocinfo.doctype='Purchase' OR mov.busdoc.busdocinfo.doctype='Inventory') " 
            + "AND mov.transactiontype='Inventory' AND mov.product.productid=:productid ORDER BY mov.transdate desc")
    Double findAveragePurchaseOrAdjustment(@Param("productid") long productid);

    @Query("SELECT avg(mov.cost) FROM ProductTransaction AS mov WHERE (mov.busdoc.busdocinfo.doctype='Purchase' OR mov.busdoc.busdocinfo.doctype='Inventory') " 
            + "AND mov.transactiontype='Inventory' AND mov.product.productid=:productid AND mov.transdate<=:toDate ORDER BY mov.transdate desc")
    Double findAveragePurchaseOrAdjustment(@Param("productid") long productid, @Param("toDate") Date toDate);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov WHERE (mov.busdoc.busdocinfo.doctype='Purchase' OR mov.busdoc.busdocinfo.doctype='Inventory') " 
            + "AND mov.transactiontype='Inventory' AND mov.product.productid=:productid ORDER BY mov.transdate desc")
    List<ProductTransaction> findLastCostPurchaseOrAdjustment(@Param("productid") long productid, Pageable pageable);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov WHERE (mov.busdoc.busdocinfo.doctype='Purchase' OR mov.busdoc.busdocinfo.doctype='Inventory') " 
            + "AND mov.transactiontype='Inventory' AND mov.product.productid=:productid AND mov.transdate<=:toDate ORDER BY mov.transdate desc")
    List<ProductTransaction> findLastCostPurchaseOrAdjustment(@Param("productid") long productid, @Param("toDate") Date toDate, Pageable pageable);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov WHERE mov.busdoc.busdocinfo.doctype='Purchase' " 
            + "AND mov.transactiontype='Inventory' AND mov.product.productid=:productid ORDER BY mov.transdate desc")
    List<ProductTransaction> findLastCostPurchase(@Param("productid") long productid, Pageable pageable);

    @Query("SELECT OBJECT(mov) FROM ProductTransaction AS mov WHERE mov.busdoc.busdocinfo.doctype='Purchase' " 
            + "AND mov.transactiontype='Inventory' AND mov.product.productid=:productid AND mov.transdate<=:toDate ORDER BY mov.transdate desc")
    List<ProductTransaction> findLastCostPurchase(@Param("productid") long productid, @Param("toDate") Date toDate, Pageable pageable);
}
