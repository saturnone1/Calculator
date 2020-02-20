package com.taewon.cal.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.taewon.cal.db.model.DataInfo;
import com.taewon.cal.db.model.ListDB;
import com.taewon.cal.ui.OnDataListener;

import java.util.ArrayList;

public class DBControl {

    private SQLiteDatabase db;

    Handler handler = new Handler();

    public DBControl(SQLiteDatabase data) {
        this.db = data;
    }

    //데이터를 집어넣는 메소드
    public void selectData(final SQLiteDatabase db, final OnDataListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<DataInfo> dataList;
                dataList = selectData(db);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDataChange(dataList);
                    }
                });
            }
        }).start();
    }

    private ArrayList<DataInfo> selectData(SQLiteDatabase db){

        ArrayList<DataInfo> dataList = new ArrayList<DataInfo>();

        // y,n 구분해서 불러오는것 쿼리문으로 옮기기!
        String selection = ListDB.ListEntry.DELETE_YN + " = ?";
        String[] selectionArgs = {"n"};
        Cursor cursor = db.query(ListDB.ListEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            //string으로 변환해서 저장

            String wTime = cursor.getString(cursor.getColumnIndex(ListDB.ListEntry.CREATE_TM));
            String wExp = cursor.getString(cursor.getColumnIndex(ListDB.ListEntry.EXP));
            String wRes = cursor.getString(cursor.getColumnIndex(ListDB.ListEntry.RESULT));
            String wId = cursor.getString(cursor.getColumnIndex(ListDB.ListEntry._ID));

            DataInfo mixing = new DataInfo();
            mixing.setDay(wTime);
            mixing.setSusik(wExp);
            mixing.setResult(wRes);
            mixing.setId(wId);
            dataList.add(mixing);
        }
        cursor.close();

        return dataList;
    }

    public void insertColumn(final String delete, final String time, final String expression, final String result) {
        new Thread(new Runnable() {
            public void run() {
                ContentValues values = new ContentValues();
                values.put(ListDB.ListEntry.DELETE_YN, delete);
                values.put(ListDB.ListEntry.CREATE_TM, time);
                values.put(ListDB.ListEntry.EXP, expression);
                values.put(ListDB.ListEntry.RESULT, result);
                // a potentially time consuming task
                db.insert(ListDB.ListEntry.TABLE_NAME, null, values);
            }
        }).start();

        return;
    }

    private void modifyUpdate(final ArrayList<DataInfo> seldel) {
        String toY = "y";
        ContentValues values = new ContentValues();
        values.put(ListDB.ListEntry.DELETE_YN, toY);
        String selection = ListDB.ListEntry._ID + " LIKE ?";

        int i = 0;
        while (i < seldel.size()) {
            if (seldel.get(i).isDel()) {
                String[] selectionArgs = {seldel.get(i).getId()};
                db.update(ListDB.ListEntry.TABLE_NAME, values, selection, selectionArgs);
            }
            i++;
        }
    }

    public void modifyUpdateSelect (final SQLiteDatabase db, final ArrayList<DataInfo> delData, final OnDataListener dataListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                modifyUpdate(delData);
                final ArrayList<DataInfo> selectDataList = selectData(db);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dataListener.onDataChange(selectDataList);
                    }
                });
            }
        }).start();
    }
}
