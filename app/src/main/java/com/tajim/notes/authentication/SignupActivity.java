package com.tajim.notes.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;
import com.tajim.notes.databinding.ActivitySignupBinding;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

import org.json.JSONObject;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvLogin.setOnClickListener(v->{startActivity(new Intent(SignupActivity.this, LoginActivity.class));});

        handleSignUp();
    }

    private void handleSignUp(){
        binding.btnSubmit.setOnClickListener(v->{
            String email = binding.edEmail.getText().toString().trim();
            String password = binding.edPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()){
                alert("Input Required", "Please fill in all the required fields.");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                alert("Invalid Email", "The Email you've entered is invalid, please enter a valid Email");
                return;
            }
            if (!isPassword(password)){
                alert("Invalid Password", CONSTANTS.PASSWORD_CRITERIA);
                return;
            }

            JSONObject jsonObject = jsonObjMaker(
                    CONSTANTS.EMAIL , email,
                    CONSTANTS.PASSWORD, password
            );

            reqJsonObj(CONSTANTS.URL + "authentication/signUp.php", jsonObject, new jsonObjCallBack() {
                @Override
                public void onSuccess(JSONObject result) {

                }
            });




        });
    }
}