package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	boolean existsByUserName(String userName);

	Token findByUserName(String userName);

}
