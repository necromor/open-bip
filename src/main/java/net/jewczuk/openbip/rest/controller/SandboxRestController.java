package net.jewczuk.openbip.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.jewczuk.openbip.constants.UIMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.rest.objects.AjaxGenericResponse;
import net.jewczuk.openbip.rest.objects.AjaxSandboxResponse;
import net.jewczuk.openbip.service.SandboxService;
import net.jewczuk.openbip.to.SandboxTO;

@RestController
@RequestMapping("/api/sandbox")
public class SandboxRestController {
	
	@Autowired
	private SandboxService sandboxService;
	
	@PutMapping("/save")
	public AjaxGenericResponse saveSandbox(@RequestBody SandboxTO sandbox) {
		Long editorID = 1L;
		
		boolean result = false;
		String message = UIMessages.SAVE_SANDBOX_SUCCESS;
		try {
			sandboxService.editSandbox(sandbox, editorID);	
		} catch (BusinessException e) {	
			message = UIMessages.SAVE_SANDBOX_FAILURE;
			result = true;
		}	

		return new AjaxGenericResponse(result, message);
	}
	
	@GetMapping("/get-content/{link}")
	public AjaxSandboxResponse getSandboxContent(@PathVariable String link) {
		boolean result = false;
		String message = UIMessages.SANDBOX_LOAD_SUCCESS;
		SandboxTO sandbox = new SandboxTO();
		
		try {
			sandbox = sandboxService.getSandboxByLink(link);	
		} catch (BusinessException e) {	
			message = UIMessages.SANDBOX_LOAD_FAILURE;
			result = true;
		}	
		
		return new AjaxSandboxResponse(result, message, sandbox.getContent());
	}

}
