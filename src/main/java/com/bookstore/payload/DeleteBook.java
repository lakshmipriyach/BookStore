package com.bookstore.payload;

import lombok.Data;

@Data
public class DeleteBook {

	private String isbn;
	private String userId;

}
