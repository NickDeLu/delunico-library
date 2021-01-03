package ca.sheridancollege.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Book;
import ca.sheridancollege.beans.Review;

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
				+ "(SELECT AVG(stars) FROM reviews "
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
	/**
	 * This method adds a given book to the book table
	 * in the h2 database
	 * @param book the given book object to add
	 */
	public void addBook(Book book) {
	
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = 
				"INSERT INTO books (title, author) VALUES (:title, :author)";
		
		namedParameters
			.addValue("title", book.getTitle())
			.addValue("author", book.getAuthor());

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
	/**
	 * This method adds a review to the review table in 
	 * the h2 database with a given bookID and review
	 * @param bookID the id of book to add a review for
	 * @param review the text of the review to add
	 */
	public void addReview(Long bookID, String review, double stars) {
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			String query = 
					"INSERT INTO reviews (bookId, text, stars) VALUES (:bookID, :review, :stars)";
			
			namedParameters
				.addValue("bookID", bookID)
				.addValue("review", review)
				.addValue("stars", stars);

			jdbc.update(query, namedParameters);
			
	}
	public void addUser(User user) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = 
				"INSERT INTO user_table (username,password,enabled) "
				+ "VALUES (:username, :password, :enabled)"
				+ "INSERT INTO authorities(username, authority) "
				+ "VALUES(:username, :authority)";
		
		namedParameters
			.addValue("username", user.getUsername())
			.addValue("password", user.getPassword())
			.addValue("enabled", 1)
			.addValue("authority", user.getAuthorities());
		

		jdbc.update(query, namedParameters);
		
	}
		
	
	
}
