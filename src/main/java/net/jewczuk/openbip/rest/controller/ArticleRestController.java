package net.jewczuk.openbip.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.rest.objects.AjaxGenericResponse;
import net.jewczuk.openbip.service.ArticleService;

@RestController
@RequestMapping("/api/article")
public class ArticleRestController {

	@Autowired
	private ArticleService articleService;
	
	@PutMapping("/children-positions/{link}")
	public AjaxGenericResponse saveNewChildrenPositions(@RequestBody String[] children, @PathVariable String link) {		
		Long editorID = 1L;
		
		boolean result = false;
		String message = UIMessages.POSITIONS_CHILDREN_SUCCESS;
		try {
			articleService.saveChildrenPositions(link, children, editorID);	
		} catch (BusinessException e) {	
			message = UIMessages.POSITIONS_CHILDREN_FAILURE;
			result = true;
		}	
		
		return new AjaxGenericResponse(result, message);	
	}
	
	@PutMapping("/attachments-positions/{link}")
	public AjaxGenericResponse saveNewAttachmentsPositions(@RequestBody String[] children, @PathVariable String link) {		
		Long editorID = 1L;
		
		boolean result = false;
		String message = UIMessages.POSITIONS_ATTACHMENTS_SUCCESS;
		try {
			articleService.saveAttachmentsPositions(link, children, editorID);	
		} catch (BusinessException e) {	
			message = UIMessages.POSITIONS_ATTACHMENTS_SUCCESS;
			result = true;
		}	
		
		return new AjaxGenericResponse(result, message);	
	}
	
	
}
