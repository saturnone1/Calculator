package com.taewon.cal.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taewon.cal.db.DBHelper;
import com.taewon.cal.db.model.ListDB;
import com.taewon.cal.R;
import com.taewon.cal.db.DBControl;
import com.taewon.cal.db.model.DataInfo;

import java.util.ArrayList;

public class LineActivity extends AppCompatActivity {
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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 만들기
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 허용
        getSupportActionBar().setDisplayShowTitleEnabled(true); //타이틀 보여주기

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //DB읽어오기
        mDBHelper = new DBHelper(getBaseContext());
        mSqlDb = mDBHelper.getWritableDatabase();
        mDBcontrol = new DBControl(mSqlDb);

        //데이터 집어넣기
        OnDataListener dataListener;
        dataListener = new OnDataListener() {
            @Override
            public void onDataChange(ArrayList<DataInfo> dataList) {

                mDataList = dataList;

                //데이터 집어넣기 후 어댑터 생성
                mAdapter = new RecAdapter(mDataList);
                mRecyclerView.setAdapter(mAdapter);

                //기록 있음 없음시 레이아웃 전환
                layoutEmpty();
            }
        };

        mDBcontrol.selectData(mSqlDb, dataListener);
    }

    //액티비티 종료시 dbhelper 종료
    @Override
    protected void onDestroy() {
        mDBHelper.close();
        super.onDestroy();
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
                mAdapter.setDelMode();
                return true;
            case R.id.all_del:
                mAdapter.setAllDelMode(mDataList);
                return true;
            case R.id.button_Del:
                delQuestion(); //삭제 결정 확인
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

    //삭제 결정을 확인하는 부분 -> 그 후 삭제 처리까지.
    public void delQuestion() {
        new AlertDialog.Builder(this)
                .setTitle("기록")
                .setMessage("삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //예 선택시 처리 로직
                        OnDataListener dataListener;
                        dataListener = new OnDataListener() {
                            @Override
                            public void onDataChange(ArrayList<DataInfo> dataList) {
                                //Data 삭제 한 상태에서 dataSet 변경을 어댑터에 알려주지 않았었다.그래서 다시 알려주어야 함.

                                mDataList = dataList;
                                mAdapter.setData(mDataList);
                                mAdapter.notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                layoutEmpty();
                            }
                        };

                        mDBcontrol.modifyUpdateSelect(mSqlDb, mAdapter.getDataSet(), dataListener);
                    }
                })

                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                        Toast.makeText(getBaseContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        layoutEmpty();
                    }
                })
                .show();
    }
}

