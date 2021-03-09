package com.example.myapplication;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Registeration extends AppCompatActivity {
    private ImageButton registerationButton ;
    private TextView policiesText;

    private EditText nationalID;
    private EditText nationalIDOfRelative;
    private EditText mobilePhone;
    private EditText password;
    private EditText repatePassword;
    private CheckBox checkBoxPolicies;
    private FirebaseFirestore db;
    boolean mobilePhoneIsUsed=false;

    //TODO : get name from real data base after integration with real database
    String[] _firstName =  new String[] { "Adam", "Alex", "Aaron", "Ben", "Carl", "Dan", "David", "Edward", "Fred", "Frank", "George", "Hal", "Hank", "Ike", "John", "Jack", "Joe", "Larry", "Monte", "Matthew", "Mark", "Nathan", "Otto", "Paul", "Peter", "Roger", "Roger", "Steve", "Thomas", "Tim", "Ty", "Victor", "Walter"};

    String[] _lastName = new String[] { "Anderson", "Ashwoon", "Aikin", "Bateman", "Bongard", "Bowers", "Boyd", "Cannon", "Cast", "Deitz", "Dewalt", "Ebner", "Frick", "Hancock", "Haworth", "Hesch", "Hoffman", "Kassing", "Knutson", "Lawless", "Lawicki", "Mccord", "McCormack", "Miller", "Myers", "Nugent", "Ortiz", "Orwig", "Ory", "Paiser", "Pak", "Pettigrew", "Quinn", "Quizoz", "Ramachandran", "Resnick", "Sagar", "Schickowski", "Schiebel", "Sellon", "Severson", "Shaffer", "Solberg", "Soloman", "Sonderling", "Soukup", "Soulis", "Stahl", "Sweeney", "Tandy", "Trebil", "Trusela", "Trussel", "Turco", "Uddin", "Uflan", "Ulrich", "Upson", "Vader", "Vail", "Valente", "Van Zandt", "Vanderpoel", "Ventotla", "Vogal", "Wagle", "Wagner", "Wakefield", "Weinstein", "Weiss", "Woo", "Yang", "Yates", "Yocum", "Zeaser", "Zeller", "Ziegler", "Bauer", "Baxster", "Casal", "Cataldi", "Caswell", "Celedon", "Chambers", "Chapman", "Christensen", "Darnell", "Davidson", "Davis", "DeLorenzo", "Dinkins", "Doran", "Dugelman", "Dugan", "Duffman", "Eastman", "Ferro", "Ferry", "Fletcher", "Fietzer", "Hylan", "Hydinger", "Illingsworth", "Ingram", "Irwin", "Jagtap", "Jenson", "Johnson", "Johnsen", "Jones", "Jurgenson", "Kalleg", "Kaskel", "Keller", "Leisinger", "LePage", "Lewis", "Linde", "Lulloff", "Maki", "Martin", "McGinnis", "Mills", "Moody", "Moore", "Napier", "Nelson", "Norquist", "Nuttle", "Olson", "Ostrander", "Reamer", "Reardon", "Reyes", "Rice", "Ripka", "Roberts", "Rogers", "Root", "Sandstrom", "Sawyer", "Schlicht", "Schmitt", "Schwager", "Schutz", "Schuster", "Tapia", "Thompson", "Tiernan", "Tisler" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);
        nationalID = findViewById(R.id.regestration_national_id);
        nationalIDOfRelative = findViewById(R.id.regestration_national_id_of_relative);
        mobilePhone = findViewById(R.id.regestration_phone);
        password = findViewById(R.id.password);
        repatePassword= findViewById(R.id.repeatPassword);
        checkBoxPolicies = findViewById(R.id.checkBoxPolicies);

        db = FirebaseFirestore.getInstance();

        registerationButton=findViewById(R.id.registrationButton2);
        registerationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("here");
                final String nationalIDValue = nationalID.getText().toString().trim();
                final String nationalIDOfRelativeValue = nationalIDOfRelative.getText().toString().trim();
                final String mobilePhoneValue = mobilePhone.getText().toString().trim();
                final String passwordValue = password.getText().toString().trim();
                final String repatePasswordValue = repatePassword.getText().toString().trim();
                final boolean checkBoxPoliciesValue = checkBoxPolicies.isChecked();

                boolean isValid = true;

                if(TextUtils.isEmpty(nationalID.getText()) || nationalID.getText().toString().length() != 10){
                    isValid = false;
                    nationalID.setError("الحقل مطلوب , عدد خاناته ١٠");
                }
                if(TextUtils.isEmpty(nationalIDOfRelative.getText())|| nationalIDOfRelative.getText().toString().length() != 10){
                    isValid = false;
                    nationalIDOfRelative.setError("الحقل مطلوب , عدد خاناته ١٠");
                }
                if(TextUtils.isEmpty(mobilePhone.getText())){
                    isValid = false;
                    mobilePhone.setError("الحقل مطلوب");
                }
                if(TextUtils.isEmpty(password.getText()) || password.getText().toString().length()< 6){
                    isValid = false;
                    password.setError("الحقل مطلوب ، عدد الخانات يجب ان يكون اكثر من ٦");
                }
                if(TextUtils.isEmpty(repatePassword.getText())){
                    isValid = false;
                    repatePassword.setError("الحقل مطلوب");
                }

                if(!password.getText().toString().equals(repatePassword.getText().toString())){
                    isValid = false;
                    repatePassword.setError("كلمتا السر لا تتطابقان");
                }

                if(TextUtils.isEmpty(password.getText())){
                    isValid = false;
                    password.setError("الحقل مطلوب");
                }
                if(!checkBoxPolicies.isChecked()){
                    isValid = false;
                    checkBoxPolicies.setError("الحقل مطلوب");
                }

                if(isValid){
                    DocumentReference docRef = db.collection("users").document(nationalIDValue);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            System.out.println("IN REGISTRATION");
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (! document.exists()) {
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                    String formattedDate = df.format(c);

                                    // Create a reference to the cities collection
                                    CollectionReference usersRef = db.collection("users");

                                    // Create a query against the collection.
                                    Query mobilePhoneQuery = usersRef.whereEqualTo("mobile", mobilePhoneValue);

                                    mobilePhoneIsUsed = false;
                                    mobilePhoneQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                   mobilePhoneIsUsed=true;
                                                }
                                            } else {

                                            }
                                        }
                                    });

                                    if(! mobilePhoneIsUsed){
                                        final Map<String, Object> user = new HashMap<>();
                                        user.put("name",_firstName[(int) Math.random()*1000% _firstName.length]+" "+_lastName[(int) Math.random()*1000% _lastName.length]);
                                        user.put("nationalID", nationalIDValue);
                                        user.put("nationalIDOfRelative", nationalIDOfRelativeValue);
                                        user.put("mobile", mobilePhoneValue);
                                        user.put("password", passwordValue);
                                        user.put("age", "");
                                        user.put("gender", "");
                                        user.put("joinedAt",formattedDate );

                                        db.collection("users").document(nationalIDValue)
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        new UserSession(getApplicationContext()).createUserLoginSession(user.get("name").toString(),user.get("nationalID").toString(),user.get("mobile").toString(),user.get("password").toString());
                                                        Toast.makeText(Registeration.this," تم انشاء الحساب ",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Registeration.this,Home.class));
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Registeration.this,"حدث خطأ بالشبكة",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        System.out.println(" رقم الهاتف مستخدم من قبل ");
                                        Toast.makeText(Registeration.this," رقم الهاتف مستخدم من قبل ",Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    System.out.println(" الرقم الوطني مسجل من قبل ");
                                    Toast.makeText(Registeration.this," الرقم الوطني مسجل من قبل ",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });


                }

            }
        });

        policiesText =findViewById(R.id.policiesText);
        String text="أوافق على السياسات والشروط الخاصة بالتطبيق" ;
        final SpannableString text2= new SpannableString(text);
        ClickableSpan conditions = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Registeration.this, TermsAndConditions.class));

            }


            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#CF2424"));
            }
        };
        text2.setSpan(conditions,10,27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        policiesText.setText(text2);
        policiesText.setHighlightColor(Color.TRANSPARENT);
        policiesText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}