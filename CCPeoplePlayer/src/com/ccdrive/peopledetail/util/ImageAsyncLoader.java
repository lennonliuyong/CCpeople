package com.ccdrive.peopledetail.util;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ImageAsyncLoader {
	private HashMap<String, SoftReference<Bitmap>> imgeCache;

	public ImageAsyncLoader() {
		imgeCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Bitmap loadDrawable(final String imageURL,
			final ImageCallback imageCallback) {
		if (imgeCache.containsKey(imageURL)) {
			SoftReference<Bitmap> softReference = imgeCache.get(imageURL);
			Bitmap drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				imageCallback.imgeLoader((Bitmap) msg.obj, imageURL);
			}

		};

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();

				Bitmap drawable = getImage(imageURL);
				imgeCache.put(imageURL, new SoftReference<Bitmap>(drawable));
				Message msg = handler.obtainMessage(0, drawable);
				handler.sendMessage(msg);
			}

		}.start();

		return null;
	}

	public Bitmap getImage(String urlPath) {
		System.out.println("logo的圖片為" + urlPath);
		URL url;
		Bitmap map = null;
		BitmapFactory.Options options;

		try {
			url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			conn.setRequestMethod("GET");
			conn.connect();
			if (conn.getResponseCode() == 200) {
				options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 1; // width，hight设为原来的十分一
				map = BitmapFactory.decodeStream(conn.getInputStream(), null,
						options);
				return map;
			}
		} catch (MalformedURLException e) {
			Log.e("ur-->", "URL不正确");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public interface ImageCallback {
		public void imgeLoader(Bitmap draw, String imgeURL);
	}

}
