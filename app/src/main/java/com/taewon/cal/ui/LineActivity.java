package com.taewon.cal.ui;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taewon.cal.db.DBHelper;
import com.taewon.cal.db.model.ListDB;
import com.taewon.cal.R;
import com.taewon.cal.db.DBControl;
import com.taewon.cal.db.model.DataInfo;

import java.util.ArrayList;

public class LineActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RelativeLayout mEmptyView;
    private Toolbar mToolbar;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecAdapter mAdapter;

    private DBHelper mDBHelper;
    private SQLiteDatabase mSqlDb;
    private DBControl mDBcontrol;

    private ArrayList<DataInfo> mDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclelayout);

        //툴바 선언
        mToolbar = findViewById(R.id.list_toolbar);
        mEmptyView = findViewById(R.id.emptylayout);
        mRecyclerView = findViewById(R.id.recView);

        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 만들기

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //DB읽어오기
        mDBHelper = new DBHelper(getBaseContext());
        mSqlDb = mDBHelper.getWritableDatabase();
        mDBcontrol = new DBControl(mSqlDb);

        //데이터 집어넣기
        mDataList = selectData(mSqlDb);

        //데이터 집어넣기 후 어댑터 생성
        mAdapter = new RecAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);

        //기록 있음 없음시 레이아웃 전환
        layoutEmpty();
    }

    //액티비티 종료시 dbhelper 종료
    @Override
    protected void onDestroy() {
        mDBHelper.close();
        super.onDestroy();
    }

    //데이터를 집어넣는 메소드
    private ArrayList<DataInfo> selectData(SQLiteDatabase db) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sel_del:
                mAdapter.setDelMode(true);
                return true;
            case R.id.all_del:
                return true;
            case R.id.del_wow:
                mDBcontrol.modifyUpdate(mAdapter.getDataSet());
                mAdapter.notifyDataSetChanged();
                layoutEmpty();
                break;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void layoutEmpty(){
        if(mDataList.size() == 0){
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        else{
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
