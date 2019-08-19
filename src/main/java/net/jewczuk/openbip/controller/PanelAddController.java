package net.jewczuk.openbip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.utils.TransformUtils;

@Controller
@RequestMapping("/panel/dodaj")
public class PanelAddController {

	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/artykul")
	public String showAddArticle(Model model) {
		
		DisplaySingleArticleTO article = (DisplaySingleArticleTO) model.asMap().getOrDefault("newArticle", new DisplaySingleArticleTO());
		model.addAttribute("newArticle", article);
		
		return ViewNames.ARTICLE_ADD;
	}
	
	@PostMapping("/artykul.do")
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
}
