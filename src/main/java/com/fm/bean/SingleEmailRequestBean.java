package com.fm.bean;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class SingleEmailRequestBean {

	@NotBlank
	@Email
	private String email;

	public SingleEmailRequestBean() {
		super();
	}

	public SingleEmailRequestBean(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
