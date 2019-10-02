package net.jewczuk.openbip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.config.CustomUserDetails;
import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.EditorService;

@Controller
public class LoginController {
	
	@Autowired
	private EditorService editorService;

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
	
	@GetMapping("/redaktor/zmien-haslo")
	public String showFormChangepass(Model model) {
		
		return ViewNames.CHANGE_PASS;
	}
	
	@PostMapping("/redaktor/zmien-haslo.do")
	public String changePassword(Model model, RedirectAttributes attributes,
			String oldPass, String newPass, String newPass2) {
		
		CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		
		if (!newPass.equals(newPass2)) {
			model.addAttribute("error", UIMessages.PASSWORDS_MISSMATCH);
			return ViewNames.CHANGE_PASS;
		}
		
		//validate new pass
		
		try {
			editorService.changePassword(principal.getUsername(), oldPass, newPass);
		} catch (BusinessException be) {
			model.addAttribute("error", be.getMessage());
			return ViewNames.CHANGE_PASS;
		}
		
		if (isAdmin(principal)) {
			attributes.addFlashAttribute("editorSuccess", UIMessages.PASS_CHANGE_SUCCESS);
			return "redirect:/admin/";
		} else {
			attributes.addFlashAttribute("mainMenuSuccess", UIMessages.PASS_CHANGE_SUCCESS);
			return "redirect:/panel/";
		}
	}

	private boolean isAdmin(CustomUserDetails principal) {
		SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
		return principal.getAuthorities().contains(adminRole);
	}
}
