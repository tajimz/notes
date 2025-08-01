package com.tajim.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.tajim.notes.adapters.RecyclerAdapterMain;
import com.tajim.notes.databinding.ActivityMainBinding;
import com.tajim.notes.notes.AddNoteActivity;
import com.tajim.notes.others.SplashActivity;
import com.tajim.notes.others.SqliteHelper;
import com.tajim.notes.utils.BaseActivity;
import com.tajim.notes.utils.CONSTANTS;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    SqliteHelper sqliteHelper;
    RecyclerAdapterMain recyclerAdapterMain;
    boolean search = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initVars();
        setupRecycler();
        setupPopup();
        handleSearch();


        binding.addNotes.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra(CONSTANTS.REASON, CONSTANTS.ADDNOTE_URL);
            startActivity(intent);
        });


    }

    private void initVars(){
        sqliteHelper = new SqliteHelper(this);
        recyclerAdapterMain = new RecyclerAdapterMain(MainActivity.this,sqliteHelper, binding);
    }
    private void setupRecycler(){
        binding.recyclerMain.setAdapter(recyclerAdapterMain);
        binding.recyclerMain.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupPopup(){
        binding.imageMore.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.popup_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {

                int id = item.getItemId();
                if (id == R.id.sync){
                    syncData(()->{
                        recyclerAdapterMain.refreshData();
                    });

                    return true;
                }else if (id == R.id.privacyPolicy){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://trtajim.xyz/apps/notes/agreements/privacyPolicy.html")));
                    return true;
                }else if (id == R.id.termsConditions){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://trtajim.xyz/apps/notes/agreements/termsConditions.html")));
                    return true;
                }else if (id == R.id.logOut){
                    logOut();

                }
                return false;

            });
            popupMenu.show();
        });
    }

    private void handleSearch(){

        binding.imageSearch.setOnClickListener(v->{

            openSearch();




        });

        binding.imageClose.setOnClickListener(v->{

            closeSearch();

        });



        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String fullText = s.toString();
                recyclerAdapterMain.filterData(sqliteHelper.getDataAsArray(fullText));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }
    private void closeSearch(){
        search = false;
        binding.tv.setVisibility(View.VISIBLE);
        binding.imageSearch.setVisibility(View.VISIBLE);
        binding.imageMore.setVisibility(View.VISIBLE);
        binding.searchView.setVisibility(View.GONE);
        binding.imageClose.setVisibility(View.INVISIBLE);
        closeKeyboard(binding.searchView);
        binding.searchView.setText("");
        recyclerAdapterMain.filterData(sqliteHelper.getDataAsArray(""));
    }
    private void openSearch(){
        search = true;
        binding.tv.setVisibility(View.INVISIBLE);
        binding.imageSearch.setVisibility(View.GONE);
        binding.imageMore.setVisibility(View.GONE);
        binding.searchView.setVisibility(View.VISIBLE);
        binding.imageClose.setVisibility(View.VISIBLE);
        openKeyboard(binding.searchView);
    }

    private void logOut (){
        alert("Log Out? ", "Are you sure you want to log out? Your data will be securely saved to the cloud. You'll need to log in again to continue. Click OK to proceed.", ()->{

            editSharedPref(CONSTANTS.EMAIL, null);
            sqliteHelper.clearData();
            startActivity(new Intent(this, SplashActivity.class));
        });
    }


    @Override
    public void onBackPressed() {

        if (search){
            closeSearch();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        recyclerAdapterMain.refreshData();

    }


}