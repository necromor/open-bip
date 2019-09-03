package net.jewczuk.openbip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.UploadService;

@Controller
@RequestMapping("/panel/usun")
public class PanelDeleteController {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UploadService uploadService;
	
	@GetMapping("/zalacznik/{fileName}/{link}")
	public String removeAttachment(@PathVariable String link, 
										@PathVariable String fileName,
										RedirectAttributes attributes) {
		try {
			Long editorID = 1L;
			articleService.deleteAttachment(link, fileName, editorID);
			uploadService.deleteFile(fileName);
			attributes.addFlashAttribute("articleSuccess", UIMessages.ARTICLE_ATTACHMENT_DELETE_SUCCESS);
			return "redirect:/panel/zarzadzaj/" + link;	
		} catch (BusinessException e) {
			attributes.addFlashAttribute("articleFailure", UIMessages.ARTICLE_ATTACHMENT_DELETE_FAILURE);
			return "redirect:/panel/zarzadzaj/" + link;	
		}
	}
	
	@GetMapping("/artykul/{link}")
	public String removeArticle(@PathVariable String link,
									RedirectAttributes attributes) {
		try {
			Long editorID = 1L;
			articleService.deleteArticle(link, editorID);
			attributes.addFlashAttribute("articleSuccess", UIMessages.ARTICLE_DELETE_SUCCESS);
			return "redirect:/panel/lista-artykulow";	
		} catch (BusinessException e) {
			attributes.addFlashAttribute("articleFailure", UIMessages.ARTICLE_DELETE_FAILURE);
			return "redirect:/panel/zarzadzaj/" + link;	
		}
	}
}
