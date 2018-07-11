package com.metrosoft.arafat.salebook.products;

/**
 * Created by anonymous on 11/4/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.activity.ProductList;
import com.metrosoft.arafat.salebook.holder.Category;

import java.util.ArrayList;
import java.util.HashMap;

import static com.metrosoft.arafat.salebook.activity.MainActivity.parent_cat;
import static com.metrosoft.arafat.salebook.activity.ProductMain.org_id;

//import static com.metrosoft.arafat.salebook.app.AppConfig.org_id;

public class NFALProdCat extends Fragment {

    ArrayList<Category> householditems = new ArrayList<>();
    ArrayList<Category> furnitureitems = new ArrayList<>();
    ArrayList<Category> fittingsitems = new ArrayList<>();
    HashMap<String,String> hasmap= new HashMap<>();
    RecyclerView Householdview,Furnitureview,Fittingsview, Giftview;
    String household_items_code[] = {"121","122","123","106","104","114","117","120","109","129","105","113","116","103","107","102","110","111","115","101","027","118","108", "025","112","124","119"};
    String household_items[] = {"Baby potty","Baby potty wheel","Basket","Bucket","Bowl","Chopping board","Cloth clip","Dust pan",
                                "Food box","Food cover","Glass","Glass stand","Hanger","Jug","Lid","Mug","Pen holder","Plate","Printed container",
                                "Rack","Soap case","Spice jar","Tiffin box", "Tissu holder","Tray","Washing net","Water pot"};
    int  household_images[]  = {R.drawable.baby_potty,R.drawable.baby_potty_wheel,R.drawable.basket ,R.drawable.bucket,R.drawable.bowl,
                                R.drawable.choping_board,R.drawable.cloth_clip,R.drawable.dust_pan,R.drawable.food_box,R.drawable.food_cover,
                                R.drawable.glass,R.drawable.glass_stand,R.drawable.hanger,R.drawable.jug,R.drawable.lid,R.drawable.mug,
                                R.drawable.pen_stand,R.drawable.plate,R.drawable.printed_container,R.drawable.rack,R.drawable.soap_case,
                                R.drawable.spice_jar,R.drawable.tiffin_box,R.drawable.tissu_holder,R.drawable.tray,R.drawable.washing_net,
                                R.drawable.water_pot};
    String house_holde_parent[]={"Household"};
    String furniture_items_code  []= {"126","127","125","128"};
    String furniture_items  []= {"Baby Chair","Chair","Stool","Table"};
    int furniture_images    []= {R.drawable.baby_chair,R.drawable.chair,R.drawable.stool,R.drawable.table};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        householditems.clear();
        furnitureitems.clear();
        fittingsitems.clear();
        Log.e("NFAL Products ParentCat",""+parent_cat);
        if(!parent_cat.isEmpty()&&parent_cat=="hh") {
            addHousehold();
        }else if(!parent_cat.isEmpty()&&parent_cat=="fur") {
            addFurniture();
        }
     //   addFittings();


        getActivity().setTitle("Products Category");
      //  getActivity().setTitle(R.drawable.furnituer_large);
    }


    private void addHousehold() {
        for(int i =0;i<household_items.length;i++){
            Category item = new Category();

            item.setCatId(household_items_code[i]);
            item.setCatName(household_items[i]);
            item.setImageResourceId(household_images[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            item.setParentCat("Household");
            hasmap.put(""+i,household_items[i]);
            householditems.add(item);
        }
       // household_items.notifyAll();
       // household_images.notifyAll();
    }
    private void addFurniture() {
        for(int i =0;i<furniture_items.length;i++){
            Category item = new Category();
            item.setCatId(furniture_items_code[i]);
            item.setCatName(furniture_items[i]);
            item.setImageResourceId(furniture_images[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            item.setParentCat("Furniture");
            hasmap.put(""+i,furniture_items[i]);
            furnitureitems.add(item);
        }
       // furniture_items.notifyAll();
       // furniture_images.notifyAll();
    }

   /* private void addFittings() {
        for(int i =0;i<household_items.length;i++){
            Category item = new Category();
            item.setCatId(""+i);
            item.setCatName(household_items[i]);
            item.setImageResourceId(household_images[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            hasmap.put(""+i,household_items[i]);
            householditems.add(item);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_horizontal_list_view, container, false);
        //Household
        Householdview = (RecyclerView) view.findViewById(R.id.cardView);
        Householdview.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (householditems.size() > 0 & Householdview != null) {
            Householdview.setAdapter(new MyAdapter(householditems));
        }
        Householdview.setLayoutManager(MyLayoutManager);
//Furniture
        Furnitureview = (RecyclerView) view.findViewById(R.id.furnitureview)
        ;
        Furnitureview.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager1 = new LinearLayoutManager(getActivity());
        MyLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (furnitureitems.size() > 0 & Furnitureview != null) {
            Furnitureview.setAdapter(new MyAdapter(furnitureitems));
        }
        Furnitureview.setLayoutManager(MyLayoutManager1);
//Fittings
       /* Fittingsview = (RecyclerView) view.findViewById(R.id.fittingsview);
        Fittingsview.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(getActivity());
        MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (householditems.size() > 0 & Fittingsview != null) {
            Fittingsview.setAdapter(new MyAdapter(householditems));
        }
        Fittingsview.setLayoutManager(MyLayoutManager2);*/
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Category> list;

        public MyAdapter(ArrayList<Category> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.titleParent.setText(list.get(position).getParentCat());
            holder.titleTextView.setText(list.get(position).getCatName());
            holder.cat_menu_id.setText(list.get(position).getCatId());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            holder.coverImageView.setTag(list.get(position).getImageResourceId());
          //  holder.likeImageView.setTag(R.drawable.ic_like);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView,cat_menu_id,titleParent;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public FrameLayout item_frame;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            titleParent =(TextView)v.findViewById(R.id.parent_cat);

            cat_menu_id = (TextView) v.findViewById(R.id.cat_menu_id);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            item_frame = (FrameLayout)v.findViewById(R.id.item_layout);
            item_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String CatId=""+cat_menu_id.getText().toString().trim();
                    String prod_cat=""+titleParent.getText().toString().trim();

                    Toast.makeText(getActivity(),prod_cat+"\t-\t"+titleTextView.getText()+"-\t"+CatId+"\tSelected",Toast.LENGTH_SHORT).show();
                    gotoProductpage(CatId,prod_cat);
                    //Intent i = new Intent(MyViewHolder.this, ProductList.class);
              /*      Intent i = new Intent((getContext().this,ProductList.class)
                    //startActivity(new Intent(MainActivity.this, QrCodeScannerActivity.class));
                  i.putExtra("ID",ID);
                    i.putExtra("name",name);
                    i.putExtra("phone",phone);
                    i.putExtra("email",email);
                    i.putExtra("org_id",org_id);
                    startActivity(i);
                    finish();*/
                }
            });
          //  likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
           // shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
        /*    likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_like){

                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);

                        Toast.makeText(getActivity(),titleTextView.getText()+" added to favourites",Toast.LENGTH_SHORT).show();

                    }else{

                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);
                        Toast.makeText(getActivity(),titleTextView.getText()+" removed from favourites",Toast.LENGTH_SHORT).show();


                    }

                }
            });



            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));



                }
            });*/



        }
    }

    private void gotoProductpage(final String cat_id,String prod_cat) {


        Intent i = new Intent(getActivity(), ProductList.class);
        i.putExtra("cat_id",cat_id);
        i.putExtra("org_id",org_id);
        i.putExtra("prod_cat",prod_cat);

        startActivity(i);
        Log.e("ProductList(NFAL)","cat"+cat_id+"\torg\t"+org_id+"\tprodcat\t"+prod_cat);

    }


}