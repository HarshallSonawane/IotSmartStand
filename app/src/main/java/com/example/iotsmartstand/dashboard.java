package com.example.iotsmartstand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class dashboard extends AppCompatActivity  {

    private long timeCountInMilliSeconds = 1 * 60000;
    private int count=0;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private EditText tag;
    FirebaseFirestore db;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;
    EditText message;
    private SharedPreferences preferences;
    private boolean isAppVisible = true;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home: {
                break;
            }
            case R.id.todo:{
                Intent i=new Intent(getApplicationContext(), com.example.iotsmartstand.ToDo.class);
                startActivity(i);
                break;
            }
            case R.id.timeline:{
                Intent i=new Intent(getApplicationContext(), com.example.iotsmartstand.History.class);
                startActivity(i);
                break;
            }
            case R.id.pomodoro:{
                Intent i=new Intent(getApplicationContext(), com.example.iotsmartstand.Pomodoro.class);
                startActivity(i);
                break;
            }
            case R.id.appTimeline:{
                Intent i=new Intent(getApplicationContext(), com.example.iotsmartstand.Timeline.class);
                startActivity(i);
                break;
            }
            case R.id.about:{
                Intent i=new Intent(getApplicationContext(),About.class);
                startActivity(i);
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = FirebaseFirestore.getInstance();
        initViews();
        initListeners();
        isAppVisible = true;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        toolbar=findViewById(R.id.Toolbar);
        drawerLayout=findViewById(R.id.drawerLayout);
        tag=findViewById(R.id.enter);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }
    private void initViews() {
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        editTextMinute = (EditText) findViewById(R.id.editTextMinute);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageViewReset = (ImageView) findViewById(R.id.imageViewReset);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);
    }


    private void initListeners() {
        imageViewReset.setOnClickListener(this::onClick);
        imageViewStartStop.setOnClickListener(this::onClick);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
                break;
        }
    }


    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }



    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // making edit text not editable
            if (editTextMinute.getText().toString().isEmpty()) {
                editTextMinute.setEnabled(true);
                editTextMinute.setVisibility(View.VISIBLE);
            }
            else{
                editTextMinute.setEnabled(false);
                tag.setEnabled(false);
                editTextMinute.setVisibility(View.GONE);

            }

            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {

            // hiding the reset icon
            imageViewReset.setVisibility(View.GONE);
            editTextMinute.setVisibility(View.VISIBLE);
            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.drawable.icon_start);
            // making edit text editable
            editTextMinute.setEnabled(true);
            tag.setEnabled(true);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();


        }

    }

    private void setTimerValues() {
        int time = 0;
        if (!editTextMinute.getText().toString().isEmpty()) {
            // fetching value from edit text and type cast to integer
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            // toast message to fill edit text
            Toast.makeText(getApplicationContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
        }
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }


    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                count++;
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the reset icon
                imageViewReset.setVisibility(View.GONE);
                // changing stop icon to start icon
                imageViewStartStop.setImageResource(R.drawable.icon_start);
                // making edit text editable
                editTextMinute.setEnabled(true);
                editTextMinute.setVisibility(View.VISIBLE);
                tag.setEnabled(true);
                tag.setVisibility(View.VISIBLE);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
                if(timerStatus== TimerStatus.STOPPED && count==1){
                    String time = editTextMinute.getText().toString();
                    String tags = tag.getText().toString();
                    Date date = new Date();
                    Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                    //dt.format(newDate);
                    // SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
                    String stringdate =   SimpleDateFormat.getDateInstance().format(new Date());
                    Map<String, Object> timeline = new HashMap<>();
                    timeline.put("time", time);
                    timeline.put("tag", tags);
                    timeline.put("date",stringdate);
                    db.collection("timeline")
                            .add(timeline)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(dashboard.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {

                                    Toast.makeText(dashboard.this, "Failed", Toast.LENGTH_SHORT).show();


                                }
                            });
                }

                }



        }.start();
        countDownTimer.start();
        count=0;

    }


    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }



    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }




}