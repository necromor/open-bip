package net.jewczuk.openbip.rest.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.jewczuk.openbip.rest.objects.AjaxGenericResponse;

@RestController
@RequestMapping("/api/sandbox")
public class SandboxRestController {
	
	@PutMapping("/save")
	public AjaxGenericResponse saveSandbox() {
		
		//tmp
		return new AjaxGenericResponse(false, "Sandbox saved", "200");
	}

}
