package spring.cloud.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.user.annotation.Aop;


@RestController
@RequestMapping("/testAop")
public class AopController {

	
	@Aop("sss")
	@GetMapping("/aop")
	public String testAop(String s,String a) {
		int i=1/0;
		return "xxxxxxxxxxxxxxx";
	}
}
