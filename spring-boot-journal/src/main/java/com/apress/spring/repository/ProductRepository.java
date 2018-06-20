package com.apress.spring.repository;


import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.transaction.annotation.Transactional;


import com.apress.spring.domain.Product;
import com.apress.spring.domain.RowLock;


@Transactional
@RepositoryRestResource(collectionResourceRel = "entry", path = "journal")
public interface ProductRepository extends JpaRepository<Product, Long> { 
        


	@Query("select a from Product a  where prod_id=:id")
	List<Product> findByCustomQuery(@Param("id") long id);

	@Query("select a from Product a where sleep(0.1)=0")
        List<Product> sleepCustomQuery();

	@Query("select a from Product a where id=:id and sleep(10)=0")
        List<Product> findByIdAndSleepCustomQuery(@Param("id") long id);

	@Query("select max(prod_id)+1 from Product where sleep(15)=0")
        int getMaxProdIdValue();
	
        @Modifying
	@Query("update Product a set a.prod_nm = :prod_nm where a.prod_id=:prod_id and sleep(15)=0")
        int updateProdNmByProdId(@Param("prod_nm") String prod_nm, @Param("prod_id") long prod_id);
}
