package com.taewon.cal.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taewon.cal.R;
import com.taewon.cal.db.model.DataInfo;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecHolder> {

    private ArrayList<DataInfo> mDataSet;
    private boolean mIsDelMode = false;

    public ArrayList<DataInfo> getDataSet() {
        return mDataSet;
    }

    public RecAdapter(ArrayList<DataInfo> data){
        this.mDataSet = data;
    }

    public void setDelMode(boolean sel){
        this.mIsDelMode = sel;
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

        //선택삭제를 눌렀는지
        if (mIsDelMode == true) {
            holder.checkBox.setVisibility(View.VISIBLE);

            holder.checkBox.setOnCheckedChangeListener(null);

            //체크여부를 확인
            holder.checkBox.setChecked(mDataSet.get(position).isDel());

            //체크박스에 리스너 구현해서 클릭시 이벤트
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mDataSet.get(position).setDel(isChecked);
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
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


