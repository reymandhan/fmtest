package com.fm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fm.constant.RelationType;
import com.fm.model.User;
import com.fm.model.UserRelation;
import com.fm.repository.UserRelationRepository;
import com.fm.repository.UserRepository;
import com.fm.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRelationRepository userRelationRepository;

	@Override
	@Transactional
	public boolean createUserFriendship(String [] emails) {
		boolean status = false;
		
		try{
			List<String>ids = new ArrayList<>();
			
			for (String email:emails){
				User user = userRepository.findByEmail(email);
				if (user == null){
					String id = UUID.randomUUID().toString();
					ids.add(id);
					
					userRepository.save(new User(id,email));
				}else{
					ids.add(user.getId());
				}
			}
			
			userRelationRepository.save(new UserRelation(UUID.randomUUID().toString(),ids.get(0), ids.get(1),RelationType.FRIEND.value(), false));
			userRelationRepository.save(new UserRelation(UUID.randomUUID().toString(),ids.get(1), ids.get(0),RelationType.FRIEND.value(), false));
			
			status = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return status;
	}
	
}
