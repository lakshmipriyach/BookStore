package com.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_registration")
public class Register {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "user_id")
	private String userId;

	@NotNull(message = "UserName is mandatory")
	@Column(unique = true)
	private String userName;

	@NotNull(message = "Password is mandatory")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@AssertTrue(message = "Password and Confirm Password do not match")
    public boolean isPasswordConfirmed() {
        return password != null && confirmPassword != null && password.equals(confirmPassword);
    }

	@OneToOne(mappedBy = "register", cascade = CascadeType.DETACH)
	private Login login;
}


