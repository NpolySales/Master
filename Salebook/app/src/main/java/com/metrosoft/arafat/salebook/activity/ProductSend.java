package com.metrosoft.arafat.salebook.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.Utils.EnglishNumberToWords;
import com.metrosoft.arafat.salebook.app.AppConfig;
import com.metrosoft.arafat.salebook.app.AppController;
import com.metrosoft.arafat.salebook.helper.GPSTracker;
import com.metrosoft.arafat.salebook.helper.PdfHeaderFooter;
import com.metrosoft.arafat.salebook.helper.SQLiteHandler;
import com.metrosoft.arafat.salebook.holder.HashMapColumn;
import com.metrosoft.arafat.salebook.holder.OrderItemAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.os.Build.ID;
import static com.metrosoft.arafat.rxpad.R.id.userInput_shipto;
import static com.metrosoft.arafat.salebook.activity.MainActivity.Nameview;
import static com.metrosoft.arafat.salebook.activity.MainActivity.PhoneView;
import static com.metrosoft.arafat.salebook.activity.MainActivity.cust_address;
import static com.metrosoft.arafat.salebook.activity.MainActivity.cust_id;
import static com.metrosoft.arafat.salebook.activity.MainActivity.cust_name;
import static com.metrosoft.arafat.salebook.activity.MainActivity.cust_phone;
import static com.metrosoft.arafat.salebook.activity.MainActivity.parent_cat;
import static com.metrosoft.arafat.salebook.activity.MainActivity.saletype;
import static com.metrosoft.arafat.salebook.activity.ProductList.attachimg;
import static com.metrosoft.arafat.salebook.activity.ProductMain.emp_code;
import static com.metrosoft.arafat.salebook.activity.ProductMain.org_id;
import static com.metrosoft.arafat.salebook.activity.ProductMain.party_code;
import static com.metrosoft.arafat.salebook.app.AppConfig.URL_SET_ORDER;
import static com.metrosoft.arafat.salebook.app.AppConfig.toSMS;
import static com.metrosoft.arafat.salebook.app.AppConfig.toemail;
import static com.metrosoft.arafat.salebook.helper.SQLiteHandler.PrntCat;
import static com.metrosoft.arafat.salebook.holder.OrderItemAdapter.qntyChanged;
import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.ERROR;
import static greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme.SnackbarStyle.INFO;

public class ProductSend extends AppCompatActivity   {
    private ProgressDialog pDialog;
    FloatingActionButton fab_send, fab_delete;
   static ListView listitems;
    static String PrCat;
    private final int REFRESH_TIME = 5000;
   HashMap<String,String> OrderHeap = new HashMap<>();
    ArrayList<HashMap<String, String>>  OrderList = new ArrayList<HashMap<String, String>>();
    ArrayList< HashMap<String,HashMap<String,String>>>ol = new ArrayList<HashMap<String,HashMap<String,String>>>();
    public String mob_order_id, prod_code, prod_name , uom, qnty,prod_price, order_by, is_active, image_str,note,total_price,pdfpath,party_phone,sms_body;
    public static String bank_names,shipto,payto,customerclass,ordertype,pricelist,new_address,new_discount,mop,tempmop,tnc,bank_cheque,delivery_date,trns,wareh,moneyAsWords;
    public static double sum,percent;
    public static double tempsum,netsum=0.0;
    //startsending order params
    private static SQLiteHandler db;
    ImageView imgattch;
    public static AutoCompleteTextView paytotext,shiptotext,order_type,price_list;
    public  static  EditText new_add, new_disc,bank_check,chk_dt,deli_date,transport;
    private Spinner mode_of_payment,Transport,warehouse,bank_name;
    private static final int CAMERA_REQUEST = 1888;
    InputStream is=null;
    String result=null;
    String line=null;
    public static String SEND_ORD_ID ="",customerphone;
    private  static  PdfWriter writer ;
    static ArrayList<HashMapColumn> localdbitems=null;
    EditText Pqnty,Pprice;
    static TextView TQnty,Tprice;
    static  OrderItemAdapter or;
    static Context ctx;
    static Calendar myCalendar = Calendar.getInstance();
    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;
    private static String p_qnty,p_price,s;
    static  String ordersheetname;
    // Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_send);
        setContentView(R.layout.activity_product_send);
        LGSnackbarManager.prepare(getApplicationContext(),        LGSnackBarThemeManager.LGSnackbarThemeName.MATERIAL);
        new_address=new_discount="0";
        ctx=getApplicationContext();
        Intent intent = getIntent();
        View v = new View(getApplicationContext());
        if (intent.hasExtra("org_id")) {
            emp_code = intent.getStringExtra("emp_code").replaceAll("[^0-9]", "");
            org_id = intent.getStringExtra("org_id");
            party_code= intent.getStringExtra("party_code");
            total_price= intent.getStringExtra("total_price");
            party_phone= intent.getStringExtra("cust_phone");
            customerphone= party_phone;

        }

       fab_send = (FloatingActionButton)findViewById(R.id.fab_send);
        fab_delete = (FloatingActionButton)findViewById(R.id.fab_delete);
        listitems = (ListView)findViewById(R.id.listitems);
        TQnty =(TextView)findViewById(R.id.totalQnty);
        Tprice =(TextView)findViewById(R.id.totaluniteprice);
     //   listitems.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        getItems();
        //Pqnty =(EditText)findViewById(R.id.qnty);
       // Pprice =(EditText)findViewById(R.id.price);

        int itemsCount = listitems.getCount();
        Log.e("List Total Items","\t"+itemsCount);

        //NOT USING----------------------------------------------------------------
        for (int i = 0; i < itemsCount; i++) {
            Log.e("List Items","\t"+i+"/"+itemsCount);
            View viewX = listitems.getAdapter().getView(i,null,listitems);
          //  View view12 = listitems.getChildAt(i);
            String Qnty = ((EditText) viewX.findViewById(R.id.qnty)).getText().toString();
            String Price = ((EditText) viewX.findViewById(R.id.price)).getText().toString();
            String Code = ((TextView) viewX.findViewById(R.id.p_code)).getText().toString();

            try {
                //price update
                db.UpdateTablePRICE(emp_code.replace("\"", "").replaceAll("[^0-9]", ""),party_code.replace("\"", "").replaceAll("[^0-9]", ""),Code.trim(),Price.trim());
                Log.e("PriceUpdate","Price Clicked\t"+i+"\t"+emp_code+"\t"+party_code+"\t"+Code);
                //quantity update
                db.UpdateTableQNTY(emp_code.replace("\"", "").replaceAll("[^0-9]", ""),party_code.replace("\"", "").replaceAll("[^0-9]", ""),Code.trim(),Qnty.trim());
               Log.e("QntyUpdate","Quantity Clicked\t"+i+"\t"+emp_code+"\t"+party_code+"\t"+Code);
                or.notifyDataSetChanged();
                getItems();
            }
            catch (Exception e){
                Log.e("PriceUpdate","Error Occured during Update Price\t"+ e.toString());
            }

        }
        //-------------------------------------------------------------------------------------------





    }


public  static  void RefreshItems(){
    if (qntyChanged){
        or.notifyDataSetChanged();
        listitems.destroyDrawingCache();
        db = new SQLiteHandler(ctx);
        localdbitems=db.getOrderItemObject(party_code.replace("\"", "").replaceAll("[^0-9]", ""));
        or= new OrderItemAdapter(ctx,R.layout.order_items_row,localdbitems);
        listitems.setVisibility(View.VISIBLE);
        listitems.setAdapter(or);
        Log.e("Refresh List","Size:\t"+listitems.getCount()+"\tContext:\t"+ctx.getPackageName().trim());
        //getItems();

    }
}

    private void getItems() {
   try {
        db = new SQLiteHandler(getApplicationContext());
        localdbitems = db.getOrderItemObject(party_code.replace("\"", "").replaceAll("[^0-9]", ""));
        or = new OrderItemAdapter(this, R.layout.order_items_row, localdbitems);
        listitems.setAdapter(or);
    }catch (Exception E)
    {
        E.printStackTrace();
        Log.e("LOCALDB","LOCAL DB GET ERROR FOR "+party_code+""+E.toString());

        LGSnackbarManager.show(ERROR, "Something went wrong!");
       // continue;
    }

    }

    //Deleting saved orders
    public void delOrder(final View view){

        db = new SQLiteHandler(getApplicationContext());
        final Snackbar sb= Snackbar.make(view, "Confirm Deleting?", Snackbar.LENGTH_LONG).setActionTextColor(Color.RED)
                .setAction("Delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.deleteTempOrder(party_code);
                        Intent intent = new Intent(ProductSend.this,
                                MainActivity.class);//Add the bundle to the intent

                        intent.putExtra("org_id",org_id);
                        intent.putExtra("emp_code",ID);
                        intent.putExtra("party_code",party_code);
                        startActivity(intent);
                    }
                } );
        View snackBarView = sb.getView();

        snackBarView.setBackgroundColor(Color.BLACK);
        sb.show();


    }
    //Calculate total
    public void calcTotal(View view) {
        LGSnackbarManager.show(INFO, "Calculating, please wait!");

        int listsize= localdbitems.size();
        try {

            float totalqnty=0;
            float totlprice=0;

            for (int x = 0; x < localdbitems.size(); x++) {
               // totalqnty += Integer.parseInt(localdbitems.get(x).getQnty());
                View orderlistview = listitems.getAdapter().getView(x,null,listitems);
                EditText qntyText=orderlistview.findViewById(R.id.qnty);
                TextView PriceText=orderlistview.findViewById(R.id.unittotal);
                totalqnty +=Float.parseFloat(qntyText.getText().toString());
                totlprice +=Float.parseFloat(PriceText.getText().toString());
            }
                Log.e("ORDER TOTAL","\t totalqnty"+totalqnty+"\t totlprice"+totlprice);
            TQnty.setText(""+totalqnty);
            Tprice.setText(""+totlprice);

        }catch (Exception e){
            e.printStackTrace();
            Log.e("Calc Error","Error While calculating"+e.toString());
            LGSnackbarManager.show(ERROR, "Calculation ERROR");
        }




    }


    // Create PDF
    public void createPdf(final  View view)
    {
        Document doc = new Document();
        doc = new Document(PageSize.A4);
     try {
            new PdfHeaderFooter();

            pdfpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/salebook";

            File dir = new File(pdfpath);
            if(!dir.exists())
                dir.mkdirs();
            Log.e("PDFCreator", "PDF Path: " + pdfpath);
            final String timedate = new SimpleDateFormat("dd.MM.yyyy.HH.mm").format(new Date());
            ordersheetname=timedate+"-["+party_code+"]-["+emp_code.toString().trim()+"]-"+"ordersheet.pdf";

            File file = new File(dir, ordersheetname);
            FileOutputStream fOut = new FileOutputStream(file);
            writer.getInstance(doc,fOut);

           // Log.e("input field",  "\tdiscount: " + new_discount + "-" + Integer.parseInt(new_discount) + "\tAddress " + new_address);
            //Log.e("input field",  "\tdiscount: " + + "-" + Integer.parseInt(new_discount) + "\tAddress " + new_address);

            //open the document
            doc.open();
            addMetaData(doc);
           addTitlePage(doc);
            addFooterPart(doc);
        //    addFooter(doc );

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.npoly);
            bitmap.createScaledBitmap(bitmap,100,60,false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
             Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.ALIGN_CENTER);
            doc.close();
             } catch (Exception de) {
            Log.e("PDFCreator", "DocumentException:" + de.toString());
        }
        finally
        {
            doc.close();
        }

    }
    public void addMetaData(Document document) throws DocumentException

    {
        document.addTitle("ORDER SHEET");
        document.addSubject("Order Info");
        document.addHeader("COMPANY_NAME","NPIL/NFAL");
        document.addKeywords("Order, DO, Order sheet, Collection");
        document.addAuthor("HCO-02-074");
        document.addCreator("Generated by:"+emp_code.toString().trim());
        document.addLanguage("ENG-International");
    }
    private void addTitlePage(Document document) throws DocumentException, IOException {
        Font catFont = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD
                | Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
        Font smallnormal = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
        Font normal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 10);
        Font tablerowfont = new Font(Font.FontFamily.TIMES_ROMAN, 8);

        DecimalFormat df = new DecimalFormat("0.00");
        //add image to document
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.npoly);
        bitmap.createScaledBitmap(bitmap, 63, 30, false);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image myImg = Image.getInstance(stream.toByteArray());
        myImg.setAlignment(Image.ALIGN_CENTER);
        String oid = org_id.trim();


        String company_address = "UDAY TOWER; Plot No-57-57/A, Gulshan Avenue, Circle-1,Dhaka-1212, Bangladesh\n";
// Start New Paragraph
        Paragraph prHead = new Paragraph();
// Set Font in this Paragraph
        prHead.setFont(titleFont);

        prHead.add(myImg);

// Add item into Paragraph
        if (oid.equals("101")) {

            String company_name = "National Polymer Industries Ltd";
            prHead.add("\n" + company_name + "\n");
        } else if (oid.equals("102")) {

            String company_name = "National Fittings & Accessories Ltd";
            prHead.add("\n" + company_name + "\n");
        } else {
            String company_name = "Company Missing";
            prHead.add("\n" + company_name + "\n");
        }


// Create Table into Document with 1 Row
        PdfPTable myTable = new PdfPTable(1);
// 100.0f mean width of table is same as Document size
        myTable.setWidthPercentage(100.0f);

// Create New Cell into Table
        PdfPCell myCell = new PdfPCell(new Paragraph(""));
        myCell.setBorder(Rectangle.BOTTOM);

// Add Cell into Table
        myTable.addCell(myCell);

        prHead.setFont(smallBold);
        prHead.add(company_address + "\n");
        prHead.setAlignment(Element.ALIGN_CENTER);

// Add all above details into Document
        document.add(prHead);
        document.add(myTable);
        document.add(myTable);

// Now Start another New Paragraph
        Paragraph prPersinalInfo = new Paragraph();
        prPersinalInfo.setFont(smallBold);
        prPersinalInfo.add("ORDER SHEET\n\n");

        prPersinalInfo.setAlignment(Element.ALIGN_CENTER);

        document.add(prPersinalInfo);
        document.add(myTable);
        document.add(myTable);


        Paragraph prProfile = new Paragraph();
        prProfile.setFont(smallnormal);
        prProfile.add("SL. No : " + SEND_ORD_ID+"\n");
        prProfile.add("Party Code   : " + party_code + "\t\t\t\t " + cust_name.getText().toString().trim() + "\n");
        prProfile.add(" " + cust_address.getText().toString().trim() + "\n");
        prProfile.add(" " + cust_phone.getText().toString().trim() + "\n");
        prProfile.add(" Marketer Code: " + emp_code.toString().trim() + "\t\t");
        prProfile.add(" Marketer " + Nameview.getText().toString().trim() + "\n");
        prProfile.add(" Marketer " + PhoneView.getText().toString().trim() + "\n");
            if (parent_cat == "hh" && !parent_cat.isEmpty()) {
            PrCat = "Household";
            } else if (parent_cat == "fur" && !parent_cat.isEmpty()) {
            PrCat = "Fruniture";
            } else if (parent_cat == "pipe" && !parent_cat.isEmpty()) {
            PrCat = "Pipe";
            } else if (parent_cat == "door" && !parent_cat.isEmpty()) {
            PrCat = "Door";
            }
            else if (parent_cat == "ceil" && !parent_cat.isEmpty()) {
            PrCat = "Ceiling";
            }
            else if (parent_cat == "tube" && !parent_cat.isEmpty()) {
            PrCat = "Tubewel";
            }    else {
            PrCat = "" + PrntCat;
            }
            if (new_address.isEmpty()) {
            new_address = shipto;
            } else if (shipto.isEmpty()){
            new_address = new_add.getText().toString().trim();
            }
        prProfile.add(" Delivery Site Address:\t"+new_address+"\n");
        prProfile.add(" "+saletype.getText().toString().trim()+" -"+PrCat+"\n");
        document.add(myTable);
        prProfile.setFont(smallBold);
        prProfile.add("\nOrder Details:\n\n");
        prProfile.setFont(smallBold);
        document.add(prProfile);
        /*------------order details---*/

        //specify column widths
        float[] columnWidths = {0.3f,1.2f,2.7f,0.5f,0.5f,0.5f,0.7f,1.0f};
        //create PDF table with the given widths
        PdfPTable table = new PdfPTable(columnWidths);
        // set table width a percentage of the page width
        table.setWidthPercentage(95f);
        Log.e("attTitlePage","Loading Table Data");
        //insert column headings
        insertCell(table, "Products price claculation", Element.ALIGN_RIGHT, 8, bfBold12);
        insertCell(table, "SL", Element.ALIGN_RIGHT, 1, bfBold12);
        insertCell(table, "Product Code", Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Item Description", Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Qnty", Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Gross", Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, " % ", Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Net", Element.ALIGN_CENTER, 1, bfBold12);

        insertCell(table, "Total Amount", Element.ALIGN_CENTER, 1, bfBold12);
        table.setHeaderRows(1);

        //insert an empty row
       // insertCell(table, "", Element.ALIGN_LEFT, 6, bfBold12);
        //create section heading by cell merging
        // insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
        double orderTotal, total = 0;
        //just some random data to fill

        //count listarray size
        //int listsize=list.size();
        int lissz= localdbitems.size();
        sum=0;
        try {
           // View view;

            for (int x = 0; x < localdbitems.size(); x++) {
                int sl = x + 1;
                //String p_code = localdbitems.get(x).getCode();
                String p_code = localdbitems.get(x).getCode();
                String p_name = localdbitems.get(x).getName();
                String p_gross = localdbitems.get(x).getGROSS_PRICE();
                //View view=listitems.getChildAt(x);
                View view = listitems.getAdapter().getView(x,null,listitems);
                EditText qntyText=view.findViewById(R.id.qnty);
                EditText PriceText=view.findViewById(R.id.price);
                //  String p_qnty = list.get(x).get("QNTY");
                //if(Integer.parseInt(qntyText.getText().toString())>0){
                 p_qnty   =qntyText.getText().toString();
                 p_price  =PriceText.getText().toString();
                 s        =p_price;
              //  }


                //String p_price = list.get(x).get("PROD_PRICE");


                //String s = p_price.indexOf(".") < 0 ? p_price : p_price.replaceAll("0*$", "").replaceAll("\\.$", "");
                double unit_gross = (Double.parseDouble(p_qnty) * Double.parseDouble(s));
                percent = ((Double.parseDouble(p_gross)-Double.parseDouble(s))/Double.parseDouble(p_gross))*100;

                insertCell(table, "" + sl, Element.ALIGN_CENTER, 1, tablerowfont);
                insertCell(table, p_code, Element.ALIGN_CENTER, 1, tablerowfont);
                insertCell(table, p_name, Element.ALIGN_LEFT, 1, tablerowfont);
                insertCell(table, p_qnty, Element.ALIGN_RIGHT, 1, tablerowfont);
                insertCell(table, p_gross, Element.ALIGN_RIGHT, 1, tablerowfont);
                insertCell(table, ""+String.format("%.2f", percent), Element.ALIGN_CENTER, 1, tablerowfont);
                insertCell(table, s, Element.ALIGN_CENTER, 1, tablerowfont);
                insertCell(table, "" + String.format("%.2f", unit_gross), Element.ALIGN_RIGHT, 1, tablerowfont);

                sum = sum + unit_gross;
                //  orderTotal = Double.valueOf(df.format(Math.random() * 1000));
                // total = total + orderTotal;
                // insertCell(table, df.format(orderTotal), Element.ALIGN_RIGHT, 1, bf12);

            }
            //merge the cells to create a footer for that section
            insertCell(table, "Gross Total: ", Element.ALIGN_RIGHT, 7, bfBold12);
            insertCell(table, "" +  String.format("%.2f", sum), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "Discount: ", Element.ALIGN_RIGHT, 7, bfBold12);
            insertCell(table, "" + new_discount + "\t%", Element.ALIGN_RIGHT, 1, bfBold12);
            //float percent = (n * 100.0f) / v;
            tempsum = (double)( sum * (double)Integer.parseInt(new_discount)) / (double)100;
            Log.e("Net Sum", "sum " + sum + "\tdiscount " + new_discount + "-" + Integer.parseInt(new_discount) + "\tAddress " + new_address);
            netsum=sum-tempsum;

            insertCell(table, "Net Total: ", Element.ALIGN_RIGHT, 7, bfBold12);
            insertCell(table, "" +  String.format("%.2f", netsum), Element.ALIGN_RIGHT, 1, bfBold12);

            //repeat the same as above to display another location
            //    insertCell(table, "", Element.ALIGN_LEFT, 4, bfBold12);
            // insertCell(table, "California Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
        } catch (Exception e){
            e.printStackTrace();
            Log.e("Error On Order Details", "sum: " + sum + "\tdiscount: " + new_discount + "-" + Integer.parseInt(new_discount) + "\tAddress: " + new_address+"\t\t"+e.toString());

        }
        orderTotal = 0;
        Paragraph paragraph= new Paragraph();
        //add the PDF table to the paragraph
        paragraph.add(table);
        // add the paragraph to the document
        document.add(paragraph);


// Create new Page in PDF
        // document.newPage();
    }




    private void addFooterPart(Document doc) throws DocumentException,IOException {
        Font FooterHeead = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font Footersub = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL | Font.UNDERLINE, BaseColor.DARK_GRAY);
        Font Footersubwarn = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.RED);

        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
      //  Font bangla= new Font(Font.FontFamily.UNDEFINED,12);
        Font font = FontFactory.getFont("/assts/SolaimanLipi.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
        BaseFont baseFont = font.getBaseFont();
        Font bangla= new Font(baseFont);
      //  MoneyConverters converter = MoneyConverters.ENGLISH_BANKING_MONEY_VALUE;
       // String moneyAsWords = converter.asWords(new BigDecimal(total_price));
        EnglishNumberToWords eng= new EnglishNumberToWords();
        //String moneyAsWords=""+ eng.convert(Integer.parseInt(total_price));
        moneyAsWords=""+ eng.convert((int)netsum);
     //   String warn= "\n"+ getString(R.string.order_sheet_warn_bn);
        /*String warn ="বি:দ্র: কোম্পনীর কোন প্রতিনিধির সাথে কোন অবস্থাতেই নগদ বা ক্যাশ টাকার লেনদেন করবেন না। নগদ টাকা লেনদেনের ফলে কোন প্রকার সমস্যা সৃষ্টি হলে কোম্পানী কোন প্রকার দ্বায় দায়িত্ব গ্রহণ করবে না।\n" +
                " কোম্পানীর নামে ডি.ডি, টি.টি, এম.টি, পে-অর্ডার এবং চেকই একমাত্র বৈধ লেনদেন বলে গন্য হবে। উক্ত নিয়ম ব্যাতীত অন্য কোন উপায়ে সংগঠিত কোন লেনদেনই কোম্পানীর নিকট বৈধ বলে গন্য হবে না।";
*/


        Paragraph prFootLeft = new Paragraph();
        prFootLeft.setFont(smallBold);
        prFootLeft.add("Amount in Words     :\t"+moneyAsWords+" \tTaka (BDT) Only.");
        prFootLeft.add("\n\n");
        doc.add(prFootLeft);
        //Table
        float[] columnWidths = {50f,50f};
        PdfPTable table = new PdfPTable(10);
        table.setTotalWidth(Utilities.millimetersToPoints(180));
        table.setWidthPercentage(95f);
        table.setLockedWidth(true);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.addCell(getLeftCell(5));
        table.addCell(getRigtCell(5));

        table.completeRow();
        doc.add(table);
        //add footer image
        Paragraph pdffoot = new Paragraph();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.footer_warning_lg);
        bitmap.createScaledBitmap(bitmap, 560, 100, true);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image footimg = Image.getInstance(stream.toByteArray());
        footimg.scaleAbsolute(550f,60f);
        footimg.setAlignment(Image.ALIGN_CENTER);
        pdffoot.add(footimg);
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());
        String copyright=" \n\t\t\t\t \u00A9 Copyright : NPOLY."+"  Printing Date with time: "+timeStamp;
        pdffoot.setAlignment(Element.ALIGN_CENTER|Element.ALIGN_BOTTOM);
        pdffoot.add(copyright);
        doc.add(pdffoot);

    }

    private PdfPCell getLeftCell(int cm) {
        PdfPCell cell = new PdfPCell();

          cell.setColspan(cm);
          cell.setBorder(PdfPCell.NO_BORDER);
        //cell.setColspan((int)cm);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph prFootLeft = new Paragraph(
                String.format("",10 * cm),
                new Font(Font.FontFamily.HELVETICA, 8));
       // Paragraph prFootLeft = new Paragraph();
       //  prFootLeft.setFont(normal);
        prFootLeft.add("Terms & Condition    :\t "+tnc );
        prFootLeft.add("\nMode of Payment      :\t "+mop);
        prFootLeft.add("\nDiscount                   : \t "+new_discount+"\t% discount");
      //  prFootLeft.setFont(smallBold);
        prFootLeft.add("\nBank Name and Cheque No    :\t "+bank_names+"-"+bank_cheque+"\nCheque Date:"+chk_dt.getText().toString().trim());
        //doc.add(prFootRight);


        cell.addElement(prFootLeft);
        return cell;
    }

    private PdfPCell getRigtCell(int cm) {
        PdfPCell cell = new PdfPCell();

        cell.setColspan(cm);
       // cell.setColspan((int)cm);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph prFootRight = new Paragraph(
                String.format("", 10 * cm),
                new Font(Font.FontFamily.HELVETICA, 8));
       // Paragraph prFootRight= new Paragraph();
       // mode_of_paymet,tnc,bank_cheque,delivery_date,trns,wareh
        prFootRight.add("Delivery Date  :\t"+delivery_date);
        prFootRight.add("\nTransport        :\t"+trns);
        prFootRight.add("\nWarehouse      :\t"+wareh);
        //doc.add(prFootRight);

        prFootRight.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(prFootRight);
        return cell;
    }


    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }



    /*----------------pdf end-----------------------------------------------------------------*/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    public void startSendingOrder(final View view) {
     //   or.notifyDataSetChanged();
     //   listitems.destroyDrawingCache();
     //   getItems();
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

          new_add =(EditText)mView.findViewById(R.id.new_address);
          new_disc =(EditText)mView.findViewById(R.id.discount_txt);
//Report footer information
        //mode_of_payment =(Spinner) mView.findViewById(R.id.mode_of_payment);

        mode_of_payment = (Spinner) mView.findViewById(R.id.mode_of_payment);
        tempmop=""+mode_of_payment.getSelectedItem().toString().trim();
//        Log.e("Mode Of payment",""+mode_of_payment.getSelectedItem().toString());
        bank_check = (EditText)mView.findViewById(R.id.bank_check);
        deli_date = (EditText)mView.findViewById(R.id.deli_date);
        chk_dt = (EditText)mView.findViewById(R.id.cheque_date);
        Transport = (Spinner) mView.findViewById(R.id.transport);

        warehouse = (Spinner) mView.findViewById(R.id.warehouse);
        bank_name =(Spinner)mView.findViewById(R.id.bank);

        shiptotext.setVisibility(View.VISIBLE);
        paytotext.setVisibility(View.VISIBLE);
        snap.setVisibility(View.VISIBLE);
        bank_name.setVisibility(View.VISIBLE);
        chk_dt.setVisibility(View.VISIBLE);
        if(shiptotext.getText().toString().trim().isEmpty()||shiptotext.getText().toString().trim().equals("Delivery Address")||shiptotext.getText().toString().trim().equals("")) {
            new_add.setVisibility(View.VISIBLE);
            new_address = new_add.getText().toString().trim();
        }
        else {
            new_add.setVisibility(View.GONE);
        }
        new_disc.setVisibility(View.GONE);

        new_discount = new_disc.getText().toString().trim();
        final String party_code=ProductMain.party_code;
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

        deli_date.setOnClickListener(new View.OnClickListener() {
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
                    String myFormat = "dd/MM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    deli_date.setText(sdf.format(myCalendar.getTime()));
                }

            };
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProductSend.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        chk_dt.setOnClickListener(new View.OnClickListener() {
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
                    String myFormat = "dd/MM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    chk_dt.setText(sdf.format(myCalendar.getTime()));
                }

            };
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProductSend.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

                        //payto=paytotext.getText().toString().trim ().split("\\-")[0];
                        //shipto=shiptotext.getText().toString().trim().split("\\-")[0];
                        payto=paytotext.getText().toString().trim ().replaceAll("\\|\\|*","");
                        shipto=shiptotext.getText().toString().trim().replaceAll("\\|\\|*","");

                        ordertype=order_type.getText().toString().trim();
                        pricelist=price_list.getText().toString().trim();
                        new_address = new_add.getText().toString().trim();
                        //new_discount= new_disc.getText().toString().trim();
                        if (new_discount.isEmpty()){
                            new_discount= "Auto";
                        }
                    //    final String taglia = mode_of_payment.getSelectedItem().toString();

                      //  String taglia = spinnerTaglia.getSelectedItem().toString();

                    if(tempmop.isEmpty()){
                        mop="Payment Method Not defined.";
                        }else{
                        mop=""+mode_of_payment.getSelectedItem().toString().trim();
                    }



                        tnc = note;
                        bank_cheque =bank_check.getText().toString().trim();
                        delivery_date =deli_date.getText().toString().trim();
                        bank_names =bank_name.getSelectedItem().toString().trim();

                        trns= ""+Transport.getSelectedItem().toString();
                        wareh= ""+warehouse.getSelectedItem().toString().trim();

                        System.out.println("Return and processing"+payto);
                        Log.e("Return Additional info","Transport\t"+trns+"Mode Of Payment\t"+mop+"PayTo\t"+payto+"\t New add: "+new_address+"\tDiscount:"+new_discount);
                        SendOrder(view, image_str, note,payto,shipto,mop);


                    }
                })
                .setNeutralButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                LGSnackbarManager.show(INFO, "Order saving, please wait..");
                                try{

                                    //additional data
                                    note=""+userInputDialogEditText.getText().toString().trim()+"";

                                    //payto=paytotext.getText().toString().trim ().split("\\-")[0];
                                    //shipto=shiptotext.getText().toString().trim().split("\\-")[0];
                                    payto=paytotext.getText().toString().trim ().replaceAll("\\|\\|*","");
                                    shipto=shiptotext.getText().toString().trim().replaceAll("\\|\\|*","");

                                    ordertype=order_type.getText().toString().trim();
                                    pricelist=price_list.getText().toString().trim();
                                    new_address = new_add.getText().toString().trim();
                                    //new_discount= new_disc.getText().toString().trim();
                                    if (new_discount.isEmpty()){
                                        new_discount= "Auto";
                                    }
                                    //    final String taglia = mode_of_payment.getSelectedItem().toString();

                                    //  String taglia = spinnerTaglia.getSelectedItem().toString();

                                    if(tempmop.isEmpty()){
                                        mop="Payment Method Not defined.";
                                    }else{
                                        mop=""+mode_of_payment.getSelectedItem().toString().trim();
                                    }



                                    tnc = note;
                                    bank_cheque =bank_check.getText().toString().trim();
                                    delivery_date =deli_date.getText().toString().trim();
                                    bank_names =bank_name.getSelectedItem().toString().trim();

                                    trns= ""+Transport.getSelectedItem().toString();
                                    wareh= ""+warehouse.getSelectedItem().toString().trim();

                                    //create pdf
                                    createPdf(view);
                                    // db.deleteTempOrder(party_code);
                                    db.deleteTempOrder(party_code);

                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.e("PDF ERROR","PDF error occurred"+e.toString());
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




    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // imageView.setImageBitmap(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream); //compress to which format you want.
                photo.createScaledBitmap(photo,700,600,false);
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

//BILL LOCATION
    private void getpartyBillLocataion(String party_code) {

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

                      //  payto =ship_code.split("\\-")[0];
                         payto = ship_code.replaceAll("\\|\\|*","");
                        paytotext.setText(payto);
                        Log.i("Selected Payment",""+paytotext+" - "+payto);

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
//SHIPMENT Location
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

                        //shipto =ship_code.split("\\-")[0];
                        shipto = ship_code.replaceAll("\\|\\|*","");
                        shiptotext.setText(shipto);
                        Log.i("Selected Shipping",""+shiptotext+" - "+shipto);
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
//SMS SEND------------------------------------------------------------------------------------------
    public void sendSMS(String phoneNo, String Message) {
        if (phoneNo.isEmpty()) {
            phoneNo = "01938804250";

        }


        Log.e("SMSsend","TO:\t"+phoneNo+"\n Body \t"+Message);
        PackageManager pm = this.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)) {
            Toast.makeText(this, "Sorry, your device probably can't send SMS...", Toast.LENGTH_SHORT).show();

        }
        else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                try {
                    Log.e("SMSPermission","Permission Granted!");
                    SmsManager sms = SmsManager.getDefault();

                    ArrayList<String> messageParts = sms.divideMessage(Message);

/* ---- Preparing Intents To Check While Sms Sent & Delivered ---- */

                    Context curContext = this.getApplicationContext();

                    int partsCount = messageParts.size();

                    ArrayList<PendingIntent> sentPendings = new ArrayList<PendingIntent>(partsCount);

                    ArrayList<PendingIntent> deliveredPendings = new ArrayList<PendingIntent>(partsCount);

                    for (int i = 0; i < partsCount; i++) {

    /* Adding Sent PendingIntent For Message Part */

                        PendingIntent sentPending = PendingIntent.getBroadcast(curContext,
                                0, new Intent("SENT"), 0);

                        curContext.registerReceiver(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context arg0, Intent arg1) {
                                switch (getResultCode()) {
                                    case Activity.RESULT_OK:
                                        Toast.makeText(getBaseContext(), "Message Sent.",
                                                Toast.LENGTH_LONG).show();
                                        break;
                                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                        Toast.makeText(getBaseContext(),
                                                "Not Sent: Generic failure.", Toast.LENGTH_LONG)
                                                .show();
                                        break;
                                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                                        Toast.makeText(
                                                getBaseContext(),
                                                "Not Sent: No service (possibly, no SIM-card).",
                                                Toast.LENGTH_LONG).show();
                                        break;
                                    case SmsManager.RESULT_ERROR_NULL_PDU:
                                        Toast.makeText(getBaseContext(), "Not Sent: Null PDU.",
                                                Toast.LENGTH_LONG).show();
                                        break;
                                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                                        Toast.makeText(
                                                getBaseContext(),
                                                "Not Sent: Radio off (possibly, Airplane mode enabled in Settings).",
                                                Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }, new IntentFilter("SENT"));

                        sentPendings.add(sentPending);

    /* Adding Delivered PendingIntent For Message Part */

                        PendingIntent deliveredPending = PendingIntent.getBroadcast(
                                curContext, 0, new Intent("DELIVERED"), 0);

                        curContext.registerReceiver(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context arg0, Intent arg1) {
                                switch (getResultCode()) {
                                    case Activity.RESULT_OK:
                                        Toast.makeText(getBaseContext(), "Delivered.",
                                                Toast.LENGTH_LONG).show();
                                        break;
                                    case Activity.RESULT_CANCELED:
                                        Toast.makeText(getBaseContext(),
                                                "Not Delivered: Canceled.", Toast.LENGTH_LONG)
                                                .show();
                                        break;
                                }
                            }
                        }, new IntentFilter("DELIVERED"));

                        deliveredPendings.add(deliveredPending);
                    }

/* -----------------Adding recipiants ---------------------------------------------- */
/*
// string input by a user
                    String userInput = "122323,12344221,1323442";

// split it between any commas, stripping whitespace afterwards
                    String numbers[] = userInput.split(", *");*/


                    String numbers[] = toSMS;


                    for(String number : numbers) {
                        sms.sendMultipartTextMessage(number, null, messageParts, sentPendings, deliveredPendings);
                    }



                } catch (Exception ErrVar) {
                    Toast.makeText(getApplicationContext(), ErrVar.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ErrVar.printStackTrace();
                    Log.e("ErrorSMS", "Failed  to send\t" + ErrVar.toString().trim());
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                }
            }
        }
    }

//---------------------------SMS sending end-------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void SendOrder(View view, String image_str, final String  note, final String payTo, final String shipTo, final String pyament) {

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
            String FINAL_ORD ="";
            //String party_code = party_code;

            is_active = "Y";
            int itemsCount = listitems.getChildCount();
            int itemscnt=listitems.getAdapter().getCount();
            int cnt =localdbitems.size();
            int maxsend=0;
            for (int i = 0, count = cnt; i < count; ++i) {

           //     view = listitems.getChildAt(i);
                mob_order_id = "" + year + "" + month + "" + i;
                String PSL = ""+i;
                String PQNTY = ""+localdbitems.get(i).getQnty().toString().trim();
                String PPRICE = ""+localdbitems.get(i).getPrice().toString().trim();
                prod_code= ""+localdbitems.get(i).getCode().toString().trim();
                prod_name = ""+localdbitems.get(i).getName().toString().trim();
                uom         = ""+localdbitems.get(i).getUom().toString().trim();

              /*  String PQNTY    = ((EditText) view.findViewById(R.id.qnty)).getText().toString().trim();
                String PSL      = ((TextView) view.findViewById(R.id.pid)).getText().toString().toString();
                String PPRICE   = ((EditText) view.findViewById(R.id.price)).getText().toString().toString();
                prod_code       = ((TextView) view.findViewById(R.id.p_code)).getText().toString().toString();
                prod_name       = ((TextView) view.findViewById(R.id.name)).getText().toString().toString();
                uom             = ((TextView) view.findViewById(R.id.uom)).getText().toString().toString();*/
                qnty            = PQNTY;
                prod_price      = PPRICE;

                Log.e("Insert Item count", "" + i + ">" + PSL + ">>" + PQNTY+" Time Stamp:"+timestamp);
                //  OrderHeap.put(PSL, PQNTY);
                // OrderList.add(OrderHeap);
                //String mob_order_id,String prod_code,String prod_name ,String uom,String qnty,String order_by,String org_id,String party_code,String order_date,String is_active
                //Local  db\


               // if ((!qnty.isEmpty()&&!prod_price.isEmpty())||(!qnty.equals("")&&!prod_price.equals(""))||!qnty.equals("0")){
                    if (Integer.parseInt(qnty)>0){
                    Log.e ("Inserting values",": "+mob_order_id+"-"+prod_code+"-"+ prod_name+"-"+ uom+"-"+ qnty+"-"+prod_price+"-" +emp_code+"-"+ org_id+"-"+ party_code+"-"+ order_date+"-"+ is_active+"\tpayment:"+pyament);

                    //loaca db
                   // OrderHeap.put(PSL, PQNTY);
                   // OrderList.add(OrderHeap);
                    //main db
                   StoreOrdRemoteDb(prod_code, prod_name, uom, qnty,prod_price, emp_code, org_id, party_code,order_date,image_str,note,payTo,shipTo,cnt,"P-"+pyament);
               //     createPdf(view);
                    Log.e("Must be Address",""+payTo+"\t"+shipTo+"\tfinal order"+FINAL_ORD);


                }
                else{
                    final Snackbar sb= Snackbar.make(view, "Quantity or Price Missing", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
                            .setAction("Action", null);
                    View snackBarView = sb.getView();
                    snackBarView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackBarView.setBackgroundColor(Color.RED);
                    sb.show();
                }
                maxsend++;

            }
            if(maxsend==itemscnt){
                //SendEmail(FINAL_ORD,image_str,party_code,order_by,attachimg,note);
                //sendSMS(party_phone,sms_body);
                //setFinalOrdre(emp_code, org_id, party_code);
                createPdf(view);
                db.deleteTempOrder(party_code);

            }
            //  SendEmail(ORD_ID,image_str,party_code,order_by,attachimg,note);
            //refresh on successfull order

        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("Error on inserting",e.toString());
        }


    }

    private void setFinalOrdre( final String emp_code, final String org_id, final String party_code) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL_SET_ORDER);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("emp_code", emp_code));
            nameValuePairs.add(new BasicNameValuePair("org_id", org_id));
            nameValuePairs.add(new BasicNameValuePair("party_code", party_code));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            Log.e("Finalize OK"," Order Saved!" +response.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Finalize Error","Can't Finalize saving orders" +e.toString());
        }


    }

    private String StoreOrdRemoteDb(
                                        final   String pprod_code,
                                        final   String pprod_name ,
                                        final   String puom,
                                        final   String pqnty,
                                        final   String pprod_price,
                                        final   String porder_by,
                                        final   String porg_id,
                                        final   String pparty_code ,
                                        final   String porder_date,
                                        final   String pimage_str,
                                        final   String pNote,
                                        final   String pPayTo,
                                        final   String pShipTo,
                                        final int cnt,
                                        final String mop
    ) {
        // Tag used to cancel the request
        Log.e("Sending order","Please Wait."+pprod_code+"-"+pprod_name+"-"+puom+"-"+pqnty+"-"+pprod_price+"-"+porder_by+"-"+porg_id+"-"+pparty_code+"-"+porder_date+"-"+pimage_str+"-"+pNote+"-"+pPayTo+"-"+pShipTo+"-"+mop);
        final String tag_string_req = "req_register";
        final int eamil_count=1;
        final String final_order;

        // if(revieworeder.isEmpty()){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PRODUCT_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Sending Order", "DO Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.e("JSON RESPONSE : ",""+error);
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        SEND_ORD_ID = jObj.getString("order");
                        final String final_order=SEND_ORD_ID;
                        Log.e("Order Number" ,SEND_ORD_ID+" Generated ");
                        if ( final_order==SEND_ORD_ID) {
                            if (!SendEmail(final_order,image_str,party_code,order_by,attachimg,note).isEmpty()) {

                               ConfirmToBack(final_order);

                            }
                        }


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("URL ERROR on insert",e.toString());

                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Sending Order", "DO Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
               // hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //mob_order_id, prod_code, prod_name, uom, qnty, order_by, org_id, party_code, order_date, is_active
                //  params.put("mob_order_id", mob_order_id);
                Log.e("Binding Parmas","prod_code"+pprod_code+"\tqnty"+pqnty+"\tprod_price"+pprod_price+"\t Payment:"+mop);

                params.put("prod_code", ""+pprod_code);
                params.put("prod_name", ""+pprod_name);
                //params.put("hasvalue",OrderHeap);
                params.put("uom", ""+puom);
                params.put("qnty", ""+pqnty);
                params.put("prod_price", ""+pprod_price);
                params.put("order_by",""+porder_by);
                params.put("org_id", ""+porg_id);
                params.put("party_code", ""+pparty_code);
                params.put("order_date", ""+porder_date);
                params.put("image_str", ""+pimage_str);
                params.put("note", ""+pNote);
                params.put("payto", ""+pPayTo);
                params.put("shipto", ""+pShipTo);
                params.put("order_type", ""+saletype.getText().toString().trim().replaceAll("[\\\"\\+\\.\\^:,]\\-", ""));
                params.put("totalitems", ""+cnt);
                params.put("mop", ""+mop.toString().trim().replaceAll("[\\\"\\+\\.\\^:,]\\-", ""));
                return params;

            }

            private Map<String, String> params(Map<String, String> map){
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
                    if(pairs.getValue()==null){
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return SEND_ORD_ID;

    }

    private String SendEmail(String oid, final String img, final String party_code, final String order_by, final Bitmap attachimg, final String note) {
        try {
            imgattch.setDrawingCacheEnabled(true);

            String filename="DOAttachment.jpeg";
            String subject = "An Order "+oid+" done and \n "+ordersheetname+" Attached.";
            String Addrs= getAddress(LoginActivity.finallat, LoginActivity.finallong);
            //     dbDataView.setText(Html.fromHtml(branchInfoString ));
            String username= Nameview.getText().toString().trim();
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
            //String pdfpath="/salebook/salebook_order.pdf";
            String pdfpath="/salebook/"+ordersheetname;
            File pdffile = new File(root,pdfpath);
            //   file.setReadable(true,false);
            Log.e("File path"," "+file+"\t&\t"+pdffile);
            Uri uri = Uri.fromFile(file);
            Uri pdfuri = Uri.fromFile(pdffile);
            sendEmailAlert(file,pdffile,subject,body);
            Log.e("Email Subject",""+subject);

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
        GPSTracker gps = new GPSTracker(ProductSend.this);
        Geocoder geocoder = new Geocoder(ProductSend.this, Locale.getDefault());
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
    private void sendEmailAlert(File fileName, File pdffile ,String subject,String text) {
try {
    final File file = fileName;
    final File filepdf = pdffile;
    List<Intent> targetedShareIntents = new ArrayList<Intent>();

    final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    //   Intent intent = new Intent(Intent.ACTION_MAIN);
    //   intent.addCategory(Intent.CATEGORY_APP_EMAIL);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);

    // intent.setType("message/email");
    intent.setType("message/rfc822");
    Log.e("EmailAlert", "Sending Email with subject: " + subject);

    List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
    if (!resInfo.isEmpty()) {
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            final Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("message/rfc822");
            targetedShareIntent.putExtra(Intent.EXTRA_EMAIL, toemail);
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
          /*  if (!filepdf.exists() || !filepdf.canRead()) {
                Toast.makeText(getApplicationContext(), "Attachment Error", Toast.LENGTH_SHORT).show();
                return;
            }*/
            ArrayList<Uri> uris = new ArrayList<Uri>();
            //File path:  /storage/emulated/0/DirName/DOAttachment.jpeg	&	/storage/emulated/0/salebook/salebook_order.pdf
            Uri uri = Uri.fromFile(file);
            Uri pdfUri = Uri.fromFile(filepdf);
            uris.add(uri);
            uris.add(pdfUri);
            //  targetedShareIntent.putParcelableArrayListExtra (Intent.EXT
            // RA_STREAM, uris);
            //targetedShareIntent.putExtra (Intent.EXTRA_STREAM, uri);
            targetedShareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);

            if (TextUtils.equals(packageName, "com.microsoft.office.outlook")) {
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            } else {
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text + " \n [Outlook Email client Not found]");
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
            //  startActivity(chooserIntent);
            startActivity(Intent.createChooser(chooserIntent, "Share using?"));
            finish();
            return;
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


} catch (Exception e){
    e.printStackTrace();
    Log.e("Email Error",e.toString());
}



    }
    /*----------------------------Order sending close--------------------------------------------------------------------*/
    private void ConfirmToBack(final String oid){
        // SendEmail(ORD_ID,image_str,party_code,order_by);
        sms_body="An order has been placed on behalf of your "+cust_id.getText().toString().trim()+"\n Your Order Number is "+oid+"\n Delivery Date:"+delivery_date+"\n Order By:"+emp_code ;


        FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(ProductSend.this)
                //alert.setImageRecourse(R.mipmap.ic_launcher)
                .setTextTitle("Order Submitted").setBackgroundColor(R.color.bg_pink).setTitleColor(R.color.azure)
                .setTextSubTitle("Your Order Number is: \n"+oid+"").setSubtitleColor(R.color.white)
                .setBody("| Now you can select new customer(party) for new order |").setBodyColor(R.color.white)
                .setNegativeColor(R.color.white)
            /*    .setNegativeButtonText("NO")
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        //SendEmail(oid, image_str, party_code, order_by, attachimg, note);
                        //ProductList.this.finish();
                    }
                })*/
                .setPositiveButtonText("OK")
                .setPositiveColor(R.color.white)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {

                     //   SendEmail(oid,image_str,party_code,order_by,attachimg,note);

                        //disable sms send
                    //    sendSMS(party_phone,sms_body);

                            //Toast.makeText(MainActivity.this, "Updating", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Opening Email Client", Toast.LENGTH_LONG).show();
                            // if(!SendEmail(oid, image_str, party_code, order_by, attachimg, note).isEmpty()&& SendEmail(oid, image_str, party_code, order_by, attachimg, note)=="TRUE"){
                            Toast.makeText(getApplicationContext(), "Order Successfully Sent!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ProductSend.this,
                                    MainActivity.class);//Add the bundle to the intent
                            intent.putExtra("org_id", org_id);
                            intent.putExtra("emp_code", ID);
                            intent.putExtra("party_code", party_code);
                            startActivity(intent);

                    }
                })
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(false).setButtonsGravity(FancyAlertDialog.PanelGravity.CENTER.CENTER)
                .build();
        alert.show();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();}

    }



}
