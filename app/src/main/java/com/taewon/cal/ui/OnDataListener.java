package com.taewon.cal.ui;

import com.taewon.cal.db.model.DataInfo;

import java.util.ArrayList;

public interface OnDataListener {
    public abstract void onDataChange(ArrayList<DataInfo> dataList);
}