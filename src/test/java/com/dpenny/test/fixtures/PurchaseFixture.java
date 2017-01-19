package com.dpenny.test.fixtures;

import java.util.Calendar;

import com.dpenny.models.Purchase;

public class PurchaseFixture {
	public Purchase purchase1, purchase2, purchase3;
	
	public PurchaseFixture() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.YEAR, 1910);

		purchase1 = new Purchase();
		
		purchase1.setDate(calendar.getTime());
		purchase1.setId(1);
		purchase1.setProductId(1);
		purchase1.setUsername("user1");
		
		calendar.set(Calendar.YEAR, 1920);

		purchase2 = new Purchase();
		
		purchase2.setDate(calendar.getTime());
		purchase2.setId(2);
		purchase2.setProductId(2);
		purchase2.setUsername("user2");
		
		calendar.set(Calendar.YEAR, 1930);

		purchase3 = new Purchase();
		
		purchase3.setDate(calendar.getTime());
		purchase3.setId(3);
		purchase3.setProductId(3);
		purchase3.setUsername("user3");
	}

}
