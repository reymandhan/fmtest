package com.fm.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseBean {
	private Boolean status;
	private String messages;
	private List<String> errors;

	public CommonResponseBean(Boolean status) {
		this.status = status;
	}

	public CommonResponseBean(Boolean status, String messages, List<String> errors) {
		super();
		this.status = status;
		this.messages = messages;
		this.errors = errors;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	

}
