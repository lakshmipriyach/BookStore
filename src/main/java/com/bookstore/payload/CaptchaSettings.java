package com.bookstore.payload;

import lombok.Data;

@Data
public class CaptchaSettings {
	private String captcha;
	private String hiddenCaptcha;
	private String realCaptcha;
	public String getCaptcha() {
		return captcha;
	}	

}
