package ca.sheridancollege.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.Book;
import ca.sheridancollege.beans.Review;
import ca.sheridancollege.database.DatabaseAccess;

@Controller
public class HomeController {

	@Autowired
	DatabaseAccess da;

	@Autowired
	private JdbcUserDetailsManager jdbcUserDetailsManager;

	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
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
			@RequestParam String[] authorities, Model model) {
		try {
			List<GrantedAuthority> authorityList = new ArrayList<>();

			for (String authority : authorities) {
				authorityList.add(new SimpleGrantedAuthority(authority));
			}
			String encodedPassword = passwordEncoder.encode(password);
			User user = new User(userName, encodedPassword, authorityList);
			da.addUser(user);
			model.addAttribute("message", "User successfully added");

		} catch (Exception e) {
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
	public String addBook(@RequestParam String title, @RequestParam String author, Model model) {

		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		da.addBook(book);
		return "redirect:/";
	}
	/**
	 * This method maps to the home template page from the "/" root
	 * @param auth the authorities of the current user
	 * @param model where to store the userName, roles, and books
	 * @return the name of the home template
	 */
	@GetMapping("/")
	public String goHome(Authentication auth, Model model) {
		if (auth != null) {
			String userName = auth.getName();
			List<String> roles = new ArrayList<>();
			for (GrantedAuthority authority : auth.getAuthorities()) {
				roles.add(authority.getAuthority());
			}
			model.addAttribute("userName", userName);
			model.addAttribute("roles", roles);
		}
		da.aveReviews();
		List<Book> books = da.getBooks();
		model.addAttribute("books", books);
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
	public String submitReview(@RequestParam long id, @RequestParam String review, @RequestParam double stars) {
		da.addReview(id, review, stars);
		return "redirect:/viewBook/" + id;
	}
	/**
	 * This method maps to the add-review template and
	 * adds the book to add a review for to the model
	 * @param bookID the id of the book to add a review for
	 * @param model where to store the book
	 * @return the path to access the add-review template
	 */
	@GetMapping("user/addReview/{bookID}")
	public String AddReview(@PathVariable long bookID, Model model) {
		model.addAttribute("book", da.getBook(bookID));
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
		if (auth != null) {
			model.addAttribute("userName", auth.getName());
		}
		List<Review> reviews = da.getReviews(bookID);
		model.addAttribute("reviews", reviews);
		Book book = da.getBook(bookID);
		model.addAttribute("book", book);
		return "view-book";
	}
	/**
	 * This method maps the /addPage get request to the
	 * add-book template 
	 * @return the path to the add-book template
	 */
	@GetMapping("admin/addPage")
	public String managerIndex() {
		return "admin/add-book";
	}
	/**
	 * This method maps the /login get request to the
	 * login template
	 * @return the name of the login template
	 */
	@GetMapping("login")
	public String login() {
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
		model.addAttribute("userAuthority", authorities.get(1));
		return "register";
	}
	/**
	 * This methods maps the permission-denied request to
	 * the /error/permission-denied template
	 * @return the path to the permission-denied template
	 */
	@GetMapping("permission-denied")
	public String error() {
		return "error/permission-denied";
	}

}
