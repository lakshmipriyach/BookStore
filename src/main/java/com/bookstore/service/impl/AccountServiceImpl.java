package com.bookstore.service.impl;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Login;
import com.bookstore.entity.Register;
import com.bookstore.entity.Role;
import com.bookstore.entity.Token;
import com.bookstore.entity.User;
import com.bookstore.exception.BookStoreAPIException;
import com.bookstore.payload.ForgotPassword;
import com.bookstore.payload.ForgotpasswordRequest;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.LoginRepository;
import com.bookstore.repository.RegisterRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.TokenRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.JwtTokenProvider;
import com.bookstore.service.AccountService;


@Service
public class AccountServiceImpl implements AccountService {

	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	private RegisterRepository registerRepository;
	private LoginRepository loginRepository;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private TokenRepository tokenRepository;
	private PasswordEncoder passwordEncoder;
	private BookRepository bookRepository;
	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;

	public AccountServiceImpl(RegisterRepository registerRepository, LoginRepository loginRepository,
			UserRepository userRepository, RoleRepository roleRepository, TokenRepository tokenRepository,
			PasswordEncoder passwordEncoder, BookRepository bookRepository, AuthenticationManager authenticationManager,
			JwtTokenProvider jwtTokenProvider) {
		this.registerRepository = registerRepository;
		this.loginRepository = loginRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.tokenRepository = tokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.bookRepository = bookRepository;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// For User Registration

	@Override
	public User register(Register register) {
		// Check if a user with the same username already exists
		if (registerRepository.existsByUserName(register.getUserName())) {
			throw new BookStoreAPIException(HttpStatus.BAD_REQUEST, "User exists!");
		}

		// Validate if passwords match
		if (!register.isPasswordConfirmed()) {
			throw new BookStoreAPIException(HttpStatus.BAD_REQUEST, "Password and Confirm Password do not match");
		}

		// Validate password length
		if (register.getPassword().length() < 8) {
			throw new BookStoreAPIException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long");
		}

		// Generate a random 5-digit alphanumeric userId
		String randomUserId = generateRandomUserId();

		// Create a new CreateUserResult entity and set the generated userId
		User user = new User();
		user.setUserId(randomUserId);
		user.setUserName(register.getUserName());

		// Set the userId in the Register entity
		register.setUserId(randomUserId);

		// Save the new user registration data in user_registration table
		register = registerRepository.save(register);

		if (register.isPasswordConfirmed()) {
			// Create a new Login entity only if passwords match
			Login login = new Login();
			login.setUserName(register.getUserName());
			login.setPassword(passwordEncoder.encode(register.getPassword()));
			login.setUserId(randomUserId);

			// Set the user's role (e.g., "ROLE_USER")
			Set<Role> roles = new HashSet<>();
			Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(
					() -> new BookStoreAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "ROLE_USER role not found."));
			roles.add(userRole);
			login.setRoles(roles);

			// Save the new Login entity only if passwords match
			login = loginRepository.save(login);

			// Set the Login entity in the Register entity
			register.setLogin(login);
		}

		// Save the CreateUserResult entity in create_user_result table
		if (register.isPasswordConfirmed() && register.getPassword().length() >= 8) {
			user = userRepository.save(user);
		}

		return user;
	}

	// Custom method to generate a random 5-digit alphanumeric userId
	private String generateRandomUserId() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder userIdBuilder = new StringBuilder(5);

		for (int i = 0; i < 5; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			userIdBuilder.append(randomChar);

		}
		return userIdBuilder.toString();
	}

	// To generate token for the user

	/*
	 * public Token generateToken(Login login) { // Check if a user with the same
	 * username exists Login user =
	 * loginRepository.findByUserName(login.getUserName()); if (user == null) {
	 * throw new BookStoreAPIException(HttpStatus.BAD_REQUEST,
	 * "User does not exist!"); }
	 * 
	 * Authentication authentication = authenticationManager .authenticate(new
	 * UsernamePasswordAuthenticationToken(login.getUserName(),
	 * login.getPassword()));
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(authentication);
	 * 
	 * String token = jwtTokenProvider.generateToken(authentication);
	 * 
	 * // Create a Token object with the token, expiration, status, and result Token
	 * tokenResponse = new Token(); tokenResponse.setToken(token);
	 * 
	 * // Set the expiration time in the desired format long expirationTimeMillis =
	 * System.currentTimeMillis() + jwtExpirationDate; SimpleDateFormat dateFormat =
	 * new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	 * dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); String expirationTime =
	 * dateFormat.format(new Date(expirationTimeMillis));
	 * tokenResponse.setExpires(expirationTime);
	 * 
	 * tokenResponse.setStatus("Success");
	 * tokenResponse.setResult("User authorized successfully");
	 * 
	 * return tokenResponse; }
	 */

	// Is User authorized or not

	@Override
	public Token authorizedUser(Login login) {

		Login user = loginRepository.findByUserName(login.getUserName());
		if (user == null) {
			throw new BookStoreAPIException(HttpStatus.BAD_REQUEST, "User does not exist!");

		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getUserName(), login.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);

		// Create a Token object with the token, expiration, status, and result
		Token tokenResponse = new Token();
		tokenResponse.setToken(token);

		// Set the expiration time in the desired format
		long expirationTimeMillis = System.currentTimeMillis() + jwtExpirationDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String expirationTime = dateFormat.format(new Date(expirationTimeMillis));
		tokenResponse.setExpires(expirationTime);

		tokenResponse.setUserId(user.getUserId());
		tokenResponse.setUserName(user.getUserName()); // this is not coming in output
		tokenResponse.setStatus("Success");
		tokenResponse.setResult("User authorized successfully");

		return tokenResponse;
	}

	// Delete the User details

	@Override
	public String deleteUser(String userId) {
		// Check if a user with the provided userId exists in the user_login table
		User user = userRepository.findByUserId(userId);
		Login userLogin = loginRepository.findByUserId(userId);
		Register userRegistration = registerRepository.findByUserId(userId);

		if (user != null) {
			// If the user exists, remove their roles, then delete them
			userLogin.getRoles().clear(); // Remove roles from the user
			loginRepository.save(userLogin); // Save the user without roles

			// Delete the user, login, and registration
			userRepository.delete(user);
			loginRepository.delete(userLogin);
			registerRepository.delete(userRegistration);

			return "User Deleted Successfully";
		} else {
			throw new BookStoreAPIException(HttpStatus.NOT_FOUND, "User not found");
		}
	}

	// Get the User Details

	@Override
	public User getUserDetails(String userId) {
		// Find the corresponding CreateUserResult
		User user = userRepository.findByUserId(userId);

		if (user == null) {
			throw new BookStoreAPIException(HttpStatus.NOT_FOUND, "User not found");
		}

		user.getUserId();
		user.getUserName();

		// Fetch and populate the user's books
		List<Book> userBooks = bookRepository.findByUserId(userId);

		// Create a new list to store the books for GetUserResult
		List<Book> getUserBooks = new ArrayList<>();

		for (Book book : userBooks) {
			Book userBook = new Book();
			userBook.setUserId(user.getUserId());
			userBook.setIsbn(book.getIsbn());
			userBook.setTitle(book.getTitle());
			userBook.setSubTitle(book.getSubTitle());
			userBook.setAuthor(book.getAuthor());
			userBook.setPublishDate(book.getPublishDate());
			userBook.setPublisher(book.getPublisher());
			userBook.setPages(book.getPages());
			userBook.setDescription(book.getDescription());
			userBook.setWebsite(book.getWebsite());

			getUserBooks.add(userBook);
		}

		// Set the books for GetUserResult
		user.setBooks(getUserBooks);

		return user;
	}

	// Logout

	@Override
	public String logout(String userId) {
		User user = userRepository.findByUserId(userId);

		if (user == null) {
			throw new BookStoreAPIException(HttpStatus.NOT_FOUND, "User not found");
		}

		// Clear the security context
		SecurityContextHolder.clearContext();

		return "Logout Successful";
	}

	// Forgot Password

	@Override
	public ForgotpasswordRequest forgotPassword(ForgotPassword forgotPassword) {
		
		String userName = forgotPassword.getUserName();
	    String newPassword = forgotPassword.getNewPassword();
	    String confirmPassword = forgotPassword.getConfirmPassword();
	    
	    // Find the user by username
	    Login login = loginRepository.findByUserName(userName);
	    
	    if (login == null) {
	        throw new BookStoreAPIException(HttpStatus.NOT_FOUND, "User not found");
	    }
	    
	    if (!newPassword.equals(confirmPassword)) {
	        throw new BookStoreAPIException(HttpStatus.BAD_REQUEST, "Password and Confirm Password do not match");
	    }
 
	    User user = userRepository.findByUserName(userName);
 
	    if (user == null) {
	        throw new BookStoreAPIException(HttpStatus.NOT_FOUND, "User not found");
	    }
 
	    if (newPassword.length() < 8) {
	        throw new BookStoreAPIException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long");
	    }

	    // Update the user's password
	    login.setPassword(passwordEncoder.encode(newPassword));  
	    // Save the updated user entity
	    loginRepository.save(login);
	    
	    ForgotpasswordRequest forgotPasswordRequest = new ForgotpasswordRequest();
	    forgotPasswordRequest.setUserId(login.getUserId());
	    forgotPasswordRequest.setMessage("Password reset successful");
 
	    return forgotPasswordRequest;
	}

}
