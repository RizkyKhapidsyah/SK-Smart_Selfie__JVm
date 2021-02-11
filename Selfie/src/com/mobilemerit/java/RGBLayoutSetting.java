package com.mobilemerit.java;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.mobilemerit.my.inteface.OnRGBSettingListener;
import com.mobilemerit.selfie.R;

public class RGBLayoutSetting {

	private AlertDialog dialog; 
	private AlertDialog.Builder builder;
	private Activity activity;
	private LayoutInflater inflater;
	private SeekBar rSbar,gSbar,bSbar;
	private OnRGBSettingListener settingListner;
	private String EVENT;
	public RGBLayoutSetting(Activity a,String event) {
		this.activity=a;
		builder=new AlertDialog.Builder(a);
		inflater=LayoutInflater.from(a);
		this.EVENT=event;
		
	}
	public void showDialog(){
		builder.setTitle("Set Saturation Level!");
		View v=inflater.inflate(R.layout.rgb_setting_dialog,null,false);
		builder.setView(v);
		/**Findinng the seekbars from the view*/
		rSbar=(SeekBar)v.findViewById(R.id.seek_bar_red);
		gSbar=(SeekBar)v.findViewById(R.id.seek_bar_green);
		bSbar=(SeekBar)v.findViewById(R.id.seek_bar_blue);
		/**Setting initial progress of the seekbar*/
		//rSbar.setProgress(10);
		//gSbar.setProgress(10);
		//bSbar.setProgress(10);
		/**Arrenging the listners for the save setting*/
		builder.setPositiveButton("Apply",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				RGBSettingPOJO newSetting=new RGBSettingPOJO();
				newSetting.setRED(rSbar.getProgress());
				newSetting.setGREEN(gSbar.getProgress());
				newSetting.setBLUE(bSbar.getProgress());
				newSetting.setEvent(EVENT);
				settingListner=(OnRGBSettingListener)activity;
				settingListner.onSettingChange(newSetting);
			}
		});
		builder.setNegativeButton("Cancle",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog=builder.create();
		dialog.show();
	}
	 
}
