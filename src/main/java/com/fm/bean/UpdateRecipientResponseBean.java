package com.fm.bean;

import java.util.List;

public class UpdateRecipientResponseBean extends CommonResponseBean{
	private List<String> recipients;

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	
}

