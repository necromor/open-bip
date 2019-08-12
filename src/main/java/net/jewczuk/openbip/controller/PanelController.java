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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;
import net.jewczuk.openbip.to.HistoryTO;
import net.jewczuk.openbip.utils.TransformUtils;

@Controller
@RequestMapping("/panel")
public class PanelController {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private HistoryService historyService;
	
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
	public String addArticle(Model model, DisplaySingleArticleTO newArticle, RedirectAttributes attributes) {
		
		newArticle.setLink(TransformUtils.createLinkFromTitle(newArticle.getTitle()));
		DisplaySingleArticleTO savedArticle;
		Long editorID = 1L;
		try {
			savedArticle = articleService.saveArticle(newArticle, editorID);
			attributes.addFlashAttribute("articleSuccess", UIMessages.ADD_ARTICLE_SUCCESS);
			return "redirect:/panel/zarzadzaj/" + savedArticle.getLink();		
		} catch (BusinessException e) {	
			model.addAttribute("error", e.getMessage());
			model.addAttribute("newArticle", newArticle);
			return ViewNames.ARTICLE_ADD;
		}	
	}
	
	@GetMapping("/edytuj-tytul/{link}")
	public String showFormEditTitle(@PathVariable String link, Model model) {
		
		String template = ViewNames.ARTICLE_EDIT_TITLE;
		try {
			EditArticleTO newArticle = articleService.getArticleByLinkToEdit(link);

			model.addAttribute("newArticle", newArticle);
		} catch (EmptyResultDataAccessException empty) {
			throw new ResourceNotFoundException();
		}

		return template;
	}
	
	@PostMapping("/edytuj-tytul/{link}.do")
	public String editTitle(@PathVariable String link, Model model, EditArticleTO editedArticle, RedirectAttributes attributes) {
		
		editedArticle.setLink(TransformUtils.createLinkFromTitle(editedArticle.getTitle()));
		
		EditArticleTO savedArticle;
		Long editorID = 1L;
		try {
			savedArticle = articleService.editTitle(editedArticle, editorID);
			attributes.addFlashAttribute("articleSuccess", UIMessages.EDIT_TITLE_SUCCESS);
			return "redirect:/panel/zarzadzaj/" + savedArticle.getLink();		
		} catch (BusinessException e) {		
			model.addAttribute("error", e.getMessage());
			model.addAttribute("newArticle", editedArticle);
			return ViewNames.ARTICLE_EDIT_TITLE;
		}
	}
	
	@GetMapping("/twoja-aktywnosc")
	public String showEditorHistory(Model model) {
		Long editorID = 1L;
		List<HistoryTO> history = historyService.getAllLogEntriesByEditor(editorID);
		
		model.addAttribute("history", history);
		return ViewNames.LOG_LIST;
	}

}
