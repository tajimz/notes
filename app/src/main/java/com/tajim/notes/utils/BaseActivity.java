package com.tajim.notes.utils;

import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    protected String getSharedPref(String keyword){
        SharedPreferences sharedPreferences = getSharedPreferences(CONSTANTS.SHAREDPREF, MODE_PRIVATE);
        return sharedPreferences.getString(keyword, null);
    }

    protected void editSharedPref(String keyword, String value){
        SharedPreferences sharedPreferences = getSharedPreferences(CONSTANTS.SHAREDPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyword, value);
        editor.apply();
    }
    protected void delayTime (int timeInMS, Runnable toRun){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toRun.run();
            }
        }, timeInMS);
    }


}
