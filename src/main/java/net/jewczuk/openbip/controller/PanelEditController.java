package net.jewczuk.openbip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.SandboxService;
import net.jewczuk.openbip.to.ArticleDisplayTO;
import net.jewczuk.openbip.to.ArticleEditTO;
import net.jewczuk.openbip.to.SandboxTO;
import net.jewczuk.openbip.utils.TransformUtils;

@Controller
@RequestMapping("/panel/edytuj")
public class PanelEditController {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private SandboxService sandboxService;
	
	@GetMapping("/tytul/{link}")
	public String showFormEditTitle(@PathVariable String link, Model model) {
		ArticleEditTO newArticle = articleService.getArticleByLinkToEdit(link);
		model.addAttribute("newArticle", newArticle);

		return ViewNames.ARTICLE_EDIT_TITLE;
	}
	
	@PostMapping("/tytul/{link}.do")
	public String editTitle(@PathVariable String link, Model model, 
						ArticleEditTO editedArticle, RedirectAttributes attributes) {
		
		editedArticle.setLink(TransformUtils.createLinkFromTitle(editedArticle.getTitle()));
		
		ArticleEditTO savedArticle;
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
	
	@GetMapping("/tresc/{link}")
	public String showFormEditContent(@PathVariable String link, Model model) {
		Long editorID = 1L;
		ArticleDisplayTO article = articleService.getArticleByLink(link);
		List<SandboxTO> sandboxes = sandboxService.getSandboxesByEditorId(editorID);
		model.addAttribute("article", article);
		model.addAttribute("sandboxes", sandboxes);

		return ViewNames.ARTICLE_EDIT_CONTENT;
	}
	
	@PostMapping("/tresc/{link}.do")
	public String editContent(@PathVariable String link, Model model, 
							ArticleDisplayTO article, RedirectAttributes attributes) {
		article.setLink(link);
		
		ArticleDisplayTO savedArticle;
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
	
	@GetMapping("/brudnopis/{link}")
	public String showFormEditSandbox(@PathVariable String link, Model model, RedirectAttributes attributes) {

		try {
			SandboxTO sandbox = sandboxService.getSandboxByLink(link);
			model.addAttribute("sandbox", sandbox);
			return ViewNames.SANDBOX_EDIT;
		} catch (BusinessException e) {
			attributes.addFlashAttribute("sandboxFailure", UIMessages.SANDBOX_NOT_EXISTS);
			return "redirect:/panel/lista-brudnopisow/";
		}	
	}
	
	@PostMapping("/brudnopis.do")
	public String editSandbox(Model model, SandboxTO sandbox, RedirectAttributes attributes) {
		
		Long editorID = 1L;
		
		try {
			sandboxService.editSandbox(sandbox, editorID);
			attributes.addFlashAttribute("sandboxSuccess", UIMessages.EDIT_SANDBOX_SUCCESS);
			return "redirect:/panel/lista-brudnopisow/";		
		} catch (BusinessException e) {	
			model.addAttribute("error", e.getMessage());
			model.addAttribute("sandbox", sandbox);
			return ViewNames.SANDBOX_EDIT;
		}
	}	

}
