package org.jsp.jsp_gram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.jsp.jsp_gram.dto.User;
import org.jsp.jsp_gram.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class AppController {

	@Autowired
	UserService service;

	@GetMapping({"/","/login"})
	public String loadLogin() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String loadRegister(ModelMap map,User user) {
     return service.loadRegister(map, user);
	}
	
	@PostMapping("/register")
	public String register(@Valid  User user,BindingResult result) throws Exception {
  	return service.register(user,result);
	}

	@GetMapping("/otp/{id}")
	public String loadOtpPage(@PathVariable int id,ModelMap map) {
		map.put("id", id);
		return "user-otp.html";
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam int otp,@RequestParam int id,HttpSession session) {
		return service.verifyOtp(otp,id,session);
	}

	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, HttpSession session) {
		return service.resendOtp(id, session);
	}

	//login
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
		return service.login(username, password,session);
	}
	@GetMapping("/home")
	public String loadHome(HttpSession session){
		return service.loadHome(session);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session){
return service.logout(session);
	}
}
