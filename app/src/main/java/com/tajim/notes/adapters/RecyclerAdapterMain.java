package com.tajim.notes.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tajim.notes.utils.BaseActivity.convertDate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajim.notes.MainActivity;
import com.tajim.notes.databinding.ActivityMainBinding;
import com.tajim.notes.databinding.LayoutRecyclerMainBinding;
import com.tajim.notes.notes.AddNoteActivity;
import com.tajim.notes.others.SqliteHelper;
import com.tajim.notes.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapterMain extends RecyclerView.Adapter<RecyclerAdapterMain.ViewHolderMain> {
    SqliteHelper sqliteHelper;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    Context context;
    ActivityMainBinding activityMainBinding;
    public RecyclerAdapterMain(Context context,SqliteHelper sqliteHelper, ActivityMainBinding activityMainBinding){
        this.sqliteHelper = sqliteHelper;
        this.activityMainBinding = activityMainBinding;
        this.context = context;
        arrayList = sqliteHelper.getDataAsArray("");
        if (arrayList.isEmpty()) activityMainBinding.tvEmpty.setVisibility(VISIBLE);
        else activityMainBinding.tvEmpty.setVisibility(GONE);
    }
    public class ViewHolderMain extends RecyclerView.ViewHolder{
        LayoutRecyclerMainBinding binding;
        public ViewHolderMain(LayoutRecyclerMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    @NonNull
    @Override
    public ViewHolderMain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutRecyclerMainBinding binding = LayoutRecyclerMainBinding.inflate(inflater, parent, false);

        return new ViewHolderMain(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMain holder, int position) {

        HashMap<String, String> hashMap = arrayList.get(position);

        String title = hashMap.get(CONSTANTS.TITLE);
        String body =  hashMap.get(CONSTANTS.BODY);
        String date =  hashMap.get(CONSTANTS.DATE);
        String id =  hashMap.get(CONSTANTS.DBID);

        holder.binding.tvTitle.setText(title);
        holder.binding.tvBody.setText(convertDate(date) + " " + body);

        holder.binding.mother.setOnClickListener(v->{
            Intent intent = new Intent(context, AddNoteActivity.class);
            intent.putExtra(CONSTANTS.REASON, CONSTANTS.EDITNOTE_URL);
            intent.putExtra(CONSTANTS.TITLE, title);
            intent.putExtra(CONSTANTS.BODY, body);
            intent.putExtra(CONSTANTS.DATE, date);
            intent.putExtra(CONSTANTS.DBID, id);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void refreshData() {
        arrayList = new ArrayList<>();
        arrayList = sqliteHelper.getDataAsArray("");
        if (arrayList.isEmpty()) activityMainBinding.tvEmpty.setVisibility(VISIBLE);
        else activityMainBinding.tvEmpty.setVisibility(GONE);
        notifyDataSetChanged();

    }

    public void filterData(ArrayList<HashMap<String,String>> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();

    }



}
