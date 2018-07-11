package com.metrosoft.arafat.salebook.holder;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.metrosoft.arafat.rxpad.R;

import java.util.ArrayList;
import java.util.HashMap;
/*
import static com.metrosoft.arafat.salebook.holder.HashMapColumn.FIFTH_COLUMN;
import static com.metrosoft.arafat.salebook.holder.HashMapColumn.FOURTH_COLUMN;
import static com.metrosoft.arafat.salebook.holder.HashMapColumn.SECOND_COLUMN;
import static com.metrosoft.arafat.salebook.holder.HashMapColumn.SEVENTH_COLUMN;
import static com.metrosoft.arafat.salebook.holder.HashMapColumn.SIXTH_COLUMN;
import static com.metrosoft.arafat.salebook.holder.HashMapColumn.THIRD_COLUMN;*/

/**
 * Created by hp on 12/21/2017.
 */

// NOT USING from 9th January 2018

public class HashMapAdapter extends BaseAdapter {

  ArrayList<HashMapColumn> arrayproducts;
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    AlertDialog.Builder builder;
    public String[] Current;
    public HashMapAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
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
    public View getView(final int position,  View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

       LayoutInflater inflater=activity.getLayoutInflater();
        final ViewHolder holder;
        if(convertView == null){
         //   convertView=inflater.inflate(R.layout.order_items_row, parent,false);
           convertView=inflater.inflate(R.layout.order_items_row, null);
            holder = new ViewHolder();
            holder.txtFirst    =(TextView) convertView.findViewById(R.id.pid);
            holder.txtSecond   =(TextView) convertView.findViewById(R.id.name);
            holder.txtThird    =(TextView) convertView.findViewById(R.id.uom);
            holder.txtFourth   =(EditText) convertView.findViewById(R.id.qnty);
            holder.txtFifth    =(EditText) convertView.findViewById(R.id.price);
            holder.txtSixth    =(TextView) convertView.findViewById(R.id.p_code);
            holder.txtSeventh  =(TextView) convertView.findViewById(R.id.p_desc);


            convertView.setTag(holder);


        }
         else{
            holder = (ViewHolder) convertView.getTag();
        }

          final HashMap<String, String> map=list.get(position);

       // txtFirst.setText(map.get(FIRST_COLUMN));
        final int pos= position;




        holder.txtFirst.setText(""+pos);
        holder.txtSecond.setText(list.get(position).get("PROD_NAME"));
        holder.txtThird.setText(list.get(position).get("UOM"));
        holder.txtSixth.setText(list.get(position).get("PROD_CODE"));
        holder.txtSeventh.setText(list.get(position).get("PROD_DESC"));
        //qnty and price
     //   holder.txtFourth.setText(map.get(FOURTH_COLUMN));
//QNTY change--------------------------------------------------------------------------------------

       // dataSet=map;
        String qnty= list.get(position).get("QNTY");
        try {
            if (qnty.length() > 0 && !qnty.equalsIgnoreCase("0")) {
                holder.txtFourth.setText(qnty);
            } else {
                holder.txtFourth.setText("");
            }
        } catch (Exception e) {
            holder.txtFourth.setText("");
        }




        // holder.txtFourth.setText(list.get(position).get(FOURTH_COLUMN));
//Price Change--------------------------------------------------------------------------------------
      //  holder.txtFifth.setText(map.get(FIFTH_COLUMN));
        String price= list.get(position).get("PROD_PRICE");
        try {
            if (price.length() > 0 && !price.equalsIgnoreCase("0")) {
                holder.txtFifth.setText(price);
            } else {
                holder.txtFifth.setText("");
            }
        } catch (Exception e) {
            holder.txtFifth.setText("");
        }



        return convertView;
    }

    static class ViewHolder {
        TextView  txtFirst,
        txtSecond,
        txtThird,

        txtSixth,
        txtSeventh;
        EditText txtFourth,
        txtFifth;
        int ref;
        EditText prod_qnty,prod_price;
        TextWatcher qtyWatcher;
        TextWatcher priceWatcher;

    }

}