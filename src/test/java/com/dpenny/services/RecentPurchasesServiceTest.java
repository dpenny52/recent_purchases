package com.dpenny.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.dpenny.models.Product;
import com.dpenny.models.Purchase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecentPurchasesServiceTest {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Environment env;
	
	@Autowired
	RecentPurchasesService service;
	
	private MockRestServiceServer mockServer;
	
	private String externalAPIUrl;
	
	@Before
	public void setUp() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
		
		externalAPIUrl = env.getProperty("externalAPIUrl");
	}
	
	@Test
	public void testGetRecentPurchasesForUser() {
		mockServer.expect(requestTo(externalAPIUrl + "/purchases/by_user/Jarret_Schumm?limit=5"))
		.andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess().body("{\"purchases\":[{\"id\":732631,\"username\":\"Jarret_Schumm\",\"productId\":655062,\"date\":\"2016-11-13T06:03:02.150Z\"},{\"id\":993200,\"username\":\"Jarret_Schumm\",\"productId\":546179,\"date\":\"2016-11-08T07:22:23.154Z\"},{\"id\":4025,\"username\":\"Jarret_Schumm\",\"productId\":759415,\"date\":\"2016-11-16T02:25:00.155Z\"}]}")
				.contentType(MediaType.APPLICATION_JSON));
		
		List<Integer> expectedProductIds = new ArrayList<Integer>();
		expectedProductIds.add(655062);
		expectedProductIds.add(546179);
		expectedProductIds.add(759415);
		
		ArrayList<Purchase> purchases = new ArrayList<Purchase>(service.getRecentPurchasesForUser("Jarret_Schumm"));
		
		for(Integer i : expectedProductIds) {
			boolean test = false;
			for(Purchase p : purchases) {
				if(p.getProductId() == i) {
					test = true;
				}
			}
			assertTrue(test);
		}
	}
	
	@Test
	public void testGetRecentPurchasesForUser_userNotExists() {
		mockServer.expect(requestTo(externalAPIUrl + "/purchases/by_user/thisUserDoesNotExist?limit=5"))
		.andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess().body("{\"purchases\":[]}")
				.contentType(MediaType.APPLICATION_JSON));
		
		ArrayList<Purchase> purchases = new ArrayList<Purchase>(service.getRecentPurchasesForUser("thisUserDoesNotExist"));
		
		assertTrue(purchases.isEmpty());
	}
	
	@Test
	public void testGetProductById() {
		mockServer.expect(requestTo(externalAPIUrl + "/products/655062"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body("{\"product\":{\"id\":655062,\"face\":\"ヽ༼ຈل͜ຈ༽ง\",\"price\":540,\"size\":17}}")
					.contentType(MediaType.APPLICATION_JSON));
		
		Product product = service.getProductById(655062);
		
		assertEquals(product.getFace(), "ヽ༼ຈل͜ຈ༽ง");
		assertEquals(product.getId(), 655062);
		assertEquals(product.getPrice(), 540);
		assertEquals(product.getSize(), 17);
		assertNull(product.getRecent());
	}
	
	@Test
	public void testGetProductById_productNotExists() {
		mockServer.expect(requestTo(externalAPIUrl + "/products/-1"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body("{}")
					.contentType(MediaType.APPLICATION_JSON));
		
		Product product = service.getProductById(-1);
		
		assertNull(product);
	}
	
	@Test
	public void testGetPurchasersForProduct() {
		mockServer.expect(requestTo(externalAPIUrl + "/purchases/by_product/" + 655062))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body("{\"purchases\":[{\"id\":732631,\"username\":\"Jarret_Schumm\",\"productId\":655062,\"date\":\"2016-11-13T06:03:02.150Z\"},{\"id\":103990,\"username\":\"Hershel.Anderson90\",\"productId\":655062,\"date\":\"2016-11-13T09:31:12.156Z\"},{\"id\":597951,\"username\":\"Nat.Koch90\",\"productId\":655062,\"date\":\"2016-11-14T10:47:48.158Z\"}]}")
					.contentType(MediaType.APPLICATION_JSON));
		
		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add("Jarret_Schumm");
		expectedResult.add("Hershel.Anderson90");
		expectedResult.add("Nat.Koch90");
		
		List<String> purchasers = service.getPurchasersByProductId(655062);
		
		for(String name : expectedResult) {
			assertTrue(purchasers.contains(name));
		}
	}
	
	@Test
	public void testGetPurchasersForProduct_productNotExists() {
		mockServer.expect(requestTo(externalAPIUrl + "/purchases/by_product/" + -1))
		.andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess().body("{\"purchases\":[]}")
				.contentType(MediaType.APPLICATION_JSON));
		
		List<String> purchasers = service.getPurchasersByProductId(-1);
		
		assertTrue(purchasers.isEmpty());
	}
}
