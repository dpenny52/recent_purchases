package com.dpenny.controllers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.dpenny.exceptions.UserNotFoundException;
import com.dpenny.models.Product;
import com.dpenny.models.Purchase;
import com.dpenny.services.RecentPurchasesService;
import com.dpenny.test.fixtures.ProductFixture;
import com.dpenny.test.fixtures.PurchaseFixture;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RecentPurchasesControllerTest {
	
	@LocalServerPort
	int port;
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@InjectMocks
	RecentPurchasesController controller;
	
	@Spy
	RecentPurchasesService service;
	
	PurchaseFixture purchaseFixture;
	ProductFixture productFixture;
	
	List<Purchase> recentPurchasesForUserReturn;
	List<String> purchasers;
	
	@Before
	public void setUp() {
		controller = new RecentPurchasesController();

		purchaseFixture = new PurchaseFixture();
		productFixture = new ProductFixture();
		
		initMockReturnObjects();

		MockitoAnnotations.initMocks(this);
	}
	
	private void initMockReturnObjects() {
		recentPurchasesForUserReturn = new ArrayList<Purchase>();
		recentPurchasesForUserReturn.add(purchaseFixture.purchase1);
		recentPurchasesForUserReturn.add(purchaseFixture.purchase2);
		recentPurchasesForUserReturn.add(purchaseFixture.purchase3);
		
		purchasers = new ArrayList<String>();
		purchasers.add(purchaseFixture.purchase1.getUsername());
		purchasers.add(purchaseFixture.purchase2.getUsername());
		purchasers.add(purchaseFixture.purchase3.getUsername());
	}
	
	@Test
	public void testGetRecentPurchases() {
		
		setBasicMockReturns();
		
		ArrayList<Product> products = (ArrayList<Product>) controller.getRecentPurchasesForUser(purchaseFixture.purchase1.getUsername()).getBody();
		
		assertEquals(products.get(0).getFace(), productFixture.product1.getFace());
		assertEquals(products.get(1).getFace(), productFixture.product2.getFace());
		assertEquals(products.get(2).getFace(), productFixture.product3.getFace());
	}
	
	@Test
	public void testGetRecentPurchases_sortOrder() {
		
		setBasicMockReturns();
		
		List<String> lessPurchasers = new ArrayList<String>();
		lessPurchasers.add(purchaseFixture.purchase1.getUsername());
		lessPurchasers.add(purchaseFixture.purchase2.getUsername());
		Mockito.doReturn(lessPurchasers).when(service).getPurchasersByProductId(productFixture.product1.getId());
		
		ArrayList<Product> products = (ArrayList<Product>) controller.getRecentPurchasesForUser(purchaseFixture.purchase1.getUsername()).getBody();
		
		assertEquals(products.get(0).getFace(), productFixture.product2.getFace());
		assertEquals(products.get(1).getFace(), productFixture.product3.getFace());
		assertEquals(products.get(2).getFace(), productFixture.product1.getFace());
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testGetRecentPurchases_userNotExists() {
		Mockito.doReturn(new ArrayList<Purchase>()).when(service).getRecentPurchasesForUser("nonExistantUser");
		controller.getRecentPurchasesForUser("nonExistantUser").getBody();
	}
	
	@Test
	public void testGetRecentPurchases_userNotExistsReturns404() {
		Mockito.doReturn(new ArrayList<Purchase>()).when(service).getRecentPurchasesForUser("nonExistantUser");
		
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/recent_purchases/nonExistantUser", String.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetRecentPurchases_illegalArgument() {
		Mockito.doReturn(new ArrayList<Purchase>()).when(service).getRecentPurchasesForUser("{nonExistantUser}");
		
		restTemplate.getForEntity("http://localhost:" + port + "/api/recent_purchases/{nonExistantUser}", String.class);
	}
	
	private void setBasicMockReturns() {
		Mockito.doReturn(recentPurchasesForUserReturn).when(service).getRecentPurchasesForUser(purchaseFixture.purchase1.getUsername());
		
		Mockito.doReturn(productFixture.product1).when(service).getProductById(purchaseFixture.purchase1.getId());
		Mockito.doReturn(productFixture.product2).when(service).getProductById(purchaseFixture.purchase2.getId());
		Mockito.doReturn(productFixture.product3).when(service).getProductById(purchaseFixture.purchase3.getId());
		
		Mockito.doReturn(purchasers).when(service).getPurchasersByProductId(productFixture.product1.getId());
		Mockito.doReturn(purchasers).when(service).getPurchasersByProductId(productFixture.product2.getId());
		Mockito.doReturn(purchasers).when(service).getPurchasersByProductId(productFixture.product3.getId());
	}
}
