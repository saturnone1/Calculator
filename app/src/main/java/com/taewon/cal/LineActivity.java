package com.taewon.cal;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LineActivity extends Activity {

    private RecyclerView reView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataInfo> datNoDel;

    private RecAdapter seodapter;

    private DBHelper dbHelper;
    private SQLiteDatabase dbW2;
    private Toolbar toolbar;
    private DBControl dBcontrol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclelayout);

        //툴바 선언
        toolbar = findViewById(R.id.list_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 만들기

        //DB읽어오기
        dbHelper = new DBHelper(getBaseContext());
        dbW2 = dbHelper.getWritableDatabase();
        dBcontrol = new DBControl(dbW2);

        //읽어온 리스트를 저장할 데이터세트
        datNoDel = new ArrayList<DataInfo>();

        //데이터 집어넣기
        SelectD(dbW2);

        //데이터 집어넣기 후 어댑터 생성
        seodapter = new RecAdapter(datNoDel);

        reView = findViewById(R.id.recView);
        reView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        reView.setLayoutManager(layoutManager);
        reView.setAdapter(seodapter);
    }

    //액티비티 종료시 dbhelper 종료
    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    //데이터를 집어넣는 메소드
    private void SelectD(SQLiteDatabase db) {

        // y,n 구분해서 불러오는것 쿼리문으로 옮기기!
        String selection = ListDB.ListEntry.DELETE_YN + " = ?";
        String[] selectionArgs = {"n"};
        Cursor cursor = db.query(ListDB.ListEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            String isDel = cursor.getString(cursor.getColumnIndex("DeleteIF"));

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
            datNoDel.add(mixing);
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sel_del:
                seodapter.setSelMod(true);
                return true;
            case R.id.all_del:
                return true;
            case R.id.del_wow:
                dBcontrol.delUpdate(seodapter.getmDataSet());
                seodapter.notifyDataSetChanged();
                break;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
