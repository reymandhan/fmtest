package com.fm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_relations")
@Entity
public class UserRelation extends BaseModel {

	private String id;
	private String userId;
	private String relatedUserid;
	private Integer type;
	private Boolean isBlocked;
	
	public UserRelation(){
		super();
	}

	public UserRelation(String id, String userId, String relatedUserid, Integer type, Boolean isBlocked) {
		super();
		this.id = id;
		this.userId = userId;
		this.relatedUserid = relatedUserid;
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
	public String getRelatedUserid() {
		return relatedUserid;
	}

	public void setRelatedUserid(String relatedUserid) {
		this.relatedUserid = relatedUserid;
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

}
