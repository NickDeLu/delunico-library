package ca.sheridancollege.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Review {
	
	private long id;
	private long bookID;
	private String Text;
}
