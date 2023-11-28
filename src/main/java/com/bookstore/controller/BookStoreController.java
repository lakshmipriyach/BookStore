package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entity.Books;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.UserNotFoundException;
import com.bookstore.payload.AddListOfBooks;
import com.bookstore.payload.BooksResult;
import com.bookstore.payload.DeleteBook;
import com.bookstore.payload.DeleteBookResponse;
import com.bookstore.payload.DeleteBookResult;
import com.bookstore.payload.DeleteUsersBooks;
import com.bookstore.payload.Message;
import com.bookstore.payload.ReplaceIsbn;
import com.bookstore.service.BookStoreService;

@RestController
@RequestMapping("/BookStore")
@CrossOrigin(origins = "http://localhost:5173/")
public class BookStoreController {

	@Autowired
	private BookStoreService bookStoreService;

	public BookStoreController(BookStoreService bookStoreService) {
		this.bookStoreService = bookStoreService;
	}

	// Post the Books
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/Books")
	public ResponseEntity<Books> addBook(@RequestBody Books books) {
		Books newBook = bookStoreService.postBook(books);
		return new ResponseEntity<>(newBook, HttpStatus.CREATED);
	}

	// Get All Books
	@GetMapping("/Books")
	public ResponseEntity<List<Books>> getAllBooks() {
		List<Books> allBooks = bookStoreService.getAllBooks();
		return ResponseEntity.ok(allBooks);
	}

	// Delete the Book
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/Books/{isbn}")
	public ResponseEntity<DeleteBookResponse> deleteBook(@PathVariable String isbn) {
		DeleteBookResponse bookToDelete = bookStoreService.deleteBook(isbn);

		if (bookToDelete.getMessage().equals("Book not found")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookToDelete);
		} else {
			return ResponseEntity.ok(bookToDelete);
		}
	}

	// Get the Book By isbn
	@GetMapping("/Books/{isbn}")
	public ResponseEntity<Books> getBookByIsbn(@PathVariable String isbn) {
		Books book = bookStoreService.getBookByIsbn(isbn);
		return ResponseEntity.ok(book);
	}

	// Add the Book For The User
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PostMapping("/Book")
	public ResponseEntity<BooksResult> addBookToUser(@RequestBody AddListOfBooks addListOfBooks) {
		try {
			BooksResult result = bookStoreService.addBookToUser(addListOfBooks);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (UserNotFoundException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle user not found
		} catch (BookNotFoundException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle book not found
		}

	}

	// Update the book for user
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PutMapping("/Book/{isbn}")
	public ResponseEntity<Message> updateBookToUser(@PathVariable("isbn") String isbn,
			@RequestBody ReplaceIsbn replaceIsbn) {
		replaceIsbn.setIsbn(isbn);

		Message message = bookStoreService.updateBookToUser(replaceIsbn);
		if (message.getCode() == 400) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} else {
			return ResponseEntity.ok(message);
		}

	}

	// Delete the book for user
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@DeleteMapping("/Book")
	public ResponseEntity<DeleteBookResult> deleteBookFromUser(@RequestBody DeleteBook deleteBook) {
		DeleteBookResult result = bookStoreService.deleteBookFromUser(deleteBook);

		if (result.getMessage().equals("Book Deleted successfully")) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
	    }
	}
	
	// Delete all books to user
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@DeleteMapping("/Book/{userId}")
	public ResponseEntity<DeleteUsersBooks> deleteUsersBooks(@PathVariable String userId) {
		DeleteUsersBooks result = bookStoreService.deleteUsersBooks(userId);

		if (result.getMessage().equals("No books found")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
	    } else {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
	    }
	}
}
