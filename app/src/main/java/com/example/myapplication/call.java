package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.model.PhoneBookItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class call extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        // navigation drawer code
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TableLayout table = this.findViewById(R.id.phone_book);

        db = FirebaseFirestore.getInstance();
        db.collection("phone-book")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String lastCategory = "";
                            List<PhoneBookItem> phoneBookItems = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                phoneBookItems.add(new PhoneBookItem(
                                         document.getData().get("name").toString().trim()
                                        ,document.getData().get("phone").toString().trim()
                                        ,document.getData().get("category").toString().trim()));
                            }
                            Collections.sort(phoneBookItems);
                            for (final PhoneBookItem phoneBookItem:phoneBookItems) {
                                if(phoneBookItem.getCategory().equals(lastCategory)) {
                                    TableRow row = (TableRow) LayoutInflater.from(call.this).inflate(R.layout.phone_book_item, null);
                                    TextView phone =  row.findViewById(R.id.title_of_phone);
                                    phone.setText(phoneBookItem.getPhone());
                                    TextView title =  row.findViewById(R.id.phone);
                                    title.setText(phoneBookItem.getName());
                                    phone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse("tel:"+phoneBookItem.getPhone()));
                                            startActivity(intent);
                                        }
                                    });
                                    table.addView(row);
                                }else{
                                    lastCategory = phoneBookItem.getCategory();
                                    TableRow row = (TableRow) LayoutInflater.from(call.this).inflate(R.layout.phone_book_divider, null);
                                    ((TextView) row.findViewById(R.id.category_title)).setText(phoneBookItem.getCategory());
                                    table.addView(row);
                                    row = (TableRow) LayoutInflater.from(call.this).inflate(R.layout.phone_book_item, null);
                                    TextView phone =  row.findViewById(R.id.title_of_phone);
                                    phone.setText(phoneBookItem.getPhone());
                                    TextView title =  row.findViewById(R.id.phone);
                                    title.setText(phoneBookItem.getName());
                                    phone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse("tel:"+phoneBookItem.getPhone()));
                                            startActivity(intent);
                                        }
                                    });
                                    table.addView(row);
                                }
                            }
                        }
                    }

                });

        table.requestLayout();
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
                startActivity(new Intent(call.this, Home.class));
                break;
            case R.id.nav_photo:
                startActivity(new Intent(call.this, camera.class));
                break;
            case R.id.nav_call:
                startActivity(new Intent(call.this, call.class));
                break;
            case R.id.nav_conditions:
                startActivity(new Intent(call.this, TermsAndConditions.class));
                break;
            case R.id.nav_changePassword:
                startActivity(new Intent(call.this, ChangePassword.class));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(call.this, MainActivity.class));
                break;


            case R.id.nav_follow:
                startActivity(new Intent(call.this, Follow.class));
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



}