package net.jewczuk.openbip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@Controller
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;

	@GetMapping("/artykul/{link}")
	public String showArticle(@PathVariable String link, Model model) {
		
		String template = ViewNames.SHOW_ARTICLE;
		try {
			DisplaySingleArticleTO article = articleService.getArticleByLink(link);
			model.addAttribute("article", article);
		} catch (EmptyResultDataAccessException empty) {
			throw new ResourceNotFoundException();
		}
		
		return template;
	}
	
	@GetMapping("/historia/{link}")
	public String showHistory(@PathVariable String link, Model model) {
		
		String template = ViewNames.SHOW_HISTORY;;
		try {
			DisplayArticleHistoryTO history = articleService.getHistoryByLink(link);
			model.addAttribute("history", history);
		} catch (EmptyResultDataAccessException empty) {
			throw new ResourceNotFoundException();
		}
		
		return template;
	}
}
