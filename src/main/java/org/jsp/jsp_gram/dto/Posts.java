package org.jsp.jsp_gram.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String imageUrl;
    private String caption;
    @UpdateTimestamp
    private LocalDateTime  postedTime;
    @ManyToOne
   private User user;
   @Transient
	private MultipartFile image;

    	@ManyToMany(fetch = FetchType.EAGER)
	List<User> likedUsers = new ArrayList<User>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	List<Comment> comments = new ArrayList<Comment>();
	
	public boolean hasLiked(int id) {
		for (User likedUser : likedUsers) {
			if (likedUser.getId() == id) {
				return true;
			}
		}
		return false;
	}
}
