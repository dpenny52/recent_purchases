package com.dpenny.models;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchasesResponse {
	
	private ArrayList<Purchase> purchases;
	
	PurchasesResponse() {
	}

	public ArrayList<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(ArrayList<Purchase> purchases) {
		this.purchases = purchases;
	}
}
