package net.jewczuk.openbip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.utils.TransformUtils;

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
	
	@GetMapping("/lista-artykulow")
	public String showArticleList(Model model) {
		
		List<ArticleLinkTO> allArticles = articleService.getAllArticles();
		model.addAttribute("allArticles", allArticles);
		
		return ViewNames.ARTICLE_LIST;
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
	
	@GetMapping("/dodaj-artykul")
	public String showAddArticle(Model model) {
		
		DisplaySingleArticleTO article = (DisplaySingleArticleTO) model.asMap().getOrDefault("newArticle", new DisplaySingleArticleTO());
		model.addAttribute("newArticle", article);
		
		return ViewNames.ARTICLE_ADD;
	}
	
	@PostMapping("/dodaj-artykul.do")
	public String addArticle(Model model, DisplaySingleArticleTO newArticle) {
		
		newArticle.setLink(TransformUtils.createLinkFromTitle(newArticle.getTitle()));
		DisplaySingleArticleTO savedArticle;
		try {
			savedArticle = articleService.saveArticle(newArticle);
			return "redirect:/panel/zarzadzaj/" + savedArticle.getLink();
		} catch (BusinessException e) {
			
			model.addAttribute("newArticle", newArticle);
			return ViewNames.ARTICLE_ADD;
		}	
	}

}
