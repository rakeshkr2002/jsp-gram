package org.jsp.jsp_gram.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 3, max =10,message = "it should be between 3 and 10 characters")
    private String firstName;
    @Size(min = 1, max =15,message = "it should be between 1 and 15 characters")
    private String lastName;
    @Size(min = 5, max =15,message = "it should be between 5 and 15 characters")
    private String username;
    @Email(message = "Enter proper email address")
    @NotEmpty(message = "It is reqiured field")
    private String email;
    @DecimalMin(value = "6666666666",message = "It should be proper mobile number")
    @DecimalMax(value="9999999999",message = "It should be proper mobile number")
    private long mobile;
    @Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",message = "It shoud be min 8 and one uppercase one lowercase one expression ")
    private String password;
    @Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "It shoud be min 8 and one uppercase one lowercase one expression  ")
    @Transient
    private String confirmPassword;
    @NotNull(message = "It is required field")
    private String gender;
	private int otp;
	private boolean verified;
}
