package net.jewczuk.openbip.controller;

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
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;
import net.jewczuk.openbip.utils.TransformUtils;

@Controller
@RequestMapping("/panel/edytuj")
public class PanelEditController {
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/tytul/{link}")
	public String showFormEditTitle(@PathVariable String link, Model model) {
		EditArticleTO newArticle = articleService.getArticleByLinkToEdit(link);
		model.addAttribute("newArticle", newArticle);

		return ViewNames.ARTICLE_EDIT_TITLE;
	}
	
	@PostMapping("/tytul/{link}.do")
	public String editTitle(@PathVariable String link, Model model, 
						EditArticleTO editedArticle, RedirectAttributes attributes) {
		
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
	
	@GetMapping("/tresc/{link}")
	public String showFormEditContent(@PathVariable String link, Model model) {
		DisplaySingleArticleTO article = articleService.getArticleByLink(link);
		model.addAttribute("article", article);

		return ViewNames.ARTICLE_EDIT_CONTENT;
	}
	
	@PostMapping("/tresc/{link}.do")
	public String editContent(@PathVariable String link, Model model, 
							DisplaySingleArticleTO article, RedirectAttributes attributes) {
		
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

}
