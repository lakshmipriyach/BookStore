package com.bookstore.payload;

import lombok.Data;

@Data
public class DeleteBookResponse {

	private String isbn;
	private String message;

}
