package net.jewczuk.openbip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@Controller
@RequestMapping("/panel")
public class PanelController {
	
	@Autowired
	ArticleService articleService;
	
	@GetMapping("")
	public String redirectToMainPanelPage() {	
		return "redirect:/panel/";
	}
	
	@GetMapping("/")
	public String showMainPanelPage(Model model) {
		
		List<ArticleLinkTO> mainMenuArticles = articleService.getMainMenu();
		model.addAttribute("mainMenuArticles", mainMenuArticles);
		return ViewNames.PANEL_MAIN;
	}
	
	@GetMapping("/zarzadzaj/{link}")
	public String showArticleManagmentPage(@PathVariable String link, Model model) {
		
		String template = ViewNames.ARTICLE_MANAGEMENT;
		try {
			DisplaySingleArticleTO article = articleService.getArticleByLink(link);

			model.addAttribute("article", article);
		} catch (EmptyResultDataAccessException empty) {
			throw new ResourceNotFoundException();
		}

		return template;
	}

}
