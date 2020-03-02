package com.appsmata.qtoa.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

import com.appsmata.qtoa.R;

public class AppStart extends AppCompatActivity {
    private TextView mytext;
    private ImageView myimage;

    private long ms=0, splashTime=5000;
    private boolean splashActive = true, paused=false;
    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();
        mytext = findViewById(R.id.text);
        myimage = findViewById(R.id.image);

        setFirstUse();

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        myimage.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade1);
        mytext.startAnimation(animation2);

        Thread mythread = new Thread() {
            public void run() {
            try {
                while (splashActive && ms < splashTime) {
                    if(!paused)
                        ms=ms+100;
                    sleep(100);
                }
            } catch(Exception e) {}
            finally {
                checkSession();
            }
            }
        };
        mythread.start();
    }

    public void checkSession() {
        if (prefget.getBoolean("app_user_loggedin", false))
            startActivity(new Intent(AppStart.this, HomeView.class));
        else startActivity(new Intent(AppStart.this, UserLogin.class));
        finish();
    }

    public void setFirstUse() {
        if (!prefget.getBoolean("app_first_use", false)) {
            prefedit.putBoolean("app_first_use", true);
            prefedit.putBoolean("app_user_loggedin", false);
            prefedit.putLong("app_first_data", System.currentTimeMillis());
            prefedit.commit();
        }

    }

}