package ca.sheridancollege.database;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Book;
import ca.sheridancollege.beans.Review;
import ca.sheridancollege.beans.User;

@Repository
public class DatabaseAccess {

	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	/**
	 * This method gets all the approved authorities from
	 * the authority table in the h2 database
	 * @return returns a list of authorities as strings
	 */
	public List<String> getAuthorities(){
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT DISTINCT authority FROM authorities";
		
		List<String> authorities = jdbc.queryForList(query, params, String.class);
		
		return authorities;
	}
	/**
	 * This method gets all reviews from specified book from 
	 * the review table in the h2 database
	 * @param bookID the id of the specified book
	 * @return a list of reviews for that book 
	 */
	public List<Review> getReviews(long bookID){
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT * FROM reviews WHERE bookId = :bookID ";
		params
		.addValue("bookID", bookID);
		
		BeanPropertyRowMapper<Review> reviewMapper = 
				new BeanPropertyRowMapper<Review>(Review.class);
		
		List<Review> reviews = jdbc.query(query, params, reviewMapper);
		
		return reviews;
	}
	/**
	 * 
	 * @param bookID
	 * @return
	 */
	public void aveReviews(){
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "UPDATE books SET aveStars = "
				+ "(SELECT CAST(AVG(stars) as DECIMAL(10,1)) FROM reviews "
				+ "WHERE reviews.bookId = books.id) "
				+ "WHERE books.id in (SELECT bookId FROM reviews)";
		
		jdbc.update(query, params);
	}
	/**
	 * This method gets all the books from the books table
	 * in the h2 database
	 * @return a list of all books 
	 */
	public List<Book> getBooks(){
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT * FROM books";
		
		BeanPropertyRowMapper<Book> bookMapper = 
				new BeanPropertyRowMapper<Book>(Book.class);
		
		List<Book> books = jdbc.query(query, params, bookMapper);
		return books;
	}
	public List<String> allBooks() {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT * FROM books";
		
		BeanPropertyRowMapper<Book> bookMapper = 
				new BeanPropertyRowMapper<Book>(Book.class);
		
		List<Book> books = jdbc.query(query, params, bookMapper);
		ArrayList<String> allBooks = new ArrayList<String>();
		for (Book book : books) {
			allBooks.add(book.getTitle()); 
		}
		System.out.println(allBooks.toString());
		return allBooks;
	}
	/**
	 * This method adds a given book to the book table
	 * in the h2 database
	 * @param book the given book object to add
	 */
	public void addBook(Book book) {
	
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = 
				"INSERT INTO books (title, author, img, description) VALUES (:title, :author, DEFAULT, :description)";
		
		namedParameters
			.addValue("title", book.getTitle())
			.addValue("author", book.getAuthor())
			.addValue("description", book.getDescription());
			
			if(!(book.getImg().isEmpty())) {
				System.out.println("its not null" + book.getImg());
				namedParameters.addValue("img", book.getImg());
				query = "INSERT INTO books (title, author, img, description) VALUES (:title, :author, :img, :description)";
			}
		jdbc.update(query, namedParameters);
		
	}
	
	/**
	 * This method get a specific book based on a given id
	 * from the book table in the h2 database
	 * @param bookID the id of the book to get
	 * @return the book object that corresponds to the given id
	 */
	public Book getBook(long bookID) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT * FROM books WHERE id = :bookID ";
		params
		.addValue("bookID", bookID);
		
		BeanPropertyRowMapper<Book> bookMapper = 
				new BeanPropertyRowMapper<Book>(Book.class);
		
		List<Book> books = jdbc.query(query, params, bookMapper);
		
		if(books.isEmpty()) {
			return null;
		}else {
			return books.get(0);
		}
	}
	public User getUser(String username) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT * FROM user_table WHERE username = :username";
		params
		.addValue("username", username);
		
		BeanPropertyRowMapper<User> userMapper = 
				new BeanPropertyRowMapper<User>(User.class);
		
		List<User> user = jdbc.query(query, params, userMapper);
		
		if(user.isEmpty()) {
			return null;
		}else {
			return user.get(0);
		}
	}
	public String getUsername(String email) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT username FROM user_table WHERE email = :email";
		params
		.addValue("email", email);
		
		BeanPropertyRowMapper<User> userMapper = 
				new BeanPropertyRowMapper<User>(User.class);
		
		List<User> user = jdbc.query(query, params, userMapper);
		
		if(user.isEmpty()) {
			return null;
		}else {
			return user.get(0).getUsername();
		}
	}
	/**
	 * This method adds a review to the review table in 
	 * the h2 database with a given bookID and review
	 * @param bookID the id of book to add a review for
	 * @param review the text of the review to add
	 */
	public void addReview(Review review) {
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			String query = 
					"INSERT INTO reviews (bookId, text, stars, userId, username) VALUES (:bookId, :review, :stars, :userId, :username)";
			namedParameters
				.addValue("bookId", review.getBookId())
				.addValue("review", review.getText())
				.addValue("stars", review.getStars())
				.addValue("userId", getUser(review.getUsername()).getId())
				.addValue("username", review.getUsername());
				
			jdbc.update(query, namedParameters);
			
	}
	public void addUser(User user) {
		System.out.println("database access");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = 
				"INSERT INTO user_table (username,password,email,enabled) "
				+ "VALUES (:username, :password, :email, :enabled)";
		String query2 =
				"INSERT INTO authorities (userId, username, authority) "
				+ "VALUES (:userId, :username, :authority)";
		
		namedParameters
			.addValue("username", user.getUsername())
			.addValue("password", user.getPassword())
			.addValue("email", user.getEmail())
			.addValue("enabled", true)
			.addValue("authority", "ROLE_USER");

		jdbc.update(query, namedParameters);
		
		namedParameters.addValue("userId", getUser(user.getUsername()).getId());
		jdbc.update(query2, namedParameters);
		
	}
	public List<Book> searchBooks(String search) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params
		.addValue("search", search);
		String query = "SELECT * FROM books WHERE title ilike '%' || (:search) || '%'";
		
		BeanPropertyRowMapper<Book> bookMapper = 
				new BeanPropertyRowMapper<Book>(Book.class);
		
		List<Book> books = jdbc.query(query, params, bookMapper);
		
		return books;
	}
	public boolean recoverPassword(String email,String newPassword) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT email FROM user_table WHERE email = :email";
		String query2 = 
				"UPDATE user_table SET password = :password WHERE email = :email ";
		params
		.addValue("email", email)
		.addValue("password", newPassword);
		
		BeanPropertyRowMapper<User> UserMapper = 
				new BeanPropertyRowMapper<User>(User.class);
		
		List<User> users = jdbc.query(query, params, UserMapper);
		
		if(users.isEmpty()) {
			return false;
		}else {
			jdbc.update(query2, params);
			return true;
		}
		
	}
	public void addMyBook(Long bookId, long userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = 
				"INSERT INTO myBooks (userId,bookId) VALUES (:userId,:bookId)";
		namedParameters
			.addValue("bookId", bookId)
			.addValue("userId", userId);
			
		jdbc.update(query, namedParameters);
		
	}
	public List<Book> getMyBooks(long userId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "SELECT * FROM BOOKS WHERE books.id in (SELECT myBooks.bookid FROM myBooks WHERE userid = :userId)";
		params
		.addValue("userId", userId);
		
		BeanPropertyRowMapper<Book> bookMapper = 
				new BeanPropertyRowMapper<Book>(Book.class);
		
		List<Book> books = jdbc.query(query, params, bookMapper);
		
		return books;	
	}
	public void deleteMyBook(Long bookId, long userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = 
				"DELETE FROM myBooks WHERE userId = :userId AND bookid = :bookId";
		namedParameters
			.addValue("bookId", bookId)
			.addValue("userId", userId);

		jdbc.update(query, namedParameters);
	}
	
	public void updateUser(User user) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = 
				"UPDATE user_table SET username = :username, email = :email WHERE id = :userId ";
		String queryReview = "UPDATE reviews SET username = :username WHERE userId = :userId";

		String queryAuthorities = "UPDATE authorities SET username = :username WHERE userId = :userId ";

		params
		.addValue("userId", user.getId())
		.addValue("username", user.getUsername())
		.addValue("email", user.getEmail());
		System.out.println(user.getId()+"this is the user ID");
		if(user.getPassword() !=null) {
			System.out.println(user.getPassword() + "the password");
			query = 
				"UPDATE user_table SET username = :username, "
				+ "email = :email, password = :password WHERE id = :userId";
			params.addValue("password", user.getPassword());
		}

		jdbc.update(queryAuthorities, params);

		jdbc.update(queryReview, params);
		
		jdbc.update(query, params);
		
	}
}
