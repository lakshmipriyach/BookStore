package com.bookstore.payload;

import java.util.List;

import lombok.Data;

@Data
public class AddListOfBooks {

	private String userId;
	private List<CollectionOfIsbn> collectionOfIsbns;

}
