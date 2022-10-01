package com.example.iotsmartstand.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotsmartstand.History;
import com.example.iotsmartstand.Model.HistoryModel;
import com.example.iotsmartstand.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    private History history;
    private ArrayList<HistoryModel> historyList;
    //private FirebaseFirestore firestore;
    public HistoryAdapter(History historyclass, ArrayList<HistoryModel> historyList) {
        this.historyList = historyList;
        history = historyclass;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(history).inflate(R.layout.each_history,parent,false);
       // firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     HistoryModel historyModel= historyList.get(position);
     holder.date.setText("Date: "+ historyModel.getDate());
     holder.time.setText("Start Time: "+ String.valueOf(historyModel.getTime()));
     //holder.amount.setText((String.valueOf(historyModel.getAmount())));
     float amount=(float) historyModel.getAmount();
     if (amount>60 ){
         float amountNew=(float) historyModel.getAmount()/60;
         String str= String.format( "%.2f",amountNew);
         holder.amount.setText("Time Concentrated: "+ str+ " minutes");
     }
     else{
         String str=Float.toString(amount);
         holder.amount.setText("Time Concentrated: "+ str+ " seconds");
     }
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
