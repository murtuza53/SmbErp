	package com.smb.erp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smb.erp.entity.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	//@Query("SELECT a FROM Author a WHERE firstName = ?1 AND lastName = ?2")
    //List<Author> findByFirstNameAndLastName(String firstName, String lastName);
    
	//@Query(value = "SELECT * FROM author WHERE first_name = :firstName", nativeQuery = true)
    //List<Author> findAuthorsByFirstName(@Param("firstName") String firstName);
	
	@Query("SELECT p FROM ProductCategory p WHERE p.prodcategry.prodcatId=?1 ORDER BY p.catname")
	List<ProductCategory> findByParentId(Long parentId);
}
