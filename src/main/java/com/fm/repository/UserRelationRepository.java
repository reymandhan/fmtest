package com.fm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fm.model.UserRelation;

@Repository("userRelationRepository")
public interface UserRelationRepository extends JpaRepository<UserRelation, String> {

	@Query("SELECT count(1) FROM UserRelation u JOIN u.user uu JOIN u.relatedUser uru WHERE uu.email =:email1 and uru.email=:email2 and type=1")
	public int isFriendshipExists(@Param("email1") String email1, @Param("email2") String email2);

	@Query("SELECT u.email FROM User u WHERE id in (SELECT ur.relatedUserId FROM UserRelation ur JOIN ur.user uu WHERE uu.email=:email)")
	public List<String> retrieveFriendList(@Param("email") String email);

	@Modifying
	@Query("DELETE FROM UserRelation ur WHERE ur.userId = (select id from User where email=:email) or ur.relatedUserId = (select id from User where email=:email)")
	public void deleteUserRelation(@Param("email") String email);
}
