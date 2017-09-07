package com.fm.service;

import javax.validation.ConstraintViolationException;

public interface UserService {
	public boolean createUserFriendship(String[]emails) throws ConstraintViolationException;
	
	public void deleteUser(String email1, String email2);
}
