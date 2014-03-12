package com.timkonieczny.yuome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogs {

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
}
