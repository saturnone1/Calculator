package com.taewon.cal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ListDB.ListEntry.TABLE_NAME + " (" +
                    ListDB.ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ListDB.ListEntry.DELETE_YN+ " TEXT," +
                    ListDB.ListEntry.CREATE_TM+ " TEXT," +
                    ListDB.ListEntry.EXP+" TEXT," +
                    ListDB.ListEntry.RESULT +" TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ListDB.ListEntry.TABLE_NAME;
    
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Calcutator.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //DB 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    //DB 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //DB 다운그레이드
    public void onDowngrade(SQLiteDatabase db, int oldversion, int newVersion){
        onUpgrade(db,oldversion, newVersion);
    }
}
