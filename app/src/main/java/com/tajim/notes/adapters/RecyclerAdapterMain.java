package com.tajim.notes.adapters;

import static com.tajim.notes.utils.BaseActivity.convertDate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajim.notes.MainActivity;
import com.tajim.notes.databinding.LayoutRecyclerMainBinding;
import com.tajim.notes.notes.AddNoteActivity;
import com.tajim.notes.others.SqliteHelper;
import com.tajim.notes.utils.CONSTANTS;

public class RecyclerAdapterMain extends RecyclerView.Adapter<RecyclerAdapterMain.ViewHolderMain> {
    SqliteHelper sqliteHelper;
    Cursor cursor;
    Context context;
    public RecyclerAdapterMain(Context context,SqliteHelper sqliteHelper){
        this.sqliteHelper = sqliteHelper;
        this.context = context;
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
