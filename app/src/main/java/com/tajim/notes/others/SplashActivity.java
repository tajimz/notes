package com.tajim.notes.others;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tajim.notes.MainActivity;
import com.tajim.notes.R;
import com.tajim.notes.authentication.LoginActivity;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends BaseActivity {
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sqliteHelper = new SqliteHelper(this);
        endSplash(4000);



    }

    private void endSplash(int delayTimeInMS){
        delayTime(delayTimeInMS, ()->{
            if (getSharedPref(CONSTANTS.EMAIL) != null){
                syncData();

            }else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }


        });
    }

    private void syncData(){
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

                    // ✅ Finalize on UI thread
                    runOnUiThread(() -> {
                        editSharedPref(CONSTANTS.LAST_MODIFIED, getDate());
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    });

                }).start();
            }
        });
    }

}