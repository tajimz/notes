package com.tajim.notes.notes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.tajim.notes.MainActivity;
import com.tajim.notes.databinding.ActivityAddNoteBinding;
import com.tajim.notes.others.SqliteHelper;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;
import org.json.JSONException;
import org.json.JSONObject;

public class AddNoteActivity extends BaseActivity {
    ActivityAddNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvDate.setText(getDate());
        checkReason();


    }

    private void checkReason(){
        String reason = getIntent().getStringExtra(CONSTANTS.REASON);
        if (CONSTANTS.ADDNOTE_URL.equals(reason)){
            handleSubmit();
        }else if(CONSTANTS.EDITNOTE_URL.equals(reason)){

            handleEdit();

        }
    }
    private void handleSubmit(){
        binding.btnSubmit.setOnClickListener(v->{

            String title = binding.edTitle.getText().toString().trim();
            String body = binding.edBody.getText().toString().trim();
            if (!isInputValid(title, body)) return;
            String email = getSharedPref(CONSTANTS.EMAIL);

            createNote(title, body, email, getDate());


        });
    }

    private void handleEdit(){
        String title = getIntent().getStringExtra(CONSTANTS.TITLE);
        String body = getIntent().getStringExtra(CONSTANTS.BODY);
        String date = getIntent().getStringExtra(CONSTANTS.DATE);
        String id = getIntent().getStringExtra(CONSTANTS.DBID);

        binding.tvDate.setText("Last Modified: "+date);
        binding.edTitle.setText(title);
        binding.edBody.setText(body);
        binding.btnSubmit.setText("Edit Note");

        binding.btnSubmit.setOnClickListener(v->{
            String editedTitle = binding.edTitle.getText().toString().trim();
            String editedBody = binding.edBody.getText().toString().trim();
            if (!isInputValid(editedTitle, editedBody)) return;
            String email = getSharedPref(CONSTANTS.EMAIL);

            editNote(editedTitle, editedBody, email, getDate(), id);

        });
    }

    private boolean isInputValid(String title, String body){

        if (title.isEmpty() || body.isEmpty()){
            alert("Invalid Input", "Please fill all fields", ()->{});
            return false;
        }
        return true;
    }

    private void createNote(String title, String body, String email, String date){
        JSONObject jsonObject = jsonObjMaker(
                CONSTANTS.TITLE, title,
                CONSTANTS.BODY, body,
                CONSTANTS.EMAIL, email,
                CONSTANTS.DATE, date
        );

        reqJsonObj(CONSTANTS.URL + CONSTANTS.ADDNOTE_URL, jsonObject, new jsonObjCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    String status = result.getString("status");

                    if (status.equals("success")) finishNoteOperation(title, body, date ,result.getString("noteId"),true);
                    else alert("Notice", status, ()->{});
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }




    private void editNote(String title, String body, String email, String date,String id){
        JSONObject jsonObject = jsonObjMaker(
                CONSTANTS.TITLE, title,
                CONSTANTS.BODY, body,
                CONSTANTS.EMAIL, email,
                CONSTANTS.DATE, date,
                CONSTANTS.DBID, id
        );

        reqJsonObj(CONSTANTS.URL + CONSTANTS.EDITNOTE_URL, jsonObject, new jsonObjCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    String status = result.getString("status");

                    if (status.equals("success")) finishNoteOperation(title, body, date ,id,false);
                    else alert("Notice", status, ()->{});
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }



    private void finishNoteOperation(String title, String body, String date, String noteId, Boolean created){

        SqliteHelper sqliteHelper = new SqliteHelper(this);
        if (created) sqliteHelper.insertData(title, body, date, noteId);
        else sqliteHelper.updateData(title, body, date, noteId);

        startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
        finish();
        Toast.makeText(this, "Operation successful", Toast.LENGTH_SHORT).show();

    }

}