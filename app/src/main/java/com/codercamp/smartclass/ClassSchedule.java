package com.codercamp.smartclass;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import codercamp.smartclass.R;

public class ClassSchedule extends AppCompatActivity {
    Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        toolbar1 = findViewById(R.id.classtoolBar);
        toolbar1.setTitle("Class Schedule");
        setSupportActionBar(toolbar1);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}