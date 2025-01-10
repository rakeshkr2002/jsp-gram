package org.jsp.jsp_gram.dto;

import java.time.LocalDateTime;


import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

}
