	package com.smb.erp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smb.erp.entity.ProductCategory;

@Repository
public interface ProductCategoryRepository extends BaseRepository<ProductCategory, Long> {
	//@Query("SELECT a FROM Author a WHERE firstName = ?1 AND lastName = ?2")
    //List<Author> findByFirstNameAndLastName(String firstName, String lastName);
    
	//@Query(value = "SELECT * FROM author WHERE first_name = :firstName", nativeQuery = true)
    //List<Author> findAuthorsByFirstName(@Param("firstName") String firstName);
	
	@Query("SELECT p FROM ProductCategory p WHERE p.prodcategry.prodcatId=?1 ORDER BY p.catname")
	List<ProductCategory> findByParentId(Long parentId);
        
        @Query(value = "SELECT * FROM prodcategry c1 LEFT JOIN prodcategry c2 ON c2.parentid = c1.prodcatId WHERE c2.prodcatId IS NULL ORDER BY c1.catname", nativeQuery = true)
        List<ProductCategory> findCategoryLeafNodes();

        @Query(value = "SELECT * FROM prodcategry c1 LEFT JOIN prodcategry c2 ON c2.parentid = c1.prodcatId WHERE c2.prodcatId IS NULL AND c1.catname LIKE %?1% ORDER BY c1.catname", nativeQuery = true)
        List<ProductCategory> findCategoryLeafNodesByCriteria(String criteria);
}
