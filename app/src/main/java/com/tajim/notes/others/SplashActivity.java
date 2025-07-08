package com.tajim.notes.others;

import android.content.Intent;
import android.os.Bundle;
import com.tajim.notes.MainActivity;
import com.tajim.notes.R;
import com.tajim.notes.authentication.LoginActivity;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        endSplash(4000);


    }

    private void endSplash(int delayTimeInMS){
        delayTime(delayTimeInMS, ()->{
            if (getSharedPref(CONSTANTS.EMAIL) != null){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finish();
        });
    }
}