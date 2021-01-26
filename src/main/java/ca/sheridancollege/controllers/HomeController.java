package ca.sheridancollege.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.Book;
import ca.sheridancollege.beans.Review;
import ca.sheridancollege.beans.User;
import ca.sheridancollege.database.DatabaseAccess;

@Controller
public class HomeController {

	@Autowired
	DatabaseAccess da;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private JdbcUserDetailsManager jdbcUserDetailsManager;

	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
	
	@PostMapping("recover")
	public String recoverAccount(Model model,@RequestParam String email) {
		String newPassword = generatePassword();
		String encodedPassword = passwordEncoder.encode(newPassword);
		String message = "Your new password is "+ newPassword;
		if(da.recoverPassword(email,encodedPassword)) {
			sendEmail(email,"ELibrary Password Recovery", message);
			model.addAttribute("message","A password recovery email has been successfully sent and should arrive momentarily");
		}else {
			model.addAttribute("message","Email does not exist");
			return "recover-account";
		}
		model.addAttribute("allbooks",da.allBooks());
		return "login";
	}
	@GetMapping("user/myBooks")
	public String goMyBooks(Authentication auth, Model model) {
		auth = resetAuthentication(auth);
		if(auth != null) {
			String userName = auth.getName();
			List<Book> books = da.getMyBooks(da.getUser(userName).getId());
			model.addAttribute("books",books);
			model.addAttribute("username",userName);
		}
		model.addAttribute("allbooks",da.allBooks());
		return "user/myBooks";
	}
		
	@GetMapping("addMyBooks/{bookId}")
	public String addMyBooks(Model model, @PathVariable Long bookId,Authentication auth) {
		if(auth != null) {
			String userName = auth.getName();
			da.addMyBook(bookId,da.getUser(userName).getId());
			model.addAttribute("username",userName);
			return "redirect:/viewBook/" + bookId;
		}
		return "redirect:/";
	}
	@GetMapping("deleteMyBooks/{bookId}")
	public String deleteMyBooks(Model model, @PathVariable Long bookId,Authentication auth) {
		if(auth != null) {
			String userName = auth.getName();
			da.deleteMyBook(bookId,da.getUser(userName).getId());
			model.addAttribute("username",userName);
			return "redirect:/viewBook/" + bookId;
		}
		return "redirect:/";
	}
	public String generatePassword() {
		String chars ="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder string = new StringBuilder();
		Random rnd = new Random();
		while(string.length() < 10){
			int index = (int)(rnd.nextFloat() * chars.length());
			string.append(chars.charAt(index));
		}
		String password = string.toString();
		return password;
	}
	public void sendEmail(String recipient, String subject, String text) {
		String from = "nickdelucrative@gmail.com";
		String to = recipient;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		 
		mailSender.send(message);
	}
	
	/**
	 * This method adds new users to the database
	 * if the username does not already exist
	 * @param userName the new username to add arriving as a post request
	 * @param password the new username to add arriving as a post request
	 * @param authorities the selected authority approved for registering a new user
	 * @param model where to store a message, authorities, and books
	 * @return the name of the template for the home page
	 */
	@PostMapping("addUser")
	public String addUser(@RequestParam String userName, @RequestParam String password,
			@RequestParam String[] authorities, @RequestParam String email,Model model) {
		model.addAttribute("allbooks",da.allBooks());
		try {
			List<GrantedAuthority> authorityList = new ArrayList<>();

			for (String authority : authorities) {
				authorityList.add(new SimpleGrantedAuthority(authority));
			}
			String encodedPassword = passwordEncoder.encode(password);
			User user = new User(userName, encodedPassword, email, authorityList);
			da.addUser(user);
			model.addAttribute("message", "User successfully added");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			List<String> allAuthorities = da.getAuthorities();
			model.addAttribute("message", "User already exists");
			model.addAttribute("authorities", allAuthorities);
			return "register";
		}
		List<Book> books = da.getBooks();
		model.addAttribute("books", books);
		return "index";
	}
	/**
	 * This method adds a new book to the database and returns to home
	 * @param title the title of the new book
	 * @param author the author of the new book
	 * @param model where to store our list of all books
	 * @return a redirection to root "/" which then leads to home
	 */
	@PostMapping("admin/addBook")
	public String addBook(@ModelAttribute Book book) {
		da.addBook(book);
		return "redirect:/";
	}
	/**
	 * This method maps to the home template page from the "/" root
	 * @param auth the authorities of the current user
	 * @param model where to store the userName, roles, and books
	 * @return the name of the home template
	 */
	public Authentication resetAuthentication(Authentication auth) {
		if(auth!=null) {
			if(da.getUsername(auth.getName()) !=null){
				Authentication newAuth = new UsernamePasswordAuthenticationToken(da.getUsername(auth.getName()), auth.getCredentials(),auth.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
				auth = SecurityContextHolder.getContext().getAuthentication();
			}
		}
		return auth;
	}

	@GetMapping("/")
	public String goHome(Authentication auth,Model model) {
		auth = resetAuthentication(auth);
		if (auth != null) {
			
			List<String> roles = new ArrayList<>();
			for (GrantedAuthority authority : auth.getAuthorities()) {
				roles.add(authority.getAuthority());
			}
			model.addAttribute("username", auth.getName());
			model.addAttribute("roles", roles);
		}
		da.aveReviews();
		List<Book> books = da.getBooks();
		model.addAttribute("books", books);
		model.addAttribute("allbooks",da.allBooks());
		return "index";

	}
	/**
	 * This method adds a review to the database and 
	 * returns to the review page after
	 * @param id the id of the book to add a review for
	 * @param review the text of the review to add
	 * @return a redirection to the previous review page
	 */
	@PostMapping("submitReview")
	public String submitReview(@ModelAttribute Review review, @RequestParam String username, @RequestParam Long bookId) {
		review.setUsername(username);
		review.setBookId(bookId);
		da.addReview(review);
		return "redirect:/viewBook/" + review.getBookId();
	}
	@GetMapping("search")
	public String search(@RequestParam String search, Model model, Authentication auth) {
		List<Book> books = da.searchBooks(search);
		da.aveReviews();
		model.addAttribute("search",search);
		model.addAttribute("books",books);
		if (auth != null) {
			model.addAttribute("username", auth.getName());
		}
		model.addAttribute("allbooks",da.allBooks());
		return "results";
	}
	/**
	 * This method maps to the add-review template and
	 * adds the book to add a review for to the model
	 * @param bookID the id of the book to add a review for
	 * @param model where to store the book
	 * @return the path to access the add-review template
	 */
	@GetMapping("user/addReview/{bookID}")
	public String AddReview(@PathVariable long bookID, Model model, Authentication auth) {
		auth = resetAuthentication(auth);
		model.addAttribute("book", da.getBook(bookID));
		model.addAttribute("review", new Review());
		model.addAttribute("username", auth.getName());
		model.addAttribute("allbooks",da.allBooks());
		return "user/add-review";
	}
	/**
	 * This method maps to the view-book template and
	 * adds the books reviews and the book itself to the model
	 * @param model where to store the userName, book, and reviews
	 * @param bookID the id of the book to add a review for
	 * @param auth a list of all authorities for the current user 
	 * @return the name of the view reviews page template
	 */
	@GetMapping("viewBook/{bookID}")
	public String goToReview(Model model, @PathVariable long bookID, Authentication auth) {
		da.aveReviews();
		List<Review> reviews = da.getReviews(bookID);
		model.addAttribute("reviews", reviews);
		Book book = da.getBook(bookID);
		model.addAttribute("book", book);
		if (auth != null) {
			model.addAttribute("username", auth.getName());
			List<Book> books = da.getMyBooks(da.getUser(auth.getName()).getId());
			if(books.contains(book)) {
				model.addAttribute("favourited","favourited");
			}
		}
		model.addAttribute("allbooks",da.allBooks());
		return "view-book";
	}
	@GetMapping("user/account")
	public String GoAccount(Authentication auth,Model model) {
		auth = resetAuthentication(auth);
		if(auth !=null) {
			String userName = auth.getName();
			model.addAttribute("user",da.getUser(userName));
			model.addAttribute("username",userName);
		}
		model.addAttribute("allbooks",da.allBooks());
		return "user/account";
	}
	@PostMapping("updateUser/{id}")
	public String updateUser(@PathVariable long id, Model model,@RequestParam String username,
			@RequestParam String email, @RequestParam String password,Authentication auth) {
		User user = new User();
		if(password !="")
			user.setPassword(passwordEncoder.encode(password));
		user.setEmail(email);
		user.setUsername(username);
		user.setId(id);
		model.addAttribute("allbooks",da.allBooks());
		try {
			da.updateUser(user);
			
		}catch(Exception e) {
			if(auth!=null)
				model.addAttribute("user",da.getUser(auth.getName()));
			model.addAttribute("message","Username already exists! Try again.");
			return "user/account";
		}
		model.addAttribute("user",da.getUser(username));
		model.addAttribute("message","User details were sucessfully updated");
		
		auth = SecurityContextHolder.getContext().getAuthentication();
		Authentication newAuth = new UsernamePasswordAuthenticationToken(username, auth.getCredentials(),auth.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		return "user/account";
	}
	/**
	 * This method maps the /addPage get request to the
	 * add-book template 
	 * @return the path to the add-book template
	 */
	@GetMapping("admin/addPage")
	public String managerIndex(Model model,Authentication auth) {
		auth = resetAuthentication(auth);
		model.addAttribute("book", new Book());
		model.addAttribute("username",auth.getName());
		model.addAttribute("allbooks",da.allBooks());
		return "admin/add-book";
	}
	/**
	 * This method maps the /login get request to the
	 * login template
	 * @return the name of the login template
	 */
	@GetMapping("login")
	public String login(Model model) {	
		model.addAttribute("allbooks",da.allBooks());
		return "login";
	}
	/**
	 * This method maps the register request to the register template
	 * and adds the approved user authority to the model for new users
	 * @param model where to store the userAuthority
	 * @return the name of the register template
	 */
	@GetMapping("register")
	public String register(Model model) {
		List<String> authorities = da.getAuthorities();
		model.addAttribute("userAuthority", authorities.get(0));
		model.addAttribute("allbooks",da.allBooks());
		return "register";
	}
	/**
	 * This methods maps the permission-denied request to
	 * the /error/permission-denied template
	 * @return the path to the permission-denied template
	 */
	@GetMapping("permission-denied")
	public String error(Model model) {
		model.addAttribute("allbooks",da.allBooks());
		return "error/permission-denied";
	}
	@GetMapping("recover-account")
	public String recover(Model model) {
		model.addAttribute("allbooks",da.allBooks());
		return "recover-account";
	}

}
