package com.example.myapplication.service;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Home;
import com.example.myapplication.MainActivity;
import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.google.firebase.firestore.FirebaseFirestore.*;

public class FirebaseService {

    FirebaseFirestore db ;

    public FirebaseService() {
        db = getInstance();
    }

}
