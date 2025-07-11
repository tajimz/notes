package com.tajim.notes.utils;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tajim.notes.others.SqliteHelper;

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
                endLoading();
                jsonCallBack.onSuccess(jsonObject);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("volleyError", volleyError.toString());
                alert("Internet Connection Error", "Maybe you're offline or using a weak connection", ()->{endLoading();});


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
                endLoading();
                jsonArrayCallBack.onSuccess(jsonArray);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("volleyError", volleyError.toString());
                alert("Internet Connection Error", "Maybe you're offline or using a weak connection", ()->{endLoading();});
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

    protected static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    protected void syncData(){
        SqliteHelper sqliteHelper = new SqliteHelper(this);
        JSONObject jsonObject = jsonObjMaker(CONSTANTS.LAST_MODIFIED, getSharedPref(CONSTANTS.LAST_MODIFIED),
                CONSTANTS.EMAIL, getSharedPref(CONSTANTS.EMAIL));
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        jsonArrayReq(CONSTANTS.URL + CONSTANTS.SYNCDATA, jsonArray, new jsonArrayCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.d("jsonArray", result.toString());
                Log.d("lastModified", getSharedPref(CONSTANTS.LAST_MODIFIED));

                new Thread(() -> {
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            JSONObject jsonObject1 = result.getJSONObject(i);
                            String title = jsonObject1.getString(CONSTANTS.DBTITLE);
                            String body = jsonObject1.getString(CONSTANTS.DBBODY);
                            String date = jsonObject1.getString(CONSTANTS.DBDATE);
                            String id = jsonObject1.getString(CONSTANTS.DBID);

                            Cursor cursor = sqliteHelper.getDataById(id);
                            boolean exists = false;
                            if (cursor != null) {
                                exists = cursor.getCount() > 0;
                                cursor.close();
                            }

                            if (exists) {
                                sqliteHelper.updateData(title, body, date, id);
                            } else {
                                sqliteHelper.insertData(title, body, date, id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    runOnUiThread(() -> {
                        editSharedPref(CONSTANTS.LAST_MODIFIED, getDate());
                        Toast.makeText(getApplicationContext(), "data synced", Toast.LENGTH_SHORT).show();
                    });

                }).start();
            }
        });
    }








}
