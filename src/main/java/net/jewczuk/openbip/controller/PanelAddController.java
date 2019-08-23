package net.jewczuk.openbip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.constants.ViewNames;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.UploadService;
import net.jewczuk.openbip.to.AttachmentTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.utils.TransformUtils;
import net.jewczuk.openbip.validators.AttachmentValidator;

@Controller
@RequestMapping("/panel/dodaj")
public class PanelAddController {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private AttachmentValidator attachmentValidator;
	
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
	
	@GetMapping("/zalacznik/{link}")
	public String showFormAddAttachment(@PathVariable String link, Model model) {
		DisplaySingleArticleTO article = articleService.getArticleByLink(link);
		model.addAttribute("article", article);
		model.addAttribute("name", "");

		return ViewNames.ARTICLE_ADD_ATTACHMENT;
	}
	
	@PostMapping("/zalacznik.do")
	public String addAttachment(Model model, 
				@RequestParam String link, 
				@RequestParam String name,
				@RequestParam("file") MultipartFile file,
				RedirectAttributes attributes) {	
		
		Long editorID = 1L;	
		
		try {
			attachmentValidator.validateAddAttachment(file, name);
			AttachmentTO attachment = new AttachmentTO(file, name);
			uploadService.saveFile(file);
			articleService.addAttachment(link, attachment, editorID);
			attributes.addFlashAttribute("articleSuccess", UIMessages.ARTICLE_ATTACHMENT_ADD_SUCCESS);
			return "redirect:/panel/zarzadzaj/" + link;	
		} catch (BusinessException e) {
			DisplaySingleArticleTO article = articleService.getArticleByLink(link);
			model.addAttribute("article", article);
			model.addAttribute("name", name);
			model.addAttribute("error", e.getMessage());
			return ViewNames.ARTICLE_ADD_ATTACHMENT;
		}
	}
	
}
