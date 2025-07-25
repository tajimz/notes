package com.tajim.notes.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.tajim.notes.MainActivity;
import com.tajim.notes.R;
import com.tajim.notes.databinding.ActivityAddNoteBinding;
import com.tajim.notes.others.SqliteHelper;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;
import com.tajim.notes.utils.NoteExporter;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNoteActivity extends BaseActivity {
    ActivityAddNoteBinding binding;
    private NoteExporter noteExporter;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        noteExporter = new NoteExporter(this);
        sqliteHelper = new SqliteHelper(this);

        binding.tvDate.setText(convertDate(getDate()));
        checkReason();
        setupPopup();


    }

    private void checkReason(){
        String reason = getIntent().getStringExtra(CONSTANTS.REASON);
        if (CONSTANTS.ADDNOTE_URL.equals(reason)){
            handleSubmit();
            binding.imageMore.setVisibility(View.INVISIBLE);
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

        binding.tvDate.setText("Last Modified: "+convertDate(date));
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
        editSharedPref(CONSTANTS.LAST_MODIFIED, date);

        startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
        finish();
        Toast.makeText(this, "Operation successful", Toast.LENGTH_SHORT).show();

    }
    private void setupPopup(){
        binding.imageMore.setOnClickListener(v->{

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_addnote, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {



            int itemId = item.getItemId();
            if (itemId == R.id.export_pdf){

                noteExporter.export(binding.edBody.getText().toString().trim(), "pdf", binding.edTitle.getText().toString().trim());

                return true;
            }else if (itemId == R.id.export_docx){

                noteExporter.export(binding.edBody.getText().toString().trim(), "docx", binding.edTitle.getText().toString().trim());


                return true;
            }else if (itemId == R.id.export_txt){

                noteExporter.export(binding.edBody.getText().toString().trim(), "txt", binding.edTitle.getText().toString().trim());

                return true;
            }else if (itemId == R.id.delete_note){
                deleteNote(getIntent().getStringExtra(CONSTANTS.DBID));

            }
            return false;
        });
        popupMenu.show();

        });

    }

    private void deleteNote(String noteId){
        JSONObject jsonObject = jsonObjMaker(CONSTANTS.DBID, noteId,
                CONSTANTS.DATE, getDate());

        reqJsonObj(CONSTANTS.URL+CONSTANTS.DLTNOTE, jsonObject, new jsonObjCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String status = result.getString("status");
                    if (status.equals("success")) {
                        sqliteHelper.deleteNote(noteId);
                        startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (noteExporter != null) {
            noteExporter.handleActivityResult(requestCode, resultCode, data);
        }
    }


}