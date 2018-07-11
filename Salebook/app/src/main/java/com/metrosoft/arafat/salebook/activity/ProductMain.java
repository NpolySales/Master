package com.metrosoft.arafat.salebook.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.metrosoft.arafat.rxpad.R;
import com.metrosoft.arafat.salebook.products.NFALProdCat;
import com.metrosoft.arafat.salebook.products.NPILProdCat;


public class ProductMain extends AppCompatActivity {
    private  static  String parentcat;
    public static String party_code,org_id,emp_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_main);

        FrameLayout co=(FrameLayout)findViewById(R.id.fragmentContainer);
        AnimationDrawable animationDrawable = (AnimationDrawable) co.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        Intent intent = getIntent();
        View v = new View(getApplicationContext());
        if (intent.hasExtra("org_id")) {

            emp_code = intent.getStringExtra("emp_code").replaceAll("[^0-9]", "");
            org_id = intent.getStringExtra("org_id");
            party_code= intent.getStringExtra("party_code");
            parentcat = intent.getStringExtra("parent_cat");
        }
        Log.e("Category for: ","ID: "+emp_code+"Orgid :"+org_id+" PARTYCODE :"+party_code+"\tParentCat\t"+parentcat);
        if (fragment == null) {
            if (!org_id.isEmpty()&& org_id.equals("102")) { // NFAL user
                fragment = new NFALProdCat();
            }
            else if(!org_id.isEmpty()&& org_id.equals("101")) { //NPIL user
                fragment = new NPILProdCat();
               // Snackbar.make(v, "Not Ready For NPIL !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            else { // If user not found
                Snackbar.make(v, "No Matched Item Found !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            ;

            fm.beginTransaction().add(R.id.
                    fragmentContainer, fragment).commit();
        }
    }
}
