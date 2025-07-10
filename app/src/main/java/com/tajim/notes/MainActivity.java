package com.tajim.notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tajim.notes.adapters.RecyclerAdapterMain;
import com.tajim.notes.databinding.ActivityMainBinding;
import com.tajim.notes.notes.AddNoteActivity;
import com.tajim.notes.others.SqliteHelper;
import com.tajim.notes.utils.CONSTANTS;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SqliteHelper sqliteHelper;
    RecyclerAdapterMain recyclerAdapterMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initVars();
        setupRecycler();

        binding.addNotes.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra(CONSTANTS.REASON, CONSTANTS.ADDNOTE_URL);
            startActivity(intent);
        });


    }

    private void initVars(){
        sqliteHelper = new SqliteHelper(this);
        recyclerAdapterMain = new RecyclerAdapterMain(MainActivity.this,sqliteHelper);
    }
    private void setupRecycler(){
        binding.recyclerMain.setAdapter(recyclerAdapterMain);
        binding.recyclerMain.setLayoutManager(new LinearLayoutManager(this));
    }







    @Override
    protected void onPostResume() {
        super.onPostResume();
        recyclerAdapterMain.refreshData();

    }
}