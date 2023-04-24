package com.luv2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.service.CustomerService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	//need to inject our customer service	
	@Autowired
	private CustomerService  customerService;
	
	//need to inject the DAO to this controller
	
		//spring will scan for a component that implements customerDAO interface
		//and it will find it and make it available and inject it 
//		@Autowired
//		private CustomerDAO customerDAO;
//		
	
	@GetMapping("/list")
	public String listCustomer(Model theModel) {
		
		//get customers  from service
		List<Customer> theCustomer = customerService.getCustomers();
		
		///add the customers to the spring mvc model
		theModel.addAttribute("customers", theCustomer); 
		
		return "list-customers";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		//create a model attribute to bind form data
		Customer theCustomer = new Customer();
		
		theModel.addAttribute("customer", theCustomer);
		
		//will go look for customer-form.jsp
		return "customer-form";
		
	}
	
	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer theCustomer) {
		
		
		// save the customer using service
		customerService.saveCustomer(theCustomer);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int theId,
									Model theModel) {
		
		//get customer from database
		Customer theCustomer = customerService.getCustomer(theId);
		//set customer as a model attribute to pre-populate the form
		theModel.addAttribute("customer", theCustomer);
		// send over to  our form
		return "customer-form";
	}
	
	@GetMapping("/delete")
	public String delete (@RequestParam("customerId") int theId,
						  	Model theModel) {
		//delete the customer
		customerService.deleteCustomer(theId);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/search")
    public String searchCustomers(@RequestParam("theSearchName") String theSearchName,
                                    Model theModel) {
        // search customers from the service
        List<Customer> theCustomers = customerService.searchCustomers(theSearchName);
                
        // add the customers to the model
        theModel.addAttribute("customers", theCustomers);
        return "list-customers";        
    }
}
