package com.taewon.cal;

public class DataInfo {

    private String id;
    private String day;
    private String susik;
    private String result;
    private boolean isDel = false;

    //private 변수에 대한 getter/ setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSusik() {
        return susik;
    }

    public void setSusik(String susik) {
        this.susik = susik;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        this.isDel = del;
    }
}
