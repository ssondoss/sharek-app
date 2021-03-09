package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class send extends AppCompatActivity {
    private static final int MAX_STEP=3;
    private int CURRENT_STEP=3;
    private ImageView send;
    private EditText notes;
    private EditText phoneNumForComplain;

    String problemDescription;
    String problemImage;
    String locationDescription;
    String locationPoints;
    String extraNotes;
    String city;
    Spinner category;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_send);
        bottomProgressDots(CURRENT_STEP);
        send = findViewById(R.id.imageButton2);
        notes = findViewById(R.id.additionalNotes);
        phoneNumForComplain = findViewById(R.id.phoneNumForComplain);

        category=findViewById(R.id.complainCategories);
        ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(send.this, R.layout.spinner_custom ,getResources().getStringArray(R.array.categories));
        myAdapter.setDropDownViewResource(R.layout.spinner_custom);
        category.setAdapter(myAdapter);
        Bundle extras = getIntent().getExtras();
        db = FirebaseFirestore.getInstance();

        problemDescription = extras.getString("problemDescription");
        problemImage = extras.getString("problemImage");
        locationDescription = extras.getString("locationDescription");
        locationPoints = extras.getString("locationPoints");
        city = extras.getString("city");

        ((LinearLayout) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backStep(CURRENT_STEP);
                bottomProgressDots(CURRENT_STEP);
                onBackPressed();
                //startActivity(new Intent(send.this, location.class));

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                extraNotes = notes.getText().toString().trim();
                String id = UUID.randomUUID().toString();
                Map<String, Object> complaintOrSuggestion = new HashMap<>();
                complaintOrSuggestion.put("problemDescription",problemDescription);
                complaintOrSuggestion.put("problemImage",problemImage);
                complaintOrSuggestion.put("locationDescription",locationDescription);
                complaintOrSuggestion.put("locationPoints",locationPoints);
                complaintOrSuggestion.put("extraNotes",extraNotes);
                complaintOrSuggestion.put("date", System.currentTimeMillis());
                complaintOrSuggestion.put("id", id);
                complaintOrSuggestion.put("city",city);
                complaintOrSuggestion.put("phoneNumForComplain",phoneNumForComplain.getText().toString());
                complaintOrSuggestion.put("category",category.getSelectedItem().toString());
                complaintOrSuggestion.put("status","تم ارسالها");
                complaintOrSuggestion.put("viewed",false);
                complaintOrSuggestion.put("adminNotes","");
                HashMap<String, String> userDetails = new UserSession(getApplicationContext()).getUserDetails();
                complaintOrSuggestion.put("nationalID",userDetails.getOrDefault(UserSession.KEY_NATIONAL_ID,null));
                complaintOrSuggestion.put("username",userDetails.getOrDefault(UserSession.KEY_NAME,null));
                complaintOrSuggestion.put("mobilePhone",userDetails.getOrDefault(UserSession.KEY_MOBILE_PHONE,null));

                db.collection("complaint-and-suggestions").document(id)
                        .set(complaintOrSuggestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"تم تسجيل الشكوى بنجاح",Toast.LENGTH_LONG).show();

                                final DocumentReference countRef = db.collection("count").document("counters");

                                countRef.get().addOnCompleteListener(
                                        new OnCompleteListener<DocumentSnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                 if (task.isSuccessful()) {
                                                     DocumentSnapshot document = task.getResult();
                                                     if (document.exists()) {
                                                         int total = ((Long)(document.getData().get("total"))).intValue();
                                                         countRef
                                                                 .update("total", total+1)
                                                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                     @Override
                                                                     public void onSuccess(Void aVoid) {
                                                                     }
                                                                 })
                                                                 .addOnFailureListener(new OnFailureListener() {
                                                                     @Override
                                                                     public void onFailure(@NonNull Exception e) {
                                                                     }
                                                                 });
                                                     } else {
                                                     }
                                                 } else {
                                                 }

                                             }
                                         });



                                startActivity(new Intent(send.this,Home.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"حدث خطأ",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
    private void backStep(int progress) {
        if(progress>1){
            progress--;
            CURRENT_STEP=progress;
        }
    }
    private void nextStep(int progress) {
        if(progress <MAX_STEP){
            progress++;
            CURRENT_STEP=progress;
        }
    }

    private void bottomProgressDots(int current_index){
        current_index--;
        LinearLayout dotsLayout =(LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots= new ImageView[MAX_STEP];
        dotsLayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i]=new ImageView(this);
            int width_height=20;
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(new ViewGroup.LayoutParams( width_height , width_height));
            params.setMargins(10,10,10,10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shap_cricle);
            dots[i].setColorFilter( getResources().getColor(R.color.colorAccent) , PorterDuff.Mode.SRC_IN );
            dotsLayout.addView(dots[i]);

        }
        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shap_cricle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary) , PorterDuff.Mode.SRC_IN);
        }
    }


}