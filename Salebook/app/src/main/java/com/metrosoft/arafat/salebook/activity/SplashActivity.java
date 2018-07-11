package com.metrosoft.arafat.salebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.metrosoft.arafat.rxpad.R;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static com.metrosoft.arafat.salebook.app.AppConfig.URL_LOGIN;

public class SplashActivity extends AppCompatActivity {
    ImageView img;
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    String host;
    int timeout;
    int port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.activity_splash);
        img =(ImageView)findViewById(R.id.anim_pipe);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        host=""+URL_LOGIN;

        timeout=1000;
        LoadAnim();
      /*  if(!isReachableByTcp(host,port,timeout)){
            Toast.makeText(getApplicationContext(),"Server IPs are OK",Toast.LENGTH_LONG).show();
        }*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }



    public static boolean isReachableByTcp(String host, int port, int timeout) {
        try {

            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IP error",""+e.toString());
            return false;
        }
    }

    private void LoadAnim() {
try {

    YoYo.with(Techniques.Pulse).duration(900).repeat(4).playOn(findViewById(R.id.anim_pipe));
    //YoYo.with(Techniques.Flash).duration(900).repeat(4).playOn(findViewById(R.id.txt_loading));
}
catch (Exception e){
    e.printStackTrace();
    Log.e("Animation","Loading Animation Failed ");
}


    }
    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
