package com.mobilemerit.java;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import com.mobilemerit.selfie.R;


public class MyAlertDialogs {
	
	private Activity c;
	private AlertDialog dialog;
	private String RATEPREFS="neverRate";
	int NEVER_RATE_TRUE=1;
	public MyAlertDialogs(Activity context) {	
		this.c=context;
	}
	public void getRateMyAppDialog(){
		SharedPreferences checkPrefs=c.getSharedPreferences(RATEPREFS, Context.MODE_PRIVATE);
		int checker=checkPrefs.getInt(RATEPREFS, -1);
		if(checker!=NEVER_RATE_TRUE){
			AlertDialog.Builder builder=new AlertDialog.Builder(c);
			builder.setMessage(c.getResources().getString(R.string.ratethisapp_msg));
			builder.setTitle(c.getResources().getString(R.string.ratethisapp_title));
			builder.setPositiveButton("Rate It",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent fire=new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id=com.mobilemerit.selfie"));
					c.startActivity(fire);
					c.finish();
				}
			});
			builder.setNegativeButton("Not Now",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					c.finish();
				}
			});
			builder.setNeutralButton("Never",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences prefs=c.getSharedPreferences(RATEPREFS,Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=prefs.edit();
					editor.putInt(RATEPREFS, NEVER_RATE_TRUE);
					editor.commit();
					c.finish();
				}
			});
			dialog=builder.create();
			dialog.show();
		}
	}

}
