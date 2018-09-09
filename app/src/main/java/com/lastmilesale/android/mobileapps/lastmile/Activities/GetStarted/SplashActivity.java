package com.lastmilesale.android.mobileapps.lastmile.Activities.GetStarted;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.lastmilesale.android.mobileapps.lastmile.Activities.Main.MainActivity;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.onesignal.OneSignal;

public class SplashActivity extends Activity {

    private SharedPreferences appFile;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appFile = getSharedPreferences(getResources().getString(R.string.app_file),0);
        user_id = appFile.getString(getResources().getString(R.string.active_user_id),"none");

        //Creating transparent status bar for Android versions with API >= 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        //redirecting to WelcomeScreen
                        if(user_id.equals("none")) {
                            Intent i = new Intent(SplashActivity.this, PitchSliderActivity.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            i.putExtra("user_id",user_id);
                            startActivity(i);
                        }
                    }
                }
            };
            timerThread.start();

    }
}
