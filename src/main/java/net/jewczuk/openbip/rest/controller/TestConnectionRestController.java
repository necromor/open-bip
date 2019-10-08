package net.jewczuk.openbip.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.rest.objects.AjaxGenericResponse;
import net.jewczuk.openbip.service.ArticleService;

@RestController
@RequestMapping("/api/test")
public class TestConnectionRestController {
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/connection")
	public AjaxGenericResponse testDBConnection() {
		boolean result = false;
		String message = UIMessages.CONNECTION_FAILURE;
		
		try {
			articleService.getAllArticles();
			result = true;
			message = UIMessages.CONNECTION_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new AjaxGenericResponse(result, message);
	}

}
