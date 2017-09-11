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
	
	public UserRelation findByUserIdAndRelatedUserId(String userId, String relatedUserId);
	
	@Query("SELECT count(1) FROM UserRelation u JOIN u.user uu JOIN u.relatedUser uru WHERE uu.email =:email1 and uru.email=:email2 and type=:type")
	public int isRelationExists(@Param("email1") String email1, @Param("email2") String email2, @Param("type") int type);
	
	@Query("SELECT count(1) FROM UserRelation u JOIN u.user uu JOIN u.relatedUser uru WHERE uu.email =:email1 and uru.email=:email2")
	public int isRelationExists(@Param("email1") String email1, @Param("email2") String email2);	

	@Query("SELECT u.email FROM User u WHERE id IN (SELECT ur.relatedUserId FROM UserRelation ur JOIN ur.user uu WHERE uu.email=:email)")
	public List<String> retrieveFriendList(@Param("email") String email);

	@Query(value = "SELECT email FROM (SELECT COUNT(1) AS total, email FROM user_relations ur JOIN users u ON ur.user_id = u.id WHERE ur.related_user_id IN ( SELECT id FROM users u WHERE email IN (:email1,:email2)) GROUP BY email ) AS a WHERE total = 2", nativeQuery=true)
	public List<String> retrieveCommonFriendList(@Param("email1") String email1, @Param("email2") String email2);
	
	@Query("SELECT u.email FROM UserRelation ur JOIN ur.relatedUser ru JOIN ur.user u where ru.email=:email AND ur.isBlocked IS false AND ur.type IS NOT NULL")
	public List<String> retrieveUpdateRecipient(@Param("email") String email);

	@Modifying
	@Query("DELETE FROM UserRelation ur WHERE ur.userId = (select id from User where email=:email) or ur.relatedUserId = (select id from User where email=:email)")
	public void deleteUserRelation(@Param("email") String email);
}
