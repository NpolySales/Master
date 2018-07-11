package com.metrosoft.arafat.salebook.activity;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.report_helper.AlertDialogManager;
import com.metrosoft.arafat.salebook.report_helper.ConnectionDetector;
import com.metrosoft.arafat.salebook.report_helper.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.metrosoft.arafat.salebook.app.AppConfig.URL_VIEW_ORDERS;

public class ViewOrders  extends ListActivity {
    // Connection detector
    ConnectionDetector cd;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> orderList;

    // albums JSONArray
    JSONArray albums = null;

    // albums JSON url
    private static final String URL_ALBUMS = URL_VIEW_ORDERS;

    // ALL JSON node names
    private static final String TAG_ID = "ID";
    private static final String TAG_NAME = "NAME";
    private static final String TAG_SONGS_COUNT = "SONGS_COUNT";
    private static final String TAG_DATE = "ORDDATE";
    private static final String TAG_PARTY = "PARTY_CODE";
    private static  String emp,FrmDate,ToDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        cd = new ConnectionDetector(getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(ViewOrders.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        Intent intent = getIntent();
        if (intent.hasExtra("emp")) {

            emp = intent.getStringExtra("emp").replaceAll("[^0-9]", "");
            FrmDate = intent.getStringExtra("FromDate");
            ToDate= intent.getStringExtra("ToDate");

        }

        Log.e("Params",emp+"\t"+FrmDate+"\t"+ToDate);

        // Hashmap for ListView
        orderList = new ArrayList<HashMap<String, String>>();

        // Loading Albums JSON in Background Thread
        new LoadAlbums().execute();

        // get listview
        ListView lv = getListView();

        /**
         * Listview item click listener
         * TrackListActivity will be lauched by passing album id
         * */
        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                Intent i = new Intent(getApplicationContext(), TrackListActivity.class);

                // send album id to tracklist activity to get list of songs under that album
                String orderid = ((TextView) view.findViewById(R.id.album_name)).getText().toString();
                i.putExtra("orderid", orderid.substring( 0, 9).trim());

                startActivity(i);
            }
        });
    }

    /**
     * Background Async Task to Load all Albums by making http request
     * */
    class LoadAlbums extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewOrders.this);
            pDialog.setMessage("Listing Albums ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Albums JSON
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
           /* params.add(new BasicNameValuePair("EmpCode", emp));
            params.add(new BasicNameValuePair("FrmDate", FrmDate));
            params.add(new BasicNameValuePair("ToDt", ToDate));*/

            // getting JSON string from URL
            //    URL sourceUrl = new URL(AppConfig.URL_SHIP_LOC+"?party_code="+party_code+"&org_id="+org_id);
            String json = jsonParser.makeHttpRequest(URL_ALBUMS+"?EmpCode="+emp+"&FrmDt="+FrmDate+"&ToDt="+ToDate, "FLATGET",                    params);
         //  String json = jsonParser.makeHttpRequest(URL_ALBUMS, "GET",params);
            Log.e("URL params",""+URL_ALBUMS+"?EmpCode="+emp+"&FrmDt="+FrmDate+"&ToDt="+ToDate);

            // Check your log cat for JSON reponse
            Log.d("Albums JSON: ", "> " + json);

            try {
                albums = new JSONArray(json);

                if (albums != null) {
                    // looping through All albums
                    for (int i = 0; i < albums.length(); i++) {
                        JSONObject c = albums.getJSONObject(i);

                        // Storing each json item values in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String songs_count = c.getString(TAG_SONGS_COUNT);
                        String orddate = c.getString(TAG_DATE);
                        String partycode= c.getString(TAG_PARTY);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name+"\t"+orddate+"\t"+partycode);
                        map.put(TAG_SONGS_COUNT, songs_count);

                        // adding HashList to ArrayList
                        orderList.add(map);
                    }
                }else{
                    Log.d("Albums: ", "null");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_ID, "0");
                    map.put(TAG_NAME, "NO DATA FOUND");
                    map.put(TAG_SONGS_COUNT, "0");
                    orderList.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Ex>>\t",e.toString());
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ViewOrders.this, orderList,
                            R.layout.list_item_albums, new String[] { TAG_ID,
                            TAG_NAME, TAG_SONGS_COUNT }, new int[] {
                            R.id.album_id, R.id.album_name, R.id.songs_count });

                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}