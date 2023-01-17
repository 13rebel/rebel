package com.scm.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.repo.UserRepo;

@Controller
public class UserController {
	@Autowired
	private UserRepo userRepo;

	// handler for common data
	@ModelAttribute
	public void commonUserData(Model model, Principal principal) {
		// Geting username from login user..
		String username = principal.getName();

		// fetching data from database..
		User user = userRepo.getUserByName(username);
		// Adding data to model..
		model.addAttribute("user", user);
		

	}

	// dashboard handler
	@GetMapping("/user/dashboard")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "Dashboard");
		return "normal_user/dashboard";

	}

	// Add contect handler
	@GetMapping("/user/addcontect")
	public String addContect(Model model) {

		model.addAttribute("title", "addContect");
		model.addAttribute("contect" , new Contact());
		return "normal_user/contectForm";
	}
	
	//processing contact information
	
	@PostMapping("/user/process")
	public String processContact(@ModelAttribute("contact") Contact contact , Model model , Principal principal) {
		
			String name = principal.getName();
			User user = userRepo.getUserByName(name);
			
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepo.save(user);
			
			//model.addAttribute("contact",contact);
			
			//System.out.println(contact);
			return "normal_user/dashboard";
		
	}
	

}
