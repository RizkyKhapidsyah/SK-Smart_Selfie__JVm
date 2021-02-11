package com.mobilemerit.java;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mobilemerit.selfie.CustomCameraActivity;

public class LoadImageBitmap {


	public Bitmap displayFullImage(String imagePath) {
		FileInputStream is = null;
		BufferedInputStream bis = null;		
		Bitmap useThisBitmap = null;
		try {
			is = new FileInputStream(new File(imagePath));
			bis = new BufferedInputStream(is);		
			useThisBitmap=this.scaleImage(bis);
			// Display bitmap (useThisBitmap)
		} catch (Exception e) {
			// Try to recover
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
			}
		}
		return useThisBitmap;
	}

	
	public Bitmap scaleImage(BufferedInputStream in){		
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inJustDecodeBounds = false;
		Bitmap bmp=BitmapFactory.decodeStream(in,null,bitmapOptions);
		float imageWidth = bitmapOptions.outWidth;
		float imageHeight = bitmapOptions.outHeight;
		int displayWidth=CustomCameraActivity.display.getWidth();
		int finalWidth=0;		
		float finalHeight=0.0f;
		if(imageWidth>=displayWidth){
			finalWidth=displayWidth;
			finalHeight=((imageHeight/imageWidth)*finalWidth);
		}else if(imageWidth<displayWidth){
			finalWidth=(int)imageWidth;
			finalHeight=imageHeight;
		}
		
		Log.i("outheight",""+imageHeight);
		Log.i("outwidth",""+imageWidth);
		Log.i("displayWidth",""+displayWidth);
		Log.i("finalWidth",""+finalWidth);
		Log.i("ratio",""+(imageHeight/imageWidth));
		Log.i("calculatedheight",""+((imageHeight/imageWidth)*finalWidth));
		Log.i("finalHeight",""+finalHeight);
		
		Bitmap bitmap=Bitmap.createScaledBitmap(bmp,finalWidth,(int)finalHeight, true);
		
		return bitmap;
	}
}
