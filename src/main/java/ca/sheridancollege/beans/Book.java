package ca.sheridancollege.beans;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Book {

	private Long id;
	private String title;
	private String Author;
	private double aveStars = 0.0;
	private String img;
	private String description;
}
