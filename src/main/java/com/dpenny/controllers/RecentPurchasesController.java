package com.dpenny.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dpenny.exceptions.UserNotFoundException;
import com.dpenny.models.Product;
import com.dpenny.models.Purchase;
import com.dpenny.services.RecentPurchasesService;

@RestController
@RequestMapping("/api/recent_purchases")
public class RecentPurchasesController {
	
	@Autowired
	private RecentPurchasesService service;
	
	@RequestMapping(method = RequestMethod.GET, value="/{username:.+}")
	public ResponseEntity<Collection<Product>> getRecentPurchasesForUser(@PathVariable String username) {
		
		List<Product> recentProducts = new ArrayList<Product>();
		List<Purchase> recentPurchasesForUser = service.getRecentPurchasesForUser(username);
		
		if(recentPurchasesForUser.isEmpty()) {
			throw new UserNotFoundException(username);
		}
		
		for(Purchase p : recentPurchasesForUser) {
        	Product product = service.getProductById(p.getProductId());
        	
        	if(product != null) {
	        	product.setRecent(service.getPurchasersByProductId(product.getId()));
	        	recentProducts.add(product);
        	}
        }
		
		recentProducts.sort(new Comparator<Product>() {
			@Override
			public int compare(Product product1, Product product2) {
				return product2.getRecent().size() - product1.getRecent().size();
			}
		});
		
		return new ResponseEntity<Collection<Product>>(recentProducts, HttpStatus.OK);
	}
	
	@ExceptionHandler({UserNotFoundException.class})
	public ResponseEntity<String> userNotFound(UserNotFoundException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	//Probably due to curly braces in path variable
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> invalidArgument(IllegalArgumentException e) {
		return new ResponseEntity<String>("Illegal characters in path variable.", HttpStatus.NOT_ACCEPTABLE);
	}
}
