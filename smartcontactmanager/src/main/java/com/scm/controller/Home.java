package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helper.Message;
import com.scm.repo.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
public class Home {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepo userRepo;

	// Home handler

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home");
		return "home";
	}
	
	
	//About page handler
	
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title" ,"About");
		
		return "about";
	}

	// signup page handler
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("title", "signup");
		return "signup";
	}
	@GetMapping("/signin")
	public String signin(Model model) {
		
		model.addAttribute("title", "Login");
		return "signin";
	}

	@PostMapping("/registor")
	public String registor(@ModelAttribute("user") User user,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
			@RequestParam("confirmPassword") String confirmPassword, Model model, HttpSession session) {
		model.addAttribute("title" ,"Registor");

		try {
			/*
			StringBuffer conpass=new StringBuffer(confirmPassword);

			if (!user.getPassword().equals(conpass)) {
				System.out.println("Your password and confirm password are different");
				System.out.println(conpass);
				System.out.println(user.getPassword());
				throw new Exception("Your password and confirm password are different");
			}
			*/

			if (!agreement) {
				System.out.println("You have not aggred terms and condition");
				throw new Exception("You have not aggred terms and condition");
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println(agreement);
			System.out.println(user);

			User result = this.userRepo.save(user);

			model.addAttribute("user", result);

			session.setAttribute("message", new Message("Sucessfully Registerd!!", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!!" + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

}
