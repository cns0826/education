package com.apress.spring.repository;


import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.transaction.annotation.Transactional;

import com.apress.spring.domain.Journal;


@Transactional
@RepositoryRestResource(collectionResourceRel = "entry", path = "journal")
public interface JournalRepository extends JpaRepository<Journal, Long> { 




        

	List<Journal> findByCreatedAfter(@Param("after") @DateTimeFormat(iso = ISO.DATE) Date date);
	List<Journal> findByCreatedBetween(@Param("after") @DateTimeFormat(iso = ISO.DATE) Date after,@Param("before") @DateTimeFormat(iso = ISO.DATE) Date before);
	List<Journal> findByTitleContaining(@Param("word") String word);
	List<Journal> findBySummaryContaining(@Param("word") String word);

//	@Query("select a.title from journal a, journal b,  journal c,  journal d,  journal e,  journal f where a.title like %?1%")
	@Query("select a from Journal a  where id=:id")
	List<Journal> findByCustomQuery(@Param("id") long id);

	@Query("select a from Journal a where sleep(0.1)=0")
        List<Journal> sleepCustomQuery();

	@Query("select a from Journal a where id=:id and sleep(10)=0")
        List<Journal> findByIdAndSleepCustomQuery(@Param("id") long id);
	
}
