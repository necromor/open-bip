package net.jewczuk.openbip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
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
		DisplaySingleArticleTO article = articleService.getArticleByLink(link);
		model.addAttribute("article", article);

		return ViewNames.ARTICLE_MANAGEMENT;
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
		EditArticleTO newArticle = articleService.getArticleByLinkToEdit(link);
		model.addAttribute("newArticle", newArticle);

		return ViewNames.ARTICLE_EDIT_TITLE;
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
	
	@GetMapping("/dodaj-menu-glowne")
	public String showListOfUnatachtedArticles(Model model) {
		List<ArticleLinkTO> articles = articleService.getAllUnpinnedArticles();
		
		model.addAttribute("selectedArticles", new ArrayList<String>());
		model.addAttribute("articles", articles);
		return ViewNames.ARTICLE_PIN_MAIN_MENU;
	}
	
	@PostMapping("/dodaj-menu-glowne.do")
	public String pinSelectedToMainMenu(@RequestParam(required = false) List<String> selectedArticles, RedirectAttributes attributes) {
		Long editorID = 1L;
		
		if (selectedArticles == null) {
			attributes.addFlashAttribute("mainMenuFailure", UIMessages.ARTICLE_PINNED_FAILURE);
			return "redirect:/panel/";
		}
		
		for (String art : selectedArticles) {
			try {
				articleService.managePinningToMainMenu(art, editorID, true);
			} catch (BusinessException e) {
				attributes.addFlashAttribute("mainMenuFailure", UIMessages.ARTICLE_PINNED_FAILURE);
				return "redirect:/panel/";
			}
		}
		
		attributes.addFlashAttribute("mainMenuSuccess", UIMessages.ARTICLE_PINNED_SUCCESS);
		return "redirect:/panel/";
	}
	
	@GetMapping("/odepnij/{link}")
	public String unpinSelectedFromMainMenu(@PathVariable String link, RedirectAttributes attributes) {
		
		Long editorID = 1L;		
		try {
			articleService.managePinningToMainMenu(link, editorID, false);
			attributes.addFlashAttribute("mainMenuSuccess", UIMessages.ARTICLE_UNPINNED_SUCCESS);
		} catch (BusinessException e) {
			attributes.addFlashAttribute("mainMenuFailure", UIMessages.ARTICLE_UNPINNED_FAILURE);
		}

		return "redirect:/panel/";
	}
	
	@GetMapping("/edytuj-tresc/{link}")
	public String showFormEditContent(@PathVariable String link, Model model) {
		DisplaySingleArticleTO article = articleService.getArticleByLink(link);
		model.addAttribute("article", article);

		return ViewNames.ARTICLE_EDIT_CONTENT;
	}
	
	@PostMapping("/edytuj-tresc/{link}.do")
	public String editContent(@PathVariable String link, Model model, DisplaySingleArticleTO article, RedirectAttributes attributes) {
		
		DisplaySingleArticleTO savedArticle;
		Long editorID = 1L;
		try {
			savedArticle = articleService.editContent(article, editorID);
			attributes.addFlashAttribute("articleSuccess", UIMessages.EDIT_CONTENT_SUCCESS);
			return "redirect:/panel/zarzadzaj/" + savedArticle.getLink();		
		} catch (BusinessException e) {		
			attributes.addFlashAttribute("articleFailure", UIMessages.EDIT_CONTENT_FAILURE);
			return "redirect:/panel/zarzadzaj/" + article.getLink();
		}
	}
	
	@GetMapping("/przypnij-dziecko/{link}")
	public String showFormPinChildren(@PathVariable String link, Model model) {
		DisplaySingleArticleTO newArticle = articleService.getArticleByLink(link);
		List<ArticleLinkTO> articles = articleService.getAllUnpinnedArticles();
		
		model.addAttribute("parent", newArticle);
		model.addAttribute("selectedArticles", new ArrayList<String>());
		model.addAttribute("articles", articles);

		return ViewNames.ARTICLE_PIN_CHILD;
	}
	
	@PostMapping("/przypnij-dziecko.do")
	public String pinSelectedAsChildren(@RequestParam(required = false) List<String> selectedArticles, 
										@RequestParam String parentLink, RedirectAttributes attributes) {
		Long editorID = 1L;
		
		if (selectedArticles == null) {
			attributes.addFlashAttribute("articleFailure", UIMessages.ARTICLE_PINNED_FAILURE);
			return "redirect:/panel/zarzadzaj/" + parentLink;
		}
		
		for (String art : selectedArticles) {
			try {
				articleService.managePinningChildren(parentLink, art, editorID, true);
			} catch (BusinessException e) {
				attributes.addFlashAttribute("articleFailure", UIMessages.ARTICLE_PINNED_FAILURE);
				return "redirect:/panel/zarzadzaj/" + parentLink;
			}
		}
		
		attributes.addFlashAttribute("articleSuccess", UIMessages.ARTICLE_PINNED_SUCCESS);
		return "redirect:/panel/zarzadzaj/" + parentLink;
	}
	
	@GetMapping("/odepnij-dziecko/{parent}/{child}")
	public String unpinSelectedChild(@PathVariable String parent, @PathVariable String child, RedirectAttributes attributes) {
		
		Long editorID = 1L;		
		try {
			articleService.managePinningChildren(parent, child, editorID, false);
			attributes.addFlashAttribute("articleSuccess", UIMessages.ARTICLE_UNPINNED_SUCCESS);
		} catch (BusinessException e) {
			attributes.addFlashAttribute("articleFailure", UIMessages.ARTICLE_UNPINNED_FAILURE);
		}

		return "redirect:/panel/zarzadzaj/" + parent;
	}
}
