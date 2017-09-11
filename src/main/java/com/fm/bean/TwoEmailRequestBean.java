package com.fm.bean;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fm.annotation.EmailCollection;

public class TwoEmailRequestBean {

	@NotNull
	@Size(min=2,max=2)
	@EmailCollection
	private List< String> friends;

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

}
