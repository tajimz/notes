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

        db.execSQL("CREATE TABLE "+CONSTANTS.NOTESTABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT, noteTitle TEXT, noteBody TEXT, noteDate TEXT, noteId TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists "+CONSTANTS.NOTESTABLE);
        onCreate(db);
    }

    public void insertData(String noteTitle, String noteBody, String noteDate, String noteId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CONSTANTS.DBTITLE, noteTitle);
        contentValues.put(CONSTANTS.DBBODY, noteBody);
        contentValues.put(CONSTANTS.DBDATE, noteDate);
        contentValues.put(CONSTANTS.DBID, noteId);

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

    public void updateData(String noteTitle, String noteBody, String noteDate, String noteId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CONSTANTS.DBTITLE, noteTitle);
        contentValues.put(CONSTANTS.DBBODY, noteBody);
        contentValues.put(CONSTANTS.DBDATE, noteDate);

        // update table where noteId matches
        sqLiteDatabase.update(CONSTANTS.NOTESTABLE, contentValues, "id = '" + noteId + "'", null);
    }




}
