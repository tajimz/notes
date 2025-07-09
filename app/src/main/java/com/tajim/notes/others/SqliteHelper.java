package com.tajim.notes.others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tajim.notes.utils.CONSTANTS;


public class SqliteHelper extends SQLiteOpenHelper {
    public SqliteHelper(Context context) {
        super(context, CONSTANTS.DBNAME, null, CONSTANTS.DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+CONSTANTS.NOTESTABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT, noteTitle TEXT, noteBody TEXT, noteDate TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists "+CONSTANTS.NOTESTABLE);
        onCreate(db);
    }

    public void insertData(String noteTitle, String noteBody, String noteDate){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("noteTitle", noteTitle);
        contentValues.put("noteBody", noteBody);
        contentValues.put("noteDate", noteDate);

        sqLiteDatabase.insert(CONSTANTS.NOTESTABLE, null, contentValues);


    }

    public Cursor getData (){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM "+CONSTANTS.NOTESTABLE+ " ORDER BY id DESC", null);

    }
    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONSTANTS.NOTESTABLE, null, null);
    }



}
