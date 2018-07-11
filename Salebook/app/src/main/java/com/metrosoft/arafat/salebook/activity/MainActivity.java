package com.metrosoft.arafat.salebook.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.app.AppConfig;
import com.metrosoft.arafat.salebook.app.AppController;
import com.metrosoft.arafat.salebook.helper.PushMsg;
import com.metrosoft.arafat.salebook.helper.SQLiteHandler;
import com.metrosoft.arafat.salebook.helper.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.core.LGSnackbarAction;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.metrosoft.arafat.rxpad.R.id.party_list;
import static com.metrosoft.arafat.salebook.activity.LoginActivity.gpslat;
import static com.metrosoft.arafat.salebook.activity.LoginActivity.gpslong;
import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.ERROR;
import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.INFO;
import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.WARNING;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
        private static final String TAG = RegisterActivity.class.getSimpleName();
        FloatingActionButton fab_save, fab_del, fab_scan, fab_back, fab_next,fab_finish;
        //  AutoCompleteTextView actv;
        InputStream is=null;
        String result=null;
        String line=null;
        private Spinner cust;
        private String selectedcustomer, selected_cust;
        private ProgressDialog pDialog;
        private static String ID,name,phone,email,org_id,party_code;
        public  static String scan_result,ot,pl,parent_cat,report_for,salesrep_id;
        public static TextView Nameview,PhoneView;
        private SessionManager session;
        public static   AutoCompleteTextView text,order_type_text,price_list_text;
        public static TextView cust_id,cust_name,cust_phone,cust_address,saletype;

        static Button targetChart,achiveChart,saleChart;
        public  static  EditText  bank_check,deli_date,transport;
        private  static Spinner mode_of_payment,Trns,warehouse;
        private ZXingScannerView mScannerView;
        static   SQLiteHandler localdb;
        private Uri fileUri;
        Camera camera  =new Camera();
        static Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        LGSnackbarManager.prepare(getApplicationContext(),        LGSnackBarThemeManager.LGSnackbarThemeName.MATERIAL);
        // define Font-awesome
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        text =(AutoCompleteTextView)findViewById(R.id.party_list);

        //get user info
        // Bundle bundle = getIntent().getExtras();
        final Intent intent = getIntent();
        if (intent.hasExtra("uid")) {

            ID = intent.getStringExtra("uid").replaceAll("[^0-9]", "");
            name = intent.getStringExtra("uname").replaceAll("[\\\"\\+\\.\\^:,]\\-", "");
            phone = intent.getStringExtra("uphone").replaceAll("[^0-9]", "");
            email = intent.getStringExtra("uemail").replaceAll("[\\\"\\+\\.\\^:,]\\-", "");
            org_id = intent.getStringExtra("org_id").replaceAll("[^0-9]", "");
            party_code= intent.getStringExtra("party_code").replace("\"", "").replaceAll("[^0-9]", "");
            salesrep_id=intent.getStringExtra("salesrep_id").replace("\"", "").replaceAll("[^0-9]", "");
        }
        if (intent.hasExtra("party_code")) {
            party_code = intent.getStringExtra("party_code");

        } else {
            party_code = "Not Found!";
          //  parent_cat="null";
        }
        Log.e("New User Details: ", "salesrep_id\t"+salesrep_id+"\tParty Code: " + party_code + "  ID: " + ID + " Name: " + name + " Phone: " + phone + " Email: " + email + "ORG_ID:" + org_id + ">>" + gpslat + "-" + gpslong);



        DrawerLayout co=(DrawerLayout)findViewById(R.id.drawer_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) co.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        //setting pie chart button
        targetChart = (Button) findViewById(R.id.targetChart);
        achiveChart = (Button) findViewById(R.id.achiveChart);
        saleChart = (Button) findViewById(R.id.saleChart);
        saletype =(TextView)findViewById(R.id.cust_sale_type);


//setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        cust = (Spinner) findViewById(R.id.customer);
        cust.setPrompt("Select Customer");
        //define toolbar
        setSupportActionBar(toolbar);
        //define autocomplete text view
        // actv = (AutoCompleteTextView) findViewById(R.id.party_list);
//getting information
        getPartyList(ID, org_id);
        TouchView Tv;
        getAllProducts(org_id);

        // CreateDir();
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        fab_next = (FloatingActionButton) findViewById(R.id.fab);
        fab_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!text.getText().toString().isEmpty()) {
                    PartyConfPopup(org_id);
                }
                else {
                    text.setError("Customer Must be selected.");
                }
              //  Snackbar.make(view, "Processing. Please wait...", Snackbar.LENGTH_LONG).setAction("Action", null).show();


                //finish();
            }
        });

        fab_finish=(FloatingActionButton)findViewById(R.id.fab_finish);
        fab_finish.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                String ordcnt= db.cpountTempOrders(cust_id.getText().toString().replace("\"", "").replaceAll("[^0-9]", ""));
                if(!ordcnt.isEmpty()) {
                    Log.e("Total Taka: ",""+db.getOrderTotal(cust_id.getText().toString().replace("\"", "").replaceAll("[^0-9]", "")));
                    String total_price =""+db.getOrderTotal(cust_id.getText().toString().replace("\"", "").replaceAll("[^0-9]", ""));
                    Intent intent = new Intent(MainActivity.this,
                            ProductSend.class);//Add the bundle to the intent
                    intent.putExtra("org_id", org_id);
                    intent.putExtra("emp_code", ID);
                    intent.putExtra("total_price",total_price);

                    // cust_id.setText("Party Code   : "+PARTY_CODE.replace("\"","").replaceAll("[^0-9]",""));
                    intent.putExtra("party_code", cust_id.getText().toString().replace("\"", "").replaceAll("[^0-9]", ""));
                    intent.putExtra("cust_phone", cust_phone.getText().toString().replace("\"", "").replaceAll("[^0-9]", ""));
                    startActivity(intent);
                }
                else {
                    LGSnackbarManager.show(WARNING, "No Saved Order Found!");

                }
            }
        });

        fab_del = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //clearcanvs();
                text.clearListSelection();
                text.setText("");
                cust_id.setText("ID");
                if (!cust_id.getText().toString().isEmpty()) {
                    cust_id.setText("Party Code   : ");
                    cust_name.setText("Party Name  : ");
                    cust_phone.setText("Party Phone : ");
                    cust_address.setText("Party Address: ");
                    saletype.setText("Sale type: ");

                }
                Snackbar.make(view, "Clearing Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            //    Intent intent = new Intent(MainActivity.this,GraphViewActivity.class);
            //    startActivity(intent);
            }
        });
        fab_scan = (FloatingActionButton) findViewById(R.id.fab_scan);

        fab_scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Snackbar.make(view, "Scanning Barcode", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                scanImage();


                //text.setText(getIntent().getExtras().getString("party_code"));
                // ShowPartyInfo(party_code);

                /*Bundle b = getIntent().getExtras();
                if (b!=null){
                party_code = b.getString("party_code");
                Log.i("New Pary Code",party_code);}*/
                // startActivity(new Intent(MainActivity.this, ScanActivityMain.class));
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Nameview = (TextView) header.findViewById(R.id.user_name);
        Nameview.setText("Name: " + name.replaceAll("\\[\\+\\,\\]\\-", ""));
        TextView IdView = (TextView) header.findViewById(R.id.user_id);
        IdView.setText("Employee ID: " + ID.replaceAll("\\[\\+\\,\\]\\-", ""));
        PhoneView = (TextView) header.findViewById(R.id.user_phone);
        PhoneView.setText("Cell phone: " + phone.replaceAll("\\[\\+\\,\\]\\-", ""));
        TextView EmailView = (TextView) header.findViewById(R.id.user_email);
        EmailView.setText("Email: " + email.replaceAll("\\[\\+\\,\\]\\-", ""));
        scan_result="false";
        if (scan_result.isEmpty()&&scan_result.equals("false")) {


        } else{
            //Bundle bundle = getIntent().getExtras();
            //bundle.getString("RETURN");
           // party_code="Not found";
            //party_code= bundle.getString("party_code");
            if(!party_code.isEmpty()){
            text.setText("Party: "+party_code);
            }
            else
            {
                text.setText("Party: "+"Not Found");}

            ShowPartyInfo(party_code);
            Log.e("Get Party Code", "" + party_code);
        }
        PushMsg pm= new PushMsg();



    }
//offline products
    private void getAllProducts(final String org_id) {
        if(org_id.equals("101")) {
            String organization = "NPIL";
            LGSnackbarManager.show(WARNING, "Products loading for "+organization+", please wait..");
        }
        else if(org_id.equals("102")) {
            String organization = "NFAL";
            LGSnackbarManager.show(WARNING, "Products loading for "+organization+", please wait..");
        }
        else{
            String organization = "No organization found!";
            LGSnackbarManager.show(ERROR, "ERROR : "+organization);
        }
        localdb = new SQLiteHandler(getApplication());


       // LGSnackbarManager.show(WARNING, "Products loading for "+org_id+", please wait..");

    }

    private void scanImage() {
        // ID + " Name: " + name + " Phone: " + phone + " Email: " + email + "ORG_ID:" + org_id
        Intent i = new Intent(MainActivity.this, QrCodeScannerActivity.class);
        //startActivity(new Intent(MainActivity.this, QrCodeScannerActivity.class));
        i.putExtra("ID",ID);
        i.putExtra("name",name);
        i.putExtra("phone",phone);
        i.putExtra("email",email);
        i.putExtra("org_id",org_id);
        startActivity(i);
        finish();

    }


    @Override
    public void onDestroy(){
    super.onDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();

     //   mScannerView.stopCamera();           // Stop camera on pause
    }


    //populating data(custermers/party)
//pass parameter to get exact prty
    private  void getPartyList(final String emp,final String org){
        try
        {
           // AppConfig.emp_code  =  "2288";//ID.toString();
            //org_id   ="102";

            HttpClient httpclient = new DefaultHttpClient();
            URL sourceUrl = new URL(AppConfig.URL_CUSTMER_LIST+"?org_id="
                    + org + "&ID=" + emp );
            HttpPost httppost = new HttpPost(""+sourceUrl);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("Pass 1", "connection success "+sourceUrl+"With"+ AppConfig.emp_code +""+ org_id );
            Log.i("Pass 1", "connection success "+sourceUrl+"With"+ emp +""+org);
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
            Log.e("Pass 2", "connection success ");
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

            text = (AutoCompleteTextView)
                    findViewById(party_list);
            text.setDropDownBackgroundDrawable(new ColorDrawable(getBaseContext().getResources().getColor(R.color.colorPrimary)));
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
            text.setThreshold(2);
            text.setAdapter(dataAdapter);
            text.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int ClikedPosition, long arg3) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getBaseContext(), list.get(arg2).toString(),Toast.LENGTH_SHORT).show();
                    if (text.getText().toString() != "") {
                        selectedcustomer = text.getText().toString();
                        selected_cust = text.getText().toString();
                        String party_code=dataAdapter.getItem(ClikedPosition);
//Setting to auto complete text view
                        text.setText(party_code);
                        Log.i("Selected customer",""+selectedcustomer+" - "+party_code.split("\\-")[0]);
                        ShowPartyInfo(party_code.split("\\-")[0]);
                    }
                    else{
                    text.setText("");
                    }
                }
            });


        }
        catch(Exception e)
        {
            Log.e("Fail 3",AppConfig.URL_CUSTMER_LIST+""+ e.toString());
        }


    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void ShowPartyInfo(final String party_code) {
        Log.e("Showing Party","Party Information Showing");
        String tag_string_req = "req_customers";
        pDialog.setMessage("Getting Information ...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_CUSTMER_INFO+""+party_code, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Customer Response: " + response.toString());
                Log.i(TAG, "URL: " +  AppConfig.URL_CUSTMER_INFO+""+party_code);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session

                        // Now store the user in SQLite
                        String PARTY_CODE = jObj.getString("id");

                        JSONObject customerinfo = jObj.getJSONObject("customerinfo");
                        String custname = customerinfo.getString("name");
                        String custphone = customerinfo.optString("phone");
                        if (custphone.isEmpty()){
                            custphone ="+880";
                        }
                        String org_id = customerinfo.getString("org_id");
                        String address = customerinfo.getString("address");
                        String territory_id = customerinfo.getString("territory_id");
                        String party_sale_type = customerinfo.getString("saletype");
                        Log.i("CustomerInfo",""+PARTY_CODE+","+custname+","+custphone+","+address+","+org_id+","+territory_id);
                        cust_name = (TextView)findViewById(R.id.cust_name);
                        cust_id = (TextView)findViewById(R.id.cust_id);

                        cust_phone = (TextView)findViewById(R.id.cust_phone);
                        cust_address = (TextView)findViewById(R.id.cust_address);
                        cust_id.setText("Party Code   : "+PARTY_CODE.replace("\"","").replaceAll("[^0-9]",""));
                        cust_name.setText("Party Name  : "+custname.replace("\"",""));
                        cust_phone.setText("Party Phone : "+custphone.replace("\"","").replaceAll("[^0-9]",""));
                        cust_address.setText("Party Address: "+address.replace("\"",""));
                        saletype.setText("Sale Type: "+party_sale_type.replace("\"",""));

                      //  mScannerView.stopCamera();
                        // Inserting row in users table

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                      //  mScannerView.stopCamera();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Log.e(TAG, "URL: " +  AppConfig.URL_CUSTMER_INFO+""+party_code);
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
             //  params.put("email", email);
             //  params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }
    //creating deirctory
        private void CreateDir() {
        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName/");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        String fileName="Products";
        File file = new File(new File("/sdcard/DirName/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
           // imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    //    Toast.makeText(MainActivity.this,"Dirctory Created :"+""+Environment.getExternalStorageDirectory().toString(),Toast.LENGTH_LONG).show();
    }


    private void PartyConfPopup(final String org_id) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        final View mView = layoutInflaterAndroid.inflate(R.layout.usert_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        TextView DailogTitle = (TextView)mView.findViewById(R.id.dialogTitle);
        DailogTitle.setText("CONFIRM CUSTOMER");
        TextView DailogMessage = (TextView)mView.findViewById(R.id.dialogMsg);
        party_code=cust_id.getText().toString().trim().replace("\"", "").replaceAll("[^0-9]", "");
        DailogMessage.setText(party_code+" - Was Selected, Want to make an Order?");
        parent_cat="null";
        mode_of_payment =(Spinner)mView.findViewById(R.id.mode_of_payment);
        bank_check = (EditText)mView.findViewById(R.id.bank_check);
        deli_date = (EditText)mView.findViewById(R.id.deli_date);
        Trns = (Spinner)mView.findViewById(R.id.transport);
        warehouse = (Spinner) mView.findViewById(R.id.warehouse);

        mode_of_payment.setVisibility(View.GONE);
        bank_check.setVisibility(View.GONE);
        deli_date.setVisibility(View.GONE);
        Trns.setVisibility(View.GONE);
        warehouse.setVisibility(View.GONE);

        RadioGroup radiogroupNFAL = (RadioGroup) mView.findViewById(R.id.radiogroup);
        RadioGroup radiogroupNPIL = (RadioGroup) mView.findViewById(R.id.radiogroupNPIL);
//for NFAL
        if (org_id.equals("102")&&!org_id.isEmpty()) {
            Log.e("Order Type for",""+org_id);
            TextView radiohint = (TextView) mView.findViewById(R.id.radiohint);
            radiohint.setVisibility(View.VISIBLE);

            radiogroupNFAL.setVisibility(View.VISIBLE);
            radiogroupNPIL.setVisibility(View.GONE);

            radiogroupNFAL.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override

                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // find which radio button is selected
                    if(checkedId == R.id.hh) {
                        Toast.makeText(getApplicationContext(), "choice: Household",
                                Toast.LENGTH_SHORT).show();
                        parent_cat ="hh";

                    } else if(checkedId == R.id.fur) {
                        Toast.makeText(getApplicationContext(), "choice: Furniture",
                                Toast.LENGTH_SHORT).show();
                        parent_cat="fur";
                    } else {
                        Toast.makeText(getApplicationContext(), "choice: Nothing Selected",
                                Toast.LENGTH_SHORT).show();
                        parent_cat="null";
                    }
                }
            });
        }
        // for NPIL
        else if (org_id.equals("101")&&!org_id.isEmpty()) {
            Log.e("Order Type for",""+org_id);
            TextView radiohint = (TextView) mView.findViewById(R.id.radiohint);
            radiohint.setVisibility(View.VISIBLE);

            radiogroupNPIL.setVisibility(View.VISIBLE);
            radiogroupNFAL.setVisibility(View.GONE);
            radiogroupNPIL.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override

                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // find which radio button is selected
                    if(checkedId == R.id.cat_pipe) {
                        Toast.makeText(getApplicationContext(), "choice: Pipes",
                                Toast.LENGTH_SHORT).show();
                        parent_cat ="pipe";

                    } else if(checkedId == R.id.cat_door) {
                        Toast.makeText(getApplicationContext(), "choice: Doors",
                                Toast.LENGTH_SHORT).show();
                        parent_cat="door";
                    }
                    else if(checkedId == R.id.cat_ceil) {
                        Toast.makeText(getApplicationContext(), "choice: Ceiling",
                                Toast.LENGTH_SHORT).show();
                        parent_cat="ceil";
                    }
                    else if(checkedId == R.id.cat_tube) {
                        Toast.makeText(getApplicationContext(), "choice: Tubewel",
                                Toast.LENGTH_SHORT).show();
                        parent_cat="tube";
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "choice: Nothing Selected",
                                Toast.LENGTH_SHORT).show();
                        parent_cat="null";
                    }
                }
            });
        }


       order_type_text =(AutoCompleteTextView)mView.findViewById(R.id.order_type);
     //   order_type_text.setVisibility(View.VISIBLE);
        price_list_text =(AutoCompleteTextView)mView.findViewById(R.id.price_list);
     //   price_list_text.setVisibility(View.VISIBLE);
        final EditText New_address =(EditText)mView.findViewById(R.id.new_address);

        if(cust_address.getText().toString()==""){
            New_address.setVisibility(View.VISIBLE);
        }

        //getOrderType();
        //getPriceList();

        try {
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            Log.e("parent_cat",""+parent_cat);
                            if (parent_cat!="null" && !parent_cat.isEmpty()) {
                                // ToDo get user input here
                                Intent intent = new Intent(MainActivity.this,
                                        ProductMain.class);//Add the bundle to the intent
                                intent.putExtra("org_id", org_id);
                                intent.putExtra("emp_code", ID);
                                intent.putExtra("parent_cat", parent_cat);
                                intent.putExtra("new_address", "New Address: " + New_address.getText().toString().trim());
                                // cust_id.setText("Party Code   : "+PARTY_CODE.replace("\"","").replaceAll("[^0-9]",""));
                                intent.putExtra("party_code", party_code.replace("\"", "").replaceAll("[^0-9]", ""));
                                startActivity(intent);
                            } else if(parent_cat=="null"||parent_cat.isEmpty()){
                                dialogBox.cancel();
                            }
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
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
       // outState.putParcelable("file_uri", fileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        //fileUri = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    public void onBackPressed() {

        Log.i("Back Button","Back Button ");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Log.i("Back Button","Back Button IF");
            }

            else {
            Log.i("Back Button","Back Button Else");
            moveTaskToBack(true);
            finish();
            //   mScannerView.stopCamera();
            //  super.onBackPressed();
            }


            }
            catch (Exception e){
                e.printStackTrace();

                //startActivity(new Intent(MainActivity.this, MainActivity.class));
              //  mScannerView.stopCamera();
              //  super.onBackPressed();
                     Log.i("Back Button",""+e.toString());
              //  startActivity(new Intent(this, MainActivity.class));
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_msg){
            return true;
        }
        else if(id == R.id.action_notification){
            return true;
        }
        else if(id == R.id.action_settings){
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sync) {
            getPartyList(ID,org_id);
           // db.deleteTempOrderfull();
            // Handle the camera action
            //Toast.makeText(MainActivity.this, "Sync Started", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_chart) {
           // Intent i = new Intent(MainActivity.this, ReportActivity.class);
          //  startActivity(i);
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
            View mView = layoutInflaterAndroid.inflate(R.layout.report_popup, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
            alertDialogBuilderUserInput.setView(mView);
            final EditText FrmDt =(EditText)mView.findViewById(R.id.FrmDt);
            final EditText ToDt =(EditText)mView.findViewById(R.id.ToDt);
            RadioGroup reportGroup =(RadioGroup)mView.findViewById(R.id.rep_radiogroup);

            reportGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override

                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // find which radio button is selected
                    if(checkedId == R.id.rep_do) {
                        Toast.makeText(getApplicationContext(), "choice: DO Report",
                                Toast.LENGTH_SHORT).show();
                        report_for="do_report";

                    } else if(checkedId == R.id.rep_sales) {
                        Toast.makeText(getApplicationContext(), "choice: Sales Report",
                                Toast.LENGTH_SHORT).show();
                        report_for="sale_report";
                    }
                 else if(checkedId == R.id.rep_coll) {
                    Toast.makeText(getApplicationContext(), "choice: Collection Report",
                            Toast.LENGTH_SHORT).show();
                        report_for="col_report";

                }
                    else if(checkedId == R.id.rep_do_col) {
                        Toast.makeText(getApplicationContext(), "choice: DO vs Collection Report",
                                Toast.LENGTH_SHORT).show();
                        report_for="do_vs_col_report";

                    }


                    else {
                        Toast.makeText(getApplicationContext(), "choice: Nothing Selected",
                                Toast.LENGTH_SHORT).show();
                        report_for="";
                    }
                }
            });





            FrmDt.setOnClickListener(new View.OnClickListener() {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        //String myFormat = "dd/MM/yyyy"; //In which you need put here
                        String myFormat = "MM-dd-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        FrmDt.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MainActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            ToDt.setOnClickListener(new View.OnClickListener() {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                      //  String myFormat = "dd/MM/yyyy"; //In which you need put here
                        String myFormat = "MM-dd-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        ToDt.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MainActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("View", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // ToDo get user input here

                        Intent i = new Intent(MainActivity.this, ReportActivity.class);
                        i.putExtra("FrmDt",FrmDt.getText().toString());
                        i.putExtra("TomDt",ToDt.getText().toString());
                        i.putExtra("report_for",report_for);
                            i.putExtra("org_id", org_id);
                            i.putExtra("emp_code",ID.replaceAll("[^0-9]", ""));
                            i.putExtra("salesrep_id",salesrep_id.replaceAll("[^0-9]", ""));
                        startActivity(i);
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




        } else if (id == R.id.nav_view_orders) {

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
            View mView = layoutInflaterAndroid.inflate(R.layout.view_order_dialog, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
            alertDialogBuilderUserInput.setView(mView);
            TextView DailogTitle = (TextView)mView.findViewById(R.id.dialogTitle);
            DailogTitle.setText("View Order?");
            TextView DailogMessage = (TextView)mView.findViewById(R.id.dialogMsg);
            DailogMessage.setText("Input From date and To date");
            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInput);

            final EditText FrmDt =(EditText)mView.findViewById(R.id.FrmDate);
            final EditText ToDt =(EditText)mView.findViewById(R.id.ToDate);

            userInputDialogEditText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_FILTER);



            FrmDt.setOnClickListener(new View.OnClickListener() {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        //String myFormat = "dd/MM/yyyy"; //In which you need put here
                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        FrmDt.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MainActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            ToDt.setOnClickListener(new View.OnClickListener() {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        //  String myFormat = "dd/MM/yyyy"; //In which you need put here
                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        ToDt.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MainActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("View", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // ToDo get user input here
                            //Party/customer password check
                            // intent party/customer password from MainActivity***
                            if(!FrmDt.getText().toString().isEmpty()&&!ToDt.getText().toString().isEmpty()) {

                                Intent i = new Intent(getApplicationContext(), ViewOrders.class);

                                // send album id to tracklist activity to get list of songs under that album
                              //  String FrmDt = FrmDt.getText().toString();
                                i.putExtra("FromDate", FrmDt.getText().toString());
                                i.putExtra("ToDate", ToDt.getText().toString());
                                i.putExtra("emp", ID);


                                startActivity(i);

                                final String vieworder = userInputDialogEditText.getText().toString().trim();
                                Toast.makeText(getApplicationContext(), "Order Showing for "+vieworder, Toast.LENGTH_LONG).show();
                            }
                            else {
                                userInputDialogEditText.setError("Field cannot be left blank.");
                            }
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


        } else if (id == R.id.nav_manage) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        }
        else if (id==R.id.nav_print){
            startActivityForResult(new Intent(Settings.ACTION_PRINT_SETTINGS), 0);
        }

        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
           // sharingIntent.setType(HttpResponse(url));
            String shareBody = "Here is the share content body";
           // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share with"));

        } else if (id == R.id.nav_logout) {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            if (getIntent().getBooleanExtra("EXIT", false)) {
                finish();
                session.setLogin(false);
                System.exit(0);
            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
