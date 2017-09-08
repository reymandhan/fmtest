package com.fm.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

public interface UserService {
	public boolean createUserFriendship(String[]emails) throws ConstraintViolationException;
	
	public List<String> retrieveFriendList(String email);
	
	public List<String> retrieveCommonFriendList(String email1, String email2);
	
	public void deleteUser(String email1, String email2, boolean deleteSecondEmail);
}
