package net.jewczuk.openbip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.config.CustomUserDetails;
import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.EditorService;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.service.MainPageService;
import net.jewczuk.openbip.service.SandboxService;
import net.jewczuk.openbip.to.ArticleDisplayTO;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.EditorTO;
import net.jewczuk.openbip.to.HistoryTO;
import net.jewczuk.openbip.to.SandboxTO;
import net.jewczuk.openbip.to.TreeBranchTO;

@Controller
@RequestMapping("/panel")
public class PanelController {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private SandboxService sandboxService;
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private MainPageService mainPageService;
	
	@GetMapping("")
	public String redirectToMainPanelPage() {	
		return "redirect:/panel/";
	}
	
	@GetMapping("/")
	public String showMainPanelPage(Model model, RedirectAttributes attributes) {
		if (checkIfPasswordDefault()) {
			attributes.addFlashAttribute("passGeneric", UIMessages.PASS_GENERIC);
			return "redirect:/redaktor/zmien-haslo";
		} else {
			List<ArticleLinkTO> mainMenuArticles = articleService.getMainMenu();
			model.addAttribute("mainMenuArticles", mainMenuArticles);
			return ViewNames.PANEL_MAIN;
		}
	}

	@GetMapping("/zarzadzaj/{link}")
	public String showArticleManagmentPage(@PathVariable String link, Model model) {
		ArticleDisplayTO article = articleService.getArticleByLink(link);
		model.addAttribute("article", article);

		return ViewNames.ARTICLE_MANAGEMENT;
	}
	
	@GetMapping("/lista-artykulow")
	public String showArticleList(Model model) {
		
		List<ArticleLinkTO> allArticles = articleService.getAllArticles();
		model.addAttribute("allArticles", allArticles);
		
		return ViewNames.ARTICLE_LIST;
	}
	
	@GetMapping("/drzewo-artykulow")
	public String showArticleTree(Model model) {
		
		List<TreeBranchTO> branches = articleService.getTree();
		model.addAttribute("branches", branches);
		
		return ViewNames.ARTICLE_TREE;
	}
	
	@GetMapping("/twoja-aktywnosc")
	public String showEditorHistory(Model model) {
		return "redirect:/panel/twoja-aktywnosc/30";
	}
	
	@GetMapping("/twoja-aktywnosc/{limit}")
	public String showEditorHistoryLimit(@PathVariable int limit, Model model) {
		Long editorID = getIdOfLoggedEditor();
		List<HistoryTO> history = historyService.getLimitedLogEntriesByEditor(editorID, limit);
		
		model.addAttribute("history", history);
		model.addAttribute("limit", limit);
		model.addAttribute("newlimit", limit + 30);
		return ViewNames.LOG_LIST;
	}

	@GetMapping("/przypnij")
	public String showListOfUnatachtedArticles(Model model) {
		List<ArticleLinkTO> articles = articleService.getAllUnpinnedArticles();
		
		model.addAttribute("selectedArticles", new ArrayList<String>());
		model.addAttribute("articles", articles);
		return ViewNames.ARTICLE_PIN_MAIN_MENU;
	}
	
	@PostMapping("/przypnij.do")
	public String pinSelectedToMainMenu(@RequestParam(required = false) List<String> selectedArticles, RedirectAttributes attributes) {
		Long editorID = getIdOfLoggedEditor();
		
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
		Long editorID = getIdOfLoggedEditor();	
		
		try {
			articleService.managePinningToMainMenu(link, editorID, false);
			attributes.addFlashAttribute("mainMenuSuccess", UIMessages.ARTICLE_UNPINNED_SUCCESS);
		} catch (BusinessException e) {
			attributes.addFlashAttribute("mainMenuFailure", UIMessages.ARTICLE_UNPINNED_FAILURE);
		}

		return "redirect:/panel/";
	}
	
	@GetMapping("/przypnij-dziecko/{link}")
	public String showFormPinChildren(@PathVariable String link, Model model) {
		ArticleDisplayTO newArticle = articleService.getArticleByLink(link);
		List<ArticleLinkTO> articles = articleService.getAllUnpinnedArticles();
		
		model.addAttribute("parent", newArticle);
		model.addAttribute("selectedArticles", new ArrayList<String>());
		model.addAttribute("articles", articles);

		return ViewNames.ARTICLE_PIN_CHILD;
	}
	
	@PostMapping("/przypnij-dziecko.do")
	public String pinSelectedAsChildren(@RequestParam(required = false) List<String> selectedArticles, 
										@RequestParam String parentLink, RedirectAttributes attributes) {
		Long editorID = getIdOfLoggedEditor();
		
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
		Long editorID = getIdOfLoggedEditor();
		
		try {
			articleService.managePinningChildren(parent, child, editorID, false);
			attributes.addFlashAttribute("articleSuccess", UIMessages.ARTICLE_UNPINNED_SUCCESS);
		} catch (BusinessException e) {
			attributes.addFlashAttribute("articleFailure", UIMessages.ARTICLE_UNPINNED_FAILURE);
		}

		return "redirect:/panel/zarzadzaj/" + parent;
	}
	
	@GetMapping("/lista-brudnopisow")
	public String showSandboxList(Model model) {
		Long editorID = getIdOfLoggedEditor();
		
		List<SandboxTO> all = sandboxService.getSandboxesByEditorId(editorID);
		model.addAttribute("sandboxes", all);
		
		return ViewNames.SANDBOX_LIST;
	}
	
	@GetMapping("/strona-glowna")
	public String showMainPageChooser(Model model) {	
		ArticleLinkTO mainPage = mainPageService.getMainPage();
		List<ArticleLinkTO> articles = articleService.getAllUnpinnedArticles();
		
		model.addAttribute("article", mainPage);
		model.addAttribute("allOthers", articles);
		
		return ViewNames.ARTICLE_MAIN_PAGE;
	}
	
	@GetMapping("/ustaw-strona-glowna/{link}")
	public String setMainPage(Model model, @PathVariable String link, RedirectAttributes attributes) {	
		mainPageService.setMainPage(link);
		attributes.addFlashAttribute("mainPageSuccess", UIMessages.MAIN_PAGE_SUCCESS);
		
		return "redirect:/panel/strona-glowna";
	}
	
	private Long getIdOfLoggedEditor() {
		CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		return principal.getUserId();
	}
	
	private boolean checkIfPasswordDefault() {
		CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		
		EditorTO loggedIn = new EditorTO();
		loggedIn.setPassGeneric(true);
		
		try {
			loggedIn = editorService.getByEmail(principal.getUsername());
		} catch (BusinessException e) {
			// Should not happen - logged in user exists
		}
		return loggedIn.isPassGeneric();
	}
}
