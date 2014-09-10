package com.timkonieczny.yuome;

public class SummaryRowItem {
    private String title;
    private String description, total;
    float debit, credit;
    int debitDrawableHeight, creditDrawableHeight;

    public int getDebitDrawableHeight() {
		return debitDrawableHeight;
	}

	public void setDebitDrawableHeight(int debitDrawableHeight) {
		this.debitDrawableHeight = debitDrawableHeight;
	}

	public int getCreditDrawableHeight() {
		return creditDrawableHeight;
	}

	public void setCreditDrawableHeight(int creditDrawableHeight) {
		this.creditDrawableHeight = creditDrawableHeight;
	}

	public SummaryRowItem(String title, String desc) {
        this.title = title;
        this.description = desc;
    }
    
    public String getDesc() {
        return description;	
    }
    
    public void setDesc(String desc) {
        this.description = desc;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public float getCredit(){
    	return credit;
    }
    
    public float getDebit(){
    	return debit;
    }
    
    public void setCredit(float credit){
    	this.credit=credit;
    }
    
    public void setDebit(float debit){
    	this.debit=debit;
    }
    
    public void setTotal(String total){
    	this.total=total;
    }
    
    public String getTotal(){
    	return total;
    }
    
    @Override
    public String toString() {
        return title + "\n" + description;
    }
}