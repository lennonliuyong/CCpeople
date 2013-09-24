package com.ccdrive.peopledetail.adapter;

import com.ccdrive.peopledetail.main.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private ImageView imageView;
	private TextView textView , cateTextView;

	private View baseView;

	public ViewHolder(View baseView) {
		this.baseView = baseView;
	}

	public ImageView getImageView() {
		if (imageView == null) {
			imageView = (ImageView) baseView.findViewById(R.id.item_img);
		}
		return imageView;
	}

	public TextView getTextView() {
		if (textView == null) {
			textView = (TextView) baseView.findViewById(R.id.item_name);
		}
		return textView;
	}
	public TextView getCateTextView() {
		if (cateTextView==null) {
			cateTextView = (TextView) baseView.findViewById(R.id.item_categoryname);
		}
		return cateTextView;
	}
}
