package com.springboot.dbauthentication;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	
	@GetMapping("/getMessage")
	public String getMessage() {
		
		return "I am a protected resource";
	}
}
