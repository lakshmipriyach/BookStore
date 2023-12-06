package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entity.Login;
import com.bookstore.entity.Register;
import com.bookstore.entity.Token;
import com.bookstore.entity.User;
import com.bookstore.payload.CaptchaSettings;
import com.bookstore.payload.ForgotPassword;
import com.bookstore.payload.ForgotpasswordRequest;
import com.bookstore.service.AccountService;
import com.bookstore.utils.CaptchaGenerator;

import cn.apiclub.captcha.Captcha;

@RestController
@RequestMapping("/Account")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
public class AccountController {

	@Autowired
	private AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	// Register the User
	@PostMapping("/User")
	public ResponseEntity<User> register(@RequestBody Register register) {
		User result = accountService.register(register);
		return new ResponseEntity<>(result, HttpStatus.CREATED);

	}

	/*
	 * // Token generation
	 * 
	 * @PostMapping("/GenerateToken") public ResponseEntity<Token>
	 * generateToken(@RequestBody Login login) { Token result =
	 * accountService.generateToken(login); return new ResponseEntity<>(result,
	 * HttpStatus.OK); }
	 */

	// Authorized or not
	@PostMapping("/Authorized")
	public ResponseEntity<Token> authorizedUser(@RequestBody Login login) {
		Token result = accountService.authorizedUser(login);
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	// Delete User Details
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Users with 'USER' or 'ADMIN' roles can access this method
	@DeleteMapping("/User/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable String userId) {
		String result = accountService.deleteUser(userId);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	// Get User details
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Users with 'USER' or 'ADMIN' roles can access this method
	@GetMapping("/User/{userId}")
	public ResponseEntity<User> getUserDetails(@PathVariable String userId) {
		User result = accountService.getUserDetails(userId);
		return new ResponseEntity<User>(result, HttpStatus.OK);
	}

	// Logout
	@PostMapping("/logout/{userId}")
	public ResponseEntity<String> logout(@PathVariable String userId) {
		String result = accountService.logout(userId);
		return new ResponseEntity<String>(result, HttpStatus.OK);

	}

	// Forgot password
	@PostMapping("/forgotpassword")
	public ResponseEntity<ForgotpasswordRequest> forgotPassword(@RequestBody ForgotPassword forgotPassword) {
		ForgotpasswordRequest response = accountService.forgotPassword(forgotPassword);
		return ResponseEntity.ok(response);
	}
	
	//Captch Api's 	
		@GetMapping("/verify")
		  public String register(Model model) {
			  model.addAttribute("captcha", genCaptcha());
			  return "verifyCaptcha";
		  }
		@PostMapping("/verify")
		  public String verify(@ModelAttribute CaptchaSettings captchaSettings, Model model) {
			  if(captchaSettings.getCaptcha().equals(captchaSettings.getHiddenCaptcha())){
				  model.addAttribute("message","Captcha verified successfully");
				  return "success";
			  }
			  else {
				  model.addAttribute("message","Invalid Captcha");
				  model.addAttribute("captcha",genCaptcha());
			  }
			  return "verifyCaptcha";
		  }
		//captcha implementation
		private CaptchaSettings genCaptcha() {
			  CaptchaSettings captchaSettings = new CaptchaSettings();
			  Captcha captcha = CaptchaGenerator.generateCaptcha(260, 80);
			  captchaSettings.setHiddenCaptcha(captcha.getAnswer());
			  captchaSettings.setCaptcha("");
			  captchaSettings.setRealCaptcha(CaptchaGenerator.encodeCaptchatoBinary(captcha));
			  return captchaSettings;
		  }
}
