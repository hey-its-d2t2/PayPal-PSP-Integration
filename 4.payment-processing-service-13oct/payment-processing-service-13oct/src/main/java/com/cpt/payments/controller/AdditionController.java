package com.cpt.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpt.payments.service.interfaces.ReconService;

@RestController
public class AdditionController {
	
	private ReconService reconService;
	
	public AdditionController(ReconService reconService) {
		this.reconService = reconService;
	}
	
    @PostMapping("/add")
    public int add(@RequestParam int num1, @RequestParam int num2) {
        System.out.println("num1:" + num1 + "|num2:" + num2);
    	
        int sumResult = num1 + num2;
        System.out.println("sumResult:" + sumResult);

        return sumResult;
    }
    
    @PostMapping("/testRecon")
    public String testReconLogic() {
    	
    	reconService.reconcilePayments();
    	
    	return "Recon triggered.";
    	
    }
}
