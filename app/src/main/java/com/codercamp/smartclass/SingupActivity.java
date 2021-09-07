package com.codercamp.smartclass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import codercamp.smartclass.R;

public class SingupActivity extends AppCompatActivity {
    public EditText username, email, number, batchNo, password;
    public CircularProgressButton singUpButton;
    public TextView SingInText;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public DatabaseReference reference;
    public String UserName, UserEmail, UserNumber, UserBatchNo, UserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        initView();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
    }

    private void initView() {

        username = findViewById(R.id.UserName);
        number = findViewById(R.id.UserPhone);
        batchNo = findViewById(R.id.UserBatchNo);
        email = findViewById(R.id.UserEmail);
        password = findViewById(R.id.UserPassword);

        singUpButton = findViewById(R.id.SingUpBtn);

        SingInText = findViewById(R.id.SingInText);


        SingInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SingupActivity.this, SinginActivity.class));
                Animatoo.animateZoom(SingupActivity.this);
                finish();
            }
        });

        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UserName = username.getText().toString();
                UserEmail = email.getText().toString();
                UserNumber = number.getText().toString();
                UserPassword = password.getText().toString();
                UserBatchNo = batchNo.getText().toString();

                if (UserName.isEmpty()) {
                    username.setError("Please Enter Name");
                    username.requestFocus();
                } else if (UserEmail.isEmpty()) {
                    email.setError("Please Enter Email");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()) {
                    email.setError("Please Enter Valid Email");
                    email.requestFocus();
                } else if (UserNumber.isEmpty()) {
                    number.setError("Please Enter Number");
                    number.requestFocus();
                } else if (UserBatchNo.isEmpty()) {
                    batchNo.setError("Please Enter Batch No");
                    batchNo.requestFocus();
                } else if (UserPassword.isEmpty()) {
                    password.setError("Please Enter Password");
                    password.requestFocus();
                } else if (UserPassword.length() < 8) {
                    password.setError("Password Length must be greater then 8 Character");
                    password.requestFocus();
                } else {
                    CreateUser();
                }

            }
        });
    }

    private void CreateUser() {

        singUpButton.startAnimation();

        auth.createUserWithEmailAndPassword(UserEmail, UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UpdateUI();
                        } else {
                            singUpButton.stopAnimation();
                            Toast.makeText(SingupActivity.this, "Sing Up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                singUpButton.stopAnimation();
                Toast.makeText(SingupActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void UpdateUI() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate_andTime = sdf.format(new Date());

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", UserName);
        map.put("email", UserEmail);
        map.put("number", UserNumber);
        map.put("batch", UserBatchNo);
        map.put("password", UserPassword);
        map.put("date", currentDate_andTime);

        reference.child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    singUpButton.stopAnimation();
                    Toast.makeText(SingupActivity.this, "Sing Up Successfully", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SingUpActivity.this, "Database Update Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SingupActivity.this, SinginActivity.class));
                    Animatoo.animateZoom(SingupActivity.this);
                    finish();
                } else {
                    singUpButton.stopAnimation();
                    Toast.makeText(SingupActivity.this, "Database Update Failed", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                singUpButton.stopAnimation();
                Toast.makeText(SingupActivity.this, "Database Update Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        singUpButton.dispose();
    }
}