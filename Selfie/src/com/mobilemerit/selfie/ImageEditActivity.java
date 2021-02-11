package com.mobilemerit.selfie;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;
import com.mobilemerit.java.CameraPreview;
import com.mobilemerit.java.ImageEffects;
import com.mobilemerit.java.LoadImageBitmap;
import com.mobilemerit.java.RGBLayoutSetting;
import com.mobilemerit.java.RGBSettingPOJO;
import com.mobilemerit.my.inteface.OnRGBSettingListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class ImageEditActivity extends FragmentActivity implements
		OnRGBSettingListener, OnClickListener {

	private ImageView imageToEdit;
	private Bitmap orignalBitmap;
	private String CLICKED_IMAGE_FILE_PATH;
	private static final String EVENT_GAMA = "gama";
	private static final String EVENT_FILTER = "filter";
	private static final String EVENT_SEPIA = "sepia";
	private static final String EVENT_GREY = "grey";
	private ImageButton btn_gama, btn_filter, btn_grey, btn_sepia, btn_save,
			btn_undo;
	private String SAVED_MEDIA_PATH = null;
	private Bitmap EFFECTED_BITMAP = null;
	private AdView adView;
	private InterstitialAd interstitial;
	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.image_edit_activity);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionBarColor));
		
		/** Findinng the view elements */
		imageToEdit = (ImageView) this.findViewById(R.id.item_click_image);
		btn_gama = (ImageButton) this.findViewById(R.id.btn_gama);
		btn_filter = (ImageButton) this.findViewById(R.id.btn_filter);
		btn_grey = (ImageButton) this.findViewById(R.id.btn_grey);
		btn_sepia = (ImageButton) this.findViewById(R.id.btn_sepia);
		btn_save = (ImageButton) this.findViewById(R.id.btn_save);
		btn_undo = (ImageButton) this.findViewById(R.id.btn_undo);

		btn_gama.setOnClickListener(this);
		btn_filter.setOnClickListener(this);
		btn_grey.setOnClickListener(this);
		btn_sepia.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_undo.setOnClickListener(this);

		CLICKED_IMAGE_FILE_PATH = getIntent().getStringExtra("path");
		// Toast.makeText(getBaseContext(),
		// ""+CLICKED_IMAGE_FILE_PATH,Toast.LENGTH_SHORT).show();
		this.setUpImage(CLICKED_IMAGE_FILE_PATH);
		
		/**Admob Code*/
		adView=(AdView)this.findViewById(R.id.ad1);
		AdRequest request=new AdRequest();
		request.setTesting(true);
		request.addTestDevice(AdRequest.TEST_EMULATOR);
		adView.loadAd(request);    
		interstitial = new InterstitialAd(ImageEditActivity.this, getResources().getString(R.string.admob_id));
	    interstitial.loadAd(request);
	    interstitial.setAdListener(new AdListener() {
			
			@Override
			public void onReceiveAd(Ad arg0) {
				// TODO Auto-generated method stub
				if(interstitial.isReady())
					interstitial.show();
			}
			
			@Override
			public void onPresentScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLeaveApplication(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDismissScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	public void setUpImage(String path) {
		LoadImageBitmap imageLoader = new LoadImageBitmap();
		orignalBitmap = imageLoader.displayFullImage(path);
		if(CameraPreview.isPortrait){
			orignalBitmap = ImageEffects.rotate(imageLoader.displayFullImage(path));
		}else{
			orignalBitmap=imageLoader.displayFullImage(path);
		}
		imageToEdit.setImageBitmap(orignalBitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.welcome, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.share:
			this.actionMenu("share");
			break;
		case R.id.setWallpaper:
			this.actionMenu("wallapaer");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void actionMenu(String action) {
		if (action.equals("share")) {
			if (SAVED_MEDIA_PATH != null) {
				shareImage(SAVED_MEDIA_PATH);
			} else {
				shareImage(CLICKED_IMAGE_FILE_PATH);
			}
		} else {
			if (SAVED_MEDIA_PATH != null) {
				createSetAsIntent(SAVED_MEDIA_PATH);
			} else {
				createSetAsIntent(CLICKED_IMAGE_FILE_PATH);
			}
		}
	}

	@Override
	public void onSettingChange(RGBSettingPOJO newSetting) {
		// TODO Auto-generated method stub
		if (newSetting.getEvent().equals(ImageEditActivity.EVENT_GREY)) {
			// CURRENT_ACTIVE_EFFECT=EVENT_GREY;
			EFFECTED_BITMAP = ImageEffects.doGreyscale(orignalBitmap);
			imageToEdit.setImageBitmap(EFFECTED_BITMAP);

		} else if (newSetting.getEvent().equals(ImageEditActivity.EVENT_FILTER)) {
			// CURRENT_ACTIVE_EFFECT=EVENT_FILTER;
			EFFECTED_BITMAP = ImageEffects.doColorFilter(orignalBitmap,
					newSetting.getRED(), newSetting.getGREEN(),
					newSetting.getBLUE());
			imageToEdit.setImageBitmap(EFFECTED_BITMAP);

		} else if (newSetting.getEvent().equals(ImageEditActivity.EVENT_SEPIA)) {
			// CURRENT_ACTIVE_EFFECT=EVENT_SEPIA;
			EFFECTED_BITMAP = ImageEffects.createSepiaToningEffect(
					orignalBitmap, 10, newSetting.getRED(),
					newSetting.getGREEN(), newSetting.getBLUE());
			imageToEdit.setImageBitmap(EFFECTED_BITMAP);
		} else if (newSetting.getEvent().equals(ImageEditActivity.EVENT_GAMA)) {
			// CURRENT_ACTIVE_EFFECT=EVENT_GAMA;
			EFFECTED_BITMAP = ImageEffects.doGamma(orignalBitmap,
					newSetting.getRED(), newSetting.getGREEN(),
					newSetting.getBLUE());
			imageToEdit.setImageBitmap(EFFECTED_BITMAP);
		}
		// debug code
		Log.i("RED", "" + newSetting.getGREEN());
		Log.i("GREEN", "" + newSetting.getGREEN());
		Log.i("BLUE", "" + newSetting.getBLUE());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_grey:
			this.setEffect(EVENT_GREY);
			break;
		case R.id.btn_gama:
			this.setEffect(EVENT_GAMA);
			break;
		case R.id.btn_sepia:
			this.setEffect(EVENT_SEPIA);
			break;
		case R.id.btn_filter:
			this.setEffect(EVENT_FILTER);
			break;
		case R.id.btn_save:
			new saveEditedImage().execute("");
			break;
		case R.id.btn_undo:
			imageToEdit.setImageBitmap(orignalBitmap);
			break;
		default:
			break;
		}
	}

	public void setEffect(String effect) {
		if (effect.equals(EVENT_GREY)) {
			/** Overite the EFFECTED_BITMAP to the effect */
			EFFECTED_BITMAP = ImageEffects.doGreyscale(orignalBitmap);
			imageToEdit.setImageBitmap(EFFECTED_BITMAP);
		} else if (effect.equals(EVENT_FILTER)) {
			EFFECTED_BITMAP = ImageEffects.applySnowEffect(orignalBitmap);
			imageToEdit.setImageBitmap(EFFECTED_BITMAP);
		} else {
			RGBLayoutSetting setting = new RGBLayoutSetting(
					ImageEditActivity.this, effect);
			setting.showDialog();
		}

	}

	class saveEditedImage extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			if (EFFECTED_BITMAP == null) {
				return null;
			} else {
				FileOutputStream out = null;
				try {
					SAVED_MEDIA_PATH = CustomCameraActivity
							.getOutputMediaFile().getPath();
					out = new FileOutputStream(new File(SAVED_MEDIA_PATH));
					EFFECTED_BITMAP.compress(Bitmap.CompressFormat.JPEG, 90,
							out);
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {

				}
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Toast.makeText(getBaseContext(), "Photo Saved...",
						Toast.LENGTH_SHORT).show();
				orignalBitmap = null;
				orignalBitmap = EFFECTED_BITMAP;
				// EFFECTED_BITMAP=null;
				imageToEdit.setImageBitmap(new LoadImageBitmap()
						.displayFullImage(SAVED_MEDIA_PATH));
			} else {
				Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT)
						.show();
			}
			super.onPostExecute(result);
		}
	}

	public void createSetAsIntent(String image) {
		// TODO
		Uri u = Uri.parse("file://" + image);
		Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
		intent.setDataAndType(u, "image/png");
		intent.putExtra("mimeType", "image/png");
		startActivity(intent);
	}

	public void shareImage(String path) {
		// TODO
		//Toast.makeText(getBaseContext(), "" + path, Toast.LENGTH_SHORT).show();
		Uri imageUri = Uri.parse("file://" + path);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/png");
		intent.putExtra(Intent.EXTRA_STREAM, imageUri);
		startActivity(Intent.createChooser(intent, "Share"));
	}
}
