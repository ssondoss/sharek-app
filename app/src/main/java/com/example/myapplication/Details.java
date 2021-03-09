package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Locale;

public class Details extends AppCompatActivity {

    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    String id;
    Button solved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();
        id=extras.getString("id");
        getData();

      //  solved = findViewById(R.id.solved);

//        solved.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DocumentReference washingtonRef = db.collection("complaint-and-suggestions").document(id);
//
//                washingtonRef
//                        .update("status", "تم حلها من طرف المستخدم")
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // getData();
//                                Toast.makeText(Details.this,"تم تغير الحالة الى تم حلها ",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Details.this,"حدث خطأ بالشبكة",Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//            }
//        });

    }

    public void getData(){
        DocumentReference docRef = db.collection("complaint-and-suggestions").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String date = document.getData().get("date").toString();
                    String id = document.getData().get("id").toString();
                    String extraNotes = document.getData().get("extraNotes").toString();
                    String locationDescription = document.getData().get("locationDescription").toString();
                    String locationPoints = document.getData().get("locationPoints").toString();
                    String problemDescription = document.getData().get("problemDescription").toString();
                    final String problemImage = document.getData().get("problemImage").toString();
                    String status = document.getData().get("status").toString();
                    String category = document.getData().get("category").toString();
                    String mobilePhone = document.getData().get("mobilePhone").toString();
                    String city = document.getData().get("city").toString();

                   // TextView event_id = findViewById(R.id.event_id);
                    TextView event_date = findViewById(R.id.event_date);
                    TextView event_status = findViewById(R.id.event_status);
                    TextView event_description = findViewById(R.id.event_description);
                    TextView event_location = findViewById(R.id.event_location);
                    TextView user_notes = findViewById(R.id.user_notes);
                    TextView admin_notes = findViewById(R.id.admin_notes);
                    TextView cityTextView = findViewById(R.id.city);
                    TextView mobile = findViewById(R.id.mobile);
                    TextView categoryTextView = findViewById(R.id.category);
                   // event_id.setText(id);
                    event_date.setText(getDate(Long.parseLong(date)));
                    if(status.equals("تم ارسالها"))
                        event_status.setText("تم استلامها");
                    else
                        event_status.setText(status);
                    if(!problemDescription.equals(""))
                        event_description.setText(problemDescription);
                    else
                        event_description.setText("لا يوجد");
                    if(locationDescription.equals("")){
                        event_location.setText("لا يوجد");

                    } else {
                        event_location.setText(locationDescription);
                    }

                    cityTextView.setText(city);
                    if(!mobilePhone.equals(""))
                        mobile.setText(mobilePhone);
                    else
                        mobile.setText("لا يوجد");

                    if(!category.equals(""))
                        categoryTextView.setText(category);
                    else
                        categoryTextView.setText("لا يوجد");
                    if (extraNotes.equals(""))
                        user_notes.setText("لا يوجد");
                    else
                    user_notes.setText(extraNotes);
                    if(document.getData().get("adminNotes").toString().equals(""))
                        admin_notes.setText("لا يوجد");
                    else
                    admin_notes.setText(document.getData().get("adminNotes").toString());
                    System.out.println("problemImage = " + problemImage);
                    if(!problemImage.equals("")) {
                        if (problemImage.contains("images")) {
                            System.out.println(problemImage);
                            StorageReference islandRef = storageRef.child(problemImage);

                            final long ONE_MEGABYTE = 20024 * 20024;
                            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                   // VideoView video = findViewById(R.id.video);
                                   // video.setVisibility(View.INVISIBLE);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                   // ImageView image = findViewById(R.id.problemImage);
                                   // image.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        } else if (problemImage.contains("videos")) {

                           // VideoView video = findViewById(R.id.video);
                          //  ImageView image = findViewById(R.id.problemImage);
                           // image.setVisibility(View.INVISIBLE);
                            MediaController mediaController = new MediaController(Details.this);
                         //   mediaController.setAnchorView(video);
                          //  video.setMinimumHeight(300);
                          //  video.setMinimumWidth(300);
                           // video.setMediaController(mediaController);
                           // video.setVideoPath("https://firebasestorage.googleapis.com/v0/b/sharek-80e70.appspot.com/o/"+problemImage.replace("/","%2F")+"?alt=media&token=4f4fcf03-e48c-4247-975e-96f6e8befcfe");
                          //  video.setZOrderOnTop(true);//this line solve the problem
                          //  video.start();
                        } else {
                        }
                    }else {
                       // VideoView video = findViewById(R.id.video);
                       // video.setVisibility(View.GONE);
                      //  ImageView image = findViewById(R.id.problemImage);
                      //  image.setVisibility(View.GONE);

                    }
                }else{
                }

            }
        });
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();

        return date;
    }


}