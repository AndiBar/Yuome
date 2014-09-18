package com.timkonieczny.yuome;

public class Receipt {

	private String id, date, shop, owner, names, sum;
	private boolean isOwner;
	
	public Receipt(String id, String date, String shop, String owner, String names, String sum, boolean isOwner) {
		super();
		this.date = date;
		this.shop = shop;
		this.owner = owner;
		this.names = names;
		this.sum = sum;
		this.id = id;
		this.isOwner = isOwner;
	}
	
	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public String getId(){
		return id;
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
	
	public String getOwner() {
		return owner;
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
