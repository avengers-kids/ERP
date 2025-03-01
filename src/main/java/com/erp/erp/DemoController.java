package com.erp.erp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	@GetMapping("/test")
	public String demo() {
		return "Hello";
	}
}
