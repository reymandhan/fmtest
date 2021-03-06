package com.fm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "user_relations")
@Entity
public class UserRelation extends BaseModel {

	private String id;
	private String userId;
	private String relatedUserId;
	private Integer type;
	private Boolean isBlocked;
	
	private User user;
	private User relatedUser;
	
	public UserRelation(){
		super();
	}

	public UserRelation(String id, String userId, String relatedUserid, Integer type, Boolean isBlocked) {
		super();
		this.id = id;
		this.userId = userId;
		this.relatedUserId = relatedUserid;
		this.type = type;
		this.isBlocked = isBlocked;
		this.setCreatedDt(new Date());
	}

	@Id
	@Column
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "related_user_id")
	public String getRelatedUserId() {
		return relatedUserId;
	}

	public void setRelatedUserId(String relatedUserId) {
		this.relatedUserId = relatedUserId;
	}

	@Column
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "is_blocked")
	public Boolean getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	@ManyToOne
	@JoinColumn(name="user_id",insertable=false, updatable=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name="related_user_id", insertable=false, updatable=false)
	public User getRelatedUser() {
		return relatedUser;
	}

	public void setRelatedUser(User relatedUser) {
		this.relatedUser = relatedUser;
	}
	
	

}
