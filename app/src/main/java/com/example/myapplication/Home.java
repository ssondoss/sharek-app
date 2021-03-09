package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ImageButton cameraButton;
    private ImageButton callButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();

        new UserSession(getApplicationContext()).checkLogin();
        // navigation drawer code
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar , R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final DocumentReference countRef = db.collection("count").document("counters");

        countRef.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                int total = ((Long)(document.getData().get("total"))).intValue();
                                int solved = ((Long)(document.getData().get("solved"))).intValue();
                                int rejected = ((Long)(document.getData().get("rejected"))).intValue();

                                TextView totalLabel = findViewById(R.id.sent);
                                TextView solvedLabel = findViewById(R.id.solved);
                                TextView rejectedLabel = findViewById(R.id.rejected);

                                totalLabel.setText(total+"");
                                solvedLabel.setText(solved+"");
                                rejectedLabel.setText(rejected+"");

                            } else {
                            }
                        } else {
                        }

                    }
                });


        // home icons
       cameraButton =findViewById(R.id.goToCamera) ;
       cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View view) {
                startActivity(new Intent(Home.this, camera.class));
            }
       });
       callButton =findViewById(R.id.goToCall);
       callButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Home.this, call.class));
           }
       });
}


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(Home.this, Home.class));
                break;
            case R.id.nav_photo:
                startActivity(new Intent(Home.this, camera.class));
                break;
            case R.id.nav_call:
                startActivity(new Intent(Home.this, call.class));
                break;
            case R.id.nav_conditions:
                startActivity(new Intent(Home.this, TermsAndConditions.class));
                break;
            case R.id.nav_changePassword:
                startActivity(new Intent(Home.this, ChangePassword.class));
                break;
            case R.id.nav_logout:
                new UserSession(getApplicationContext()).logoutUser();
                break;


            case R.id.nav_follow:
                startActivity(new Intent(Home.this, Follow.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);}
        else {
        super.onBackPressed();}
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new UserSession(getApplicationContext()).checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new UserSession(getApplicationContext()).checkLogin();
    }
}