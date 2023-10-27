package com.bookstore.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "user_id") // This links to the user ID
	private String userId;

	@Column(nullable = false)
	private String isbn;

	@Column(nullable = false)
	private String title;

	private String subTitle;

	@Column(nullable = false)
	private String author;

	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime publishDate;

	private String publisher;
	private int pages;
	private String description;
	private String website;

	@ManyToOne
	@JoinColumn(name = "user_result_id") // Specify the name of the foreign key column
	@JsonIgnore
	private User user;

	@ManyToOne // Establish a many-to-one relationship with Books
	@JoinColumn(name = "books_id") // Specify the name of the foreign key column
	@JsonIgnore
	private Books books;

}
