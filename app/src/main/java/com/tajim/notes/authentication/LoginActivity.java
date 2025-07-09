package com.tajim.notes.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.tajim.notes.MainActivity;
import com.tajim.notes.databinding.ActivityLoginBinding;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;
import org.json.JSONException;
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
        handleForgotPass();



    }

    private void handleLogin(){
        binding.btnSubmit.setOnClickListener(v->{
            String email = binding.edEmail.getText().toString().trim();
            String password = binding.edPassword.getText().toString().trim();

            if (!isValidInput(email, password)) return;

            JSONObject jsonObject = jsonObjMaker(
                    CONSTANTS.EMAIL ,  email,
                    CONSTANTS.PASSWORD, password
            );

            reqJsonObj(CONSTANTS.URL + CONSTANTS.LOGIN_URL, jsonObject, new jsonObjCallBack() {
                @Override
                public void onSuccess(JSONObject result) {

                    try {
                        String status = result.getString("status");
                        if (status.equals("success")) loginSuccess(email, password);
                        else alert("Alert", status,  ()->{});

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }





                }
            });


        });
    }

    private boolean isValidInput(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            alert("Input Required", "Please fill in all the required fields.", ()->{});
            return false;
        }
        return true;
    }

    protected void handleForgotPass(){
        binding.tvForgot.setOnClickListener(v->{
            alert("Forgot Password?",
                    "As you know, the password cannot be reset directly. Please visit the website below and submit your email. If your email is correct, you will receive your password hint within 24 hours.", () -> {startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://trtajim.xyz/apps/notes/authentication/forgot.html")));});
        });
    }
    private void loginSuccess(String email, String password){
        editSharedPref(CONSTANTS.EMAIL, email);
        editSharedPref(CONSTANTS.PASSWORD, password);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}