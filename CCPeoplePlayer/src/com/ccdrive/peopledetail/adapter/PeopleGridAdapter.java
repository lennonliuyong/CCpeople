package com.ccdrive.peopledetail.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ccdrive.peopledetail.bean.PeopleBean;
import com.ccdrive.peopledetail.ceche.GridItemCache;
import com.ccdrive.peopledetail.main.R;
import com.ccdrive.peopledetail.util.ImageAsyncLoader;
import com.ccdrive.peopledetail.util.ImageAsyncLoader.ImageCallback;

public class PeopleGridAdapter extends BaseAdapter {

	private Context context;
	private ImageAsyncLoader loader;
	private List<PeopleBean> list;
	private GridView gridView;
	private AQuery aQuery;
	private int mcount=0;

	public PeopleGridAdapter(Context context, List<PeopleBean> newList,
			GridView gridView) {
		this.context = context;
		setPeopleList(newList);
		this.gridView = gridView;
		loader = new ImageAsyncLoader();
		aQuery = new AQuery(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public PeopleBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridItemCache gridItemCache;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.people_item, null);
			gridItemCache = new GridItemCache(convertView);
			convertView.setTag(gridItemCache);
		} else {
			gridItemCache = (GridItemCache) convertView.getTag();
		}
		PeopleBean peopleBean = getItem(position);
		String imageURL = peopleBean.getPic().trim();
//		System.out.println("下载的图片的地址为===>"+position+"====="+imageURL);
		TextView textView = gridItemCache.getTextView();
		textView.setText(peopleBean.getChineseName());
		ImageView imageView = gridItemCache.getImageView();
		convertView.findViewById(R.id.item_img).setBackgroundResource(
				R.drawable.grid_item_default);
		if (imageURL==null||imageURL.equals("")) {
			aQuery.id(R.id.down_progressBar).visibility(View.VISIBLE);
		}
		aQuery.id(convertView.findViewById(R.id.item_img)).image(imageURL);
//		 imageView.setTag(imageURL);
//		 Bitmap drawable=loader.loadDrawable(imageURL, new ImageCallback(){
//		
//		 @Override
//		 public void imgeLoader(Bitmap draw, String imgeURL) {
//		 ImageView imageTag=(ImageView)gridView.findViewWithTag(imgeURL);
//		 if(imageTag!=null&&draw!=null){
//		 imageTag.setImageBitmap(draw);
//		 }
//		 }
//		
//		 });
//		 if(drawable==null){
//		 imageView.setImageResource(R.drawable.grid_item_default);
//		 }else{
//		 imageView.setImageBitmap(drawable);
//		 }
		
		if (position==0) {
			mcount ++;
		}else {
			mcount=0;
		}
		if (mcount>1) {
		return convertView;
		}
		return convertView;
	}
	public void changData(ArrayList<PeopleBean> newList) {
		setPeopleList(newList);
		notifyDataSetChanged();
	}
	public void setPeopleList(List<PeopleBean> newList) {
		if (newList != null) {
			this.list = newList;
			return;
		}
		this.list = new ArrayList<PeopleBean>();
	}
}
