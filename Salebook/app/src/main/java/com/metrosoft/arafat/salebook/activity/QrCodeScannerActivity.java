package com.metrosoft.arafat.salebook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static com.metrosoft.arafat.salebook.activity.LoginActivity.gpslat;
import static com.metrosoft.arafat.salebook.activity.LoginActivity.gpslong;

import static com.metrosoft.arafat.salebook.activity.MainActivity.scan_result;
import static com.metrosoft.arafat.salebook.activity.MainActivity.text;
//import static com.metrosoft.arafat.salebook.activity.MainActivity.ShowPartyInfo;
public class QrCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static String ID,name,phone,email,org_id,party_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
        Intent intent = getIntent();
        if(intent.hasExtra("ID")){

            ID = intent.getStringExtra("ID");
            name = intent.getStringExtra("name");
            phone = intent.getStringExtra("phone");
            email = intent.getStringExtra("email");
            org_id = intent.getStringExtra("org_id");
            Log.e("QR Get inetnt: ", "Party Code: "+party_code+"  ID: " + ID + " Name: " + name + " Phone: " + phone + " Email: " + email + "ORG_ID:" + org_id + ">>" + gpslat + "-" + gpslong);
        }
    }

    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QrCodeScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera(camId);
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
        mScannerView = null;
    }

    @Override
    public void handleResult(Result rawResult) {

        final String result = rawResult.getText();
        Log.e("QRCodeScanner", rawResult.getText());
        Log.e("QRCodeScanner", rawResult.getBarcodeFormat().toString());

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Scan Result");

                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                        startActivity(browserIntent);*/
                       // startActivity(new Intent(QrCodeScannerActivity.this, MainActivity.class));
                        text.setText(result.toString().trim());
                        Log.e("QR Set inetnt: ", "Party Code: "+result.toString().trim()+"  ID: " + ID + " Name: " + name + " Phone: " + phone + " Email: " + email + "ORG_ID:" + org_id + ">>" + gpslat + "-" + gpslong);
                        Intent in = new Intent(QrCodeScannerActivity.this,MainActivity.class);

//Add your data to bundle
                        scan_result="true";
                        in.putExtra("RETURN","true");
                        in.putExtra("party_code", result.toString().trim());
                        in.putExtra("ID",ID);
                        in.putExtra("uname",name);
                        in.putExtra("uphone",phone);
                        in.putExtra("uemail",email);
                        in.putExtra("org_id",org_id);
                        //ShowPartyInfo(result);

                        startActivity(in);
                        finish();



                    }
                });
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
            }
        });
                builder.setMessage(rawResult.getText());
                AlertDialog alert1 = builder.create();
                alert1.show();
    }
}