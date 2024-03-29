package com.josipk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.josipk.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

}
