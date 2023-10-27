package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Books;

public interface BooksRepository extends JpaRepository<Books, Long> {

	Books findByIsbn(String isbn);

	Books deleteBookByIsbn(String isbn);

}
