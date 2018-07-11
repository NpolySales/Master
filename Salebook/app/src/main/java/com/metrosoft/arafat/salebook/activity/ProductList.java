package com.metrosoft.arafat.salebook.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.geniusforapp.fancydialog.FancyAlertDialog.PanelGravity;
import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.Utils.LoadingAnim;
import com.metrosoft.arafat.salebook.app.AppConfig;
import com.metrosoft.arafat.salebook.app.AppController;
import com.metrosoft.arafat.salebook.helper.GPSTracker;
import com.metrosoft.arafat.salebook.helper.SQLiteHandler;
import com.metrosoft.arafat.salebook.holder.ListViewAdapter;
import com.metrosoft.arafat.salebook.products.products;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.os.Build.ID;
import static android.support.design.widget.Snackbar.make;
import static com.metrosoft.arafat.rxpad.R.id.userInput_shipto;
import static com.metrosoft.arafat.salebook.activity.MainActivity.PhoneView;
import static com.metrosoft.arafat.salebook.activity.MainActivity.saletype;
import static com.metrosoft.arafat.salebook.activity.ProductMain.emp_code;
import static com.metrosoft.arafat.salebook.activity.ProductMain.org_id;
import static com.metrosoft.arafat.salebook.activity.ProductMain.party_code;
import static com.metrosoft.arafat.salebook.app.AppConfig.toemail;

public class ProductList extends AppCompatActivity  {
    ImageView imgattch;

    private SQLiteHandler db;
    ProgressBar proCollageList;
    private ProgressDialog pDialog;
    private LoadingAnim lm;
    static String cat_id;
    static String revieworeder="";
    FloatingActionButton fab_send, fab_delete,fab_show,fab_save;
    public static String[] arrTemp;
    public static String[] arrText;
    ArrayList<products> productList;
    public static String ORD_ID;
    private ListViewAdapter adapter;
    private  ListView listCollege;
    HashMap<String,String> OrderHeap = new HashMap<>();
    final Context context = this;
    public static Bitmap attachimg;
    private static final int CAMERA_REQUEST = 1888;
    ArrayList< HashMap<String,HashMap<String,String>>>ol = new ArrayList<HashMap<String,HashMap<String,String>>>();
    ArrayList<HashMap<String, String>>  OrderList = new ArrayList<HashMap<String, String>>();
    public   EditText pqnty, ProdDiscount,ProgPrice;;
    public static String true_false;
    public String mob_order_id, prod_code, prod_name , uom, qnty,prod_price, order_by, is_active, image_str,note,sale_type,prod_cat;
    public static String shipto,payto,customerclass,ordertype,pricelist;
    public static AutoCompleteTextView paytotext,shiptotext,order_type,price_list;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    InputStream is=null;
    String result=null;
    String line=null;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        pqnty=((EditText)findViewById(R.id.prod_qnty));
        productList = new ArrayList<products>();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        lm = new LoadingAnim(this);
        lm.setCancelable(false);
        listCollege = (ListView)findViewById(R.id.listCollege);
       // listCollege.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        proCollageList = (ProgressBar)findViewById(R.id.proCollageList);
        //getting passed data
        fab_save=(FloatingActionButton)findViewById(R.id.fab_save);
        fab_show=(FloatingActionButton)findViewById(R.id.fab_apply);
        fab_delete=(FloatingActionButton)findViewById(R.id.fab_delete);
        Intent intent = getIntent();
        OrderHeap = new HashMap<>();
        OrderList = new ArrayList<HashMap<String, String>>();
        db = new SQLiteHandler(getApplicationContext());
       // arrTemp = new String[productList.size()];

        if (intent.hasExtra("cat_id")) {

            //cat_id="CAT1";
            cat_id=intent.getStringExtra("cat_id");
            org_id=intent.getStringExtra("org_id");
            prod_cat= intent.getStringExtra("prod_cat");

        }
        new GetHttpResponse(this).execute();
        // arrText = new String[];
        // arrTemp = new String[arrText.length];

        Log.e("Product list params","Cat:"+cat_id+" Org: "+org_id+"\tArraylistsize:"+productList.size());

        int itemsCount = listCollege.getChildCount();
        for (int ii1 = 0; ii1 < itemsCount; ii1++) {
            View view12 = listCollege.getChildAt(ii1);
            int itemquantity = Integer.parseInt(((EditText) view12.findViewById(R.id.prod_qnty)).getText().toString());
            Log.e("ProductList","itemquantity\t"+itemquantity);
         //   Toast.makeText(getApplicationContext(),"ss"+itemquantity,Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                boolean search;
                try {
                    Log.e("Searching for", "" + searchQuery);
                    adapter.filter(searchQuery.toString().trim());
                    listCollege.invalidate();
                    search=true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    search=false;
                }
                return search;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
          //  searchView.setOnQueryTextListener(this);

            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    //button events
    /*----------------------------Saving Order start--------------------------------------------------------------------*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public  void saveOrder(View view){
        Context ctx;

        is_active = "Y";

        int itemsCount = listCollege.getChildCount();
        int itemscnt=listCollege.getAdapter().getCount();
        Log.e("List Size","Child "+itemsCount+"\tlist "+listCollege.getAdapter().getCount()+"\tProductitem "+productList.size());
        sale_type=""+saletype.getText().toString().trim();
        int maxsave=0;

        try {
            for (int i = 0, count = itemscnt; i < count; ++i) {
                // view = listCollege.getChildAt(i);
                // view =listCollege.getChildAt(itemscnt);
                String PQNTY = "" + productList.get(i).getQnty().toString().trim();
                String PSL = "" + productList.get(i).getSl().toString().trim();
                String PDESC = "" + productList.get(i).getDesc().toString().trim();
                String GPPRICE = "" + productList.get(i).getPrice().toString().trim();
                String NPRICE= ""+productList.get(i).getNet().toString().trim();
                String PROGPRICE= ""+productList.get(i).getProgPrice().toString().trim();
                prod_code = "" + productList.get(i).getCode().toString().trim();
                prod_name = "" + productList.get(i).getName().toString().trim();
                uom = "" + productList.get(i).getUom().toString().trim();


                //    String PQNTY = ""+((EditText) view.findViewById(R.id.prod_qnty)).getText().toString().trim();
                // String PSL = ""+((TextView) view.findViewById(R.id.prod_sl)).getText().toString().toString();
                // String PDESC = ""+((TextView) view.findViewById(R.id.adapter_text_description)).getText().toString().toString();
                // String PPRICE=""+((EditText) view.findViewById(R.id.prod_price)).getText().toString().toString();
                //   prod_code = ""+((TextView) view.findViewById(R.id.prod_code)).getText().toString().toString();
                //   prod_name = ""+((TextView) view.findViewById(R.id.adapter_text_title)).getText().toString().toString();
                //  uom = ""+((TextView) view.findViewById(R.id.prod_uom)).getText().toString().toString();
                //PRICE CALCULATION
                qnty = PQNTY;
                if (!PROGPRICE.isEmpty()&& NPRICE.isEmpty()){
                    prod_price=PROGPRICE;
                }
                else if(NPRICE.isEmpty()&&!GPPRICE.isEmpty()){
                    prod_price = GPPRICE;}
                else if (!NPRICE.isEmpty()&&PROGPRICE.isEmpty()){
                    prod_price = NPRICE;
                }
                else {
                    prod_price="0";
                }
                Log.e("Loop", "Iterator: " + i + "\tValue: " + PQNTY);
                //  OrderHeap.put(PSL, PQNTY);
                // OrderList.add(OrderHeap);
                //String mob_order_id,String prod_code,String prod_name ,String uom,String qnty,String order_by,String org_id,String party_code,String order_date,String is_active
                //Local  db\


                if (!qnty.isEmpty() && !qnty.equals("0")) {
                    Log.e("Insert in Offline Order", PSL + ": " + prod_code + "-" + prod_name + "-" + uom + "-" + qnty + "-\tgross:\t"+GPPRICE+"-\tNet:\t" + prod_price + "-" + emp_code + "-" + org_id + "-" + party_code + "--" + is_active);
                    //  String SL,String prod_code,String prod_name ,String uom,String qnty,String price, String order_by,String org_id,String party_code,String order_date,String is_active
                    //db.saveTempOrder(prod_code, prod_name, PDESC, uom, qnty, prod_price, emp_code, org_id, party_code, is_active,sale_type.substring(11,23),prod_cat);
                    db.saveTempOrder(prod_code, prod_name, PDESC, uom, qnty,GPPRICE, prod_price, emp_code, org_id, party_code, is_active,sale_type.substring(11),prod_cat);
                }
                maxsave++;
            }
            if(maxsave==itemscnt){
                final Snackbar sb= Snackbar.make(view, "Data Saved", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
                        .setAction("Action", null);
                View snackBarView = sb.getView();
                snackBarView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackBarView.setBackgroundColor(Color.BLUE);
                sb.show();

            }
//        db.getTempOrders();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("saveOrder Exp: ",""+e.toString());
        }



    }

    /*----------------------------Order sending start--------------------------------------------------------------------*/
    //Not using
    public void sendOrder(final View view) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.usert_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        TextView DailogTitle = (TextView)mView.findViewById(R.id.dialogTitle);
        DailogTitle.setText("Process Order?");
        TextView DailogMessage = (TextView)mView.findViewById(R.id.dialogMsg);
        final TextView CameraWarn =(TextView)mView.findViewById(R.id.camra_warn);
        imgattch =(ImageView)mView.findViewById(R.id.attachment);
        DailogMessage.setText("Any special note for this order?\n Order is One step Closer, Would you want to confirm this order? ");
        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInput);
        userInputDialogEditText.setVisibility(View.VISIBLE);
        final ImageButton snap = (ImageButton)mView.findViewById(R.id.btncamera);
        shiptotext = (AutoCompleteTextView) mView.findViewById(userInput_shipto);
        paytotext = (AutoCompleteTextView) mView.findViewById(R.id.userInput_payto);
        order_type = (AutoCompleteTextView) mView.findViewById(R.id.order_type);
        price_list = (AutoCompleteTextView) mView.findViewById(R.id.price_list);
       shiptotext.setVisibility(View.VISIBLE);
        paytotext.setVisibility(View.VISIBLE);
        snap.setVisibility(View.VISIBLE);
        String party_code=ProductMain.party_code;
       getpartyShipLocation(party_code);
        getpartyBillLocataion(party_code);
        final TextView camerahint = (TextView)mView.findViewById(R.id.camera_hint);
        camerahint.setVisibility(View.VISIBLE);
        CameraWarn.setVisibility(View.VISIBLE);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraWarn.setVisibility(View.GONE);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(cameraIntent, CAMERA_REQUEST);
             //   MagicalTakePhoto magicalTakePhoto =  new MagicalTakePhoto(ProductList.this,3000);
              //  magicalTakePhoto.takePhoto("ORDER_ATTACHMENT");
            }
        });


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    public void onClick(DialogInterface dialogBox, int id) {

                        // ToDo get user input here
                        //Party/customer password check
                        // intent party/customer password from MainActivity***

                     //   if(!userInputDialogEditText.getText().toString().isEmpty())
                            note=""+userInputDialogEditText.getText().toString().trim()+"";
                            payto=paytotext.getText().toString().trim ().split("\\-")[0];
                            shipto=shiptotext.getText().toString().trim().split("\\-")[0];
                            ordertype=order_type.getText().toString().trim();
                            pricelist=price_list.getText().toString().trim();

                            System.out.println("Return and processing"+payto);
                            Log.e("Return Additional info","PayTo\t"+payto);
                            //not using
                            SendOrder(view, image_str, note,payto,shipto);


                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();




    }
//-------------------------------------------------------------------------------------------------
    //BILL location
    private void getpartyBillLocataion(final String party_code) {

        try
        {
            // AppConfig.emp_code  =  "2288";//ID.toString();
            //org_id   ="102";

            HttpClient httpclient = new DefaultHttpClient();
            URL sourceUrl = new URL(AppConfig.URL_BILL_LOC+"?party_code="+party_code+"&org_id="+org_id);
            HttpPost httppost = new HttpPost(""+sourceUrl);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.i("Pass 1", "connection success: "+sourceUrl+"With"+ party_code);
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
            Log.e("Pass 2", "connection success "+party_code);
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONArray JA=new JSONArray(result);
            JSONObject json= null;
            final String[] str1 = new String[JA.length()];
            for(int i=0;i<JA.length();i++)
            {
                json=JA.getJSONObject(i);
                // str1[i]=json.getString("NAME");
                str1[i]=json.getString("ID");
            }



            paytotext.setDropDownBackgroundDrawable(new ColorDrawable(getBaseContext().getResources().getColor(R.color.yellow)));
            final List<String> list = new ArrayList<String>();
            list.clear();

            for(int i=0;i<str1.length;i++)
            {
                list.add(str1[i]);
            }
            HashSet hs = new HashSet();
            hs.addAll(list);
            list.clear();
            list.addAll(hs);
            Collections.sort(list);
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            paytotext.setThreshold(2);
            paytotext.setAdapter(dataAdapter);
            paytotext.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1  , int ClikedPosition, long arg3) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getBaseContext(), list.get(arg2).toString(),Toast.LENGTH_SHORT).show();
                    if (paytotext.getText().toString() != "") {
                        //  shipto = shiptotext.getText().toString().split("\\-")[0];

                        String ship_code=dataAdapter.getItem(ClikedPosition);
                        //Setting to auto complete text view
                        paytotext.setText(ship_code);
                        payto =ship_code.split("\\-")[0];
                        Log.i("Selected Shipping",""+paytotext+" - "+ship_code.split("\\-")[0]);
                        //  ShowPartyInfo(party_code.split("\\-")[0]);
                    }
                    else{
                        paytotext.setText("");
                    }
                }
            });


        }

        catch(Exception e)
        {
            Log.e("Fail 3",AppConfig.URL_BILL_LOC+""+ e.toString());
            paytotext.setError("Billing Location Not Found");
        }


    }
//shipment location
    private void getpartyShipLocation( final String party_code) {
        try
        {
            // AppConfig.emp_code  =  "2288";//ID.toString();
            //org_id   ="102";

            HttpClient httpclient = new DefaultHttpClient();
            URL sourceUrl = new URL(AppConfig.URL_SHIP_LOC+"?party_code="+party_code+"&org_id="+org_id);
            HttpPost httppost = new HttpPost(""+sourceUrl);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
             Log.i("Pass 1", "connection success: "+sourceUrl+"With"+ party_code);
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
            Log.e("Pass 2", "connection success "+party_code);
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONArray JA=new JSONArray(result);
            JSONObject json= null;
            final String[] str1 = new String[JA.length()];
            for(int i=0;i<JA.length();i++)
            {
                json=JA.getJSONObject(i);
                // str1[i]=json.getString("NAME");
                str1[i]=json.getString("ID");
            }


            shiptotext.setDropDownBackgroundDrawable(new ColorDrawable(getBaseContext().getResources().getColor(R.color.bg_main)));
            final List<String> list = new ArrayList<String>();
            list.clear();

            for(int i=0;i<str1.length;i++)
            {
                list.add(str1[i]);
            }
            HashSet hs = new HashSet();
            hs.addAll(list);
            list.clear();
            list.addAll(hs);
            Collections.sort(list);
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            shiptotext.setThreshold(2);
            shiptotext.setAdapter(dataAdapter);
            shiptotext.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                 @Override
                public void onItemClick(AdapterView<?> arg0, View arg1  , int ClikedPosition, long arg3) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getBaseContext(), list.get(arg2).toString(),Toast.LENGTH_SHORT).show();
                    if (shiptotext.getText().toString() != "") {
                      //  shipto = shiptotext.getText().toString().split("\\-")[0];

                        String ship_code=dataAdapter.getItem(ClikedPosition);
                        //Setting to auto complete text view
                        shiptotext.setText(ship_code);
                        shipto =ship_code.split("\\-")[0];
                        Log.i("Selected Shipping",""+shiptotext+" - "+ship_code.split("\\-")[0]);
                      //  ShowPartyInfo(party_code.split("\\-")[0]);
                    }
                    else{
                        shiptotext.setText("");
                    }
                }
            });



        }
        catch(Exception e)
        {
            Log.e("Fail 3",AppConfig.URL_SHIP_LOC+""+ e.toString());
            shiptotext.setError("Shipment Location Not Found");
        }

    }


    private void ShowError() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        fab_save.startAnimation(shake);
        fab_save.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorRed), PorterDuff.Mode.MULTIPLY);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // imageView.setImageBitmap(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream); //compress to which format you want.
                attachimg = photo;
                byte[] byte_arr = stream.toByteArray();
                image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                Log.e("Camera Result", "Is: " + image_str);
                imgattch.setImageBitmap(attachimg);
                //Saving image to file
                String filename="DOAttachment.jpeg";
                createDirectoryAndSaveFile(attachimg , filename);

                    }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        Log.e("DIRECOTRY","Creating Directory Please wait..");

        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/DirName/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        Log.e("DIRECOTRY","Dir:"+direct+" File: "+file);

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //Log.e("DIRECOTRY","File type "+file.substring(file.lastIndexOf(".")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //NOt using
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void SendOrder(View view, String image_str, final String  note, final String payTo, final String shipTo) {
        try {

            OrderHeap.clear();
            OrderList.clear();
            ol.clear();
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            // SimpleDateFormat
            //echo $_POST['order_date'] =		date("d-M-Y H:i:s");*/
            //TIMESTAMP
            SimpleDateFormat smpldate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String strDate = smpldate.format(new Date());
            final String  order_date = strDate;
            final String timestamp =  new java.text.SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());
            order_by = emp_code;

            //String party_code = party_code;

            is_active = "Y";
            int itemsCount = listCollege.getChildCount();
            for (int i = 0, count = itemsCount; i < count; ++i) {

                view = listCollege.getChildAt(i);
                mob_order_id = "" + year + "" + month + "" + i;

                String PQNTY = ((EditText) view.findViewById(R.id.prod_qnty)).getText().toString().trim();
                String PSL = ((TextView) view.findViewById(R.id.prod_sl)).getText().toString().toString();
                String PPRICE=((EditText) view.findViewById(R.id.prod_price)).getText().toString().toString();
                prod_code = ((TextView) view.findViewById(R.id.prod_code)).getText().toString().toString();
                prod_name = ((TextView) view.findViewById(R.id.adapter_text_title)).getText().toString().toString();
                uom = ((TextView) view.findViewById(R.id.prod_uom)).getText().toString().toString();
                qnty = PQNTY;
                prod_price=PPRICE;

                Log.e("Insert Item count", "" + i + ">" + PSL + ">>" + PQNTY+" Time Stamp:"+timestamp);
                //  OrderHeap.put(PSL, PQNTY);
                // OrderList.add(OrderHeap);
                //String mob_order_id,String prod_code,String prod_name ,String uom,String qnty,String order_by,String org_id,String party_code,String order_date,String is_active
                //Local  db\


                if (!qnty.isEmpty()&&!qnty.equals("0")){
                    Log.e ("Inserting valuse",": "+mob_order_id+"-"+prod_code+"-"+ prod_name+"-"+ uom+"-"+ qnty+"-"+ order_by+"-"+ org_id+"-"+ party_code+"-"+ order_date+"-"+ is_active);
                    //loaca db
                    OrderHeap.put(PSL, PQNTY);
                    OrderList.add(OrderHeap);
                    //    db.storeOrder(mob_order_id, prod_code, prod_name, uom, qnty, order_by, org_id, party_code, order_date, is_active);
                    //main db

                    if(!revieworeder.isEmpty()){
                    UpdateOrderRemoteDb(revieworeder,prod_code,qnty,order_by,prod_price);
                        Log.e("update order: ",""+revieworeder+"\tqnty"+qnty+"\tprice "+prod_price+"\t for Product "+prod_code);
                    }
                    else{
                       StoreOrdertoRemoteDb(OrderHeap, prod_code, prod_name, uom, qnty,prod_price, order_by, org_id, party_code,order_date,image_str,note,payTo,shipTo);
                        Log.e("Must be Address",""+payTo+"\t"+shipTo);
                    }
                }
                else{
                   final Snackbar sb= Snackbar.make(view, "Quantity or Price Missing", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
                            .setAction("Action", null);
                    View snackBarView = sb.getView();
                    snackBarView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackBarView.setBackgroundColor(Color.RED);
                    sb.show();
                }


            }
            //  SendEmail(ORD_ID,image_str,party_code,order_by,attachimg,note);
            //refresh on successfull order
            for (int i = 0, count = itemsCount; i < count; ++i) {
                Log.e("Delete Item count",""+i);
                view = listCollege.getChildAt(i);

                ((EditText)view.findViewById(R.id.prod_qnty)).setText("0");
                ((EditText)view.findViewById(R.id.prod_price)).setText("0.00");

            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("Error on inserting",e.toString());
        }



    }
//--------------------------------------------------------------------------------------------------
    private void UpdateOrderRemoteDb(final String rev_order_id, final String prod_code, final String qnty, final String order_by,final  String prod_price) {
        // Tag used to cancel the request
        final String tag_string_update = "req_update";
        final int eamil_count=1;
        final String final_order;
        pDialog.setTitle(" UPDATING ORDER !");
        pDialog.setIcon(R.drawable.ic_update);
        pDialog.setMessage("Please Wait ...");
        showDialog();
       StringRequest strUpdate = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Updating Order", "DO Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.e("JSON RESPONSE : ",""+error);
                    if (!error) {
                        ORD_ID = jObj.getString("order");
                        final String final_order=ORD_ID;
                        Log.e("Order Number" ,ORD_ID+" Generated ");
                        ConfirmToBack(final_order);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Update Order", "DO Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                //mob_order_id, prod_code, prod_name, uom, qnty, order_by, org_id, party_code, order_date, is_active
                //  params.put("mob_order_id", mob_order_id);

                params.put("prod_code", prod_code);
                params.put("qnty", qnty);
                params.put("order_by", order_by);
                params.put("rev_order_id", rev_order_id);
                params.put("prod_price",prod_price);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strUpdate, tag_string_update);


    }

    //$prod_code, $prod_name, $uom, $qnty,$order_by,$org_id,$party_code
private String StoreOrdertoRemoteDb(final HashMap OrderHeap, final  String prod_code,final String prod_name ,
                                    final String uom,final String qnty,final String prod_price, final String order_by,final String org_id,
                                    final String party_code ,final  String order_date, final  String image_str, final String Note, final String PayTo, final String ShipTo) {
// Tag used to cancel the request
        final String tag_string_req = "req_register";
        final int eamil_count=1;
        final String final_order;
        pDialog.setTitle(" SENDING ORDER !");
        pDialog.setIcon(R.drawable.ic_menu_send);
        pDialog.setMessage("Please Wait ...");
        showDialog();


       // if(revieworeder.isEmpty()){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PRODUCT_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Sending Order", "DO Response: " + response.toString());
                hideDialog();
              //  lm.hide();
             /*   JSONObject FirstArray = jArray.getJSONObject(0); // first Array in JSONArray
                JSONObject data = FirstData.getString("data");
                String FirstStatusValue = data.getString("Status");*/
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.e("JSON RESPONSE : ",""+error);
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                         ORD_ID = jObj.getString("order");
                        final String final_order=ORD_ID;
                        Log.e("Order Number" ,ORD_ID+" Generated ");
                       // JSONObject user = jObj.getJSONObject("user");
                       // String name = user.getString("name");
                      //  String email = user.getString("email");
                      //  String phone = user.getString("phone");
                     //  String created_at = user.getString("created_at");
                     //  String created_at = user.getString("created_at");
                        // Inserting row in users table
                        db.storeOrder(ORD_ID, prod_code, prod_name, uom, qnty, order_by, org_id, party_code, order_date, is_active);


                      // if (!SendEmail(final_order,image_str,party_code,order_by,attachimg,note).isEmpty()) {
                   //     Log.e("ORDERNUMBER LENGTH",": "+ORD_ID.length());

                        if ( final_order==ORD_ID) {
                            if (!SendEmail(final_order,image_str,party_code,order_by,attachimg,note).isEmpty()) {
                                ConfirmToBack(final_order);
                            }
                        }


                      //  Log.e("Emailcount"," >"+i);

                     //  }

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Sending Order", "DO Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                //mob_order_id, prod_code, prod_name, uom, qnty, order_by, org_id, party_code, order_date, is_active
              //  params.put("mob_order_id", mob_order_id);

                params.put("prod_code", prod_code);
                params.put("prod_name", prod_name);
                //params.put("hasvalue",OrderHeap);
                params.put("uom", uom);
                params.put("qnty", qnty);
                params.put("prod_price", prod_price);
                params.put("order_by", order_by);
                params.put("org_id", org_id);
                params.put("party_code", party_code);
                params.put("order_date", order_date);
                params.put("image_str", ""+image_str);
                params.put("note", ""+note);
                params.put("payto", ""+PayTo);
                params.put("shipto", ""+ShipTo);
                params.put("order_type", ""+saletype.getText().toString().trim());
                 return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return final_order =ORD_ID;

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();}

    }
    private void ConfirmToBack(final String oid){
       // SendEmail(ORD_ID,image_str,party_code,order_by);

        FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(ProductList.this)
                //alert.setImageRecourse(R.mipmap.ic_launcher)
                .setTextTitle("Order Submitted").setBackgroundColor(R.color.bg_pink).setTitleColor(R.color.azure)
                .setTextSubTitle("Your Order Number is: \n"+oid+"").setSubtitleColor(R.color.white)
                .setBody("| Now you can select new customer(party) for new order |").setBodyColor(R.color.white)
                .setNegativeColor(R.color.white)
                .setNegativeButtonText("NO")
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        //SendEmail(oid, image_str, party_code, order_by, attachimg, note);
                        //ProductList.this.finish();
                    }
                })
                .setPositiveButtonText("OK")
                .setPositiveColor(R.color.white)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        //Toast.makeText(MainActivity.this, "Updating", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Opening Email Client", Toast.LENGTH_LONG).show();
                       // if(!SendEmail(oid, image_str, party_code, order_by, attachimg, note).isEmpty()&& SendEmail(oid, image_str, party_code, order_by, attachimg, note)=="TRUE"){
                            Toast.makeText(getApplicationContext(), "Order Successfully Sent!", Toast.LENGTH_LONG).show();

                           Intent intent = new Intent(ProductList.this,
                                    MainActivity.class);//Add the bundle to the intent
                            intent.putExtra("org_id",org_id);
                            intent.putExtra("emp_code",ID);
                            intent.putExtra("party_code",party_code);
                            startActivity(intent);
                          //  Toast.makeText(getApplicationContext(), "Email sent Successfully Sent!", Toast.LENGTH_LONG).show();

                       // }


                    }
                })
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(false).setButtonsGravity(PanelGravity.CENTER)
                .build();
        alert.show();
    }
    //send email

    @NonNull
    private String SendEmail(String oid, final String img, final String party_code, final String order_by, final Bitmap attachimg, final String note) {
        try {
            imgattch.setDrawingCacheEnabled(true);

            String filename="DOAttachment.jpeg";
            String subject ="Order "+oid+" has been sent successfully";
            String Addrs= getAddress(LoginActivity.finallat, LoginActivity.finallong);
       //     dbDataView.setText(Html.fromHtml(branchInfoString ));
            String username= MainActivity.Nameview.getText().toString().trim();
            String phonenumber= PhoneView.getText().toString().trim();
            String body="\nDear Sir, \n"+"Please find the attached file herewith.\nThe D.O for the customer(party) "+party_code+" has been successfully done with the order number: "+oid+"\n\n\n"+"With regards, \n \n Employee code/ID: "+order_by+"\n Employee "+username+"\n"+phonenumber+"\n Location: "+Addrs+"\n\n N.B: "+note;

            //  Uri contentUri = FileProvider.getUriForFile(this, "com.metrosoft.arafat.salebook", newFile);
            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            filelocation.setReadable(true,false);
            Uri path = Uri.fromFile(filelocation);
            ArrayList<Uri> uris = new ArrayList<>();
            uris.clear();
            String filepath= ""+Environment.getExternalStorageDirectory().getAbsolutePath()+"/DirName/"+filename;
          //  uris.add(Uri.parse("file://" + filepath));
//File
         /*   File file = new File(new File("/sdcard/DirName/"), filename);
            file.setReadable(true);
            uris.add(Uri.parse("file://" + file));
            Log.e("File path","\t"+file+" \tSize:"+uris.size());

            String imageName = file.getName();
            Log.e("Attachment","Path: \t"+file+"\t name:\t"+imageName);*/


//or
            File root = Environment.getExternalStorageDirectory();
            String pathToMyAttachedFile = "/DirName/"+filename;
            File file = new File(root, pathToMyAttachedFile);
         //   file.setReadable(true,false);
            Log.e("File path"," "+file);
            Uri uri = Uri.fromFile(file);
          sendEmailAlert(file,subject,body);

       ;

            //using outlook

         /*   Intent intent = new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_SINGLE_TOP);
            intent.setType("application/octet-stream"); // or use intent.setType("message/rfc822);
            Uri data = Uri.parse("mailto:?subject=" + subject + "&body=" + body );
            intent.setData(data);
            intent .putExtra(Intent.EXTRA_EMAIL, toemail);
            if (!file.exists() || !file.canRead()||!file.canWrite()) {
                Log.e(" FILE ERROR ","File Not found");
                Toast.makeText(getApplicationContext(),"File Not found",Toast.LENGTH_LONG).show();
            }
            else {
                //file.setReadable(true);
                Log.e(" FILE OK ","File was found");

                //Uri uri = Uri.fromFile(file);

                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }*/



            //USING GMAIL
            /*      Intent emailIntent = new Intent(Intent.ACTION_SEND,Uri.parse("mailto:"));
            // set the type to 'email'
            emailIntent.setType("message/rfc822");
            // emailIntent .setType("vnd.android.cursor.dir/email");
            // emailIntent.setType("image/*");
            //  emailIntent.setAction(Intent.ACTION_GET_CONTENT);
            // String to[] = {"arafat_it@outlook.com","tareqhabib@nationalpolymer.net"};
            emailIntent .putExtra(Intent.EXTRA_EMAIL, toemail);
            // the attachment
            //emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+filelocation));

            // the mail subject
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Order "+oid+" has been sent successfully");
            //the emial body
            emailIntent .putExtra(Intent.EXTRA_TEXT, "\nDear Sir, \n"+"Please find the attached file herewith.\nThe D.O for the customer(party) "+party_code+" has been successfully done with order number "+oid+"\n\n\n"+"With regards \n \n Employee code/ID: "+order_by+"\n\nN.B:"+note);
            //emailIntent .putExtra(Intent., "Order "+oid+" has been sent successfully");
            startActivity(Intent.createChooser(emailIntent , "Send to Head Office"));*/


//using SMTP
       /*     Mail m = new Mail(email,emial_pass );
            String[] toArr = toemail;
            m.setTo(toArr);
            m.setFrom(email);
            m.setSubject(subject);
            m.setBody(body);

            String true_false;
                if(m.send()) {
                    true_false="TRUE";
                    Toast.makeText(ProductList.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    true_false="FALSE";
                    Toast.makeText(ProductList.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            return true_false;*/
            //Toast.makeText(getApplicationContext(),"Email Send",Toast.LENGTH_LONG).show();
//using library
        /*
            Configuration  configuration = new Configuration()
                    .domain("mail.nationalpolymer.net")
                    .apiKey("key-xxxxxxxxxxxxxxxxxxxxxxxxx")
                    .from("Test account", "postmaster@somedomain.com");
            Mail.using(configuration)
                    .to("arafat_it@outlook.com")
                    .subject(subject)
                    .text(body)
                    .multipart()
                    .attachment(new File(filepath))
                    .build()
                    .send();*/

            return "TRUE";


        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            return "MAIL NOT SENT";
        }
    }

    private String getAddress(double lat, double lng) {
        GPSTracker   gps = new GPSTracker(ProductList.this);
        Geocoder geocoder = new Geocoder(ProductList.this, Locale.getDefault());
        String address="";
        try {
            List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
            Address obj = addresses.get(0);
            String  add = obj.getAddressLine(0);
          /*  add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

            Log.e("Location: ", "Address" + add);
            address=add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }
    private void sendEmailAlert(File fileName,String subject,String text) {

        final File file=fileName;
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_CLEAR_TOP);

       // intent.setType("message/email");
        intent.setType("message/rfc822");

        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                String packageName = resolveInfo.activityInfo.packageName;
                Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                targetedShareIntent.setType("message/rfc822");
                targetedShareIntent.putExtra(Intent.EXTRA_EMAIL, toemail);
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                if (!file.exists() || !file.canRead()) {
                    Toast.makeText(getApplicationContext(), "Attachment Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri uri = Uri.fromFile(file);
                targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);

                if (TextUtils.equals(packageName, "com.microsoft.office.outlook")) {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                } else {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text+" \n [Outlook Email client Not found]");
                }
                targetedShareIntent.setPackage(packageName);
                targetedShareIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                targetedShareIntents.add(targetedShareIntent);
            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
            if (chooserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooserIntent);
            }
        }

        //intent.setType("email/html");
        // only email apps should handle this
        //  Intent intent = new Intent(Intent.ACTION_SEND);
        //  intent.setType("application/octet-stream"); // or use
        /*    intent.putExtra(Intent.EXTRA_EMAIL, toemail);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (!file.exists() || !file.canRead()) {
        Toast.makeText(getApplicationContext(), "Attachment Error", Toast.LENGTH_SHORT).show();
        return;
        }
        Uri uri = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
        startActivity(Intent.createChooser(intent, "Choose Outlook Email.."));

        }*/

    }
    /*----------------------------Order sending close--------------------------------------------------------------------*/
    //Discount
    public  void DiscountapplyAll( View v) {
        try{
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.product_list_discount_promo, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.prodDiscount);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Apply",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                Applydiscount(""+userInput.getText().toString().trim());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    } catch(Exception e){
            Log.e("Discount Popup","Exception\t"+e.toString());
        }

    }

//Discount apply
    private void Applydiscount(String discount) {
        try {
            int itemscnt = listCollege.getAdapter().getCount();
            Log.e("Total list items",""+itemscnt);
            for (int i = 0, count = itemscnt; i < count; ++i) {

                ViewGroup view = (ViewGroup) listCollege.getChildAt(i);
                ((EditText)view.findViewById(R.id.prodDis)).setText(""+discount);
                Log.e("New Discount", i+">"+":Apply to all:\t" + discount);

            }

         //   make(v, "Applying Discount for all items", Snackbar.LENGTH_LONG)   .setAction("Action", null).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


     /*----------------------------Order VIEW start--------------------------------------------------------------------*/
//Not using
    public void showOrder(final View view){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);

        View mView = layoutInflaterAndroid.inflate(R.layout.usert_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        TextView DailogTitle = (TextView)mView.findViewById(R.id.dialogTitle);
        DailogTitle.setText("Review Order?");
        TextView DailogMessage = (TextView)mView.findViewById(R.id.dialogMsg);
        DailogMessage.setText("Order is Now Ready to Review. Please input Order Number Below ");
        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInput);

        userInputDialogEditText.setVisibility(View.VISIBLE);

        userInputDialogEditText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_FILTER);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("View", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        //Party/customer password check
                        // intent party/customer password from MainActivity***
                        if(!userInputDialogEditText.getText().toString().isEmpty())
                            ViewOrder (userInputDialogEditText.getText().toString().trim());
                        dialogBox.cancel();
                            //SendOrder(view);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }
    private void ViewOrder( String Order) {
        new GetReveiwProducts(this).execute(Order);
    }
    /*----------------------------Order VIEW End--------------------------------------------------------------------*/
    public  void delOrder(View view){

        int itemsCount = listCollege.getChildCount();
     /*   for (int ii1 = 0; ii1 < itemsCount; ii1++) {
            View view12 = listCollege.getChildAt(ii1);
            int itemquantity = Integer.parseInt(((EditText) view12.findViewById(R.id.prod_qnty)).getText().toString());

            Toast.makeText(getApplicationContext(),"ss"+itemquantity,Toast.LENGTH_LONG).show();

        }*/
       // ViewGroup group = (ViewGroup)findViewById(R.id.listCollege);
        for (int i = 0, count = itemsCount; i < count; ++i) {
            Log.e("Delete Item count",""+i);
             view = listCollege.getChildAt(i);

                ((EditText)view.findViewById(R.id.prod_qnty)).setText("0");
            //((EditText)view.findViewById(R.id.prod_price)).setText("0.00");
        }

        make(view, "Clearing Order", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }



/*-------------------------------------------End button event--------------------------------------------------------------------------*/

   // showing products
   //1 (ALL PROUDCUTRS)
    private  class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        private Context context;
        String result;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            try
            {
                URL sourceUrl = new URL(AppConfig.URL_PRODUCT_LIST+"?cat_id="
                        + cat_id + "&org_id=" + org_id );
                HttpService httpService = new HttpService(""+sourceUrl);

                httpService.ExecutePostRequest();

                if(httpService.getResponseCode() == 200)
                {
                    result = httpService.getResponse();
                    Log.d("Result", result);
                    if(result != null)

                    {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(result);

                            JSONObject object;
                            JSONArray array;
                            products prod;

                            //  arrTemp = new String[jsonArray.length()];
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                object = jsonArray.getJSONObject(i);
                                // public products(String psl, String pcode,String pqnty, String pprice, String Name, String Desc, String UOM)
                                prod = new products(""+i,object.getString("ID"),"",object.getString("PRICE"),object.getString("PRODUCT_NAME"),object.getString("PRODUCT_DESCRIPTION"),object.getString("UOM"));

                                prod.prod_sl=""+i;
                              //  prod.prod_code = object.getString("PRODUCT_CODE");
                                prod.prod_code =object.getString("ID");
                                prod.product_name = object.getString("PRODUCT_NAME");
                                prod.product_desc = object.getString("PRODUCT_DESCRIPTION");
                                prod.prod_uom =object.getString("UOM");
                                prod.prod_price = object.getString("PRICE");

                                productList.add(prod);
                                //  arrText[i]=prod.product_name;
                            }

                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpService.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            proCollageList.setVisibility(View.GONE);
            listCollege.setVisibility(View.VISIBLE);
            if(productList != null)
            {
             //   ListAdapter adapter = new ListAdapter(productList, context);
                //AdapterListView adapter = new ListAdapter(productList, context);
                adapter = new ListViewAdapter(context,R.layout.list_adapter_view,productList);
                listCollege.setAdapter(adapter);
            }
        }
    }
    //(PRODUCTS WITH QUANTITY) -------Reveiw Products
    private class GetReveiwProducts extends AsyncTask<String, Void, Void>
    {
        private Context context;
        View mview;
        String result;

        public GetReveiwProducts(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            String identifier = params[0];

            if (!identifier.isEmpty()) {


                try {

                    Log.e("Sending Review Params: ", "Order Number: "+identifier +" Employee: "+emp_code);
                    URL sourceUrl = new URL(AppConfig.URL_REVEIW_PRODUCT + "?mob_order_id=" + identifier + "&emp_code=" + emp_code);
                    HttpService httpService = new HttpService("" + sourceUrl);

                    httpService.ExecutePostRequest();

                    if (httpService.getResponseCode() == 200) {
                        result = httpService.getResponse();
                        Log.d("Result", result);
                        if (result != null)

                        {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(result);

                                JSONObject object;
                                JSONArray array;
                                products prod;
                                productList.clear();

                                //  arrTemp = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                        object = jsonArray.getJSONObject(i);
                                        prod = new products(""+i,object.getString("PROD_CODE"),object.getString("QNTY"),object.getString("PROD_PRICE"),object.getString("PROD_NAME"),object.getString("PRODUCT_DESCRIPTION"),object.getString("UOM"));
                                        prod.prod_sl=""+i;
                                        revieworeder = object.getString("MOB_ORDER_ID");
                                        prod.prod_code = object.getString("PROD_CODE");
                                        prod.product_name = object.getString("PROD_NAME");
                                        prod.product_desc = object.getString("PRODUCT_DESCRIPTION");
                                        // prod.product_desc = object.getString("QNTY");
                                        prod.prod_qnty = object.getString("QNTY");
                                        prod.prod_uom = object.getString("UOM");
                                        prod.prod_price=object.getString("PROD_PRICE");
                                        //     prod.prod_qnty      = object.get("QNTY");

                                        //  pqnty.setText (""+object.getString("QNTY"));
                                        productList.add(prod);
                                        //  arrText[i]=prod.product_name;
                                        Log.e("Reveiw Products: ", revieworeder+" sl" + i + " name: " + object.getString("PROD_NAME") + " Qntity: " + object.getString("QNTY"));
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {

                        Toast.makeText(context, "NO MATCH FOUND "+""+httpService.getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            proCollageList.setVisibility(View.GONE);
            listCollege.setVisibility(View.VISIBLE);
            if(productList != null)
            {
              //  ListAdapter adapter = new ListAdapter(productList, context);
                adapter = new ListViewAdapter(context,R.layout.list_adapter_view,productList);
                listCollege.setAdapter(adapter);
            }
        }
    }

    public class HttpService
    {
        private ArrayList<NameValuePair> params;
        private ArrayList <NameValuePair> headers;

        private String url;
        private int responseCode;
        private String message;
        private String response;

        public String getResponse()
        {
            return response;
        }

        public String getErrorMessage()
        {
            return message;
        }

        public int getResponseCode()
        {
            return responseCode;
        }

        public HttpService(String url)
        {
            this.url = url;
            params = new ArrayList<NameValuePair>();
            headers = new ArrayList<NameValuePair>();
        }

        public void AddParam(String name, String value)
        {
            params.add(new BasicNameValuePair(name, value));
        }

        public void AddHeader(String name, String value)
        {
            headers.add(new BasicNameValuePair(name, value));
        }

        public void ExecuteGetRequest() throws Exception
        {
            String combinedParams = "";
            if(!params.isEmpty())
            {
                combinedParams += "?";
                for(NameValuePair p : params)
                {
                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                    if(combinedParams.length() > 1)
                    {
                        combinedParams  +=  "&" + paramString;
                    }
                    else
                    {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request = new HttpGet(url + combinedParams);
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request, url);
        }

        public void ExecutePostRequest() throws Exception
        {
            HttpPost request = new HttpPost(url);
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if(!params.isEmpty())
            {
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }

            executeRequest(request, url);
        }

        private void executeRequest(HttpUriRequest request, String url)
        {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse;
            try
            {
                httpResponse = client.execute(request);
                responseCode = httpResponse.getStatusLine().getStatusCode();
                message = httpResponse.getStatusLine().getReasonPhrase();

                HttpEntity entity = httpResponse.getEntity();
                if (entity != null)
                {
                    InputStream instream = entity.getContent();
                    response = convertStreamToString(instream);
                    instream.close();
                }
            }
            catch (ClientProtocolException e)
            {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            }
            catch (IOException e)
            {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            }
        }

        private String convertStreamToString(InputStream is)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try
            {
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }



}
