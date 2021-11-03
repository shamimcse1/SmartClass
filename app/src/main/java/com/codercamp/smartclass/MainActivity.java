package com.codercamp.smartclass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codercamp.smartclass.Adapter.BookShowAdapter;
import com.codercamp.smartclass.model.Model;
import com.codercamp.smartclass.model.UserModel;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import codercamp.smartclass.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar1;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageSlider imageSlider;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private LinearLayout noInternet;
    private List<Model> model;
    private BookShowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar1 = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar1);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar1, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        //This is Nav Header area Start

        View headerView = navigationView.getHeaderView(0);

        TextView headerName = headerView.findViewById(R.id.UserName);
        TextView headerEmail = headerView.findViewById(R.id.UserEmail);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel model = snapshot.getValue(UserModel.class);
                if (model != null) {
                    try {
                        headerName.setText(model.getName());
                        headerEmail.setText(model.getEmail());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


        //This is Nav Header area End

        imageSlider = findViewById(R.id.image_slider);
        getSliderImage();
        getAllData();

    }

    private void getAllData() {

        noInternet = findViewById(R.id.noInternet);
        noInternet.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        model = new ArrayList<>();
        adapter = new BookShowAdapter(model);
        recyclerView.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference("BookTime");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Model list = data.getValue(Model.class);
                    model.add(list);
                    adapter.notifyDataSetChanged();
                    noInternet.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                noInternet.setVisibility(View.GONE);
                Log.d("error", databaseError.getMessage());
            }
        });

    }

    //Get Slider Data form Databases
    private void getSliderImage() {


        reference = FirebaseDatabase.getInstance().getReference("Slider");

        final List<SlideModel> remoteImages = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren())

                    remoteImages.add(new SlideModel(data.child("imageUrl").getValue().toString(), data.child("title").getValue().toString(), ScaleTypes.FIT));
                imageSlider.setImageList(remoteImages, ScaleTypes.FIT);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("error", databaseError.getMessage());
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
        if (menuitem.getItemId() == R.id.navi_android) {
            Intent in = new Intent(MainActivity.this, MainActivity.class);
            startActivity(in);
            Animatoo.animateZoom(MainActivity.this);
        } else if (menuitem.getItemId() == R.id.classSchedule) {
            Intent in = new Intent(MainActivity.this, ClassSchedule.class);
            startActivity(in);
            Animatoo.animateZoom(MainActivity.this);
        } else if (menuitem.getItemId() == R.id.classSchedule) {
            Intent in = new Intent(MainActivity.this, ClassSchedule.class);
            startActivity(in);
            Animatoo.animateZoom(MainActivity.this);
        } else if (menuitem.getItemId() == R.id.booking) {
            Intent in = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(in);
            finish();
            Animatoo.animateZoom(MainActivity.this);
        } else if (menuitem.getItemId() == R.id.Contact) {
            Intent in = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(in);
            Animatoo.animateZoom(MainActivity.this);
        } else if (menuitem.getItemId() == R.id.Setting) {
            Intent in = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(in);
            Animatoo.animateZoom(MainActivity.this);
        } else if (menuitem.getItemId() == R.id.Logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, SinginActivity.class));
            Animatoo.animateZoom(MainActivity.this);
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


}