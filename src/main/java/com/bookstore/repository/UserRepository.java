package com.bookstore.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUserName(String userName);

	User findByUserName(String userName);

	User findByUserId(String userId);

}
