package com.tajim.notes.others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tajim.notes.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.HashMap;


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



    public ArrayList<HashMap<String,String>> getDataAsArray(String keyword){
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        HashMap<String,String> hashMap;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+CONSTANTS.NOTESTABLE+ " WHERE noteTitle LIKE '%"+keyword+"%'"+" OR noteBody LIKE '%"+keyword+"%'"+" ORDER BY id DESC", null);

        while (cursor.moveToNext()){
            String title = cursor.getString(1);
            String body = cursor.getString(2);
            String date = cursor.getString(3);
            String id = cursor.getString(4);
            hashMap = new HashMap<>();
            hashMap.put(CONSTANTS.TITLE, title);
            hashMap.put(CONSTANTS.BODY, body);
            hashMap.put(CONSTANTS.DATE, date);
            hashMap.put(CONSTANTS.DBID, id);

            arrayList.add(hashMap);

        }
        cursor.close();
        return arrayList;
    }
    public Cursor getDataById (String id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + CONSTANTS.NOTESTABLE + " WHERE noteId LIKE '" + id + "' ORDER BY id DESC", null);


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
        sqLiteDatabase.update(CONSTANTS.NOTESTABLE, contentValues, "noteId = '" + noteId + "'", null);
    }

    public void deleteNote (String noteId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.rawQuery("DELETE FROM "+CONSTANTS.NOTESTABLE+" WHERE noteId LIKE '"+noteId+"'", null);

    }





}
