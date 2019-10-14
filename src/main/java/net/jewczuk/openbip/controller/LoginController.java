package net.jewczuk.openbip.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.config.CustomUserDetails;
import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.EditorService;
import net.jewczuk.openbip.to.EditorTO;
import net.jewczuk.openbip.validators.PasswordValidator;

@Controller
public class LoginController {
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private PasswordValidator passwordValidator;
	
	@GetMapping(ApplicationProperties.SETUP_PAGE_LINK)
	public String showSetUpPage(Model model) {
		EditorTO admin = (EditorTO) model.asMap().getOrDefault("admin", new EditorTO());
		model.addAttribute("admin", admin);	
		model.addAttribute("adminPresent", editorService.isAdminPresent());
		model.addAttribute("postLink", ApplicationProperties.SETUP_PAGE_LINK + ".do");
		model.addAttribute("privacyPolicy", doesLinkExists("polityka-prywatnosci"));
		model.addAttribute("cookiesPolicy", doesLinkExists("polityka-cookies"));
		
		return ViewNames.SET_UP_PAGE;
	}
	
	@PostMapping(ApplicationProperties.SETUP_PAGE_LINK + ".do")
	public String crateAdminAccount(Model model, EditorTO admin) {		
		try {
			editorService.createAdminAccount(admin);	
			return "redirect:" + ApplicationProperties.SETUP_PAGE_LINK;
		} catch (BusinessException e) {
			model.addAttribute("admin", new EditorTO());	
			model.addAttribute("adminPresent", editorService.isAdminPresent());
			model.addAttribute("error", e.getMessage());
			model.addAttribute("postLink", ApplicationProperties.SETUP_PAGE_LINK + ".do");
			
			return ViewNames.SET_UP_PAGE;
		}
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("errorMessage", null);
		return ViewNames.LOGIN;
	}	
	
	@GetMapping("/login-error")
	public String showLoginErrorForm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		String errorMessage = null;
        
		if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = localizeError(ex.getMessage());
            }
        }
		
        model.addAttribute("errorMessage", errorMessage);

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

		try {
			passwordValidator.validatePassword(oldPass, newPass, newPass2);
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
	
	private String localizeError(String error) {
		Map<String, String> localized = new HashMap<>();
		localized.put("Bad credentials", UIMessages.LOGIN_BAD_CREDENTIALS);
		localized.put("User account has expired", UIMessages.LOGIN_EXPIRED);

		return localized.getOrDefault(error, UIMessages.LOGIN_UNIDENTIFIED);
	}
	
	private boolean doesLinkExists(String link) {
		try {
			articleService.getArticleByLink(link);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
