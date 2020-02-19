package com.taewon.cal;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecHolder> {

    private ArrayList<DataInfo> mDataSet;
    private boolean pop = false;

    public ArrayList<DataInfo> getmDataSet() {
        return mDataSet;
    }

    public RecAdapter(ArrayList<DataInfo> data){
        this.mDataSet = data;
    }

    public void setSelMod(boolean sel){
        this.pop = sel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holderlayout, parent, false);
       RecHolder tHolder = new RecHolder(v);
       return tHolder;
    }

    //홀더 객체의 특징을 만들고
    @Override
    public void onBindViewHolder(@NonNull RecHolder holder, final int position) {
        holder.calender.setText(mDataSet.get(position).getDay());
        holder.ggulgua.setText(mDataSet.get(position).getResult());
        holder.susik.setText(mDataSet.get(position).getSusik());

        holder.checkBox.setOnCheckedChangeListener(null);

        //선택삭제를 눌렀는지
        if(pop == true){
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else{
            holder.checkBox.setVisibility(View.GONE);
        }

        //체크여부를 확인
        if(mDataSet.get(position).isDel() == true){
            holder.checkBox.setChecked(true);
        }
        else{
            holder.checkBox.setChecked(false);
        }

        //체크박스에 리스너 구현해서 클릭시 이벤트
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                    mDataSet.get(position).setDel(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    //홀더의 뼈대를 만들고
    public class RecHolder extends RecyclerView.ViewHolder {
        TextView ggulgua;
        TextView calender;
        TextView susik;
        CheckBox checkBox;

        public RecHolder(View v){
            super(v);
            ggulgua = (TextView) v.findViewById(R.id.ggulgua);
            calender = (TextView) v.findViewById(R.id.calender);
            susik = (TextView) v.findViewById(R.id.susik);
            checkBox = (CheckBox) v.findViewById(R.id.checkbox);
        }
    }
}


