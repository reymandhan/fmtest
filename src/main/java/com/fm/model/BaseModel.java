package com.fm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class BaseModel {

	private Date createdDt;

	@Column(name="created_dt")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

}
