package com.tajim.notes.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajim.notes.databinding.LayoutRecyclerMainBinding;
import com.tajim.notes.others.SqliteHelper;

public class RecyclerAdapterMain extends RecyclerView.Adapter<RecyclerAdapterMain.ViewHolderMain> {
    SqliteHelper sqliteHelper;
    Cursor cursor;
    public RecyclerAdapterMain(SqliteHelper sqliteHelper){
        this.sqliteHelper = sqliteHelper;
        cursor = sqliteHelper.getData();
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
        cursor.moveToPosition(position);

        String title = cursor.getString(1);
        String body = cursor.getString(2);
        String date = cursor.getString(3);
        String id = cursor.getString(4);

        holder.binding.tvTitle.setText(title);
        holder.binding.tvBody.setText(date + " " + body);


    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void refreshData() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = sqliteHelper.getData();
        notifyDataSetChanged();
    }


}
