package com.codercamp.smartclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import codercamp.smartclass.R;

public class AdminActivity extends AppCompatActivity {

   private EditText passwordEdit;
   private Button Login;
    private Toolbar toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        passwordEdit = findViewById(R.id.passwod);
        Login = findViewById(R.id.LoginBtn);
        toolbar1 = findViewById(R.id.classtoolBar);
        toolbar1.setTitle("Admin");
        setSupportActionBar(toolbar1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordEdit.getText().toString().isEmpty()){
                    passwordEdit.setError("Please Enter Password");
                    passwordEdit.requestFocus();
                }
                else if (passwordEdit.getText().toString().equals("admin")){
                    startActivity(new Intent(AdminActivity.this,BookingActivity.class));
                }
                else {
                    Toast.makeText(AdminActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
}