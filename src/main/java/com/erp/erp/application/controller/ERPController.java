package com.erp.erp.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ERPController {
	
	@GetMapping("/test")
	public String demo() {
		return "Hello";
	}

}
