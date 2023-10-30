package com.bookstore.service;

import java.util.List;

import com.bookstore.entity.Books;
import com.bookstore.payload.AddListOfBooks;
import com.bookstore.payload.BooksResult;
import com.bookstore.payload.DeleteBook;
import com.bookstore.payload.DeleteBookResponse;
import com.bookstore.payload.DeleteBookResult;
import com.bookstore.payload.DeleteUsersBooks;
import com.bookstore.payload.Message;
import com.bookstore.payload.ReplaceIsbn;

public interface BookStoreService {

	// Post the Books
	Books postBook(Books books);

	// Get all the books
	List<Books> getAllBooks();

	// Get book by isbn
	Books getBookByIsbn(String isbn);

	// Delete the book 
	DeleteBookResponse deleteBook(String isbn);

	// Add book to user
	BooksResult addBookToUser(AddListOfBooks addListOfBooks);

	// delete book to user
	DeleteBookResult deleteBookFromUser(DeleteBook deleteBook);
	
	// delete all books to user
	DeleteUsersBooks deleteUsersBooks(String userId);

	// update book to user
	Message updateBookToUser(ReplaceIsbn replaceIsbn);

	


}
