package com.fm.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

public interface UserService {
	public boolean createUserFriendship(String[]emails) throws ConstraintViolationException;
	
	public List<String> retrieveFriendList(String email);
	
	public List<String> retrieveCommonFriendList(String email1, String email2);
	
	public void createSubscribership(String requestor, String target) throws ConstraintViolationException;
	
	public void blockUser(String requestor, String target) throws ConstraintViolationException;
	
	public List<String> retrieveUpdateRecipient(String email, String text)throws ConstraintViolationException;
	
	public void deleteUser(String email1, String email2, boolean deleteSecondEmail);
}
