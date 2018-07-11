package com.metrosoft.arafat.salebook.products;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metrosoft.arafat.rxpad.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ListAdapter extends BaseAdapter
{
	Context context;
	List<products> valueList;
	ArrayList<String> quantities = new ArrayList<>();
	ArrayList<String> p_names;
	ArrayList<String> sl;
	ArrayList<String> p_ids;
	ArrayList<String> UOM;
	static HashSet<Integer> mProductSerialList;
	public static EditText edit_qnty,prod_price;
	Map<String,String> Qnty = new HashMap<String, String>();
	public ListAdapter(List<products> listValue, Context context)
	{
		this.context = context;
		this.valueList = listValue;
	}

	@Override
	public int getCount()
	{
		return this.valueList.size();
	}

	@Override
	public Object getItem(int position)
	{
		//return this.valueList.get(position);
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		return position;
	}
	private int lastFocussedPosition = -1;
	private Handler handler = new Handler();
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		try {
			final ViewItem viewItem;
			//final RecyclerView.ViewHolder holder;
			//	final ViewHolder holder;
			if (convertView == null) {
			//	holder = new ViewItem();
				viewItem = new ViewItem();

				LayoutInflater layoutInfiater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				//LayoutInflater layoutInfiater = LayoutInflater.from(context);
				convertView = layoutInfiater.inflate(R.layout.list_adapter_view, null);
				viewItem.prod_sl = (TextView) convertView.findViewById(R.id.prod_sl);
				viewItem.prod_code = (TextView) convertView.findViewById(R.id.prod_code);
				viewItem.txtTitle = (TextView) convertView.findViewById(R.id.adapter_text_title);
				viewItem.txtDescription = (TextView) convertView.findViewById(R.id.adapter_text_description);
                viewItem.prod_uom = (TextView) convertView.findViewById(R.id.prod_uom);
				viewItem.prod_qnty = (EditText) convertView.findViewById(R.id.prod_qnty);
				viewItem.prod_price=(EditText) convertView.findViewById(R.id.prod_price);
				edit_qnty= (EditText) convertView.findViewById(R.id.prod_qnty);
				convertView.setTag(viewItem);
			} else {
				//return convertView;
				viewItem = (ViewItem) convertView.getTag();
			}
			viewItem.prod_sl.setText(valueList.get(position).prod_sl);
			viewItem.prod_code.setText(valueList.get(position).prod_code);
			viewItem.txtTitle.setText(valueList.get(position).product_name);
			viewItem.txtDescription.setText(valueList.get(position).product_desc);
			viewItem.txtDescription.setText(valueList.get(position).product_desc);
			viewItem.prod_qnty.setText(valueList.get(position).prod_qnty);
			viewItem.prod_price.setText(valueList.get(position).prod_price);
			viewItem.prod_uom.setText(valueList.get(position).prod_uom);
		//	viewItem.prod_qnty.setText(valueList.get(position).prod_qnty);
			viewItem.prod_qnty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						EditText et =(EditText)v.findViewById(R.id.prod_qnty);
						//myList.get(parentPos).put(childPos,et.getText().toString().trim());
					}
				}
			});
			viewItem.ref = position;
			Qnty = new HashMap<>();
			Qnty.clear();
			return convertView;
		} catch (Exception e){
			Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return convertView;
	}



}

class ViewItem

{
	TextView prod_sl;
	TextView prod_code;
	TextView txtTitle;
	TextView txtDescription;
    TextView prod_uom;
    EditText prod_qnty,prod_price;
	TextWatcher qtyWatcher;
	TextWatcher priceWatcher;
	int ref;




}



