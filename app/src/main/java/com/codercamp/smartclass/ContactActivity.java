package com.codercamp.smartclass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import codercamp.smartclass.R;

public class ContactActivity extends AppCompatActivity {
    Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        toolbar1 = findViewById(R.id.classtoolBar);
        toolbar1.setTitle("Contact Us");
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

    public void CallUs(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0987654321"));
        startActivity(intent);
    }
}