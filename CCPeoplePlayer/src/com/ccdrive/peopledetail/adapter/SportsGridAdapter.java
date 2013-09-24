package com.ccdrive.peopledetail.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ccdrive.peopledetail.bean.OpusBean;
import com.ccdrive.peopledetail.main.R;

public class SportsGridAdapter extends BaseAdapter {

	private Context sportsContext; 
	private List<OpusBean> sportsList;
	private AQuery aQuery;
	private ImageView sportsImage;
	private TextView myTextView;
	
	public SportsGridAdapter(Context sportsContext) {
		this.sportsContext = sportsContext; 
		if (sportsList!=null) {
			this.sportsList = sportsList;
		}
		sportsList = new ArrayList<OpusBean>();
		aQuery = new AQuery(sportsContext);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sportsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sportsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView==null) {
			convertView = LayoutInflater.from(sportsContext).inflate(R.layout.sports_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.myImageView = (ImageView) convertView.findViewById(R.id.sports_item_pic);
			viewHolder.myTextView = (TextView) convertView.findViewById(R.id.sports_item_name);
			viewHolder.myCategoryTextView = (TextView) convertView.findViewById(R.id.item_sportscategoryname);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		 OpusBean sportsOpus = (OpusBean) getItem(position);
		 String sportsPic = sportsOpus.getPic();
		 String sportsName = sportsOpus.getName();
		 String categoryName = sportsOpus.getCategory();
			if (categoryName.equals(" ”∆µ")) {
				viewHolder.myCategoryTextView.setTextColor(sportsContext.getResources().getColor(R.color.manhua));
			}else {
				viewHolder.myCategoryTextView.setTextColor(sportsContext.getResources().getColor(R.color.donghua));
			}
			if (categoryName.equals("”Œº«")) {
				viewHolder.myCategoryTextView.setTextColor(sportsContext.getResources().getColor(R.color.youji));
			}
			viewHolder.myCategoryTextView.setText("["+categoryName+"]");
		 
			 viewHolder.myImageView.setBackgroundResource(R.drawable.grid_item_default);
			 aQuery.id(convertView.findViewById(R.id.sports_item_pic)).image(sportsPic);
     		 viewHolder.myTextView.setText(sportsName);
		return convertView;
	}
	
	
	public void setSportsList(List<OpusBean> sportsList) {
		if (null!=sportsList) {
			this.sportsList = sportsList;
			return;
		}else {
		    this.sportsList = new ArrayList<OpusBean>();
		}
		notifyDataSetChanged();
		
	}
	
 static	class  ViewHolder {
		 ImageView myImageView;
		 TextView myTextView;
		 TextView myCategoryTextView;
	}

}
