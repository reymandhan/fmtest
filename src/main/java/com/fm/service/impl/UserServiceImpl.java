package com.fm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.apache.commons.validator.routines.EmailValidator;
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
	public boolean createUserFriendship(String[] emails) throws ConstraintViolationException {
		boolean status = false;

		try {
			List<String> ids = new ArrayList<>();

			for (String email : emails) {
				User user = userRepository.findByEmail(email);
				if (user == null) {
					ids.add(createNewUser(email).getId());
				} else {
					ids.add(user.getId());
				}
			}

			if (userRelationRepository.isRelationExists(emails[1], emails[0]) == 0) {
				userRelationRepository.save(new UserRelation(UUID.randomUUID().toString(), ids.get(0), ids.get(1),
						RelationType.FRIEND.value(), false));
				userRelationRepository.save(new UserRelation(UUID.randomUUID().toString(), ids.get(1), ids.get(0),
						RelationType.FRIEND.value(), false));
			} else {
				UserRelation ur = userRelationRepository.findByUserIdAndRelatedUserId(ids.get(1), ids.get(0));
				if (ur.getType() == null && ur.getIsBlocked()) {
					throw new ConstraintViolationException("Cannot add friendship, Blocked", null);
				}

				if (RelationType.FRIEND.value() == ur.getType()) {
					throw new ConstraintViolationException("Friendship is already exists", null);
				} else if (RelationType.SUBSCRIBE.value() == ur.getType()) {
					ur.setType(RelationType.FRIEND.value());
					userRelationRepository.save(ur);
					userRelationRepository.save(new UserRelation(UUID.randomUUID().toString(), ids.get(0), ids.get(1),
							RelationType.FRIEND.value(), false));
				}
			}

			status = true;
		} catch (ConstraintViolationException cve) {
			throw cve;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public List<String> retrieveCommonFriendList(String email1, String email2) {
		if (userRepository.findByEmail(email1) == null || userRepository.findByEmail(email2) == null) {
			throw new ConstraintViolationException("Email not found", null);
		}

		return userRelationRepository.retrieveCommonFriendList(email1, email2);
	}

	@Override
	public List<String> retrieveFriendList(String email) {
		if (userRepository.findByEmail(email) == null) {
			throw new ConstraintViolationException("Email not found", null);
		}

		return userRelationRepository.retrieveFriendList(email);
	}

	@Override
	@Transactional
	public void createSubscribership(String requestor, String target) throws ConstraintViolationException {

		try {
			User requestorUser = userRepository.findByEmail(requestor);
			if (requestorUser == null) {
				requestorUser = createNewUser(requestor);
			}

			User targetUser = userRepository.findByEmail(target);
			if (targetUser == null) {
				targetUser = createNewUser(target);
			}

			if (userRelationRepository.isRelationExists(requestor, target, RelationType.FRIEND.value()) == 0) {
				if (userRelationRepository.isRelationExists(requestor, target, RelationType.SUBSCRIBE.value()) == 0) {
					userRelationRepository.save(new UserRelation(UUID.randomUUID().toString(), requestorUser.getId(),
							targetUser.getId(), RelationType.SUBSCRIBE.value(), false));
				} else {
					throw new ConstraintViolationException("Requestor have subsribed to target", null);
				}
			} else {
				throw new ConstraintViolationException("Cannot subscribing, friendship is already exists", null);
			}

		} catch (ConstraintViolationException cve) {
			throw cve;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void blockUser(String requestor, String target) throws ConstraintViolationException {

		try {
			User requestorUser = userRepository.findByEmail(requestor);
			if (requestorUser == null) {
				requestorUser = createNewUser(requestor);
			}

			User targetUser = userRepository.findByEmail(target);
			if (targetUser == null) {
				targetUser = createNewUser(target);
			}

			if (userRelationRepository.isRelationExists(requestor, target) == 0) {
				userRelationRepository.save(new UserRelation(UUID.randomUUID().toString(), requestorUser.getId(),
						targetUser.getId(), null, true));
			} else {
				UserRelation ur = userRelationRepository.findByUserIdAndRelatedUserId(requestorUser.getId(),
						targetUser.getId());
				if (ur.getIsBlocked()) {
					throw new ConstraintViolationException("Requestor already blocked target", null);
				}
				ur.setIsBlocked(true);

				userRelationRepository.save(ur);
			}

		} catch (ConstraintViolationException cve) {
			throw cve;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> retrieveUpdateRecipient(String email, String text) throws ConstraintViolationException {
		if (userRepository.findByEmail(email) == null) {
			throw new ConstraintViolationException("Email not found", null);
		}
		
		List<String> result = userRelationRepository.retrieveUpdateRecipient(email);

		String[] parsedText = text.split(" ");
		List<String>tempResult = Arrays.asList(parsedText).stream().filter(s -> EmailValidator.getInstance().isValid(s))
				.collect(Collectors.toList());
		
		if (tempResult != null 	&& !tempResult.isEmpty()){
			result.addAll(tempResult);
		}

		return result;
	}

	@Override
	@Transactional
	public void deleteUser(String email1, String email2, boolean deleteSecondEmail) {
		userRelationRepository.deleteUserRelation(email1);
		userRepository.deleteByEmail(email1);
		if (deleteSecondEmail) {
			userRepository.deleteByEmail(email2);
		}
	}

	private User createNewUser(String email) {
		String id = UUID.randomUUID().toString();
		return userRepository.save(new User(id, email));
	}

}
