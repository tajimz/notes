package com.tajim.notes.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.tajim.notes.databinding.ActivityLoginBinding;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvSignup.setOnClickListener(v->{startActivity(new Intent(LoginActivity.this, SignupActivity.class));});

        handleLogin();



    }

    private void handleLogin(){
        binding.btnSubmit.setOnClickListener(v->{
            String email = binding.edEmail.getText().toString().trim();
            String password = binding.edPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()){
                alert("Input Required", "Please fill in all the required fields.");
                return;
            }
            JSONObject jsonObject = jsonObjMaker(
                    CONSTANTS.EMAIL ,  email,
                    CONSTANTS.PASSWORD, password
            );

            reqJsonObj(CONSTANTS.URL + "authentication/login.php", jsonObject, new jsonObjCallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    Toast.makeText(LoginActivity.this, "done", Toast.LENGTH_SHORT).show();
                }
            });


        });
    }
}