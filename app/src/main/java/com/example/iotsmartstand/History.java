package com.example.iotsmartstand;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotsmartstand.Adapter.HistoryAdapter;
import com.example.iotsmartstand.Model.HistoryModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<HistoryModel> historyList;
    private HistoryAdapter historyAdapter;
    private FirebaseFirestore db;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data.");
        recyclerView = findViewById(R.id.recycerlview);
        db = FirebaseFirestore.getInstance();
        progressDialog.show();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(History.this));
        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(historyAdapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
         db.collection("History").orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!= null){
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        historyList.add(dc.getDocument().toObject(HistoryModel.class));
                        historyAdapter.notifyDataSetChanged();
                    }
                    historyAdapter.notifyDataSetChanged();
                }
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }
}