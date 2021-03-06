package net.jewczuk.openbip.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.EditorService;
import net.jewczuk.openbip.service.MainPageService;
import net.jewczuk.openbip.to.ArticleDisplayTO;
import net.jewczuk.openbip.to.ArticleHistoryTO;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.RedactorTO;

@Controller
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private MainPageService mainPageService;

	@GetMapping("/")
	public String showMainPage(Model model) {
		ArticleLinkTO mainPage = mainPageService.getMainPage();
		ArticleDisplayTO article = articleService.getArticleByLink(mainPage.getLink());
		List<ArticleLinkTO> menu = articleService.getMainMenu();
	
		model.addAttribute("article", article);
		model.addAttribute("mainMenu", menu);
		return ViewNames.SHOW_ARTICLE;
	}

	@GetMapping("/artykul/{link}")
	public String showArticle(@PathVariable String link, Model model) {

		String template = ViewNames.SHOW_ARTICLE;
		try {
			ArticleDisplayTO article = articleService.getArticleByLink(link);
			List<ArticleLinkTO> menu = articleService.getMainMenu();
			List<ArticleLinkTO> breadcrumbs = articleService.getBreadcrumbs(link);
			Collections.reverse(breadcrumbs);
			model.addAttribute("article", article);
			model.addAttribute("mainMenu", menu);
			model.addAttribute("breadcrumbs", breadcrumbs);
		} catch (EmptyResultDataAccessException empty) {
			throw new ResourceNotFoundException();
		}

		return template;
	}

	@GetMapping("/historia/{link}")
	public String showHistory(@PathVariable String link, Model model) {

		String template = ViewNames.SHOW_HISTORY;
		try {
			ArticleHistoryTO history = articleService.getHistoryByLink(link);
			List<ArticleLinkTO> menu = articleService.getMainMenu();
			model.addAttribute("history", history);
			model.addAttribute("mainMenu", menu);
		} catch (EmptyResultDataAccessException empty) {
			throw new ResourceNotFoundException();
		}

		return template;
	}

	@GetMapping("/redakcja-bip")
	public String showRedactors(Model model) {
		String template = ViewNames.SHOW_REDACTORS;

		List<RedactorTO> redactors = editorService.getAllRedactors();
		List<ArticleLinkTO> menu = articleService.getMainMenu();
		model.addAttribute("redactors", redactors);
		model.addAttribute("mainMenu", menu);

		return template;
	}
}
