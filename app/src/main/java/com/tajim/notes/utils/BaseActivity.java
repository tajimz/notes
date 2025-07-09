package com.tajim.notes.utils;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        new Handler().postDelayed(toRun, timeInMS);
    }

    protected void reqJsonObj(String url, JSONObject jsonObject, final jsonObjCallBack jsonCallBack){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        startLoading();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                jsonCallBack.onSuccess(jsonObject);
                endLoading();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                endLoading();
                Log.e("volleyError", volleyError.toString());
                alert("Internet Connection Error", "Maybe you're offline or using a weak connection", ()->{});


            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    protected void jsonArrayReq(String url, JSONArray jsonArray, final jsonArrayCallBack jsonArrayCallBack){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        startLoading();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                jsonArrayCallBack.onSuccess(jsonArray);
                endLoading();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                endLoading();
                Log.e("volleyError", volleyError.toString());
                alert("Internet Connection Error", "Maybe you're offline or using a weak connection", ()->{});
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    AlertDialog loadingAlert;

    protected void startLoading() {
        ProgressBar progressBar = new ProgressBar(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(progressBar)
                .setCancelable(false)
                .create();

        dialog.show();

        dialog.getWindow().setLayout(175, 175);

        loadingAlert = dialog;
    }

    protected void endLoading(){
        if (loadingAlert != null && loadingAlert.isShowing()){
            loadingAlert.dismiss();
        }
    }


    protected void alert(String title, String body, Runnable runnable) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(body)
                .setNegativeButton("Okay", (dialog, which) -> {runnable.run();})
                .show();
    }


    public interface jsonObjCallBack {
        void onSuccess(JSONObject result);

    }

    public interface jsonArrayCallBack {
        void onSuccess(JSONArray result);

    }

    protected JSONObject jsonObjMaker(String... keyValuePairs) {
        JSONObject jsonObject = new JSONObject();

        if (keyValuePairs.length % 2 != 0) {
            return null;
        }

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i + 1];
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                Log.e("BaseActivity", "JSON build error: key=" + key, e);
                return null;
            }
        }

        return jsonObject;
    }

    protected boolean isPassword(String password){
        return password.length() >= 8 && password.length() <= 16 ;
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy , HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }







}
