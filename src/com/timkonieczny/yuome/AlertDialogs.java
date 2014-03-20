package com.timkonieczny.yuome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AlertDialogs {
	
	public Activity activity;
	public Editable[] article;
	
	public AlertDialogs(Activity activity){
		this.activity = activity;
		this.article = new Editable[3];
	}

	public static void showAlert(final Activity ParentActivity, final String title, final String message){
        ParentActivity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentActivity);
                builder.setTitle(title);
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
	public void newArticleDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		final AlertDialog alertDialog = alert.create();
		
		alertDialog.setTitle("Neuer Artikel");

        LayoutInflater factory = LayoutInflater.from(activity);            
        final View new_article = factory.inflate(R.layout.popup_new_article, null);

		// Set an EditText view to get user input 
		alertDialog.setView(new_article);

		Button new_article_button = (Button) new_article.findViewById(R.id.new_article_button);
		final EditText title_text = (EditText) new_article.findViewById(R.id.title);
		final EditText price_text = (EditText) new_article.findViewById(R.id.price);
		final EditText number_text = (EditText) new_article.findViewById(R.id.number);

		new_article_button.setOnClickListener(new OnClickListener() {
		public void onClick(View view) {
			article[0] = title_text.getText();
			article[1] = price_text.getText();
			article[2] = number_text.getText();
			ManualInputActivity.addArticle(article[0].toString(), article[1].toString(),activity);
			alertDialog.dismiss();
		  }
		});
		alertDialog.show();
	}
	public String[] getArticle(){
		String[] article_s = new String[3];
		for(int i = 0; i < article.length; i++){
			article_s[i] = article[i] == null ? "": article[i].toString();
		}
		return article_s;
	}
	public void deleteAllArticlesDialog(){
		activity.runOnUiThread(new Runnable() {
            public void run() {
				new AlertDialog.Builder(activity)
				.setTitle("Artikel löschen")
				.setMessage("Wirklich alle Artikel löschen?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	ManualInputActivity.deleteAllArticles(activity);
			    }})
           
			 .setNegativeButton(android.R.string.no, null).show();
           }
		});
	}
}
