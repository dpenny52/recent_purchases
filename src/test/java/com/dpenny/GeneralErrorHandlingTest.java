package com.dpenny;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class GeneralErrorHandlingTest {
	
	@LocalServerPort
	int port;
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Test
	public void testInvalidUriReturns400() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/asdfasdfadsf", String.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
}
