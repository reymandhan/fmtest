package com.fm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fm.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, String>{

	public User findByEmail(String email);
}
