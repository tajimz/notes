package com.tajim.notes;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.tajim.notes.databinding.ActivityMainBinding;
import com.tajim.notes.notes.AddNoteActivity;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addNotes.setOnClickListener(v->{startActivity(new Intent(MainActivity.this, AddNoteActivity.class));});


    }
}