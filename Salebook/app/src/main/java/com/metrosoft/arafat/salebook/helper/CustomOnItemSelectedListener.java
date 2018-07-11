package com.metrosoft.arafat.salebook.helper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by hp on 2/5/2018.
 */
//NOT USING
public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
public static String list_of_payment;
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {
        list_of_payment =parent.getItemAtPosition(pos).toString().trim();
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}