package com.metrosoft.arafat.salebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.metrosoft.arafat.rxpad.R;

import java.io.InputStream;

public class ReportActivity extends AppCompatActivity {
    public static WebView repWeb;
    public static String EMP,FRMDT,TODT,REPFOR,ORG;
    private String SALEREPID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        repWeb =(WebView)findViewById(R.id.rep_webview);
        Intent intent = getIntent();
        if (intent.hasExtra("emp_code")) {
        EMP = intent.getStringExtra("emp_code").replaceAll("[^0-9]", "");
        FRMDT=intent.getStringExtra("FrmDt");
        ORG=intent.getStringExtra("org_id");
        TODT=intent.getStringExtra("TomDt");
        REPFOR=intent.getStringExtra("report_for");
        SALEREPID=intent.getStringExtra("salesrep_id");
        }

        Log.e("Report Params","Employee\t"+EMP+"-"+SALEREPID+" ORG\t"+ORG+" FROMdt\t"+FRMDT+" TOdt\t"+TODT);
        LoadURL(REPFOR);

        // Add a WebViewClient
    /*   repWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                // Inject CSS when page is done loading
                injectCSS();
                super.onPageFinished(view, url);
            }
        });*/

    }

    private void injectCSS() {
        try {
            Log.e("CSS","Injecting CSS");
            InputStream inputStream = getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
         //   "javascript:document.getElementsByTagName('html')              [0].innerHTML+='<style>*{color:#fff}</style>';"
            repWeb.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('c40')[0].style.display='none'; "+
                    //"document.getElementsByClassName('woocommerce_category_listings_box a')[0].style.width='100%'; "+
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CSS error","Injecting CSS failed"+e.toString());
        }

    }

    private void LoadURL(String repfor) {
        try{
            Log.e("URL","Loading URL");
            WebSettings webSettings = repWeb.getSettings();
            webSettings.setJavaScriptEnabled(true);
           repWeb.setWebViewClient(new WebViewClient() {


                @Override
                public void onPageFinished(WebView view, String url) {
                    //so-panel widget widget_woocommerce_category panel-first-child panel-last-child
                    repWeb.loadUrl("javascript:(function() { " +
                            "document.getElementById('xdo:parameters').style.display='none'; " +
                            "document.getElementsByTagName('table')[0].style.width='100%'; " +
                            "document.getElementsByClassName('c40')[0].style.width='100%'; "+
                            //"document.getElementsByClassName('woocommerce_category_listings_box a')[0].style.width='100%'; "+
                            "})()");

                }
            });
        //    http://192.168.100.104:9502/xmlpserver/Sales+Report/DO+VS+COLLECTION+REPORT/General+Report/DO+VS+COLLECTION+STATUS+NEW+REPORT.xdo?_xpt=2&_xmode=0&_paramsP_ORG_ID_div_input=102&_paramsP_DATE_FR=01-01-2018&_paramsP_DATE_TO=01-06-2018&_xt=Simple&_xf=html
            if(!repfor.isEmpty()&&repfor.equals("do_vs_col_report")) {
                repWeb.loadUrl("http://192.168.100.104:9502/xmlpserver/Sales+Report/DO+VS+COLLECTION+REPORT/General+Report/DO+VS+COLLECTION+STATUS+NEW+REPORT.xdo?_xpt=0&_xmode=3&_paramsP_SP_ID="+SALEREPID+"&_paramsP_ORG_ID="+ORG+"&_paramsP_DATE_FR="+FRMDT+"&_paramsP_DATE_TO="+TODT+"&_xt=Simple&_xf=html");
                Log.e("URL","http://192.168.100.104:9502/xmlpserver/Sales+Report/DO+VS+COLLECTION+REPORT/General+Report/DO+VS+COLLECTION+STATUS+NEW+REPORT.xdo?_xpt=0&_xmode=3&_paramsP_ORG_ID_div_input="+ORG+"&_paramsP_DATE_FR="+FRMDT+"&_paramsP_DATE_TO="+TODT+"&_xt=Simple&_xf=html");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("URL ERROR",""+e.toString());
        }

    }
}
