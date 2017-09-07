package com.fm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fm.model.UserRelation;

@Repository("userRelationRepository")
public interface UserRelationRepository extends JpaRepository<UserRelation, String>{
	
	@Query("SELECT count(1) FROM UserRelation u JOIN u.user uu JOIN u.relatedUser uru WHERE uu.email =:email1 and uru.email=:email2 and type=1" )
    public int isFriendshipExists(@Param("email1") String email1,@Param("email2") String email2);
}
