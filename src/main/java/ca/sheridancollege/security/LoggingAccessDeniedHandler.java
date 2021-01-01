package ca.sheridancollege.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class LoggingAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			String format = "%s was trying to access %s \n";
			System.out.printf(format, auth.getName(),request.getRequestURI());
		}
		
		response.sendRedirect("/permission-denied");
	}

}
