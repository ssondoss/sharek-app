package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private TextView registerationText;
    private ImageButton loginButton ;
    private TextView forgetPassword;
    private EditText nationalID;
    private EditText password;
    String nationalIdValue;
    String trimmedNaID;
    UserSession userSession;



    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userSession=new UserSession(getApplicationContext());

        db = FirebaseFirestore.getInstance();
        nationalID = findViewById(R.id.nationalNumLogin);
        loginButton=findViewById(R.id.loginButton);
        password = findViewById(R.id.passwordLogin);

        loginButton = findViewById(R.id.loginButton);
        nationalID.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nationalIdValue =nationalID.getText().toString();
                trimmedNaID=nationalIdValue.trim();

                if(trimmedNaID.length() <10)
                {
                    nationalID.setError("الرقم الوطني يجب ان يكون 10 خانات");
                }
                else{

                //startActivity(new Intent(MainActivity.this, Home.class));
                DocumentReference docRef = db.collection("users").document(nationalID.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String realPassword = document.getData().get("password").toString();
                                String userPassword = password.getText().toString();

                                if(realPassword.equals(userPassword)){
                                    // TODO : get username and phone number from the firebase
                                    userSession.createUserLoginSession("Sondos",nationalID.getText().toString().trim(),"0798022719",realPassword);
                                    startActivity(new Intent(MainActivity.this, Home.class));
                                }else{
                                    Toast.makeText(getApplicationContext(),"خطأ في الرقم الوطني و/أو كلمة السر",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"خطأ في الرقم الوطني و/أو كلمة السر",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"جدث خطأ بالاتصال بالشبكة !",Toast.LENGTH_SHORT).show();
                        }
                    }
                });}

            }
        });
        registerationText =findViewById(R.id.registerationText);
        forgetPassword =findViewById(R.id.forgetPassword);

        String dontHveAccountText="ليس لديك حساب ؟" ;
        String forgetPasswordText= "هل نسيت كلمة السر ؟";

        //dont have account link
        final SpannableString text2= new SpannableString(dontHveAccountText);
        ClickableSpan conditions = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(MainActivity.this, Registeration.class));

            }


            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#000000"));
            }
        };
        text2.setSpan(conditions,0,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerationText.setText(text2);
        registerationText.setHighlightColor(Color.TRANSPARENT);
        registerationText.setMovementMethod(LinkMovementMethod.getInstance());


        //forget password link

        final SpannableString forgetPasswordText2= new SpannableString(forgetPasswordText);
        ClickableSpan forget = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(MainActivity.this, StartingForgetPassword.class));

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
}