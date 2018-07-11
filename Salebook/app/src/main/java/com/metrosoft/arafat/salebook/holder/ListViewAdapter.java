package com.metrosoft.arafat.salebook.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.products.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arafat on 12/13/2017.
 */
public class ListViewAdapter extends ArrayAdapter<products> {

    Context mContext;
    View v;
    private String[] arrTemp;
    LayoutInflater inflater;
    EditText ProdDiscount,ProgPrice;

    public   String[] scoresToUpdate=new String[1000];
    //public String Array scoresToUpdate =scoresToUpdate[];
    public static EditText edit_qnty,prod_price;
    public static HashMap<Integer,String> myList=new HashMap<Integer,String>();
    public List<products> parkingList;
    ArrayList<products> arrayproducts;
    ArrayList<products>tempproductsarray;

    public ListViewAdapter(Context context, int resource, ArrayList<products> apps) {
        super(context, resource);
        //this.mContext = context;
      //  this.arrayproducts = arrayproducts;
       // this.parkingList.addAll(arrayproducts);

        this.parkingList = apps;
        this.mContext = context;
        arrayproducts = new ArrayList<products>();
        tempproductsarray= new ArrayList<products>();
        arrayproducts.addAll(parkingList);
        tempproductsarray.addAll(parkingList);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public Context context;
    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        try {

            final int pos=position;
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_adapter_view, null);
                holder = new ViewHolder();
                holder.prod_sl = (TextView) convertView.findViewById(R.id.prod_sl);
                holder.prod_code = (TextView) convertView.findViewById(R.id.prod_code);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.adapter_text_title);
                holder.txtDescription = (TextView) convertView.findViewById(R.id.adapter_text_description);
                holder.prod_uom = (TextView) convertView.findViewById(R.id.prod_uom);
                holder.prod_qnty = (EditText) convertView.findViewById(R.id.prod_qnty);
                holder.prod_price = (EditText) convertView.findViewById(R.id.prod_price);
                holder.prodDiscount=(EditText)convertView.findViewById(R.id.prodDis);
                holder.netPrice=(EditText)convertView.findViewById(R.id.netPrice);
                holder.progPrcie=(EditText)convertView.findViewById(R.id.progPrcie);
                holder.totalPrice=(EditText)convertView.findViewById(R.id.totalPrice);

                // edit_qnty = (EditText) convertView.findViewById(R.id.prod_qnty);
              //  setProdQnty(holder,position);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                //  holder.prod_qnty.setText(scoresToUpdate[pos]);
            }
            holder.ref = position;

            products prod = arrayproducts.get(position);
            holder.prod_sl.setText("" + position);
            holder.prod_code.setText(prod.getCode());
            //holder.txtTitle.setText(prod.getName());
            holder.txtDescription.setText(prod.getName());
            holder.prod_uom.setText(prod.getUom());
            //holder.prod_uom.setText("UOM");
           // holder.prod_qnty.setText(arrayproducts.get(position).getQnty());
            holder.prod_price.setText(prod.getPrice());
            Log.e("row values",""+position+"\t"+prod.getUom()+"\t-"+prod.getCode()+""+prod.getName()+""+prod.getDesc()+""+prod.getUom());


//-----------try1---------------------------------------------------------------------------------
       //Qnty

            if (holder.qtyWatcher != null) {
                holder.prod_qnty.removeTextChangedListener(holder.qtyWatcher);
            }

            // Create the TextWatcher corresponding to this row
            holder.qtyWatcher = new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().equals("")||s.toString().equals("0")){
                      //  arrayproducts.get(position).setQnty(" ");
                        holder.prod_qnty.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    }
                    else{
                        arrayproducts.get(position).setQnty(s.toString());

                    }
                    if(Integer.parseInt("0"+s.toString().trim())>0){
                    //    openDiscount(""+arrayproducts.get(position).getCode(),position,arrayproducts.get(position).getQnty(),arrayproducts.get(position).getPrice());
                        holder.prod_qnty.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_pink));
                        holder.prod_qnty.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    }

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            };

            holder.prod_qnty.addTextChangedListener(holder.qtyWatcher);

            products ali = arrayproducts.get(position);
            holder.prod_qnty.setText(ali.getQnty());



            holder.prod_qnty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    try {
                        if (hasFocus) {
                            //  Toast.makeText(mContext, "Got the focus", Toast.LENGTH_LONG).show();
                            products netprice = arrayproducts.get(position);
                            holder.netPrice.setText(netprice.getNet());
                            products totalprice = arrayproducts.get(position);
                            holder.totalPrice.setText(totalprice.getTotal_price());
                        } else {
                            // Toast.makeText(mContext, "Lost the focus", Toast.LENGTH_LONG).show();
                            products netprice = arrayproducts.get(position);
                            holder.netPrice.setText(netprice.getNet());
                            products totalprice = arrayproducts.get(position);
                            holder.totalPrice.setText(totalprice.getTotal_price());
                        }
                    }
                    catch (Exception e){
                    Log.e("prod_qnty","Focus Change error"+e.toString());
                    }
                }
            });

//Discount
            if (holder.discWatcher != null) {
                holder.prodDiscount.removeTextChangedListener(holder.discWatcher);
            }

            // Create the TextWatcher corresponding to this row
            holder.discWatcher = new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().equals("")||s.toString().equals("0")){
                        //arrayproducts.get(position).setDiscont(" ");
                    }
                    else{
                        arrayproducts.get(position).setDiscont(s.toString());
                    }
                    if(Integer.parseInt("0"+s.toString().trim())>0){

                        //    openDiscount(""+arrayproducts.get(position).getCode(),position,arrayproducts.get(position).getQnty(),arrayproducts.get(position).getPrice());
                        //net price
                        int discount=Integer.parseInt(s.toString().trim());
                        String stgross_price=arrayproducts.get(position).getPrice().toString();
                        float gross_price =Float.parseFloat(stgross_price);
                        float tempnet_price = gross_price-((float)discount*(float)gross_price)/100;
                        float net_price = (float) tempnet_price;
                        arrayproducts.get(position).setNet(""+net_price);

                        //total price
                        float qnty;
                        String stqnty=arrayproducts.get(position).getQnty().toString();
                        if (!stqnty.isEmpty()) {
                             qnty = Integer.parseInt(stqnty);
                        }
                        else{
                             qnty=0.0f;
                        }
                        float temptoalprice= (float)qnty*tempnet_price;
                        float total_price=(float) temptoalprice;
                        arrayproducts.get(position).setTotal_price(""+total_price);
                    }

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            };

            holder.prodDiscount.addTextChangedListener(holder.discWatcher);

            products discprod = arrayproducts.get(position);
            holder.prodDiscount.setText(discprod.getDiscount());



            products totalprice = arrayproducts.get(position);
            holder.totalPrice.setText(totalprice.getTotal_price());



            holder.prodDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    try {


                        if (hasFocus) {
                            //  Toast.makeText(mContext, "Got the focus", Toast.LENGTH_LONG).show();
                            products netprice = arrayproducts.get(position);
                            holder.netPrice.setText(netprice.getNet());
                            products totalprice = arrayproducts.get(position);
                            holder.totalPrice.setText(totalprice.getTotal_price());
                        } else {
                            // Toast.makeText(mContext, "Lost the focus", Toast.LENGTH_LONG).show();

                            products netprice = arrayproducts.get(position);
                            holder.netPrice.setText(netprice.getNet());
                            products totalprice = arrayproducts.get(position);
                            holder.totalPrice.setText(totalprice.getTotal_price());
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e("prodDiscount","Focus Change error"+e.toString());
                    }
                }
            });


//Net price
            if (holder.netPriceWatcher != null) {
                holder.netPrice.removeTextChangedListener(holder.netPriceWatcher);
            }

            // Create the TextWatcher corresponding to this row
            holder.netPriceWatcher = new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().equals("")||s.toString().equals("0")){
                        arrayproducts.get(position).setDiscont(" ");
                    }
                    else{
                        arrayproducts.get(position).setNet(s.toString());
                    }
                    if(Float.parseFloat("0"+s.toString().trim())>0){
                        //    openDiscount(""+arrayproducts.get(position).getCode(),position,arrayproducts.get(position).getQnty(),arrayproducts.get(position).getPrice());
                        //total price
                        arrayproducts.get(position).setNet(""+s.toString().trim());
                       // int tempnet= Integer.parseInt(s.toString());
                       float tempnet= Float.valueOf(s.toString());
                        float qnty;
                        String stqnty=arrayproducts.get(position).getQnty().toString();
                        if (!stqnty.isEmpty()) {
                            qnty = Float.valueOf(stqnty.toString());
                        }
                        else{
                            qnty=0.0f;
                        }
                        float temptotal=(float)qnty* (float)tempnet;
                        float total_price=(float) temptotal;
                        arrayproducts.get(position).setTotal_price(""+total_price);

                    }

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            };

            holder.netPrice.addTextChangedListener(holder.netPriceWatcher);
            products netprice = arrayproducts.get(position);
            holder.netPrice.setText(netprice.getNet());

            holder.netPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                      //    Toast.makeText(mContext, "netPrice Got the focus", Toast.LENGTH_LONG).show();

                    } else {
                       //  Toast.makeText(mContext, "netPrice Lost the focus", Toast.LENGTH_LONG).show();

                    }
                }
            });

//-----------try 1 end ----------------------------------------------------------------------------

            return convertView;

        }
        catch(Exception e){
            Toast.makeText(mContext, "!!!"+e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("ListViewException:",""+e.toString());
        }
        return convertView;
    }


    public void filter(String charText) {

        charText = charText.toString().trim().toLowerCase(Locale.getDefault());

        parkingList.clear();
        if (charText.length() == 0) {
            if(arrayproducts.size()>0)
            {
                Log.e("arrayproducts","size"+arrayproducts.size());
                parkingList.addAll(arrayproducts);
                }
            else {
                parkingList.addAll(tempproductsarray);
                Log.e("tempproductsarray","size"+tempproductsarray.size());
            }

        } else {
            try {
                for (products postDetail : arrayproducts) {
                    if (charText.length() != 0 && postDetail.getCode().toString().trim().toLowerCase(Locale.getDefault()).contains(charText)) {
                        parkingList.add(postDetail);
                        Log.e("Found Search Code", "" + charText + "\tFOR\t" + postDetail.getCode().toString().trim());
                    } else if (charText.length() != 0 && postDetail.getName().toString().trim().toLowerCase(Locale.getDefault()).contains(charText)) {
                        parkingList.add(postDetail);
                        Log.e("Found Search name", "" + charText + "\tFOR\t" + postDetail.getName().toString().trim());
                    } /*else  if(charText.length()!=0 &&!postDetail.getCode().toString().trim().toLowerCase(Locale.getDefault()).contains(charText)){
                        parkingList.addAll(tempproductsarray);
                        Log.e("ReLoad", "Reloading Data");
                    }*/
                }
            }
            catch (Exception e){
                Log.e("LoadERROR", "Error Reloading Data"+e.toString());
            }

        }
        notifyDataSetChanged();
        arrayproducts.clear();
        arrayproducts.addAll(parkingList);
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(arrayproducts != null && arrayproducts.size() != 0){
            return arrayproducts.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public void getQnty(){
        if(edit_qnty.getText().toString().trim().equals("0"))
            edit_qnty.setText("");

    }
    static class ViewHolder {
        TextView prod_sl;
        TextView prod_code;
        TextView txtTitle;
        TextView txtDescription;
        TextView prod_uom;
        EditText prod_qnty,prod_price,prodDiscount,netPrice,progPrcie,totalPrice;
        TextWatcher qtyWatcher,discWatcher,progpriceWatcher, netPriceWatcher;
        TextWatcher priceWatcher;
        int ref;
    }

}