package com.tajim.notes.utils;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    protected void delayTime (int timeInMS, Runnable toRun){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toRun.run();
            }
        }, timeInMS);
    }


}
