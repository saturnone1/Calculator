package com.taewon.cal.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.taewon.cal.db.model.DataInfo;
import com.taewon.cal.db.model.ListDB;

import java.util.ArrayList;

public class DBControl {

    private SQLiteDatabase db;

    public DBControl(SQLiteDatabase data) {
        this.db = data;
    }

    public long insertColumn(String delete, String time, String expression, String result) {
        ContentValues values = new ContentValues();
        values.put(ListDB.ListEntry.DELETE_YN, delete);
        values.put(ListDB.ListEntry.CREATE_TM, time);
        values.put(ListDB.ListEntry.EXP, expression);
        values.put(ListDB.ListEntry.RESULT, result);
        return db.insert(ListDB.ListEntry.TABLE_NAME, null, values);
    }

    public void modifyUpdate(ArrayList<DataInfo> seldel) {

        String toY = "y";
        ContentValues values = new ContentValues();
        values.put(ListDB.ListEntry.DELETE_YN, toY);
        String selection = ListDB.ListEntry._ID + " LIKE ?";

        int i= 0;
        while(i<seldel.size()){
            if(seldel.get(i).isDel()){
                String[] selectionArgs = {seldel.get(i).getId()};
                db.update(ListDB.ListEntry.TABLE_NAME, values, selection, selectionArgs);
                seldel.remove(i);
            }
            i++;
        }
    }
}
