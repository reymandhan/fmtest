package com.fm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "USERS")
public class User extends BaseModel{

	private String id;
	private String email;
	
	public User(){
		super();
	}

	public User(String id, String email){
		super();
		this.id = id;
		this.email = email;
		setCreatedDt(new Date());
	}
	
	@Id
	@Column
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column
	@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
