package com.scm.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.repo.ContactRepo;
import com.scm.repo.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;

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
		String name = principal.getName();
		
		User user = this.userRepo.getUserByName(name);
		
		List<Contact> findContactsByUid = this.contactRepo.findContactsByUid(user.getId());
		
		model.addAttribute("contact", findContactsByUid);
		
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
	
	@GetMapping("/user/process")
	public String processContact(@ModelAttribute("contact") Contact contact , Model model , Principal principal) {
		   
			model.addAttribute("title" ,"Dashboard");
			
			String name = principal.getName();
			User user = this.userRepo.getUserByName(name);
			contact.setUser(user);
			
			this.contactRepo.save(contact);
			List<Contact> findContactsByUid = this.contactRepo.findContactsByUid(user.getId());
			
			model.addAttribute("contact", findContactsByUid);
			
			
			
			return "normal_user/dashboard";
		
	}
	// delete contact handler
	@GetMapping("/user/deletecontact/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId , Model model , HttpSession session) {
		
		this .contactRepo.deleteById(cId);
		return "redirect:/user/dashboard";
	}
	
	//update contact handler
	
	@GetMapping("/user/updatecontact/{cid}")
	public String updateContact(@PathVariable("cid")Integer cId, Model m) {
		
		m.addAttribute("title" , "UpdateContact");
		m.addAttribute("cid", cId);
		
		Contact contact = this.contactRepo.findById(cId).get();
		m.addAttribute("contact" , contact);
		
		return "normal_user/contectForm";
	}
	
	
	

}
