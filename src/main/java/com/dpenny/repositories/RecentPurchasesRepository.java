package com.dpenny.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.dpenny.models.ProductsResponse;
import com.dpenny.models.PurchasesResponse;

@Repository
public class RecentPurchasesRepository {
	
	@Autowired
	RestTemplate restTemplate;
	
	private final String externalAPIUrl = "http://74.50.59.155:6000/api";
	
	RecentPurchasesRepository() {
	}
	
	public PurchasesResponse getPurchasesByUsername(String username) {
		return restTemplate.getForObject(externalAPIUrl + "/purchases/by_user/" + username + "?limit=5", PurchasesResponse.class);
	}
	
	public PurchasesResponse getPurchasesByProductId(int productId) {
		return restTemplate.getForObject(externalAPIUrl + "/purchases/by_product/" + productId, PurchasesResponse.class);
	}
	
	public ProductsResponse getProductById(int productId) {
		return restTemplate.getForObject(externalAPIUrl + "/products/" + productId, ProductsResponse.class);
	}
}
