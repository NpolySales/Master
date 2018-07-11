package com.metrosoft.arafat.salebook.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.metrosoft.arafat.salebook.holder.HashMapColumn;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = "SQLITE DB";//;SQLiteHandler.class.getSimpleName();
public static String PrntCat;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_CUSTOMER = "customers";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String   TABLE_ORDER ="MOB_PROD_ORDER";
    private static final String   MOB_ORDER_ID ="MOB_ORDER_ID";
    //---------------------------------------------------
    public static final String   PROD_CODE ="PROD_CODE";
    public static final String   PROD_NAME ="PROD_NAME";
    public static final String   UOM ="UOM";
    public static final String   QNTY ="QNTY";
    public static final String   ORDER_BY ="ORDER_BY";
    public static final String   ORG_ID ="ORG_ID";
    public static final String   PARTY_CODE ="PARTY_CODE";
    public static final String   ORDER_DATE ="ORDER_DATE";
    private static final String   IS_ACTIVE ="IS_ACTIVE";
    private static final String   SALE_TYPE ="SALE_TYPE";
    private static final String   PROD_CAT ="PROD_CAT";
    //Temp order
    private static final String TABLE_TEMP_ORDER = "TEMPORDERDB";
    private static final String SLNO ="ORDSL";
    public static final String PRODUCTPRICE ="PROD_PRICE";
    public static final String PRODUCTLINEPRICE ="LINE_RATE";
    public static final String PROD_DESC ="PROD_DESC";

    //TEMP ORDER NUMBER
    private static final String TABLE_TEMP_ORDER_NUM = "ORDERNUM";
    private static final String MAX_ORD_NUM ="MAXORDERNUM";
    private static final String TOTAL_ORDER_HOLD ="ORDER_SUM";
    private static final String ORDER_MAX ="ORDER_MAX";


    //TEMP PRODUCTS
    private static final String ALL_PRODUCTS="ALL_PRODUCTS";
    private static final String ID="ID";
    private static final String PRODUCT_NAME="PRODUCT_NAME";
    private static final String PRODUCT_DESCRIPTION="PRODUCT_DESCRIPTION";
   // private static final String UOM="UOM";
    private static final String PRICE="PRICE";
    //private static final String ORG_ID="ORG_ID";
    private static final String CAT_ID="CAT_ID";

   /* ID,
    PRODUCT_NAME,
    UOM,
    PRODUCT_DESCRIPTION,
    PRICE
            ORG_ID
    CAT_ID*/




    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_EMAIL + " TEXT UNIQUE," + KEY_PHONE + " TEXT," + KEY_UID + " TEXT,"
                    + KEY_CREATED_AT + " TEXT" + ")";


            String CREATE_TEMP_ORDER_TABLE = "CREATE TABLE " + TABLE_TEMP_ORDER + "("
                    + PROD_CODE + " TEXT NOT NULL  ,"
                    + PROD_NAME + " TEXT  ," + PROD_DESC + " TEXT  , "
                    + UOM +       " TEXT  ," + QNTY + " TEXT, " + PRODUCTPRICE + " TEXT, " + PRODUCTLINEPRICE+" TEXT, "
                    + ORDER_BY +  " TEXT  ," + ORG_ID +    " TEXT  , "
                    + PARTY_CODE +" TEXT  NOT NULL, " + SALE_TYPE +" TEXT , "+ PROD_CAT +" TEXT , "+ ORDER_DATE +" DEFAULT CURRENT_TIMESTAMP , " + IS_ACTIVE + " TEXT ,  PRIMARY KEY(PROD_CODE,PARTY_CODE)   " + ")";

            String CREATE_PRODUCT_TABLE ="CREATE TABLE " + ALL_PRODUCTS +"("+
                    "" +ID+" TEXT NOT NULL , "+
                    "" +PRODUCT_NAME+ " TEXT ,"+
                    "" +UOM +"  TEXT,  "
                    +PRODUCT_DESCRIPTION+" TEXT, "
                    + PRICE + " TEXT, "
                    +ORG_ID + " TEXT, "
                    + CAT_ID +" TEXT "+
                    ")";

//not using
            String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "("
                    + MOB_ORDER_ID + " TEXT ," + PROD_CODE + " TEXT,"
                    + PROD_NAME + " TEXT ," + UOM + " TEXT," + QNTY + " TEXT,"
                    + ORDER_BY + " TEXT, " + "" + ORG_ID + " TEXT, " +
                    PARTY_CODE + " TEXT, " + ORDER_DATE + "  TEXT, " + IS_ACTIVE + " TEXT " + ")";

           String CREATE_MAX_ORDER_TABLE ="CREATE TABLE " + TABLE_TEMP_ORDER_NUM + "("+ KEY_ID + " INTEGER ," +  MAX_ORD_NUM  + "TEXT UNIQUE, " +TOTAL_ORDER_HOLD+" INTEGER, "+ORDER_MAX+" NUMBER, " +ORDER_BY+" TEXT, "+PARTY_CODE+" TEXT"  +")";


            db.execSQL(CREATE_ORDER_TABLE);
            db.execSQL(CREATE_LOGIN_TABLE);
            db.execSQL(CREATE_TEMP_ORDER_TABLE);
            db.execSQL(CREATE_MAX_ORDER_TABLE);
            db.execSQL(CREATE_PRODUCT_TABLE);
//db.close();
            Log.e(TAG, "Local Database tables created");
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e(TAG+"ERROR",e.toString());
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_TEMP_ORDER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_TEMP_ORDER_NUM);
        db.execSQL("DROP TABLE IF EXISTS "+ ALL_PRODUCTS);
        // Create tables again
        onCreate(db);
    }



    public String getOrderMax(final String party_code) {
        //  HashMap<String, String> tempOrd = new HashMap<String, String>();
        String pcode =party_code.replace("\"", "").replaceAll("[^0-9]", "");
        String countorder="";
        String selectMaxORD = "SELECT  * FROM " + TABLE_TEMP_ORDER_NUM+" WHERE PARTY_CODE ="+pcode+" ORDER BY PROD_CODE ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectMaxORD, null);
        if (cursor != null ) {
            cursor.moveToNext();
            if  (cursor.moveToFirst()) {

                for (int i=0; i<=cursor.getCount(); i++){
                    countorder=""+i;
                    cursor.moveToNext();
                }

            }
        }
        cursor.close();
        db.close();
        // return user
        Log.e(TAG, "Count TempOrder from Sqlite: " + countorder.toString());

        return countorder;
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email,String phone,  String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PHONE, phone); // phone
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public void storeOrder (String mob_order_id,String prod_code,String prod_name,String uom,String qnty,String order_by,String org_id,String party_code,String order_date,String is_active ){
        try {


       // String mob_order_id ="ORDER"+prod_code;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOB_ORDER_ID,mob_order_id);
        values.put(PROD_CODE,prod_code);
        values.put(PROD_NAME,prod_name);

        values.put(UOM,uom);
        values.put(QNTY,qnty);
        values.put(ORDER_BY,order_by);
        values.put(ORG_ID,org_id);
        values.put(PARTY_CODE,party_code);
        values.put(ORDER_DATE,order_date);
        values.put(IS_ACTIVE,is_active);
        long temp_ord_id = db.insert(TABLE_ORDER, null, values);
        db.close(); // Closing database connection

        Log.e(TAG, "New ORDER  inserted into sqlite: "+TABLE_ORDER+"\t" + temp_ord_id);}
        catch (Exception e){
            e.printStackTrace();
            Log.e("NOT INSERT",e.toString());
        }

    }

    /**
     *   "" +ID+" TEXT NOT NULL , "+
     "" +PRODUCT_NAME+ " TEXT ,"+
     "" +UOM +"  TEXT,  "
     +PRODUCT_DESCRIPTION+" TEXT, "
     + PRICE + " TEXT, "
     +ORG_ID + " TEXT, "
     + CAT_ID +" TEXT "+
     *
     *
     *
     * */

    public void offlineProducts(String ID, String PRODUCT_NAME,String UOM,String PRODUCT_DESCRIPTION, String PRICE, String ORG_ID, String CAT_ID   ){

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
        }
        catch ( Exception e){
            e.printStackTrace();
        }


    }


    public void saveTempOrder (String prod_code,String prod_name ,String prod_desc, String uom,String qnty,String gprice,String price, String order_by,String org_id,String party_code,String is_active, String sale_type,String prod_cat ){
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
          //  values.put(SLNO,SLNO);
            values.put(PROD_CODE,prod_code);
            values.put(PROD_NAME,prod_name);
            values.put(PROD_DESC,prod_desc);
            values.put(UOM,uom);
            values.put(QNTY,qnty);
            values.put(PRODUCTLINEPRICE,gprice);
            values.put(PRODUCTPRICE,price);
            values.put(ORDER_BY,order_by);
            values.put(ORG_ID,org_id);
            values.put(PARTY_CODE,party_code);
            values.put(SALE_TYPE,sale_type);
            values.put(PROD_CAT,prod_cat);
            values.put(IS_ACTIVE,is_active);

            long ord_id = db.insert(TABLE_TEMP_ORDER, null, values);
            db.close(); // Closing database connection

            Log.e(TAG, "New ORDER  inserted into sqlite: " + TABLE_TEMP_ORDER+"\t"+ord_id);}
        catch (Exception e){
            e.printStackTrace();
            Log.e("NOT INSERT"+TABLE_TEMP_ORDER,e.toString());
        }

    }
   /* String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE," + KEY_PHONE + " TEXT," + KEY_UID + " TEXT,"
            + KEY_CREATED_AT + " TEXT" + ")";*/

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(3));
            user.put("phone", cursor.getString(2));
            user.put("uid", cursor.getString(4));
            user.put("created_at", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public String cpountTempOrders(final String party_code) {
      //  HashMap<String, String> tempOrd = new HashMap<String, String>();
        String pcode =party_code.replace("\"", "").replaceAll("[^0-9]", "");
        String countorder="";
        String selectQuery = "SELECT  * FROM " + TABLE_TEMP_ORDER+" WHERE PARTY_CODE ="+pcode+" ORDER BY PROD_CODE ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null ) {
            cursor.moveToNext();
            if  (cursor.moveToFirst()) {

                for (int i=0; i<=cursor.getCount(); i++){
                    countorder=""+i;
                    cursor.moveToNext();
                }

               }
        }
        cursor.close();
        db.close();
        // return user
        Log.e(TAG, "Count TempOrder from Sqlite: " + countorder.toString());

        return countorder;
    }


    public String getOrderTotal(final String party_code) {
        //  HashMap<String, String> tempOrd = new HashMap<String, String>();
        String pcode =party_code.replace("\"", "").replaceAll("[^0-9]", "");
        String countorder="";
        String selectQuery = "SELECT SUM((CAST(PROD_PRICE as INTEGER))) TOTAL FROM " + TABLE_TEMP_ORDER+" WHERE PARTY_CODE ="+pcode+" ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null ) {
            cursor.moveToNext();
            if  (cursor.moveToFirst()) {

               // for (int i=0; i<=cursor.getCount(); i++){
                    countorder=cursor.getString(cursor.getColumnIndex("TOTAL"));;
                    cursor.moveToNext();
               // }

            }
        }
        cursor.close();
        db.close();
        // return user
        Log.e(TAG, "TOTAL Taka TempOrder from Sqlite: " + countorder.toString());

        return countorder;
    }



    public Cursor featchAllorders(){

        String selectQuery = "SELECT  * FROM " + TABLE_TEMP_ORDER+" ORDER BY PROD_CODE ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
            try {
        if (cursor != null) {
            cursor.moveToFirst();
        } }
        catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR"+TAG,""+e.toString());

        }
        return cursor;
    }

    public ArrayList<HashMap<String, String>> getOrderItems(final String party_code){
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        String query_select = "SELECT  * FROM " + TABLE_TEMP_ORDER+" WHERE PARTY_CODE ="+party_code+" ORDER BY PROD_CODE ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_select,null);


        int colCount = cursor.getColumnCount();
        int rowCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> col = new HashMap<String, String>();
                int size =cursor.getColumnCount();
                for (int i = 0; i < size; i++) {
                    col.put(cursor.getColumnName(i), cursor.getString(i));
                    Log.e("HashValus","\t Key"+cursor.getColumnName(i)+"\tValues: "+cursor.getString(i));
                }
                list.add(col);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }
        return list;
    }

    //Modification of products
    public ArrayList<HashMapColumn> getOrderItemObject(final String party_code){
        ArrayList<HashMapColumn> Items= new ArrayList<HashMapColumn>();


        String query_select = "SELECT  * FROM " + TABLE_TEMP_ORDER+" WHERE PARTY_CODE ="+party_code+" ORDER BY PROD_CODE ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query_select,null);
        /*int i=0;
        while(result.moveToNext()){
        //  car.add( new Cars(result.getString(result.getColumnIndex(PROD_CODE)), result.getString(result.getColumnIndex(QNTY)),result.getString(result.getColumnIndex(PR))));
        //Items.add(new HashMapColumn(result.getString(result.getColumnIndex(PROD_CODE)),result.getString(result.getColumnIndex(QNTY)),result.getString(result.getColumnIndex(PRODUCTPRICE)),result.getString(result.getColumnIndex(PROD_CODE)),result.getString(result.getColumnIndex(PROD_NAME)),result.getString(result.getColumnIndex(PROD_DESC)),result.getString(result.getColumnIndex(UOM))));
        Items.add(new HashMapColumn(""+i,result.getString(result.getColumnIndex(PROD_CODE)),result.getString(result.getColumnIndex(QNTY)),result.getString(result.getColumnIndex(PRODUCTPRICE)),result.getString(result.getColumnIndex(PROD_NAME)),result.getString(result.getColumnIndex(PROD_DESC)),result.getString(result.getColumnIndex(UOM))));
        i++;
        }*/

        int pcode = c.getColumnIndex(PROD_CODE);
        int pqnty = c.getColumnIndex(QNTY);
        int pprice = c.getColumnIndex(PRODUCTPRICE);
        int pname = c.getColumnIndex(PROD_NAME);
        int pdesc = c.getColumnIndex(PROD_DESC);
        int puom = c.getColumnIndex(UOM);
        int pcat= c.getColumnIndex(PROD_CAT);
        int psaletype= c.getColumnIndex(SALE_TYPE);
        int pgrossprice=c.getColumnIndex(PRODUCTLINEPRICE);
        //int toatal= (int)(pqnty)*(int)(pgrossprice);

        int i =0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            String pc = c.getString(pcode);
            String pq = c.getString(pqnty);
            String pp = c.getString(pprice);
            String pn = c.getString(pname);
            String pd = c.getString(pdesc);
            String pu = c.getString(puom);
            String pct=c.getString(pcat);
            String pst= c.getString(psaletype);
            String pgp= c.getString(pgrossprice);
            //showing total unite price
            String unittotal= String.valueOf(Float.parseFloat(pq)* Float.parseFloat(pp));
            PrntCat =pct;
            Items.add(new HashMapColumn(""+i,pc,pq,pgp,pp,pn,pd,pu,pst,pct,unittotal));

            Log.e("getOrderItemObject","sl\t"+i+"\tCODE\t"+pc+"\tQNTY\t"+pq+"\tPRICE\t"+pp+"\tPROD CAT\t"+pct+"\tPROD SALE TYPE\t"+pst+"\tUnit Total"+unittotal);

            i++;
         //   byte[] pic = c.getBlob(image);
           // employee_list.add(new Employee(name, email, pic));

        }
        return Items;
    }
    //updating Quantity and Price------------------------------------------------------------------
public void UpdateTableQNTY(String order_by,String party_code,String pcode,String qnty) {
    SQLiteDatabase db = this.getWritableDatabase();
    String countorder = "";
    String query_select = "SELECT  COUNT(PROD_CODE) GETPROD FROM " + TABLE_TEMP_ORDER + " WHERE  " + PROD_CODE + " = '" + pcode + "' AND " + ORDER_BY + "=" + order_by + " AND " + PARTY_CODE + "=" + party_code + "";
    Cursor c = db.rawQuery(query_select, null);
    if (c != null) {
        c.moveToNext();
        if (c.moveToFirst()) {
            countorder = c.getString(c.getColumnIndex("GETPROD"));
            c.moveToNext();
        }
    }
    c.close();
    if (!countorder.trim().isEmpty()){
    String qu = "UPDATE  " + TABLE_TEMP_ORDER + "  SET  " + QNTY + " = " + qnty + "  WHERE  " + PROD_CODE + " = '" + pcode + "' AND " + ORDER_BY + "=" + order_by + " AND " + PARTY_CODE + "=" + party_code + "";
    db.execSQL(qu);
    Log.e("UpdateTableQNTY", "Quantity Update for Product\t" + pcode + "\tQnty:\t" + qnty);
    }
    db.close();
   /* ContentValues values = new ContentValues();
    values.put(QNTY, qnty);
    db.update(TABLE_TEMP_ORDER,values,PARTY_CODE+"="+party_code+" AND "+ORDER_BY+"="+order_by+" AND "+PROD_CODE+"="+pcode,null);
    db.close();
    Log.e("UpdateTableQNTY","Quantity Update for Product\t"+pcode+"\tQnty:\t"+qnty);*/
}

    public void ZeroQntyDel(String order_by,String party_code,String pcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        String countorder = "";
        String query_select = "SELECT  COUNT(PROD_CODE) GETPROD FROM " + TABLE_TEMP_ORDER + " WHERE  " + PROD_CODE + " = '" + pcode + "' AND " + ORDER_BY + "=" + order_by + " AND " + PARTY_CODE + "=" + party_code + "";
        Cursor c = db.rawQuery(query_select, null);
        if (c != null) {
            c.moveToNext();
            if (c.moveToFirst()) {
                countorder = c.getString(c.getColumnIndex("GETPROD"));
                c.moveToNext();
            }
        }
        c.close();
        if (!countorder.trim().isEmpty()){
            String qu = "DELETE  FROM  " + TABLE_TEMP_ORDER + "  WHERE  " + PROD_CODE + " = '" + pcode + "' AND " + ORDER_BY + "=" + order_by + " AND " + PARTY_CODE + "=" + party_code + "";
            db.execSQL(qu);
            //Log.e("Delete zero", "Quantity Update for Product\t" + pcode + "\tQnty:\t" + qnty);
        }
        db.close();
   /* ContentValues values = new ContentValues();
    values.put(QNTY, qnty);
    db.update(TABLE_TEMP_ORDER,values,PARTY_CODE+"="+party_code+" AND "+ORDER_BY+"="+order_by+" AND "+PROD_CODE+"="+pcode,null);
    db.close();
    Log.e("UpdateTableQNTY","Quantity Update for Product\t"+pcode+"\tQnty:\t"+qnty);*/
    }



    public void UpdateTablePRICE(String order_by,String party_code,String pcode,String pprice){
        SQLiteDatabase db = this.getWritableDatabase();
        String qu="UPDATE  "+ TABLE_TEMP_ORDER + "  SET  "+PRODUCTPRICE+" = "+pprice+"  WHERE "+PROD_CODE+" = '"+pcode+"' AND "+ORDER_BY+"="+order_by+" AND "+PARTY_CODE+"="+party_code+";";
        db.execSQL(qu);
        Log.e("UpdateTablePRICE","Price Update for Product\t"+pcode+"\tPrice:\t"+pprice);
        db.close();
    }
    //------------------------------------------------------------------------------------------------

    public void deleteTempOrder(final String party_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String pcode =party_code.replace("\"", "").replaceAll("[^0-9]", "");
        // Delete All Rows

        db.delete(TABLE_TEMP_ORDER,
                PARTY_CODE + "=? " ,
                new String[] {pcode});
        //  db.delete(TABLE_TEMP_ORDER, pcode, null);
        db.close();

        Log.d(TAG, "Deleted all info from sqlite for " +party_code);
    }

    public void deleteTempOrderfull() {
        SQLiteDatabase db = this.getWritableDatabase();
        //  String pcode =party_code.replace("\"", "").replaceAll("[^0-9]", "");
        // Delete All Rows

        db.delete(TABLE_TEMP_ORDER, null, null);
        db.close();

        Log.i(TAG, "Deleted all user info from sqlite");
    }
//Not using
    public HashMap<String, String> getTempOrders() {
        HashMap<String, String> tempOrd = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEMP_ORDER+" ORDER BY PROD_CODE ASC";
        tempOrd.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null ) {
                cursor.moveToFirst();
            if  (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String disease_id = cursor.getString(cursor
                            .getColumnIndex("PROD_CODE"));
                    String disease_name = cursor.getString(cursor
                            .getColumnIndex("PROD_NAME"));
                    Log.e("HasmapID "+TAG,"count:"+i+"\t"+cursor.getColumnName(i)+"\tcode:"+disease_id);
                    Log.e("HasmapName "+TAG,"count:"+i+"\t"+cursor.getString(i)+"\tname :"+disease_name);

                   // IdName s = new IdName(disease_id,disease_name);
                    tempOrd.put(cursor.getColumnName(i),cursor.getString(i));
                    cursor.moveToNext();
                }
            }

        }
        cursor.close();
        db.close();
        // return user
        Log.e(TAG, "Fetching TempOrder from Sqlite: " + tempOrd.toString());

        return tempOrd;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers( ) {

        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}