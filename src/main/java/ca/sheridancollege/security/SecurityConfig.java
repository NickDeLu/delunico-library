package ca.sheridancollege.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import ca.sheridancollege.database.DatabaseAccess;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoggingAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	DatabaseAccess da;
	
	@Autowired
	private DataSource dataSource;
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
		
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		
		jdbcUserDetailsManager.setDataSource(dataSource);
		
		return jdbcUserDetailsManager;
	}
	
	@Autowired
	@Lazy //gives the controller access to this private data field, shares it.
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/user/**").hasAnyRole("USER","MANAGER")
		.antMatchers("/admin/**").hasRole("MANAGER")
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/","/**").permitAll()
		.and()
		.formLogin().loginPage("/login")
		.defaultSuccessUrl("/")
		.and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.and()
		.exceptionHandling()
		.accessDeniedHandler(accessDeniedHandler);
		
		http.csrf();
		http.headers().frameOptions();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(
                "select username,password, enabled from user_table where username=?")
        .authoritiesByUsernameQuery(
                "select username, authority from authorities where username=?")
		.passwordEncoder(passwordEncoder);//auto injected at the top of page
		
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(
                "select email,password, enabled from user_table where email=?")
        .authoritiesByUsernameQuery(
                "select user_table.username,authority from authorities inner join user_table on user_table.id=authorities.userid where email=?")
		.passwordEncoder(passwordEncoder);//auto injected at the top of page
	}
	
	
}
