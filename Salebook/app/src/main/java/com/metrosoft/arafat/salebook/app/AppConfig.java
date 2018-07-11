package com.metrosoft.arafat.salebook.app;

import java.util.Random;

import static com.metrosoft.arafat.salebook.activity.LoginActivity.pdm_email;
import static com.metrosoft.arafat.salebook.activity.LoginActivity.pdm_phone;
import static com.metrosoft.arafat.salebook.activity.LoginActivity.pzm_email;
import static com.metrosoft.arafat.salebook.activity.LoginActivity.pzm_phone;
import static com.metrosoft.arafat.salebook.activity.ProductMain.org_id;
import static com.metrosoft.arafat.salebook.activity.ProductMain.party_code;
import static com.metrosoft.arafat.salebook.activity.ProductSend.customerphone;

/**
 * Created by scribes on 8/23/2017.
 */

/*
* not that for authenticatin purpose go to
* https://myaccount.google.com/lesssecureapps
* and turn this ON for enable sending email
* */
public class AppConfig {

    public static String emp_code,terrytory_id;


   // public static String gemail ="elicitation08@gmail.com";
  //  public static String gemial_pass ="Ict@08007";
//    public  static  String customerphone=""+cust_phone.getText().toString().replace("\"", "").replaceAll("[^0-9]", "");
    public static String email ="arafat@nationalpolymer.net";
    public static String emial_pass ="Ara@npoly321#";
    public static String toemail[] = {"arafat@nationalpolymer.net","hotline@nationalpolymer.net",pdm_email,pzm_email};
    public static String toSMS[] = {"01938804250",pdm_phone,pzm_phone,customerphone};
    public static String enc="UTF-16";
   // String q = "random word £500 bank $";
    //String url = "http://example.com/query?q=" + URLEncoder.encode(q, "UTF-8");
    // Server user login url
   /* public static String URL_LOGIN = "http://115.127.7.157:8070/AAPI/login.php";    // Server user register url
    public static String URL_REGISTER = "http://115.127.7.157:8070/AAPI/register.php";
    public static String URL_CUSTMER_LIST = "http://115.127.7.157:8070/AAPI/get_customer_list.php";
    public static String URL_CUSTMER_INFO = "http://115.127.7.157:8070/AAPI/get_customer_info.php?party_code=";
    public static String URL_PRODUCT_LIST = "http
    ://115.127.7.157:8070/AAPI/get_productlist.php";*/

//IP route
public static String URL_TEST= "115.127.7.157";    // Server user register url


    public static String URL_LOGIN = "http://115.127.7.157:8070/AAPI/login.php";    // Server user register url
    public static String URL_REGISTER = "http://115.127.7.157:8070/AAPI/register.php";
    public static String URL_CUSTMER_LIST = "http://115.127.7.157:8070/AAPI/get_customer_list.php";
    public static String URL_CUSTMER_INFO = "http://115.127.7.157:8070/AAPI/get_customer_info.php?party_code=";
    public static String URL_PRODUCT_LIST = "http://115.127.7.157:8070/AAPI/get_productlist.php";
    public static String URL_PRODUCT_ORDER = "http://115.127.7.157:8070/AAPI/send_order.php";
  //  prod_code, final String emp_code, final String org_id, final String party_code
    public static String URL_SET_ORDER = "http://115.127.7.157:8070/AAPI/store_final_order.php?emp_code="+emp_code+"&org_id="+org_id+"&party_code="+party_code+"";
    public static String URL_REVEIW_PRODUCT = "http://115.127.7.157:8070/AAPI/get_reveiw_products.php";
    public static String URL_UPDATE_ORDER = "http://115.127.7.157:8070/AAPI/update_order.php";
    public static String URL_SHIP_LOC= "http://115.127.7.157:8070/AAPI/get_ship_loc.php";
    public static String URL_BILL_LOC= "http://115.127.7.157:8070/AAPI/get_bill_loc.php";

    public static String URL_VIEW_ORDERS= "http://115.127.7.157:8070/AAPI/view_orderlist.php";
    public static String URL_VIEW_ORDER_DETAILS= "http://115.127.7.157:8070/AAPI/view_orderdetails.php";
   // public static String URL_PRODUCT_ORDER = "http://192.168.100.10/AAPI/send_order.php";
    //http://10.0.2.2:8080/HelloServlet/PDRS?param1="+lat+"&param2="+lon

   // Patterns.WEB_URL.matcher(URL_LOGIN).matches()
    //Random number
    Random r = new Random();
    public int RandNum = (r.nextInt(80) + 65);

}
