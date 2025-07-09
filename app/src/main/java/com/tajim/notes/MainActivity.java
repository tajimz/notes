package com.tajim.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tajim.notes.databinding.ActivityMainBinding;
import com.tajim.notes.notes.AddNoteActivity;
import com.tajim.notes.others.SqliteHelper;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sqliteHelper = new SqliteHelper(this);

        binding.addNotes.setOnClickListener(v->{startActivity(new Intent(MainActivity.this, AddNoteActivity.class));});


    }

    private void loadData(){
        Cursor cursor = sqliteHelper.getData();
        try{
            while (cursor.moveToNext()){
                String title, body, date, id;
                title = cursor.getString(1);
                body = cursor.getString(2);
                date = cursor.getString(3);
                id = cursor.getString(4);
                Log.d("allData", "\n\ntitle: "+ title+"\nbody: " + body + "\ndate: "+date + "\nid: "+id);

            }
        }finally {
            cursor.close();
        }


    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData();
        Toast.makeText(this, "ersume called", Toast.LENGTH_SHORT).show();
    }
}