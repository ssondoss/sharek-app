package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class StartingForgetPassword extends AppCompatActivity {
    private LinearLayout goToVerficationCode ;
    private LinearLayout cancel;
    private EditText nationalIDEditText;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mobilePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_forget_password);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        nationalIDEditText = findViewById(R.id.nationalNum);
cancel=findViewById(R.id.cancelChangePasswordButton);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Intent i =new Intent(StartingForgetPassword.this, VerficationCode.class);
                i.putExtra("mobile",s);
                i.putExtra("nationalID",nationalIDEditText.getText().toString().trim());
                startActivity(i);

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                nationalIDEditText.setText("");
                Toast.makeText(getApplicationContext(),"الرجاء المحاولة مرة ثانية !",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                Toast.makeText(getApplicationContext(),"YES 2",Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(StartingForgetPassword.this, VerficationCode.class));
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };


        goToVerficationCode= findViewById(R.id.stratingChangePasswordButton);
cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});
        goToVerficationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(nationalIDEditText.getText())){
                    DocumentReference docRef = db.collection("users").document(nationalIDEditText.getText().toString().trim());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    mobilePhone = document.getData().get("mobile").toString();

                                    PhoneAuthOptions options =
                                            PhoneAuthOptions.newBuilder(mAuth)
                                                    .setPhoneNumber(mobilePhone)       // Phone number to verify
                                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                    .setActivity(StartingForgetPassword.this)                 // Activity (for callback binding)
                                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                    .build();
                                    PhoneAuthProvider.verifyPhoneNumber(options);
                                } else {
                                    Toast.makeText(getApplicationContext(),"الرقم الوطني غير مسجل !",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"جدث خطأ بالاتصال بالشبكة !",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    nationalIDEditText.setError("هذا الحقل مطلوب");
                }
            }
        });
    }
}