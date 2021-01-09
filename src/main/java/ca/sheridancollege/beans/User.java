package ca.sheridancollege.beans;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {

	private String username;
	private String password;
	private String email;
	private List<GrantedAuthority> authorities;

}
