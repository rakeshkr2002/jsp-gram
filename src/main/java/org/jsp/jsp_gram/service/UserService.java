package org.jsp.jsp_gram.service;

import java.util.Random;

import org.jsp.jsp_gram.dto.User;
import org.jsp.jsp_gram.helper.AES;
import org.jsp.jsp_gram.helper.CloudinaryHelper;
import org.jsp.jsp_gram.helper.EmailSender;
import org.jsp.jsp_gram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;


@Service
public class UserService {

	@Autowired 
	EmailSender emailSender;
	@Autowired
	UserRepository repository;

	@Autowired
	CloudinaryHelper cloudinaryHelper;
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

	//login
	public String login(String username, String password,HttpSession session) {
		User user=repository.findByUsername(username);
		if (user != null) {
	      if(password.equals(AES.decrypt(user.getPassword()))){
              if (user.isVerified()) {
				session.setAttribute("user", user);
					session.setAttribute("pass", "Login Success");
					return "redirect:/home";
			  }else{
				int otp = new Random().nextInt(100000,1000000);
				user.setOtp(otp);
				System.err.println(otp);
				emailSender.sendOtp(user.getEmail(), otp, user.getFirstName());
   				 repository.save(user);
				session.setAttribute("pass", "otp sent- successfully sent,first verify email to login");
				  return "redirect:/otp/"+user.getId();
			  }
		  }else{
			session.setAttribute("fail", "Invalid Password ");
			  return "redirect:/login";
		  }
		}else{
			session.setAttribute("fail", "Invalid Username ");
			return "redirect:/login";
		}
	}
	


	public String loadHome(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null)
			return "home.html";
		else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
			
		}
	}
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		session.setAttribute("pass", "Logout Success");
		return "redirect:/login";
	}

    public String loadProfile(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null)
			return "profile.html";
		else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
			
		}
	    }

	public String editProfile(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null)
			return "edit-profile.html";
		else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
			
		}
	}

	public String updateProfile(HttpSession session, MultipartFile image, String bio) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			user.setBio(bio);
			user.setImageUrl(cloudinaryHelper.saveImage(image));
			repository.save(user);
			return "redirect:/profile";
		} else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
		}
	
	}
}