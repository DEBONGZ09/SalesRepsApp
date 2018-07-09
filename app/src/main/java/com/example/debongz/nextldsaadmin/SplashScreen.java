package com.example.debongz.nextldsaadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    String val = shared.getString("pass", "");
                    if (val.length() == 0) {

                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {

                        SharedPreferences mshared = getSharedPreferences("SetupPref", Context.MODE_PRIVATE);
                        String loaded = mshared.getString("loaded", "");
                        if (loaded.length() == 0) {

                            Intent intent = new Intent(SplashScreen.this, SaveDataBase.class);
                            startActivity(intent);
                            finish();


                        }
                        else {

                            Intent intent = new Intent(SplashScreen.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };

        thread.start();
    }


}
