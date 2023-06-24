package org.example.SecurityApp.controllers;


import org.example.SecurityApp.models.User;
import org.example.SecurityApp.services.UsersDetailsService;
import org.example.SecurityApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UsersController {

	private final UserService userService;

	@Autowired
	public UsersController(UserService userService, UsersDetailsService usersDetailsService) {
		this.userService = userService;
	}

	@GetMapping()
	public String index(Principal principal, ModelMap model) {
		User user = userService.findByUsername(principal.getName());

		model.addAttribute("user", user);
		return "user/index";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		userService.delete(id);
		return "redirect:/auth/login";
	}
}