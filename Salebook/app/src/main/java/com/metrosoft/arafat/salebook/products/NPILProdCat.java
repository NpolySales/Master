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

public class NPILProdCat extends Fragment {
//pipes
    ArrayList<Category> upvcpipeitmes = new ArrayList<>();
    ArrayList<Category> swrpipeitmes = new ArrayList<>();
    ArrayList<Category> pprpipeitmes = new ArrayList<>();
    ArrayList<Category> cpvcpipeitmes = new ArrayList<>();
    ArrayList<Category> hdpepipeitmes = new ArrayList<>();
    ArrayList<Category> suckpipeitmes = new ArrayList<>();
    ArrayList<Category> echpipeitmes = new ArrayList<>();
//doors
    ArrayList<Category> dooritems = new ArrayList<>();
    ArrayList<Category> fittingsitems = new ArrayList<>();
    ArrayList<Category> ceilingitems = new ArrayList<>();
    ArrayList<Category> tubewellitems = new ArrayList<>();

    HashMap<String,String> hasmap= new HashMap<>();
    RecyclerView Pipeview,Doorview,Fittingsview,Ceilingview,tubewellview, Giftview;
    //uPVC   pipe , Filter , Fabricated Fittings
    String upvcpipe_items_code[] = {"001","003","005","007","009","011","013","021","023","025","027","030","035","041"};
    String upvcpipe_items_name[] = {"uPVC pipe, Filter, Fabricated Fittings","SWR Pipe & Fittings","PPR Pipe & Fittings","CPVC Pipe & Fittings","HDPE Coil Pipe","Suction Hose Pipe","Electric Corrugated Hose Pipe",
    "uPVC Sheet","Solvent Cement","Tissue holder","Soap Case","Taflon Tape","Cable Casing","Water Tank(স্রোত)"};
    int  upvcpipe_items_img[]  = {R.drawable.upvcpipes,R.drawable.swrpipe,R.drawable.pprpipes,R.drawable.cpvcpipes,R.drawable.coilpipe,R.drawable.suction_pipe,R.drawable.suction_pipe,R.drawable.upvcsheet,R.drawable.sovent_cement,R.drawable.tissu_holder,R.drawable.soap_case,R.drawable.teflon_tape,R.drawable.cablecasing,R.drawable.watertank};
    //SWR      Pipe & Fittings
   /* String swr_pipe_code  []= {"003"};
    String swr_pipe_name  []= {"SWR Pipe & Fittings"};
    int swr_pipe_img    []= {R.drawable.baby_chair};
    //PPR Pipe & Fittings
    String prp_pipe_code  []= {"005"};
    String prp_pipe_name  []= {"PPR Pipe & Fittings"};
    int prp_pipe_img    []= {R.drawable.baby_chair};*/
    //uPVC Door & uPVC Cat Door
    String upvc_door_code   []={"015","017"};
    String upvc_door_name   []={"uPVC Door","uPVC Cat Door"};
    int    upvc_door_img    []={R.drawable.upvcdoors,R.drawable.catdoors};
    String upvc_ceiling_code []={"019"};
    String upvc_ceiling_name[]={"uPVC Ceiling"};
    int    upvc_ceiling_img    []={R.drawable.celling};

    String tubewell_code []={"031","033"};
    String tubewell_name[]={"Tubewell","Tubewell Accessories"};
    int    tubewell_img   []={R.drawable.tubewell_prod,R.drawable.tubewell};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upvcpipeitmes.clear();
        dooritems.clear();
        fittingsitems.clear();
        ceilingitems.clear();
        tubewellitems.clear();




        if(!parent_cat.isEmpty()&&parent_cat=="pipe") {
            addupvcPipes();
        }else if(!parent_cat.isEmpty()&&parent_cat=="door") {
            addDoors();
        }
       else if(!parent_cat.isEmpty()&&parent_cat=="ceil") {
            addCeillings();
        }
        else if(!parent_cat.isEmpty()&&parent_cat=="tube") {
            addTubewell();
        }


        getActivity().setTitle("Products Category");
      //  getActivity().setTitle(R.drawable.furnituer_large);
    }

    private void addTubewell() {
        for(int i =0;i<tubewell_code.length;i++){
            Category item = new Category();
            item.setCatId(tubewell_code[i]);
            item.setCatName(tubewell_name[i]);
            item.setImageResourceId(tubewell_img[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            hasmap.put(""+i,tubewell_name[i]);
            tubewellitems.add(item);
            Log.e("Tubewell",""+tubewell_code[i]+"-"+tubewell_name[i]);
        }
    }

    private void addCeillings() {
        for(int i =0;i<upvc_ceiling_code.length;i++){
            Category item = new Category();
            item.setCatId(upvc_ceiling_code[i]);
            item.setCatName(upvc_ceiling_name[i]);
            item.setImageResourceId(upvc_ceiling_img[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            hasmap.put(""+i,upvc_ceiling_name[i]);
            ceilingitems.add(item);
        }
    }



    private void addupvcPipes() {
        for(int i =0;i<upvcpipe_items_name.length;i++){
            Category item = new Category();
            item.setCatId(upvcpipe_items_code[i]);
            item.setCatName(upvcpipe_items_name[i]);
            item.setImageResourceId(upvcpipe_items_img[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            hasmap.put(""+i,upvcpipe_items_name[i]);
            upvcpipeitmes.add(item);
        }
    }
    private void addDoors() {
        for(int i =0;i<upvc_door_code.length;i++){
            Category item = new Category();
            item.setCatId(upvc_door_code[i]);
            item.setCatName(upvc_door_name[i]);
            item.setImageResourceId(upvc_door_img[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            hasmap.put(""+i,upvc_door_name[i]);
            dooritems.add(item);
        }
    }

 /*   private void addFittings() {
        for(int i =0;i<upvcpipe_items_name.length;i++){
            Category item = new Category();
            item.setCatId(""+i);
            item.setCatName(upvcpipe_items_name[i]);
            item.setImageResourceId(upvcpipe_items_img[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            hasmap.put(""+i,upvcpipe_items_name[i]);
            upvcpipeitmes.add(item);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_horizontal_list_npil, container, false);


        //Tubewell
        tubewellview = (RecyclerView) view.findViewById(R.id.Tubewellview);
        tubewellview.setVisibility(View.VISIBLE);
        tubewellview.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager3 = new LinearLayoutManager(getActivity());
        MyLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (tubewellitems.size() > 0 & tubewellitems != null) {
            tubewellview.setAdapter(new MyAdapter(tubewellitems));
        }
        tubewellview.setLayoutManager(MyLayoutManager3);



        //Pipes
        Pipeview = (RecyclerView) view.findViewById(R.id.pipeVeiw);
        Pipeview.setVisibility(View.VISIBLE);
        Pipeview.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (upvcpipeitmes.size() > 0 & Pipeview != null) {
            Pipeview.setAdapter(new MyAdapter(upvcpipeitmes));
        }
        Pipeview.setLayoutManager(MyLayoutManager);
        //Doors
        Doorview = (RecyclerView) view.findViewById(R.id.doorview);
        Doorview.setVisibility(View.VISIBLE);
        Doorview.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager1 = new LinearLayoutManager(getActivity());
        MyLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (dooritems.size() > 0 & Doorview != null) {
            Doorview.setAdapter(new MyAdapter(dooritems));
        }
        Doorview.setLayoutManager(MyLayoutManager1);
        //Ceiling
        Ceilingview = (RecyclerView) view.findViewById(R.id.ceilingview);
        Ceilingview.setVisibility(View.VISIBLE);
        Ceilingview.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(getActivity());
        MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (ceilingitems.size() > 0 & ceilingitems != null) {
            Ceilingview.setAdapter(new MyAdapter(ceilingitems));
        }
        Ceilingview.setLayoutManager(MyLayoutManager2);
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

        public TextView titleTextView,cat_menu_id;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public FrameLayout item_frame;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            cat_menu_id = (TextView) v.findViewById(R.id.cat_menu_id);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            item_frame = (FrameLayout)v.findViewById(R.id.item_layout);
            item_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String CatId=""+cat_menu_id.getText().toString().trim();

                    Toast.makeText(getActivity(),titleTextView.getText()+"-"+CatId+" added to favourites",Toast.LENGTH_SHORT).show();
                    gotoProductpage(CatId);

                }
            });





        }
    }

    private void gotoProductpage(final String cat_id) {

        Intent i = new Intent(getActivity(), ProductList.class);
        i.putExtra("cat_id",cat_id);
        i.putExtra("org_id",org_id);
        startActivity(i);

    }


}