package com.timkonieczny.yuome;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable{
    private String article;
    private String price;
    private String amount;

    // Constructor
    public Article(String article, String price, String amount){
        this.article = article;
        this.price = price;
        this.amount = amount;
   }
    public String getArticle(){
    	return article;
    }
    public String getPrice(){
    	return price;
    }
    public String getAmount(){
    	return amount;
    }

   // Parcelling part
   public Article(Parcel in){
       String[] data = new String[3];

       in.readStringArray(data);
       this.article = data[0];
       this.price = data[1];
       this.amount = data[2];
   }

   public int describeContents(){
       return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
       dest.writeStringArray(new String[] {this.article,
                                           this.price,this.amount,});
   }
   public static Parcelable.Creator CREATOR = new Parcelable.Creator() {
       public Article createFromParcel(Parcel in) {
           return new Article(in); 
       }

       public Article[] newArray(int size) {
           return new Article[size];
       }
   };


}

