package com.fm.bean;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class RequestorTargetRequestBean {

	@NotBlank
	@Email
	private String requestor;

	@NotBlank
	@Email
	private String target;

	public RequestorTargetRequestBean() {
		super();
	}

	public RequestorTargetRequestBean(String requestor, String target) {
		super();
		this.requestor = requestor;
		this.target = target;
	}

	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
