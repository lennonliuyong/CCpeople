package com.ccdrive.peopledetail.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Toast toast;

	public static void showToastbyString(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, 100);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	public static void showToastbyId(Context context, int id) {
		if (toast == null) {
			toast = Toast.makeText(context, context.getResources()
					.getString(id), 100);
		} else {
			toast.setText(context.getResources().getString(id));
		}
		toast.show();
	}

}
