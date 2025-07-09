package com.tajim.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tajim.notes.adapters.RecyclerAdapterMain;
import com.tajim.notes.databinding.ActivityMainBinding;
import com.tajim.notes.notes.AddNoteActivity;
import com.tajim.notes.others.SqliteHelper;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SqliteHelper sqliteHelper;
    RecyclerAdapterMain recyclerAdapterMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sqliteHelper = new SqliteHelper(this);
        recyclerAdapterMain = new RecyclerAdapterMain(sqliteHelper);

        binding.addNotes.setOnClickListener(v->{startActivity(new Intent(MainActivity.this, AddNoteActivity.class));});

        binding.recyclerMain.setAdapter(recyclerAdapterMain);
        binding.recyclerMain.setLayoutManager(new LinearLayoutManager(this));

    }




    @Override
    protected void onPostResume() {
        super.onPostResume();
        recyclerAdapterMain.refreshData();

    }
}