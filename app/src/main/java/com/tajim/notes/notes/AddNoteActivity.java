package com.tajim.notes.notes;

import android.os.Bundle;

import com.tajim.notes.databinding.ActivityAddNoteBinding;
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
        handleSubmit();

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

                    if (status.equals("success")) addedNote();
                    else alert("Notice", status, ()->{});
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void addedNote(){
        alert("Note Added", "note has added to server", ()->{});
    }

}