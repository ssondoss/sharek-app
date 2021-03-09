package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerficationCode extends AppCompatActivity {
    private ImageButton verfictaionCodeButton;
    private String mobilePhone;
    private EditText verficationCodeEditText;
    private FirebaseAuth mAuth;
    private String nationalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication_code);
        verfictaionCodeButton = findViewById(R.id.verficationCode);
        verficationCodeEditText = findViewById(R.id.verfication_code);
        Bundle extras = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();


        mobilePhone = extras.getString("mobile");
        nationalID=extras.getString("nationalID");

        verfictaionCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("here");

                System.out.println("mobilePhone = " + mobilePhone);

                String code = verficationCodeEditText.getText().toString().trim();

                if(code.length()!=0 && mobilePhone!=null){
                    System.out.println("YEEESS");
                    System.out.println("code = " + code);
                    System.out.println("mobilePhone = " + mobilePhone);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mobilePhone, code);
                    signInWithPhoneAuthCredential(credential);
                   // startActivity(new Intent(VerficationCode.this , NewPassword.class));
                }else{
                    System.out.println("NOOO");
                    System.out.println("code = " + code);
                    System.out.println("mobilePhone = " + mobilePhone);
                    Toast.makeText(VerficationCode.this,"الرجاء ادخال الرمز المرسل ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {



        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(VerficationCode.this,NewPassword.class).putExtra("nationalID",nationalID));

                            FirebaseAuth.getInstance().signOut();


                        } else {
                            Toast.makeText(VerficationCode.this,"  الرمز خطأ ! ", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }

                });

    }

}