package com.tajim.notes.others;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import com.tajim.notes.MainActivity;
import com.tajim.notes.R;
import com.tajim.notes.authentication.LoginActivity;
import com.tajim.notes.databinding.ActivityLoginBinding;
import com.tajim.notes.databinding.ActivitySplashBinding;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

public class SplashActivity extends BaseActivity {
    SqliteHelper sqliteHelper;
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sqliteHelper = new SqliteHelper(this);
        endSplash(2000);



    }

    private void endSplash(int delayTimeInMS){
        if (getSharedPref(CONSTANTS.EMAIL) != null ) {
            binding.tvdetails.setVisibility(VISIBLE);
            binding.tvdetails.setText("Signing in as "+getSharedPref(CONSTANTS.EMAIL)+" ...");

            syncData(()->{});
        }
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