package net.jewczuk.openbip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String addArticle(Model model, EditorTO editor, RedirectAttributes attributes) {	
		editor.setActive(false);
		editor.setPassGeneric(true);

		try {
			editorService.addNewEditor(editor);
			attributes.addFlashAttribute("editorSuccess", UIMessages.ADD_EDITOR_SUCCESS);
			return "redirect:/admin";		
		} catch (BusinessException e) {	
			model.addAttribute("error", e.getMessage());
			model.addAttribute("editor", editor);
			return ViewNames.EDITOR_ADD;
		}	
	}	
	
	
}
