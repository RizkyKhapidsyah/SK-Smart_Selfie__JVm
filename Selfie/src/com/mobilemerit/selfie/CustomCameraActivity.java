package com.mobilemerit.selfie;

import java.io.File;
import com.mobilemerit.selfie.R;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.mobilemerit.java.CameraPreview;
import com.mobilemerit.java.MyAlertDialogs;
import com.mobilemerit.java.NoCameraPresentDialog;

public class CustomCameraActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mCameraPreview;
	public static Display display;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();

		mCamera = this.getCameraInstance();
		if (mCamera == null) {
			NoCameraPresentDialog noCamera = new NoCameraPresentDialog(
					CustomCameraActivity.this);
			noCamera.showDialog();
		} else {
			mCameraPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mCameraPreview);

			ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
			captureButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mCamera.takePicture(null, null, mPicture);
					// mCamera.startPreview();
				}
			});
		}
	}

	private Camera getCameraInstance() {
		Camera camera = null;
		Log.d("No of cameras", Camera.getNumberOfCameras() + "");
		for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
			CameraInfo camInfo = new CameraInfo();
			Camera.getCameraInfo(camNo, camInfo);
			if (camInfo.facing == (Camera.CameraInfo.CAMERA_FACING_FRONT)) {
				camera = Camera.open(camNo);
			}
		}
		return camera;
	}

	PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				return;
			}
			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				notifyImageCapture(pictureFile.getPath());
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}
		}
	};

	public void notifyImageCapture(String filepath) {
		Intent i = new Intent(this, ImageEditActivity.class);
		i.putExtra("path", "" + filepath);
		startActivityForResult(i, 100);
	}

	public static File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), "SmartSelfie");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("SmartSelfie", "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		Log.i("Mediapath", "" + mediaFile.getPath());
		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 100) {
			this.finish();
			startActivity(new Intent(this, CustomCameraActivity.class));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	 if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			MyAlertDialogs dialogs = new MyAlertDialogs(CustomCameraActivity.this);
			dialogs.getRateMyAppDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
}