package com.metrosoft.arafat.salebook.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.app.AppConfig;
import com.metrosoft.arafat.salebook.app.AppController;
import com.metrosoft.arafat.salebook.helper.GPSTracker;
import com.metrosoft.arafat.salebook.helper.SQLiteHandler;
import com.metrosoft.arafat.salebook.helper.SessionManager;
import com.metrosoft.arafat.salebook.helper.USSDService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.core.LGSnackbarAction;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;

import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.ERROR;
import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.INFO;
import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.WARNING;


public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String SIMTAG = RegisterActivity.class.getSimpleName();
    public static String pdm_phone,pzm_phone,pdm_email,pzm_email;

    private Button btnLogin,btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    public Context ctx;
    // private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    // private LocationRequest mLocationRequest;
    GPSTracker gps;
    static String gpslat, gpslong;
    static double finallat, finallong;
    public WebView wbview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transparentToolbar();
        setContentView(R.layout.activity_login);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        //  mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        //  mGoogleApiClient.connect();

        //session.setLogin(false);
        LinearLayout co=(LinearLayout)findViewById(R.id.loginlayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) co.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        LGSnackbarManager.prepare(getApplicationContext(),        LGSnackBarThemeManager.LGSnackbarThemeName.MATERIAL);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setAnimation(animation);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLogin.setTypeface(font);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());
        session.setLogin(false);
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        startService(new Intent(this, USSDService.class));


        CheckPermission();
        SetPermission();
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                String prefix = "88";
                view.clearAnimation();
                // Check for empty data in the form
                gps = new GPSTracker(LoginActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    // \n is for new line
                    gpslat = "" + latitude;
                    gpslong = "" + longitude;
                    finallat = latitude;
                    finallong = longitude;
                    Log.e("Location\t", "Lat" + gpslat + "\nLong:" + gpslong);
                    // Toast.makeText(getApplicationContext(), "Your Location is - \tLat: " + gpslat + "\tLong: " + gpslong, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }


                if (!email.isEmpty() && !password.isEmpty()) {
                    try {
                        // login user
                        TelephonyManager tm = (TelephonyManager)getApplicationContext(). getSystemService(TELEPHONY_SERVICE);



                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //
                            //          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                                    Manifest.permission.READ_SMS }, 1);

                            return;
                        }
                        String operator= tm.getSimOperatorName();
                        String opcode= tm.getSimOperator().toString().trim();
                        //String number = "88"+ tm.getLine1Number();
                        String number =  tm.getLine1Number();
                        // Mobile number check
                        //banglalink
                        /*if (number.isEmpty()){
                            String ussd="";
                           if (opcode.equals("47003")){
                           ussd = "*511" + Uri.encode("#");
                            //ussd = "*2" + Uri.encode("#");
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd)));
                            }

                            else if(opcode.equals("47001")){
                               ussd = "*2" + Uri.encode("#");
                               startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd)));
                            }

                                final Intent ussdi = getIntent();
                                if (ussdi.hasExtra("ussd")) {
                                    number= ussdi.getStringExtra("ussd").toString().trim().replaceAll("\\D+","");
                                }
                                else
                                {
                                    number=USDNUMBER;
                                }
                            Log.e(" phone Number:",number);
                        }*/

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                                    Manifest.permission.SEND_SMS }, 1);
                            return;
                        }
                        String getSimSerialNumber = tm.getSimSerialNumber();
                        String IMEI = tm.getDeviceId();
                       // String IMEIINDEX= tm.getImei(1);
                        //String getSimNumber = tm.getLine1Number();


                        Log.e("SIM number", "" + number + " >< " + prefix + email + " SIMSL :" + getSimSerialNumber+"\tIMEI:"+IMEI+"\t Network Operator: "+operator+"-"+tm.getSimOperator());//+8801717998754 - +8801717998754

                        //Conditional login

                     /*   if ((number.trim().toString()).equals((prefix + email).trim().toString())) {
                            checkLogin(view,email, password,gpslat,gpslong);
                        }
                        else {

                            LGSnackbarManager.show(ERROR, "Phone number dosen't match!",
                                    new LGSnackbarAction("FORCE LOGIN?", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // Action fired
                                        }
                                    }));

                        }*/
                        checkLogin(view,email, password,gpslat,gpslong,IMEI);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e(SIMTAG,"LoginError:"+e.toString());
                        LGSnackbarManager.show(ERROR, "Unknown Error Occurred");
                    }

                } else {
                    // Prompt user to enter credentials

                    LGSnackbarManager.show(WARNING, "Please Enter Login Credentials!");

                 /*  Snackbar snackbar = Snackbar.make(view, "Please Enter Login Credentials", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);

                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    snackbar.show();*/
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {             //   Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
             //   startActivity(i);
              //  finish();

                //Context ctx;
                LGSnackbarManager.show(INFO, "You can't Register, Please contact with HQ ");
           ;
            }
        });

    }



    private void SetPermission() {
        Log.e(TAG,"Setting Permisison");
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.READ_SMS }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.SEND_SMS }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.CALL_PHONE }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.WRITE_SETTINGS }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.WRITE_SECURE_SETTINGS }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.READ_PHONE_NUMBERS }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.READ_PHONE_STATE }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION }, 1);

        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.LOCATION_HARDWARE }, 1);

        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.INSTALL_LOCATION_PROVIDER }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.CAMERA }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.ACCESS_WIFI_STATE }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.INTERNET }, 1);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                Manifest.permission.GET_ACCOUNTS }, 1);


    }


    public void CheckPermission()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    Log.e(TAG, "Permission : " + p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("CDA", "onBackPressed Called");
        finish();
        moveTaskToBack(false);
        System.exit(0);
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final View view, final String email, final String password, final String gpslat, final String gpslong, final String IMEI) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setTitle("L O G I N");
        pDialog.setIcon(R.drawable.ic_menu_send);
        pDialog.setMessage("Please wait ...");

        pDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Window view = ((pDialog)).getWindow();
                view.setWindowAnimations(R.style.AppTheme);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundDrawableResource(R.color.colorPrimaryDark);
                view.setTitleColor(getResources().getColor(R.color.white));
            }
        });
        showDialog();


        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                       session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String phone = user.optString("phone");
                        String org_id = user.optString("org_id");
                        String salesrep_id= user.optString("salesrep_id");
                        String phoneimei= user.optString("imei");
                        //----------------------------------------------------------------
                        String dm_id        =user.optString("DM_ID");
                        String dm_phone     =user.optString("DM_PHONE");
                        String dm_emial     =user.optString("DM_EMAIL");
                        String zm_id        =user.optString("ZM_ID");
                        String zm_phone     =user.optString("ZM_PHONE");
                        String zm_email     =user.optString("ZM_EMAIL");


                        pdm_phone = dm_phone.replaceAll("\\D+","");
                        pzm_phone = zm_phone.replaceAll("\\D+","");
                        pdm_email = dm_emial.replace("\"","");
                        pzm_email = zm_email.replace("\"","");


                        if (phone.isEmpty()){
                            phone ="+880";
                        }
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table

                        db.deleteUsers();
                        db.addUser(name, email,phone, uid, created_at);
                        Log.i("User Details Found: ","RepId\t"+salesrep_id+"\tIMEI\t"+phoneimei+"\tID: "+uid+" Name: "+name+" Phone: "+phone+" Email: "+email+"\tDMPHONE:"+pdm_phone+"\tZMPHONE: "+pzm_phone);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        Bundle bundle = new Bundle();
//Add your data to bundle
                        bundle.putString("uid", uid.toString().trim().replace("\"",""));
                        bundle.putString("uname", name.toString().trim().replace("\"",""));
                        bundle.putString("uphone", phone.toString().trim().replace("\"",""));
                        bundle.putString("uemail", email.toString().trim().replace("\"",""));
                        bundle.putString("org_id", org_id.toString().trim().replace("\"",""));
                        bundle.putString("gpslat", gpslat);
                        bundle.putString("gpslong", gpslong);
                        bundle.putString("imei", IMEI);
                        bundle.putString("party_code", "3331");
                        bundle.putString("salesrep_id",salesrep_id.toString().trim().replace("\"",""));
//Add the bundle to the intent
                       intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error in login. Get the error message

                        String errorMsg = jObj.getString("error_msg");
                        showLoginError();

                        LGSnackbarManager.show(WARNING, "Opps! Something Went wrong, Try again.");




                    }
                    gps.stopUsingGPS();
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


                LGSnackbarManager.show(INFO, "Opps! No Internet Connection, Open Internet",
                        new LGSnackbarAction("Setting", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Action fired
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            }
                        }));

             /*   final Snackbar snackBar=  Snackbar.make(view, "Opps! No Internet Connection, Open Internet ", Snackbar.LENGTH_LONG);


                snackBar.setAction("Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                });
                snackBar.show();*/
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("gpslat",gpslat);
                params.put("gpslong",gpslong);
                params.put("imei",IMEI);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showLoginError() {
       // Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
       // inputPassword.setAnimation(shake);
        //inputPassword.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.btn_logut_bg), PorterDuff.Mode.MULTIPLY);
        //inputPassword.setTextColor(Color.RED);
        YoYo.with(Techniques.Shake).duration(1000).repeat(1).playOn(findViewById(R.id.password));

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}