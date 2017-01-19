package com.dpenny.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dpenny.models.Product;
import com.dpenny.models.ProductsResponse;
import com.dpenny.models.Purchase;
import com.dpenny.models.PurchasesResponse;
import com.dpenny.repositories.RecentPurchasesRepository;

@Service
public class RecentPurchasesService {
	
	@Autowired
	RecentPurchasesRepository repository;
	
	/**
	 * Gets a list of recent purchases for a username
	 * @param username
	 * @return List of Purchase objects
	 */
	@Cacheable(value = "purchasesForUser")
	public List<Purchase> getRecentPurchasesForUser(String username) {
		PurchasesResponse purchasesResponse = repository.getPurchasesByUsername(username);
		return purchasesResponse.getPurchases();
	}
	
	/**
	 * Gets the information for a productId
	 * @param productId
	 * @return Product object
	 */
	@Cacheable(value = "product")
	public Product getProductById(int productId) {
		ProductsResponse productsResponse = repository.getProductById(productId);
		return productsResponse.getProduct();
	}
	
	/**
	 * Gets a list of people who have purchased a given productId
	 * @param productId
	 * @return list of usernames as String
	 */
	@Cacheable(value = "purchasersForProduct")
	public List<String> getPurchasersByProductId(int productId) {
		List<String> purchasers = new ArrayList<String>();
		PurchasesResponse purchasesResponse = repository.getPurchasesByProductId(productId);
		
		for(Purchase p : purchasesResponse.getPurchases()) {
			if(!purchasers.contains(p.getUsername())) {
				purchasers.add(p.getUsername());
			}
		}
		
		return purchasers;
	}
}
