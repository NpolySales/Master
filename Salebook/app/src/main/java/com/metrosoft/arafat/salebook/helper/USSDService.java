package com.metrosoft.arafat.salebook.helper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by hp on 2/15/2018.
 */

public class USSDService extends AccessibilityService {

    public static String TAG = "XXXX";
    public static String USDNUMBER="";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");
        String text = event.getText().toString();
       // USDNUMBER =text.toString().trim().replaceAll("\\D+","");

        if (event.getClassName().equals("android.app.AlertDialog")) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            Log.e(TAG, text);
            Intent intent = new Intent("com.times.ussd.action.REFRESH");
            intent.putExtra("ussd", text);
            // write a broad cast receiver and call sendbroadcast() from here, if you want to parse the message for balance, date

        }
       // String text = event.getText().toString();
        USDNUMBER =text.toString().trim().replaceAll("\\D+","");
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

}

