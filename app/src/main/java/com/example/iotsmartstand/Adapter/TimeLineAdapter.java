package com.example.iotsmartstand.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotsmartstand.Model.TimeLineModel;
import com.example.iotsmartstand.R;
import com.example.iotsmartstand.Timeline;

import java.util.ArrayList;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.MyViewHolder>{
    private Timeline timeline;
    private ArrayList<TimeLineModel> historyList;
    //private FirebaseFirestore firestore;
    public TimeLineAdapter(Timeline historyclass, ArrayList<TimeLineModel> historyList) {
        this.historyList = historyList;
        timeline = historyclass;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(timeline).inflate(R.layout.each_timeline,parent,false);
        // firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(v);
    }
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TimeLineModel historyModel= historyList.get(position);
        holder.date.setText("Date: "+ historyModel.getDate());
        holder.time.setText("Tag: "+ String.valueOf(historyModel.getTag()));
        holder.amount.setText("Time Concentrated: "+(String.valueOf(historyModel.getTime()))+" minutes");
//        float amount=(float) TimeLineModel.getTime();
//        if (amount>60 ){
//            float amountNew=(float) TimeLineModel.getTime()/60;
//            String str= String.format( "%.2f",amountNew);
//            holder.amount.setText("Time Concentrated: "+ str+ " minutes");
//        }
//        else{
//            String str=Float.toString(amount);
//            holder.amount.setText("Time Concentrated: "+ str+ " seconds");
//        }
//     float amount=historyModel.getAmount()/60;
//     holder.amount.setText("Time Concentrated: "+ amountNew+ " minutes");
    }
    //    private boolean toBoolean(int status){
//        return status != 0;
//    }
    @Override
    public int getItemCount() {
        return historyList.size();
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,time,amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            amount=itemView.findViewById(R.id.totaltime);
        }
    }
}
