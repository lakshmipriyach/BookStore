package com.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Login login; // Reference to the Login entity

	@JsonIgnore
	private String userName;

	@Transient 
	private String userId;
	
	private String token;
	private String expires;
	private String status;
	private String result;
	
	public void setUserIdFromLogin() {
		if (login != null) {
			this.userId = login.getUserId();
		}
}
}