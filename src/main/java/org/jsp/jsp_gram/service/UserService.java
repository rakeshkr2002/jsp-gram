package org.jsp.jsp_gram.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsp.jsp_gram.dto.Posts;
import org.jsp.jsp_gram.dto.User;
import org.jsp.jsp_gram.helper.AES;
import org.jsp.jsp_gram.helper.CloudinaryHelper;
import org.jsp.jsp_gram.helper.EmailSender;
import org.jsp.jsp_gram.repository.UserRepository;
import org.jsp.jsp_gram.repository.postRepository;
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
	 postRepository postRepository;

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

    public String loadProfile(HttpSession session,ModelMap map) {
		User user = (User) session.getAttribute("user");
		if (user != null){
		List<Posts> posts = postRepository.findByUser(user);
		map.addAttribute("posts", posts);
			return "profile.html";
		}
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


	public String addPost(HttpSession session, MultipartFile file, String caption) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			try {
				Posts post = new Posts();
				post.setCaption(caption);
				post.setImageUrl(cloudinaryHelper.saveImage(file));
				post.setUser(user);
				postRepository.save(post);
				return "redirect:/profile";
			} catch (Exception e) {
				// Log the exception and set an error message
				session.setAttribute("error", "Failed to create post. Please try again.");
				e.printStackTrace();
				return "redirect:/newPost";
			}
		} else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
		}
	}
	



	public String loadPost(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "newPost";
        } else {
            session.setAttribute("fail", "Invalid Session");
            return "redirect:/login";
        }
    }

    public String deletePost(int id, HttpSession session) {
      User user = (User) session.getAttribute("user");
	  if (user != null) {
		postRepository.deleteById(id);
		session.setAttribute("pass", "Post Deleted Successfully");
		return "redirect:/profile";
	  }else{
		session.setAttribute("fail", "Invalid Session");
		return "redirect:/login";
	  }
    }

    public String followUser(int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
		if (user != null) {
			User folllowedUser = repository.findById(id).get();
			user.getFollowing().add(folllowedUser);
			folllowedUser.getFollowers().add(user);
			repository.save(user);
			repository.save(folllowedUser);
			return "redirect:/profile";
		} else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
		}
    }

    public String viewSuggestions(HttpSession session, ModelMap map) {
        User user = (User) session.getAttribute("user");
       if (user != null) {
			List<User> suggestions = repository.findByVerifiedTrue();
			List<User> usersToRemove = new ArrayList<User>();
			for (User suggestion : suggestions) {
				if (suggestion.getId() == user.getId()) {
					usersToRemove.add(suggestion);
				}
				for (User followingUser : user.getFollowing()) {
					if (followingUser.getId() == suggestion.getId()) {
						usersToRemove.add(suggestion);
					}
				}
			}
			suggestions.removeAll(usersToRemove);
			if (suggestions.isEmpty()) {
				session.setAttribute("fail", "No Suggestions");
				return "redirect:/profile";
			} else {
				map.put("suggestions", suggestions);
				return "suggestions.html";
			}
		} else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
		}
    }

	public String editPost(int id, HttpSession session, ModelMap map) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			Posts post = postRepository.findById(id).get();
			map.put("post", post);
			return "edit-post.html";
		} else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
		}
	}

	public String updatePost(Posts post, HttpSession session) throws IOException {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			if(!post.getImage().isEmpty())
			post.setImageUrl(cloudinaryHelper.saveImage(post.getImage()));
			else
				post.setImageUrl(postRepository.findById(post.getId()).get().getImageUrl());
			post.setUser(user);
			postRepository.save(post);
			session.setAttribute("pass", "Updated Success");
			return "redirect:/profile";
		} else {
			session.setAttribute("fail", "Invalid Session");
			return "redirect:/login";
		}
	}
}