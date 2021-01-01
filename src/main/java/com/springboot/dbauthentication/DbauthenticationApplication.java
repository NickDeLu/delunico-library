package com.springboot.dbauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DbauthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbauthenticationApplication.class, args);
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
	    String pwd = bcryptPasswordEncoder.encode("password");
	    System.out.println(pwd);
	}

}
