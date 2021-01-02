package ca.sheridancollege.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoggingAccessDeniedHandler accessDeniedHandler;
	
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
		
		http.csrf().disable(); //disabling this is not industry standard, allows u to see the console
		http.headers().frameOptions().disable();
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
		System.out.println(passwordEncoder.encode("bunny"));
		System.out.println(passwordEncoder.encode("duck"));
	}
}
