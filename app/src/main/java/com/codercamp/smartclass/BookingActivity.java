package com.codercamp.smartclass;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import codercamp.smartclass.R;

public class BookingActivity extends AppCompatActivity {
    public Toolbar toolbar1;
    public EditText roomNo, batchNo;
    public TextView timePicker;
    public Button BookNow;
    public String RoomNo, BatchNo;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public DatabaseReference reference;
    public int mHour, mMinute;
    public String TimeOfBooking;
    public String result, resultEnd;
    public TextView tv_hour;
    public CountDownTimer countDownTimer;
    public FirebaseRemoteConfig myconfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        toolbar1 = findViewById(R.id.classtoolBar);
        toolbar1.setTitle("Booking");
        setSupportActionBar(toolbar1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        myconfiguration = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings confiuratonsettings = new FirebaseRemoteConfigSettings.Builder().build();
        myconfiguration.setConfigSettingsAsync(confiuratonsettings);
        Map<String, Object> defaultvalues = new HashMap<>();
        defaultvalues.put("btn_enable", false);
        myconfiguration.setDefaultsAsync(defaultvalues);

        reference = FirebaseDatabase.getInstance().getReference("BookTime");
        tv_hour = findViewById(R.id.tv_hour);
        roomNo = findViewById(R.id.RoomNo);
        batchNo = findViewById(R.id.BatchNo);
        timePicker = findViewById(R.id.txtTime);
        BookNow = findViewById(R.id.BookNow);


        BookNow.setOnClickListener(v -> {

            RoomNo = roomNo.getText().toString();
            BatchNo = batchNo.getText().toString();

            if (RoomNo.isEmpty()) {
                roomNo.setError("Please Enter Room No");
                roomNo.requestFocus();
            } else if (BatchNo.isEmpty()) {
                batchNo.setError("Please Enter Batch No");
                batchNo.requestFocus();
            } else {
                UpdateUI();
            }
        });



    }

    public void timePicker(View view) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        try {
                            TimeOfBooking = hourOfDay + ":" + minute;
                            result = LocalTime.parse(TimeOfBooking).format(DateTimeFormatter.ofPattern("h:mma"));
                            // timePicker.setText(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    public void timePickerEnd(View view) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        try {
                            TimeOfBooking = hourOfDay + ":" + minute;
                            resultEnd = LocalTime.parse(TimeOfBooking).format(DateTimeFormatter.ofPattern("h:mma"));
                            timePicker.setText(result + "-" + resultEnd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    private void UpdateUI() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("roomNo", RoomNo);
        map.put("batchNo", BatchNo);
        map.put("time", result + "-" + resultEnd);
        String key = reference.push().getKey();
        assert key != null;
        reference.child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    BookNow.setVisibility(View.GONE);
                    Count_Down();
                    Toast.makeText(BookingActivity.this, "Booking Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(BookingActivity.this, "Booking Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void Count_Down() {
        countDownTimer = new CountDownTimer(5400000, 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                tv_hour.setText(String.format("%02d : %02d : %02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)%60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                BookNow.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                BookNow.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}