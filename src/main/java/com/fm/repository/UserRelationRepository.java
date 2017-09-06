package com.fm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fm.model.UserRelation;

@Repository("userRelationRepository")
public interface UserRelationRepository extends JpaRepository<UserRelation, String>{
	
}
