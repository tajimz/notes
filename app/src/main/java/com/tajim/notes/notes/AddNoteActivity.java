package com.tajim.notes.notes;

import android.os.Bundle;

import com.tajim.notes.databinding.ActivityAddNoteBinding;
import com.tajim.notes.utils.BaseActivity;

public class AddNoteActivity extends BaseActivity {
    ActivityAddNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvDate.setText(getDate());
    }
}