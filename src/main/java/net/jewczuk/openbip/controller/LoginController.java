package net.jewczuk.openbip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.jewczuk.openbip.constants.ViewNames;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLoginForm(@RequestParam(required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", true);
		}
		return ViewNames.LOGIN;
	}	
	
	@GetMapping("/access-denied")
	public String showAccessDeniedPage() {
		return ViewNames.ACCESS_DENIED;
	}
}
