package me.prateeksaigal.ocotocoffee;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(5000,1000) {

            @Override
            public void onFinish() {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }
}
