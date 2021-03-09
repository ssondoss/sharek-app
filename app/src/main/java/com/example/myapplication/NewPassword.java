package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewPassword extends AppCompatActivity {

    private String nationalID;

    private EditText password;
    private EditText repeatPassword;

    private ImageView setPassword;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        Bundle bundle = getIntent().getExtras();

        db = FirebaseFirestore.getInstance();

        password=findViewById(R.id.password);
        repeatPassword=findViewById(R.id.repeatPassword);

        setPassword=findViewById(R.id.setPassword);

        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().trim().length()>0){

                    if(password.getText().toString().trim().equals(repeatPassword.getText().toString().trim())){

                        DocumentReference userRef = db.collection("users").document(nationalID);

                        userRef
                                .update("password", password.getText().toString().trim())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(NewPassword.this,"تم اعادة ضبط كلمة السر بنجاح",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(NewPassword.this,MainActivity.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NewPassword.this,"لم يتم اعادة ضبط كلمة السر ، الرجاء المحاولة مرة ثانية",Toast.LENGTH_LONG).show();

                                    }
                                });

                    }else{
                       repeatPassword.setError("كلمتا السر غير متطابقتان");

                    }

                }else
                {
                    password.setError("الرجاء ادخل كلمة السر");
                }
            }
        });



        nationalID=bundle.getString("nationalID");


    }
}