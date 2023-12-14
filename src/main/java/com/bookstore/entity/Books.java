package com.bookstore.entity;

import java.time.LocalDate;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Books {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "ISBN is required")
	@Column(nullable = false, unique = true)
    @Pattern(regexp = "\\d{12}", message = "ISBN should be a 12-digit number")
	private String isbn;

	@NotNull(message = "Title is required")
	@Column(nullable = false,unique = true)
	private String title;

	@NotNull(message = "Subtitle is required")
	@Column(nullable = false)
	private String subTitle;

	@NotNull(message = "Author is required")
	@Column(nullable = false)
	private String author;

	@NotNull(message = "Publishdate is required")
	@Column(nullable = false)
	@JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate publishDate;

	@Column(nullable = false)
	private String publisher;
	
	@Column(nullable = false)
	@NotNull(message = "Pages cannot be blank")
	@Min(value = 1, message = "Pages should be greater than 0")
	private Integer pages;
	
	@NotNull(message = "Description is required")
	@Column(nullable = false)
	private String description;
	
	@NotNull(message = "Website is required")
	@Column(nullable = false)
	@URL
	private String website;

	@Transient // Exclude this field from the database table
	@JsonIgnore
	private String userId;
	
	@Transient
	private String message;

}
