package net.jewczuk.openbip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jewczuk.openbip.constants.ViewNames;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@GetMapping("")
	public String redirectToMainAdminPage() {	
		return "redirect:/admin/";
	}
	
	@GetMapping("/")
	public String showMainAdminPage(Model model) {
		
		return ViewNames.ADMIN_MAIN;
	}
}
