package com.taewon.cal.db.model;

import android.provider.BaseColumns;

public final class ListDB {
    private ListDB(){}

    public static class ListEntry implements BaseColumns{
        public static final String _ID = "id";
        public static final String TABLE_NAME ="Calculator";
        public static final String DELETE_YN = "DeleteIF";
        public static final String CREATE_TM = "CreateTime";
        public static final String EXP = "Expression";
        public static final String RESULT= "Result";
    }
}