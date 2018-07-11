package com.metrosoft.arafat.salebook.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.activity.ProductSend;
import com.metrosoft.arafat.salebook.helper.SQLiteHandler;

import java.util.ArrayList;

import static com.metrosoft.arafat.salebook.activity.ProductMain.emp_code;
import static com.metrosoft.arafat.salebook.activity.ProductMain.party_code;

/**
 * Created by hp on 1/9/2018.
 */

public class OrderItemAdapter  extends ArrayAdapter<HashMapColumn>  {
    private Context context;
    private ArrayList<HashMapColumn> OrdItems;
    public  static  boolean qntyChanged;
    LayoutInflater inflater;
    SQLiteHandler db;
    String NQuantity,NPrice;
    EditText editqnty,editprice;
    ViewHolder holder;
    public OrderItemAdapter(Context context, int textViewResourceId, ArrayList<HashMapColumn> objects) {
        super(context,textViewResourceId, objects);

        this.context= context;

       // OrdItems=objects;
        this.OrdItems = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder
    {
        TextView  txtFirst,txtSecond,txtThird,txtSixth,txtSeventh,txtSaleType,txtItemCat,unittotaltext;
        EditText txtFourth, txtFifth;
        int ref;
        EditText prod_qnty,prod_price;
        TextWatcher qtyWatcher;
        TextWatcher priceWatcher;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final int pos=position;
       // final ViewHolder holder;
        if (convertView == null)
        {
         //   LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_items_row, null);

            holder = new ViewHolder();

            holder.txtFirst    =(TextView) convertView.findViewById(R.id.pid);
            holder.txtSecond   =(TextView) convertView.findViewById(R.id.name);
            holder.txtThird    =(TextView) convertView.findViewById(R.id.uom);
            holder.txtFourth   =(EditText) convertView.findViewById(R.id.qnty);
            holder.txtFifth    =(EditText) convertView.findViewById(R.id.price);
            holder.txtSixth    =(TextView) convertView.findViewById(R.id.p_code);
            holder.txtSeventh  =(TextView) convertView.findViewById(R.id.p_desc);
            holder.txtItemCat   =(TextView)convertView.findViewById(R.id.item_parent_cat) ;
            holder.txtSaleType  = (TextView)convertView.findViewById(R.id.sale_type) ;
            holder.unittotaltext  = (TextView)convertView.findViewById(R.id.unittotal) ;
            convertView.setTag(holder);

        }
        else {
         //   holder = (ViewHolder) convertView.getTag();
            holder = (ViewHolder) convertView.getTag();
        }


        HashMapColumn individualitems= OrdItems.get(position);
        Log.e("CHECK","Position\t"+position+"\tCODE\t"+OrdItems.get(position).getCode().trim());
       // holder.txtFirst.setText(""+position);
        //SL
        holder.txtFirst.setText(OrdItems.get(position).getSl());
        //name
        holder.txtSecond.setText(OrdItems.get(position).getName().trim());
        //UOM
        holder.txtThird.setText(OrdItems.get(position).getUom());
        //code
        holder.txtSixth.setText(OrdItems.get(position).getCode());
        //Description
        holder.txtSeventh.setText(OrdItems.get(position).getDesc());
        //parent Cat
        holder.txtItemCat.setText(OrdItems.get(position).getParentCat());
        // Sale Type
        holder.txtSaleType.setText(OrdItems.get(position).getSaleType());


        //Longpress
        holder.txtSecond.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("Long Pressed","E");
                ShowPriceDailogue(""+OrdItems.get(position).getCode(),position,OrdItems.get(position).getQnty(),OrdItems.get(position).getPrice());
                return false;
            }
        });


        //Quantity
       holder.txtFourth.setText(OrdItems.get(position).getQnty());
        if(holder.txtFourth.getText().toString()!=null && !holder.txtFourth.getText().toString().isEmpty()){

            holder.txtFourth.setText(holder.txtFourth.getText().toString());

        }else{
            holder.txtFourth.setText("0");
        }



        //Price
        holder.txtFifth.setText(OrdItems.get(position).getPrice());
        if(holder.txtFifth.getText().toString()!=null && !holder.txtFifth.getText().toString().isEmpty()){

            holder.txtFifth.setText(holder.txtFifth.getText().toString());

        }else{
            holder.txtFifth.setText("0");
        }

        //Price
        holder.unittotaltext.setText(OrdItems.get(position).getTotal_price());
        if(holder.unittotaltext.getText().toString()!=null && !holder.unittotaltext.getText().toString().isEmpty()){

            holder.unittotaltext.setText(holder.unittotaltext.getText().toString());

        }else{
            holder.unittotaltext.setText("0.0");
        }


   /*    holder.txtFifth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ShowPriceDailogue(""+OrdItems.get(position).getCode(),position,OrdItems.get(position).getQnty(),OrdItems.get(position).getPrice());


            }

            @Override
            public void afterTextChanged(Editable editable) {
                //   Toast.makeText(context,"Quantity Clicked"+position+"\t"+emp_code+"\t"+party_code+"\t"+OrdItems.get(position).getCode().trim(), Toast.LENGTH_SHORT).show();
               try {
                    db = new SQLiteHandler(context);
                    db.UpdateTablePRICE(emp_code,party_code,OrdItems.get(position).getCode().trim(),editable.toString().trim());
                    Log.e("PriceUpdate","Price Clicked\t"+position+"\t"+emp_code+"\t"+party_code+"\t"+OrdItems.get(position).getCode().trim());
                }
                catch (Exception e){
                    Log.e("PriceUpdate","Error Occured during Update Price\t"+ e.toString());
                }
                //notifyDataSetChanged();


            }
        });*/



        return convertView;


    }



    private void ShowPriceDailogue(final String pcode, final int pos, String pqnty, String pprice) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.quantiy_price_dialogue, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
       // AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);
        TextView DailogTitle = (TextView)mView.findViewById(R.id.editdialogtitle);
        DailogTitle.setText("Change Quantity and Price?");
        TextView ProdCode=(TextView)mView.findViewById(R.id.editpcode);
        ProdCode.setText(pcode);
         editqnty=(EditText) mView.findViewById(R.id.editqnty);
         editprice=(EditText) mView.findViewById(R.id.editprice);
     //   editprice.setText(pprice);
      //  editqnty.setText(pqnty);
        Log.e("editvalues","New Values\t"+emp_code+"\t"+party_code+"\t"+pcode+"\t"+editqnty.getText().toString()+"\t"+editprice.getText().toString());

        //getOrderType();
        //getPriceList();
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        try {
                            db= new SQLiteHandler(context);
                            String tempprice=editprice.getText().toString().trim();
                            String tempqnty=editqnty.getText().toString().trim();
                            if(tempprice.isEmpty()&&tempprice.equals("")){
                                tempprice= OrdItems.get(pos).getPrice().toString().trim();
                            }
                            if(tempqnty.isEmpty()&&tempqnty.equals("")){
                                tempqnty= OrdItems.get(pos).getQnty().toString().trim();
                            }

                            //price update
                            db.UpdateTablePRICE(emp_code,party_code,pcode.trim(),tempprice);
                            Log.e("PriceUpdate","Price Clicked\t"+pos+"\t"+emp_code+"\t"+party_code+"\t"+pcode+"\t"+tempprice);
                            // item delete if qnty=0;
                            if (Integer.parseInt(editqnty.getText().toString().trim())>0) {
                                //quantity update
                                db.UpdateTableQNTY(emp_code.replace("\"", "").replaceAll("[^0-9]", ""), party_code.replace("\"", "").replaceAll("[^0-9]", ""), pcode.trim(), editqnty.getText().toString().trim());
                            }
                            else {
                                db.ZeroQntyDel(emp_code.replace("\"", "").replaceAll("[^0-9]", ""), party_code.replace("\"", "").replaceAll("[^0-9]", ""), pcode.trim());
                            }
                            Log.e("QntyUpdate","Quantity Clicked\t"+pos+"\t"+emp_code+"\t"+party_code+"\t"+pcode+"\t"+tempqnty);
                            qntyChanged=true;
                            Intent i= new Intent(context, ProductSend.class);
                            context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                            //RefreshItems();
                           // getItems();
                        }
                        catch (Exception e){
                            Log.e("ERROR ON UPDATE","Error Occured during Update\t"+ e.toString());
                            qntyChanged=false;
                        }


                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                                qntyChanged=false;
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }
    public void refreshEvents(ArrayList<HashMapColumn> Data) {
        this.OrdItems.clear();
        this.OrdItems.addAll(Data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(OrdItems != null && OrdItems.size() != 0){
            return OrdItems.size();
        }
        return 0;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}