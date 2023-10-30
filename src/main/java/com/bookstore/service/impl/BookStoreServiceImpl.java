package com.bookstore.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Books;
import com.bookstore.entity.User;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.BookStoreAPIException;
import com.bookstore.exception.UserNotFoundException;
import com.bookstore.payload.AddListOfBooks;
import com.bookstore.payload.BooksResult;
import com.bookstore.payload.CollectionOfIsbn;
import com.bookstore.payload.DeleteBook;
import com.bookstore.payload.DeleteBookResponse;
import com.bookstore.payload.DeleteBookResult;
import com.bookstore.payload.DeleteUsersBooks;
import com.bookstore.payload.Message;
import com.bookstore.payload.ReplaceIsbn;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.BooksRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.BookStoreService;

@Service
public class BookStoreServiceImpl implements BookStoreService {

	private BooksRepository booksRepository;
	private BookRepository bookRepository;
	private UserRepository userRepository;

	public BookStoreServiceImpl(BooksRepository booksRepository, BookRepository bookRepository,
			UserRepository userRepository) {
		this.booksRepository = booksRepository;
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}

	// Post the Books

	@Override
	public Books postBook(Books books) {

		Books postBook = booksRepository.findByIsbn(books.getIsbn());

		if (postBook != null) {
			throw new BookStoreAPIException(HttpStatus.BAD_REQUEST, "Book exist!");
		}

		Books addbook = new Books();

		addbook.setIsbn(books.getIsbn());
		addbook.setTitle(books.getTitle());
		addbook.setSubTitle(books.getSubTitle());
		addbook.setAuthor(books.getAuthor());
		addbook.setPublishDate(books.getPublishDate());
		addbook.setPublisher(books.getPublisher());
		addbook.setPages(books.getPages());
		addbook.setDescription(books.getDescription());
		addbook.setWebsite(books.getWebsite());

		addbook = booksRepository.save(addbook);

		return addbook;

	}

	// Get all the books

	@Override
	public List<Books> getAllBooks() {
		List<Books> books = booksRepository.findAll();
		return books;
	}

	// Delete the book

	@Override
	public DeleteBookResponse deleteBook(String isbn) {
		Books bookToDelete = booksRepository.findByIsbn(isbn);
		DeleteBookResponse response = new DeleteBookResponse();

		if (bookToDelete != null) {
			booksRepository.delete(bookToDelete); // Delete the book
			response.setIsbn(bookToDelete.getIsbn());
			response.setMessage("Book deleted successfully");
		} else {
			response.setMessage("Book not found");
		}

		return response;
	}

	// Get book by isbn

	@Override
	public Books getBookByIsbn(String isbn) {
		Books book = booksRepository.findByIsbn(isbn);

		if (book == null) {
			throw new BookNotFoundException("Book not found with ISBN: " + isbn);
		}

		return book;
	}

	// Add book to user

	@Override
	public BooksResult addBookToUser(AddListOfBooks addListOfBooks) {

		BooksResult result = new BooksResult();
		User user = userRepository.findByUserId(addListOfBooks.getUserId());

		if (user == null) {
			throw new UserNotFoundException("User not found with userId: " + addListOfBooks.getUserId());
		}

		List<CollectionOfIsbn> collectionOfIsbns = addListOfBooks.getCollectionOfIsbns();

		for (CollectionOfIsbn isbn : collectionOfIsbns) {
			Books books = booksRepository.findByIsbn(isbn.getIsbn());

			if (books == null) {
				throw new BookNotFoundException("Book not found with ISBN: " + isbn.getIsbn());
			}

			Book existingUserBook = bookRepository.findByUserIdAndIsbn(user.getUserId(), books.getIsbn());

			if (existingUserBook != null) {
				result.setUserId(user.getUserId());
				result.setMessage("Book with ISBN " + books.getIsbn() + " already exists!");
			} else {
				Book userBook = new Book();
				userBook.setUserId(user.getUserId());
				userBook.setIsbn(books.getIsbn());
				userBook.setTitle(books.getTitle());
				userBook.setSubTitle(books.getSubTitle());
				userBook.setAuthor(books.getAuthor());
				userBook.setPublishDate(books.getPublishDate());
				userBook.setPublisher(books.getPublisher());
				userBook.setPages(books.getPages());
				userBook.setDescription(books.getDescription());
				userBook.setWebsite(books.getWebsite());

				bookRepository.save(userBook);

				result.setUserId(userBook.getUserId());
				result.setMessage("Book added successfully!");
			}
		}
		return result;

	}

	// update book to user

	@Override
	public Message updateBookToUser(ReplaceIsbn replaceIsbn) {
		Message result = new Message();

		String userId = replaceIsbn.getUserId();
		String newIsbn = replaceIsbn.getIsbn(); // Use the new ISBN

		Book userBook = bookRepository.findByUserIdAndIsbn(userId, newIsbn);

		if (userBook != null) {
			// Update the ISBN
			userBook.setIsbn(newIsbn);

			// Save the updated book
			bookRepository.save(userBook);

			result.setCode(200);
			result.setMessage("Book Updated Successfully");
		} else {

			result.setMessage("Book not found for userId: " + userId + " and ISBN: " + newIsbn);
		}

		return result;
	}

	// delete book to user

	@Override
	public DeleteBookResult deleteBookFromUser(DeleteBook deleteBook) {

		DeleteBookResult result = new DeleteBookResult();

		String userId = deleteBook.getUserId();
		String isbn = deleteBook.getIsbn();

		Book deleteUserBook = bookRepository.findByUserIdAndIsbn(userId, isbn);

		if (deleteUserBook != null) {
			// If the book exists, delete it
			bookRepository.delete(deleteUserBook);

			result.setUserId(userId);
			result.setIsbn(isbn);
			result.setMessage("Book Deleted successfully");
		} else {
			// Handle the case when the book is not found
			result.setUserId(userId);
			result.setIsbn(isbn);
			result.setMessage("Book not found for userId ");
		}

		return result;
	}

	// delete all the books to user

	@Override
	public DeleteUsersBooks deleteUsersBooks(String userId) {
		List<Book> userBooksToDelete = bookRepository.findByUserId(userId);
		DeleteUsersBooks response = new DeleteUsersBooks();

		if (!userBooksToDelete.isEmpty()) {
			bookRepository.deleteAll(userBooksToDelete);
			response.setUserId(userId);
			response.setMessage("Books deleted successfully");
		} else {
			response.setUserId(userId);
			response.setMessage("No books found");
		}

		return response;
	}

}
