package com.ccdrive.peopledetail.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdrive.peopledetail.main.R;

public class TypeDetailsSubMenuAdapter extends BaseAdapter
{
  private Context context;
  private ArrayList<HashMap<String, String>>  array;
  private int selcted = -1;
  private View currentView;
  private String columns;
  public TypeDetailsSubMenuAdapter(Context paramContext, String columns,ArrayList<HashMap<String, String>>  paramArray)
  {
    this.context = paramContext;
    this.columns=columns;
    if (paramArray != null)
    {
      this.array = paramArray;
      return;
    }
    this.array = new ArrayList<HashMap<String, String>>();
  }

  public int getCount()
  {
    return this.array.size();
  }

  public HashMap<String , String> getItem(int position)
  {
    return this.array.get(position);
  }

  public long getItemId(int position)
  {
    return position;
  }

  public View getView(int position, View paramView, ViewGroup paramViewGroup)
  {

    paramView = LayoutInflater.from(this.context).inflate(R.layout.type_details_filter_item, null);
    paramView.setId(position);
    TextView localTextView = (TextView)paramView.findViewById(R.id.filter_name);
    ImageView localImageView = (ImageView)paramView.findViewById(R.id.filter_gou);
    if (this.selcted == position)
    {
      localImageView.setVisibility(0);
      paramView.setBackgroundResource(R.drawable.filter_sleted);
      currentView = paramView;
    }
    HashMap<String , String > map =array.get(position);
  	Set<Entry<String, String>> entrySet = map.entrySet();
  	Iterator<Entry<String, String>> iterator = entrySet.iterator();				
  	localTextView.setText(iterator.next().getKey());
    return paramView;
  }
  public String getColumns(){
	  return columns;
  }
public View getCurrentView(){
	return currentView;
}
  public void setSelctItem(int paramInt)
  {
    this.selcted = paramInt;
    notifyDataSetChanged();
  }
}