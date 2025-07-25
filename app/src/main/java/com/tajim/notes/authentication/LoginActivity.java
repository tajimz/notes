package com.tajim.notes.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.tajim.notes.MainActivity;
import com.tajim.notes.databinding.ActivityLoginBinding;
import com.tajim.notes.others.SqliteHelper;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    SqliteHelper sqliteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sqliteHelper   = new SqliteHelper(this);

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
                        if (status.equals("success")) loadExistingData(email, password);
                        else alert("Alert", status,  ()->{});

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
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

    private void loadExistingData(String email, String password){
        Toast.makeText(this, "Syncing", Toast.LENGTH_SHORT).show();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = jsonObjMaker(CONSTANTS.EMAIL, email);
        jsonArray.put(jsonObject);

        jsonArrayReq(CONSTANTS.URL + CONSTANTS.GET_EXISTING_NOTES, jsonArray, new jsonArrayCallBack() {
            @Override
            public void onSuccess(JSONArray result) {

                for (int i = 0 ; i < result.length(); i++){
                    try {
                        JSONObject jsonObject1 = result.getJSONObject(i);
                        String title = jsonObject1.getString(CONSTANTS.DBTITLE);
                        String body = jsonObject1.getString(CONSTANTS.DBBODY);
                        String date = jsonObject1.getString(CONSTANTS.DBDATE);
                        String id = jsonObject1.getString(CONSTANTS.DBID);
                        String deleted = jsonObject1.getString("noteDeleted");
                        new Thread(() -> {
                            if (!deleted.equals("true")){
                                sqliteHelper.insertData(title, body, date, id);
                            }

                        }).start();
                        
                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                }
                loginSuccess(email, password);
                


            }
        });

    }
    private void loginSuccess(String email, String password){
        editSharedPref(CONSTANTS.EMAIL, email);
        editSharedPref(CONSTANTS.PASSWORD, password);
        editSharedPref(CONSTANTS.LAST_MODIFIED, getDate());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    
 
}