package com.fm.bean;

import java.util.List;

public class FriendListResponseBean extends CommonResponseBean {

	private List<String> friends;

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

}
