package com.example.bolasepak.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bolasepak.db.model.DataHome;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bolaSepak";
    private static final String TABLE_DATAHOME = "data_home";
    private static final String KEY_DATAHOME_DATA = "data";
    private static final String KEY_DATAHOME_TYPE = "type";
    private static final String KEY_ID = "id";
    private static final String CREATE_TABLE_DATAHOME =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DATAHOME + " (" + KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_DATAHOME_TYPE + " TEXT NOT NULL, " +KEY_DATAHOME_DATA + " TEXT NOT NULL" + ")";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DATAHOME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATAHOME);

        onCreate(db);
    }

    public void recreateDataHome(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATAHOME);

        onCreate(db);
    }

    //get size
    public int getDataHomeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DATAHOME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    //create or insert
    public long createDataHome(DataHome dataHome) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATAHOME_DATA, dataHome.getData());
        values.put(KEY_DATAHOME_TYPE, dataHome.getType());

        long dataHome_id = db.insert(TABLE_DATAHOME, null, values);

        return dataHome_id;
    }

    //read or select
    public DataHome getDataHome(String datahome_type) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_DATAHOME + " WHERE "
                + KEY_DATAHOME_TYPE + " = " + "'" + datahome_type +"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c != null) {
            c.moveToFirst();
        }

        DataHome dh = new DataHome();
        dh.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        dh.setData(c.getString(c.getColumnIndex(KEY_DATAHOME_DATA)));
        dh.setType(c.getString(c.getColumnIndex(KEY_DATAHOME_TYPE)));

        return dh;
    }

    //update
    public int updateDataHome(DataHome  dataHome) {
        SQLiteDatabase db  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATAHOME_DATA, dataHome.getData());
        values.put(KEY_DATAHOME_TYPE, dataHome.getType());

        return db.update(TABLE_DATAHOME, values, KEY_ID + " = ?",
                new String[] {String.valueOf(dataHome.getId())});
    }

    //delete
    public void deleteDataHome(long datahome_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DATAHOME, KEY_ID + " = ?",
                new String[] {String.valueOf(datahome_id)});
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()) {
            db.close();
        }
    }

}