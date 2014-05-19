package com.timkonieczny.yuome;

public class Receipt {

	private String date, shop, names, sum;
	
	public Receipt(String date, String shop, String names, String sum) {
		super();
		this.date = date;
		this.shop = shop;
		this.names = names;
		this.sum = sum;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getShop() {
		return shop;
	}
	
	public void setShop(String shop) {
		this.shop = shop;
	}
	
	public String getNames() {
		return names;
	}
	
	public void setNames(String names) {
		this.names = names;
	}
	
	public String getSum() {
		return sum;
	}
	
	public void setSum(String sum) {
		this.sum = sum;
	}
	
	//TODO: Ort
}
