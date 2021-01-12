package ca.sheridancollege.beans;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

	private String username;
	private String password;
	private String email;
	private List<GrantedAuthority> authorities;
	private long id;
	public User(String username, String password, String email, List<GrantedAuthority> authorities) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
	}
	
	

}
