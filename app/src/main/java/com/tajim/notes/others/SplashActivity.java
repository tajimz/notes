package com.tajim.notes.others;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

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
        EdgeToEdge.enable(this);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sqliteHelper = new SqliteHelper(this);
        endSplash(1000);



    }

    private void endSplash(int delayTimeInMS){
        if (getSharedPref(CONSTANTS.EMAIL) != null ) {
            binding.tvdetails.setVisibility(VISIBLE);
            binding.tvdetails.setText("Signing in as "+getSharedPref(CONSTANTS.EMAIL)+" ...");

            syncData(()->{});
        }
        checkVersion(()->{
            delayTime(delayTimeInMS, () -> {
                if (getSharedPref(CONSTANTS.EMAIL) != null) {

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            });
        });

    }
}