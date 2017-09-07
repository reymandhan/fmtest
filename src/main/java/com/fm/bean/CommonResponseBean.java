package com.fm.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseBean {
	
	private Boolean success;
	private String messages;
	private List<String> errors;
	private Integer count;

	public CommonResponseBean() {
		super();
	}

	public CommonResponseBean(Boolean status) {
		this.success = status;
	}

	public CommonResponseBean(Boolean status, String messages, List<String> errors) {
		super();
		this.success = status;
		this.messages = messages;
		this.errors = errors;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
