package com.scm.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer>{
	
	@Query("from Contact as c where c.user.id = :Uid")
	public List<Contact> findContactsByUid(@Param("Uid") int Uid);
	
	
}
