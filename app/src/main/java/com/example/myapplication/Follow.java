package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.model.ComplaintAndSuggestions;
import com.example.myapplication.model.PhoneBookItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Follow extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageButton toDetails ;
    private DrawerLayout drawer;
    private FirebaseFirestore db;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
//        toDetails = findViewById(R.id.view_details);


       table = this.findViewById(R.id.follow_table);

        db = FirebaseFirestore.getInstance();
//        db.collection("complaint-and-suggestions").orderBy("date", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<ComplaintAndSuggestions> complaintAndSuggestions = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if(document.getData().get("nationalID").toString().equals(new UserSession(getApplicationContext()).getUserDetails().get(UserSession.KEY_NATIONAL_ID))){
//                                ComplaintAndSuggestions complaintAndSuggestion = new ComplaintAndSuggestions();
//                                complaintAndSuggestion.setId(document.getData().get("id").toString());
//                                complaintAndSuggestion.setDate(document.getData().get("date").toString());
//                                complaintAndSuggestion.setStatus(document.getData().get("status").toString());
//                                complaintAndSuggestions.add(complaintAndSuggestion);}
//
//                            }
//                           // Collections.sort(complaintAndSuggestions);
//                            for (final ComplaintAndSuggestions complaintAndSuggestion:complaintAndSuggestions) {
//
//                                    TableRow row = (TableRow) LayoutInflater.from(Follow.this).inflate(R.layout.follow_item, null);
//                                    ((TextView) row.findViewById(R.id.status)).setText(complaintAndSuggestion.getStatus());
//                                   // ((TextView) row.findViewById(R.i.id)).setText(complaintAndSuggestion.getId());
//                                    ((TextView) row.findViewById(R.id.date)).setText(complaintAndSuggestion.getDate());
//                                    ImageView toDetails=((ImageView) row.findViewById(R.id.view_details));
//                                        toDetails.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                startActivity(new Intent(Follow.this, Details.class).putExtra("id",complaintAndSuggestion.getId()));
//                                            }
//                                        });
//                                    table.addView(row);
//
//                            }
//                        }
//                    }
//
//                });

        table.requestLayout();
        // getDataAndUpdateTable();


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



        // home icons


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(Follow.this, Home.class));
                break;
            case R.id.nav_photo:
                startActivity(new Intent(Follow.this, camera.class));
                break;
            case R.id.nav_call:
                startActivity(new Intent(Follow.this, call.class));
                break;
            case R.id.nav_conditions:
                startActivity(new Intent(Follow.this, TermsAndConditions.class));
                break;
            case R.id.nav_changePassword:
                startActivity(new Intent(Follow.this, ChangePassword.class));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(Follow.this, MainActivity.class));
                break;


            case R.id.nav_follow:
                startActivity(new Intent(Follow.this, Follow.class));
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

    private void getDataAndUpdateTable(){
        cleanTable(table);
        db.collection("complaint-and-suggestions").orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ComplaintAndSuggestions> complaintAndSuggestions = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("nationalID").toString().equals(new UserSession(getApplicationContext()).getUserDetails().get(UserSession.KEY_NATIONAL_ID))){
                                    ComplaintAndSuggestions complaintAndSuggestion = new ComplaintAndSuggestions();
                                    complaintAndSuggestion.setId(document.getData().get("id").toString());
                                    complaintAndSuggestion.setDate(document.getData().get("date").toString());
                                    complaintAndSuggestion.setStatus(document.getData().get("status").toString());
                                    complaintAndSuggestions.add(complaintAndSuggestion);}

                            }
                            // Collections.sort(complaintAndSuggestions);
                            for (final ComplaintAndSuggestions complaintAndSuggestion:complaintAndSuggestions) {

                                TableRow row = (TableRow) LayoutInflater.from(Follow.this).inflate(R.layout.follow_item, null);
                                ((TextView) row.findViewById(R.id.status)).setText(complaintAndSuggestion.getStatus().equals("تم ارسالها")?"تم استلامها":complaintAndSuggestion.getStatus());
                                // ((TextView) row.findViewById(R.i.id)).setText(complaintAndSuggestion.getId());
                                ((TextView) row.findViewById(R.id.date)).setText(getDate(Long.parseLong(complaintAndSuggestion.getDate())));
                                ImageView toDetails=((ImageView) row.findViewById(R.id.view_details));
                                toDetails.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Follow.this, Details.class).putExtra("id",complaintAndSuggestion.getId()));
                                    }
                                });
                                table.addView(row);

                            }
                        }
                    }

                });
//        db.collection("complaint-and-suggestions")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<ComplaintAndSuggestions> complaintAndSuggestions = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                ComplaintAndSuggestions complaintAndSuggestion = new ComplaintAndSuggestions();
//                                complaintAndSuggestion.setId(document.getData().get("id").toString());
//                                complaintAndSuggestion.setDate(document.getData().get("date").toString());
//                                complaintAndSuggestion.setStatus(document.getData().get("status").toString());
//                                complaintAndSuggestions.add(complaintAndSuggestion);
//
//                            }
////                            Collections.sort(complaintAndSuggestions);
//                            for (ComplaintAndSuggestions complaintAndSuggestion:complaintAndSuggestions) {
//
//                                TableRow row = (TableRow) LayoutInflater.from(Follow.this).inflate(R.layout.follow_item, null);
//                                ((TextView) row.findViewById(R.id.status)).setText(complaintAndSuggestion.getStatus());
//                              //  ((TextView) row.findViewById(R.id.id)).setText(complaintAndSuggestion.getId());
//                                ((TextView) row.findViewById(R.id.date)).setText(complaintAndSuggestion.getDate());
//                                final String complainID = complaintAndSuggestion.getId();
//                                ImageView toDetails=((ImageView) row.findViewById(R.id.view_details));
//                                toDetails.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        startActivity(new Intent(Follow.this, Details.class).putExtra("id",complainID));
//                                    }
//                                });
//                                table.addView(row);
//
//                            }
//                        }
//                    }
//
//                });

        table.requestLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataAndUpdateTable();
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();

        return date;
    }


    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}