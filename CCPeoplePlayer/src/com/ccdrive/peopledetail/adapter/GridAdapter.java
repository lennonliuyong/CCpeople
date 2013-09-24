package com.ccdrive.peopledetail.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.peopledetail.bean.OpusBean;
import com.ccdrive.peopledetail.http.HttpRequest;
import com.ccdrive.peopledetail.main.R;
import com.ccdrive.peopledetail.util.ImageAsyncLoader;

public class GridAdapter extends BaseAdapter {

	private List<OpusBean> list;
	private Context context;
	private ImageAsyncLoader loader;
	private GridView gridView;
	private AQuery aQuery;
	private HttpRequest httpRequest;

	public GridAdapter(Context context, List<OpusBean> newList,
			GridView gridView) {
		this.context = context;
		this.gridView = gridView;
		setMoviesList(newList);
		// loader = new ImageAsyncLoader();
		aQuery = new AQuery(context);
		httpRequest = HttpRequest.getInstance();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewhoder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_item, null);
			viewhoder = new ViewHolder(convertView);
			convertView.setTag(viewhoder);
		} else {
			viewhoder = (ViewHolder) convertView.getTag();
		}
		OpusBean opusBean = (OpusBean) getItem(position);
		ImageView imageView = viewhoder.getImageView();
		TextView textView = viewhoder.getTextView();
		
		if (httpRequest.getType().equals("23")) { //¶¯ÂþÆµµÀ
		TextView cateTextView = viewhoder.getCateTextView();
		cateTextView.setVisibility(View.VISIBLE);
		String category = opusBean.getCategory();
		if (category.equals("Âþ»­")) {
			cateTextView.setTextColor(context.getResources().getColor(R.color.manhua));
		}else {
			cateTextView.setTextColor(context.getResources().getColor(R.color.donghua));
		}
		cateTextView.setText("["+category+"]");
		}
		textView.setText(opusBean.getName());
		
		String pic = opusBean.getPic();
		// aQuery.find(R.id.item_imgg).image(pic);
		convertView.findViewById(R.id.item_imgg).setBackgroundResource(
				R.drawable.grid_item_default);
		aQuery.id(convertView.findViewById(R.id.item_imgg)).animate(R.anim.logoanmi).image(pic);
		// Bitmap drawable = loader.loadDrawable(pic, new ImageCallback() {
		//
		// @Override
		// public void imgeLoader(Bitmap draw, String imgeURL) {un
		// ImageView imageTag = (ImageView) gridView
		// .findViewWithTag(imgeURL);
		// if (imageTag != null && draw != null) {
		// imageTag.setImageBitmap(draw);
		// }
		// }
		// });
		// downloadImage(pic,imageView);
		// if (drawable == null) {
		// imageView.setImageResource(R.drawable.grid_item_default);
		// } else {
		// imageView.setImageBitmap(drawable);
		// }
		return convertView;
	}

	public void changData(ArrayList<OpusBean> newList) {
		setMoviesList(newList);
		notifyDataSetChanged();
	}

	public void setMoviesList(List<OpusBean> newList) {
		if (newList != null) {
			this.list = newList;
			return;
		}
		this.list = new ArrayList();
	}

	private void downloadImage(String url, final ImageView iamgeView) {
		aQuery.ajax(url, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				super.callback(url, object, status);
				if (null != object || !"".equals(object)) {
					aQuery.find(R.id.item_img).image(object);
				}
				iamgeView.setImageResource(R.drawable.grid_item_default);
			}
		});
	}
}
