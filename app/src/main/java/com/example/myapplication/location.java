package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class location extends AppCompatActivity {
    private static final int MAX_STEP=3;
    Spinner state ;
    private int CURRENT_STEP=2;
    private String problemDescription;
    private String problemImage;
    private String locationPoints="";
    private EditText locationDescriptionEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Bundle extras = getIntent().getExtras();
        problemDescription=extras.getString("problemDescription");
        problemImage=extras.getString("problemImage");
        locationDescriptionEditText = findViewById(R.id.editTextTextMultiLine);
        bottomProgressDots(CURRENT_STEP);
        state=findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(location.this,
                R.layout.spinner_custom ,getResources().getStringArray(R.array.states));
        myAdapter.setDropDownViewResource(R.layout.spinner_custom);
        state.setAdapter(myAdapter);

                ((LinearLayout) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backStep(CURRENT_STEP);
                bottomProgressDots(CURRENT_STEP);
                onBackPressed();
               //startActivity(new Intent(location.this, camera.class));

            }

        });
        ((LinearLayout) findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationDescriptionEditText.getText().toString().trim().length()>0 || locationPoints.length()>2) {
                    Intent toSend =new Intent(location.this, send.class);
                    Bundle extras = new Bundle();
                    extras.putString("problemDescription",problemDescription);
                    extras.putString("problemImage",problemImage);
                    extras.putString("locationDescription",locationDescriptionEditText.getText().toString().trim());
                    extras.putString("locationPoints",locationPoints);
                    extras.putString("city",state.getSelectedItem().toString());
                    toSend.putExtras(extras);

                    startActivity(toSend);
//                    nextStep(CURRENT_STEP);
//                    bottomProgressDots(CURRENT_STEP);
                }else {
                    Toast.makeText(location.this,"ادخل العنوان",Toast.LENGTH_LONG).show();
                }

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
        ImageView[] dots= new  ImageView[MAX_STEP];
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

    public void goToMap(View view){
        startActivityForResult(new Intent(location.this,MapsActivity.class).putExtra("city",state.getSelectedItem().toString()),991);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 991) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                locationPoints=result;
                TextView locationSelectedText = findViewById(R.id.location_text);
                locationSelectedText.setText("تم اختيار موقع على الخريطة !");
                Toast.makeText(this,"تم اختيار الموقع",Toast.LENGTH_LONG).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}