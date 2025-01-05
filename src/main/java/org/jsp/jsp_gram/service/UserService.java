package org.jsp.jsp_gram.service;

import java.util.Random;

import org.jsp.jsp_gram.dto.User;
import org.jsp.jsp_gram.helper.AES;
import org.jsp.jsp_gram.helper.EmailSender;
import org.jsp.jsp_gram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;


@Service
public class UserService {

	@Autowired 
	EmailSender emailSender;
	@Autowired
	UserRepository repository;

	public String loadRegister(ModelMap map, User user) {
		map.put("user", user);
		return "register.html";
	}

	public String register(User user, BindingResult result) throws Exception {
		if (!user.getPassword().equals(user.getConfirmPassword()))
			result.rejectValue("confirmpassword", "error.confirmpassword", "Passwords not Matching");

		if (repository.existsByEmail(user.getEmail()))
			result.rejectValue("email", "error.email", "Email already Exists");

		if (repository.existsByMobile(user.getMobile()))
			result.rejectValue("mobile", "error.mobile", "Mobile Number Already Exists");

		if (repository.existsByUsername(user.getUsername()))
			result.rejectValue("username", "error.username", "Username already Taken");

		if (result.hasErrors())
			return "register.html";
		else {
			user.setPassword(AES.encrypt(user.getPassword()));
			int otp = new Random().nextInt(100000, 1000000);
			user.setOtp(otp);
			System.err.println(otp);
			emailSender.sendOtp(user.getEmail(), otp, user.getFirstName());
			repository.save(user);
			return "redirect:/otp/" + user.getId();
		}
	}

	public String verifyOtp(int otp, int id,HttpSession session) {
		User user = repository.findById(id).get();
		if (user.getOtp() == otp) {
			user.setVerified(true);
			user.setOtp(0);
			session.setAttribute("pass", "Account Created Success");
			repository.save(user);
			return "redirect:/login";
		} else {
			session.setAttribute("fail", "Invalid Otp, Try Again!!!");
			return "redirect:/otp/" + id;
		}

	}

	public String resendOtp(int id, HttpSession session) {
	User user = repository.findById(id).get();
	int otp = new Random().nextInt(100000,1000000);
	user.setOtp(otp);
	System.err.println(otp);
	emailSender.sendOtp(user.getEmail(), otp, user.getFirstName());
    repository.save(user);
	session.setAttribute("pass", "otp Resent- successfully sent");
		return "redirect:/otp/" + user.getId();
	}
	
}