package com.mobilemerit.java;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class NoCameraPresentDialog {

	Activity activity;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	private static final String DIALOG_TITLE_TEXT="Caution!";
	private static final String NEGATIVE_BTN_TEXT="exit";
	private static final String DIALOG_MSG_TEXT="Your device do not have the Front Facing Camera" +
			"!";
	public NoCameraPresentDialog(Activity a){
		this.activity=a;
		builder=new AlertDialog.Builder(a);
	}
	public void showDialog(){
		builder.setTitle(DIALOG_TITLE_TEXT);
		builder.setMessage(DIALOG_MSG_TEXT);
		
		/*builder.setPositiveButton(POSITIVE_BTN_TEXT, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		*/
		builder.setNegativeButton(NEGATIVE_BTN_TEXT,new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		});
		dialog=builder.create();
		dialog.show();
	}
}
