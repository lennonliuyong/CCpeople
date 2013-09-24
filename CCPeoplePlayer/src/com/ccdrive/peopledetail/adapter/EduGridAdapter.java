package com.ccdrive.peopledetail.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ccdrive.peopledetail.bean.OpusBean;
import com.ccdrive.peopledetail.main.R;

public class EduGridAdapter extends BaseAdapter {
	private Context context;
	private List<OpusBean> array;
	private int selcted = -1;
	private AQuery aQuery;
	private View view;
	private TextView preTv;

	public EduGridAdapter(Context paramContext, List<OpusBean> paramArray) {
		this.context = paramContext;
		if (null != paramArray) {
			this.array = paramArray;
			aQuery = new AQuery(paramContext);
			return;
		}
		this.array = new ArrayList<OpusBean>();
		System.out.println("arraysize=====>" + array.size());
	}

	public int getCount() {
		return this.array.size();
	}

	public Object getItem(int position) {
		return this.array.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View paramView,
			ViewGroup paramViewGroup) {

		paramView = LayoutInflater.from(this.context).inflate(
				R.layout.edu_grid_item, null);
		final TextView localTextView = (TextView) paramView
				.findViewById(R.id.edu_item_esp);

		localTextView.setText(array.get(position).getName());
		localTextView.setTag(array.get(position));
		localTextView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					localTextView.setBackgroundResource(R.drawable.cheng);
				} else {
					localTextView.setBackgroundResource(R.drawable.lan);
				}
			}
		});

		return paramView;
	}

	public void setSelctItem(int paramInt) {
		this.selcted = paramInt;
		// notifyDataSetChanged();
	}

	public void setArrayList(ArrayList<OpusBean> list) {
		this.array = list;
		notifyDataSetChanged();
	}

}
