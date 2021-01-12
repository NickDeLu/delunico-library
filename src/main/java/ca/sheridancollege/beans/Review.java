package ca.sheridancollege.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Review {
	
	private long id;
	private long bookId;
	private String Text;
	private long userId;
	private String username;
	private float stars;
}
