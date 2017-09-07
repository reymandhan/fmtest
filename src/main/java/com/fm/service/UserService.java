package com.fm.service;

import javax.validation.ConstraintViolationException;

public interface UserService {
	public boolean createUserFriendship(String[]emails) throws ConstraintViolationException;
}
