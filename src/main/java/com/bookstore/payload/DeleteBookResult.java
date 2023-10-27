package com.bookstore.payload;

import lombok.Data;

@Data
public class DeleteBookResult {

	private String userId;
	private String isbn;
	private String message;

}
