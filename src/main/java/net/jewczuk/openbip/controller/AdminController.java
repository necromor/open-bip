package net.jewczuk.openbip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jewczuk.openbip.constants.ViewNames;
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
}
