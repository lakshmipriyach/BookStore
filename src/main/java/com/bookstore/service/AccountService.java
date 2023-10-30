package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.entity.Login;
import com.bookstore.entity.Register;
import com.bookstore.entity.Token;

public interface AccountService {

	// For User Registration
	User register(Register register);

	// To generate token for the user
	Token generateToken(Login login);

	// Is User authorized or not
	String authorizedUser(Login login);

	// Delete the User details
	String deleteUser(String userId);

	// Get the User Details
	User getUserDetails(String userId);

}