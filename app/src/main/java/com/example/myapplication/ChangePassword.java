package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePassword extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TextView forgetPassword;
    private DrawerLayout drawer;
    private ImageView changePasswordButton;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText repatePasswordText;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        oldPasswordEditText = findViewById(R.id.old_password);
        newPasswordEditText = findViewById(R.id.new_password);
        repatePasswordText = findViewById(R.id.repeat_new_password);

        db = FirebaseFirestore.getInstance();
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(oldPasswordEditText.getText())){
                    oldPasswordEditText.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(newPasswordEditText.getText())){
                    newPasswordEditText.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(repatePasswordText.getText())){
                    repatePasswordText.setError("هذا الحقل مطلوب");
                }
                if(oldPasswordEditText.getText().toString().trim().length()>0 && newPasswordEditText.getText().toString().trim().length()>0){

                    if(new UserSession(getApplicationContext()).getPassword().equals(oldPasswordEditText.getText().toString())) {
                        if(newPasswordEditText.getText().toString().equals(repatePasswordText.getText().toString())) {
                            String nationalID = new UserSession(getApplicationContext()).getUserDetails().get(UserSession.KEY_NATIONAL_ID);
                            DocumentReference userRef = db.collection("users").document(nationalID);

                            userRef.update("password", newPasswordEditText.getText().toString().trim())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ChangePassword.this, "تم اعادة ضبط كلمة السر بنجاح", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChangePassword.this, "لم يتم اعادة ضبط كلمة السر ، الرجاء المحاولة مرة ثانية", Toast.LENGTH_LONG).show();

                                        }
                                    });
                        }else{
                            repatePasswordText.setError("كلمتا السر غير متطابقتان");
                        }
                    }else{
                        oldPasswordEditText.setError("هذا الحقل مطلوب");
                    }
                }else
                {
                    newPasswordEditText.setError("هذا الحقل مطلوب");
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar , R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        forgetPassword =findViewById(R.id.forgetPassword);
        String forgetPasswordText= "هل نسيت كلمة السر ؟";

    final SpannableString forgetPasswordText2= new SpannableString(forgetPasswordText);
    ClickableSpan forget = new ClickableSpan() {
        @Override
        public void onClick(@NonNull View view) {
            startActivity(new Intent(ChangePassword.this, StartingForgetPassword.class));

        }


        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#000000"));
        }
    };
        forgetPasswordText2.setSpan(forget,0,19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetPassword.setText(forgetPasswordText2);
        forgetPassword.setHighlightColor(Color.TRANSPARENT);
        forgetPassword.setMovementMethod(LinkMovementMethod.getInstance());
}
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(ChangePassword.this, Home.class));
                break;
            case R.id.nav_photo:
                startActivity(new Intent(ChangePassword.this, camera.class));
                break;
            case R.id.nav_call:
                startActivity(new Intent(ChangePassword.this, call.class));
                break;
            case R.id.nav_conditions:
                startActivity(new Intent(ChangePassword.this, TermsAndConditions.class));
                break;
            case R.id.nav_changePassword:
                startActivity(new Intent(ChangePassword.this, ChangePassword.class));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(ChangePassword.this, MainActivity.class));
                break;


            case R.id.nav_follow:
                startActivity(new Intent(ChangePassword.this, Follow.class));
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