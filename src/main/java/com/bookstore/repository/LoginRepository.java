package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {

	boolean existsByUserName(String userName);

	Login findByUserName(String userName);

	Login findByUserId(String userId);

}
