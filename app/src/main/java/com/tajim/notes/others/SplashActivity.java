package com.tajim.notes.others;

import android.content.Intent;
import android.os.Bundle;
import com.tajim.notes.MainActivity;
import com.tajim.notes.R;
import com.tajim.notes.authentication.LoginActivity;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

public class SplashActivity extends BaseActivity {
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sqliteHelper = new SqliteHelper(this);
        endSplash(2000);



    }

    private void endSplash(int delayTimeInMS){
        if (getSharedPref(CONSTANTS.EMAIL) != null ) syncData();
        delayTime(delayTimeInMS, () -> {
            if (getSharedPref(CONSTANTS.EMAIL) != null) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        });
    }
}