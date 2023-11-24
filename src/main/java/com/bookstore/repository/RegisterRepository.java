package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Register;

public interface RegisterRepository extends JpaRepository<Register, Long> {

	boolean existsByUserName(String userName);

	Register findByUserId(String userId);

	Register findByUserName(String username);

}
