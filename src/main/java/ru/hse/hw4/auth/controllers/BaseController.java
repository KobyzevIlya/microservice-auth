package ru.hse.hw4.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ru.hse.hw4.auth.domain.User;
import ru.hse.hw4.auth.repos.UserRepository;

@Controller
public class BaseController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String greeting(Model model) {
		Iterable<User> users = userRepository.findAll();
		
		model.addAttribute("text", users);
		
		return "base";
	}

}