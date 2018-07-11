package com.metrosoft.arafat.salebook.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.report_helper.AlertDialogManager;
import com.metrosoft.arafat.salebook.report_helper.ConnectionDetector;
import com.metrosoft.arafat.salebook.report_helper.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.metrosoft.arafat.salebook.app.AppConfig.URL_VIEW_ORDER_DETAILS;

public class TrackListActivity extends ListActivity {
	// Connection detector
	ConnectionDetector cd;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> tracksList;

	// tracks JSONArray
	JSONArray albums = null;

	// Album id
	static String  album_id, album_name;

	// tracks JSON url
	// id - should be posted as GET params to get track list (ex: id = 5)
//	private static final String URL_ALBUMS = "http://api.androidhive.info/songs/album_tracks.php";
    private static final String URL_ALBUMS = URL_VIEW_ORDER_DETAILS+"?ord_id=";
	// ALL JSON node names
	private static final String TAG_SONGS = "songs";
	private static final String TAG_ID = "id";
    private static final String TAG_PROD_CODE = "PROD_CODE";
	private static final String TAG_NAME = "PROD_NAME";
    private static final String TAG_QNTY = "QNTY";
    private static final String TAG_PROD_PRICE= "PROD_PRICE";

    //   id,PROD_CODE, PROD_NAME NAME, QNTY, PROD_PRICE


	private static final String TAG_ALBUM = "album";
	private static final String TAG_DURATION = "duration";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracks);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(TrackListActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Get album id
		Intent i = getIntent();
	    album_id = i.getStringExtra("orderid");
		//album_id = "1";
		Log.e("Order ID",""+album_id);

		// Hashmap for ListView
		tracksList = new ArrayList<HashMap<String, String>>();

		// Loading tracks in Background Thread
		new LoadTracks().execute();

		// get listview
		ListView lv = getListView();

		/**
		 * Listview on item click listener
		 * SingleTrackActivity will be lauched by passing album id, song id
		 * */
	/*	lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
									long arg3) {
				// On selecting single track get song information
				Intent i = new Intent(getApplicationContext(), SingleTrackActivity.class);

				// to get song information
				// both album id and song is needed
				String album_id = ((TextView) view.findViewById(R.id.album_id)).getText().toString();
				String song_id = ((TextView) view.findViewById(R.id.song_id)).getText().toString();

				Toast.makeText(getApplicationContext(), "Album Id: " + album_id  + ", Song Id: " + song_id, Toast.LENGTH_SHORT).show();

				i.putExtra("album_id", album_id);
				i.putExtra("song_id", song_id);

				startActivity(i);
			}
		});*/

	}

	/**
	 * Background Async Task to Load all tracks under one album
	 * */
	class LoadTracks extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TrackListActivity.this);
			pDialog.setMessage("Loading songs ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting tracks json and parsing
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// post album id as GET parameter
			params.add(new BasicNameValuePair(TAG_ID, album_id));

			// getting JSON string from URL
			String json = jsonParser.makeHttpRequest(URL_ALBUMS+""+album_id, "FLATGET",
					params);
			Log.e("order details URL",URL_ALBUMS+""+album_id);

            /*  TAG_ID = "id";
            TAG_PROD_CODE = "PROD_CODE";
            TAG_NAME = "name";
            TAG_QNTY = "QNTY";
            TAG_PROD_PRICE= "PROD_PRICE";*/
         //   id,PROD_CODE, PROD_NAME NAME, QNTY, PROD_PRICE
			// Check your log cat for JSON reponse
			Log.d("Track List JSON: ", json);

			try {

					//String album_id = jObj.getString(TAG_ID);
					//album_name = jObj.getString(TAG_ALBUM);
					//albums = jObj.getJSONArray(TAG_SONGS);
                    albums = new JSONArray(json);
					if (albums != null) {
						// looping through All songs
						for (int i = 0; i < albums.length(); i++) {
							JSONObject c = albums.getJSONObject(i);

							// Storing each json item in variable
                            String track_no = String.valueOf(i + 1);
							String song_id = c.getString(TAG_PROD_CODE);
							// track no - increment i value

							String name = c.getString(TAG_NAME);
							String qnty = c.getString(TAG_QNTY);
							String duration = c.getString(TAG_PROD_PRICE);

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							//map.put("album_id", album_id);
							//	map.put(TAG_ID, song_id);
							map.put("track_no", track_no + ".");
							map.put(TAG_NAME,song_id+"\t-\t"+ name);
							map.put(TAG_DURATION, qnty+"\t-\t"+duration);

							// adding HashList to ArrayList
							tracksList.add(map);
						}
					} else {
						Log.d("Albums: ", "null");
					}


			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all tracks
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
				/*	ListAdapter adapter = new SimpleAdapter(
							TrackListActivity.this, tracksList,
							R.layout.list_item_tracks, new String[] { "album_id", TAG_ID, "track_no",
							TAG_NAME, TAG_DURATION }, new int[] {
							R.id.album_id, R.id.song_id, R.id.track_no, R.id.album_name, R.id.song_duration });*/



                    ListAdapter adapter = new SimpleAdapter(
                            TrackListActivity.this, tracksList,
                            R.layout.list_item_tracks, new String[] {  "track_no",
                            TAG_NAME, TAG_DURATION }, new int[] {
                            R.id.track_no, R.id.album_name, R.id.song_duration });
					// updating listview
					setListAdapter(adapter);

					// Change Activity Title with Album name
					setTitle(album_name);
				}
			});

		}

	}
}