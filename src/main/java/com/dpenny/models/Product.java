package com.dpenny.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
	
	private int id;
	private String face;
	private int price;
	private int size;
	private List<String> recent;
	
	public Product() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<String> getRecent() {
		return recent;
	}

	public void setRecent(List<String> recent) {
		this.recent = recent;
	}
}
