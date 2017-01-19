package com.dpenny.test.fixtures;

import com.dpenny.models.Product;

public class ProductFixture {
	public Product product1, product2, product3;
	
	public ProductFixture() {
		
		product1 = new Product();
		
		product1.setFace("face1");
		product1.setId(1);
		product1.setPrice(10);
		product1.setSize(15);
		
		product2 = new Product();
		
		product2.setFace("face2");
		product2.setId(2);
		product2.setPrice(20);
		product2.setSize(25);
		
		product3 = new Product();
		product3.setFace("face3");
		product3.setId(3);
		product3.setPrice(30);
		product3.setSize(35);
	}
}
