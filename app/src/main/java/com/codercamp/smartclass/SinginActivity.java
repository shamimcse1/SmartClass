package com.codercamp.smartclass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import codercamp.smartclass.R;

public class SinginActivity extends AppCompatActivity {
    public EditText email, password;
    public CircularProgressButton SingInButton;
    public TextView SingUpText;
    public String UserEmail, UserPassword;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public FirebaseFirestore db;
    public DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        initView();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            String userId = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            startActivity(new Intent(SinginActivity.this, MainActivity.class));
            finish();
        }
    }
    private void initView() {

        email = findViewById(R.id.UserEmailLogIn);
        password = findViewById(R.id.UserPasswordLogIn);
        SingInButton = findViewById(R.id.SingInBtn);
        SingUpText = findViewById(R.id.SingUPText);
        
        SingUpText.setOnClickListener(v -> {
            startActivity(new Intent(SinginActivity.this, SingupActivity.class));
            Animatoo.animateZoom(SinginActivity.this);
            finish();
        });

        SingInButton.setOnClickListener(v -> {
            // Toast.makeText(LogInActivity.this, "Sing In Successfully", Toast.LENGTH_SHORT).show();

            UserEmail = email.getText().toString();
            UserPassword = password.getText().toString();

            if (UserEmail.isEmpty()) {
                email.setError("Please Enter Email");
                email.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()) {
                email.setError("Please Enter Valid Email");
                email.requestFocus();
            } else if (UserPassword.isEmpty()) {
                password.setError("Please Enter Password");
                password.requestFocus();
            } else if (UserPassword.length() < 8) {
                password.setError("Password Length must be greater then 8 Character");
                password.requestFocus();
            } else {
                SingIn();
            }

        });
    }

    private void SingIn() {

        SingInButton.startAnimation();

        auth.signInWithEmailAndPassword(UserEmail, UserPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SingInButton.stopAnimation();
                Toast.makeText(SinginActivity.this, "Sing In Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SinginActivity.this, MainActivity.class));
                Animatoo.animateZoom(SinginActivity.this);
                finish();
            } else {
                SingInButton.stopAnimation();
                Toast.makeText(SinginActivity.this, "Sing In Failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(e -> {
            SingInButton.stopAnimation();
            Toast.makeText(SinginActivity.this, "Sing In Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SingInButton.dispose();
    }

}