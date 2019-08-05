package net.jewczuk.openbip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jewczuk.openbip.constants.ViewNames;

@Controller
@RequestMapping("/panel")
public class PanelController {
	
	@GetMapping("")
	public String redirectToMainPanelPage() {	
		return "redirect:/panel/";
	}
	
	@GetMapping("/")
	public String showMainPanelPage() {
		
		return ViewNames.PANEL_MAIN;
	}

}
