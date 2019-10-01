package net.jewczuk.openbip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.EditorService;
import net.jewczuk.openbip.to.EditorTO;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private EditorService editorService;
	
	@GetMapping("")
	public String redirectToMainAdminPage() {	
		return "redirect:/admin/";
	}
	
	@GetMapping("/")
	public String showMainAdminPage(Model model) {
		List<EditorTO> editors = editorService.getAllEditorsOnly();
		model.addAttribute("editors", editors);
		
		return ViewNames.ADMIN_MAIN;
	}
	
	@GetMapping("/dodaj/redaktor")
	public String showFormAddEditor(Model model) {
		EditorTO editor = (EditorTO) model.asMap().getOrDefault("editor", new EditorTO());
		model.addAttribute("editor", editor);
		
		return ViewNames.EDITOR_ADD;
	}
	
	@PostMapping("/dodaj/redaktor.do")
	public String addEditor(Model model, EditorTO editor, RedirectAttributes attributes) {	
		editor.setActive(false);
		editor.setPassGeneric(true);

		try {
			editorService.addNewEditor(editor);
			attributes.addFlashAttribute("editorSuccess", UIMessages.ADD_EDITOR_SUCCESS);
			return "redirect:/admin/";		
		} catch (BusinessException e) {	
			model.addAttribute("error", e.getMessage());
			model.addAttribute("editor", editor);
			return ViewNames.EDITOR_ADD;
		}	
	}	
	
	@GetMapping("/resetuj-haslo/{email}/")
	public String resetPassword(@PathVariable String email, RedirectAttributes attributes) {
		
		try {
			EditorTO saved = editorService.resetPassword(email);
			attributes.addFlashAttribute("editorSuccess", saved.getFullName() + UIMessages.RESET_EDITOR_SUCCESS);		
		} catch (BusinessException e) {	
			attributes.addFlashAttribute("editorFailure", UIMessages.RESET_EDITOR_FAILURE +  email);
		}
		
		return "redirect:/admin/";
	}
	
	@GetMapping("/aktywuj/{email}/")
	public String activateEditor(@PathVariable String email, RedirectAttributes attributes) {
		
		try {
			EditorTO saved = editorService.setStatus(email, true);
			attributes.addFlashAttribute("editorSuccess", saved.getFullName() + UIMessages.ACTIVATE_EDITOR_SUCCESS);		
		} catch (BusinessException e) {	
			attributes.addFlashAttribute("editorFailure", UIMessages.ACTIVATE_EDITOR_FAILURE +  email);
		}
		
		return "redirect:/admin/";
	}
	
	@GetMapping("/deaktywuj/{email}/")
	public String deactivateEditor(@PathVariable String email, RedirectAttributes attributes) {
		
		try {
			EditorTO saved = editorService.setStatus(email, false);
			attributes.addFlashAttribute("editorSuccess", saved.getFullName() + UIMessages.DEACTIVATE_EDITOR_SUCCESS);		
		} catch (BusinessException e) {	
			attributes.addFlashAttribute("editorFailure", UIMessages.DEACTIVATE_EDITOR_FAILURE +  email);
		}
		
		return "redirect:/admin/";
	}
	
	@GetMapping("/edytuj/{email}/")
	public String showFormEditEditor(@PathVariable String email, Model model, RedirectAttributes attributes) {
		EditorTO editor;
		try {
			editor = editorService.getByEmail(email);
		} catch (BusinessException e) {
			attributes.addFlashAttribute("editorFailure", UIMessages.EDITOR_INVALID_EMAIL +  email);
			return "redirect:/admin/";
		}
		model.addAttribute("editor", editor);
		model.addAttribute("oldEmail", editor.getEmail());
		
		return ViewNames.EDITOR_EDIT;
	}
	
	@PostMapping("/edytuj/redaktor.do")
	public String editEditor(Model model, EditorTO editor, String oldEmail, RedirectAttributes attributes) {
		try {
			editorService.editEditor(editor, oldEmail);
			attributes.addFlashAttribute("editorSuccess", UIMessages.EDIT_EDITOR_SUCCESS);
			return "redirect:/admin/";		
		} catch (BusinessException e) {	
			model.addAttribute("error", e.getMessage());
			model.addAttribute("editor", editor);
			model.addAttribute("oldEmail", oldEmail);
			return ViewNames.EDITOR_EDIT;
		}	
	}
	
	
}
