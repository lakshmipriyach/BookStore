package com.bookstore.payload;

import lombok.Data;

@Data
public class DeleteUsersBooks {
	
	private String userId;
	private String message;

}
