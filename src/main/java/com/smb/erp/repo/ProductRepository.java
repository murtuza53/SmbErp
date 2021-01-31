package com.smb.erp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smb.erp.entity.Product;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
    //@Query("SELECT a FROM Author a WHERE firstName = ?1 AND lastName = ?2")
    //List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    //@Query(value = "SELECT * FROM author WHERE first_name = :firstName", nativeQuery = true)
    //List<Author> findAuthorsByFirstName(@Param("firstName") String firstName);
    @Query("SELECT p FROM Product p WHERE p.prodcategory.prodcatId=?1 ORDER BY p.productname")
    List<Product> findByCategoryId(Long catId);

    @Query("SELECT OBJECT(p) FROM Product as p "
            + "WHERE (TRIM(p.productid) LIKE %:criteria% OR p.productname LIKE %:criteria% OR "
            + "p.supplierscode LIKE %:criteria% OR p.stockid LIKE %:criteria% OR "
            + "p.barcode1 LIKE %:criteria% OR p.barcode2 LIKE %:criteria%) AND p.inactive=0 "
            + "ORDER BY p.productname asc")
    List<Product> findByCriteria(@Param("criteria") String criteria);
    
    @Query("SELECT OBJECT(p) FROM Product as p "
            + "WHERE (TRIM(p.productid)=:criteria OR p.stockid=:criteria OR "
            + "p.supplierscode=:criteria OR p.barcode1=:criteria OR p.barcode2=:criteria) AND p.inactive=0 "
            + "ORDER BY p.productname asc")
    List<Product> findByProductidOrSupplierCodeOrBarcodes(@Param("criteria") String criteria);
}
