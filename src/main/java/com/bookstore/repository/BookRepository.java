package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Book;
import com.bookstore.entity.User;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByUserId(String userId);

	List<Book> findByUser(User user);

	Book findByUserIdAndIsbn(String userId, String isbn);

	void deleteUserBookByUserId(String userId);

}
