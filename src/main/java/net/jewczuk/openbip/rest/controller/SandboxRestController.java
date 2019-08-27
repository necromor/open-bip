package net.jewczuk.openbip.rest.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.jewczuk.openbip.rest.objects.AjaxGenericResponse;
import net.jewczuk.openbip.to.SandboxTO;

@RestController
@RequestMapping("/api/sandbox")
public class SandboxRestController {
	
	@PutMapping("/save")
	public AjaxGenericResponse saveSandbox(@RequestBody SandboxTO sandbox) {
		
		System.out.println("title: " + sandbox.getTitle());
		System.out.println("link: " + sandbox.getLink());
		System.out.println("content: " + sandbox.getContent());
		
		//tmp
		return new AjaxGenericResponse(false, "Sandbox saved", "200");
	}

}
