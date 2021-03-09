package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class camera extends AppCompatActivity {
    private ImageButton pictureAndVideo;
    private ImageView imageView;
    private static final int pic_id = 123;
    private static final int MAX_STEP = 3;
    private int CURRENT_STEP = 1;
    private TextView status;
    private Uri filePath;
    private EditText description;
    private boolean haveImage = false;
    Bitmap bitmap;
    private static final int PICK_FROM_GALLERY = 1;

    private boolean isVideo = false;

    private String imagePath="";
    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        bottomProgressDots(CURRENT_STEP);
        imageView = findViewById(R.id.imageView);
        pictureAndVideo = findViewById(R.id.imageButton);
        description = findViewById(R.id.editTextTextMultiLine);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ((LinearLayout) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(camera.this, Home.class));
                backStep(CURRENT_STEP);
                bottomProgressDots(CURRENT_STEP);
            }

        });
        ((LinearLayout) findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (description.getText().toString().trim().length() > 0 || !imagePath.equals("")) {
                    // TODO : upload image on click next
                    Intent toLocationIntent =new Intent(camera.this, location.class);
                    toLocationIntent.putExtra("problemDescription", description.getText().toString().trim());
                    toLocationIntent.putExtra("problemImage", imagePath.trim());
                    startActivity(toLocationIntent);

                    //nextStep(CURRENT_STEP);
                    //bottomProgressDots(CURRENT_STEP);
                } else {
                    Toast.makeText(getApplicationContext(), "الرجاء رفع صورة للإبلاغ/الشكوى  أو شرحها بالنص ", Toast.LENGTH_LONG).show();
                }

            }
        });
        pictureAndVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayChoiceDialog();
//                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(camera_intent, pic_id);
                //  startActivityForResult(camera_intent, pic_id);
            }
        });
    }

    private void backStep(int progress) {
        if (progress > 1) {
            progress--;
            CURRENT_STEP = progress;
        }
    }

    private void nextStep(int progress) {
        if (progress < MAX_STEP) {
            progress++;
            CURRENT_STEP = progress;
        }
    }

    private void bottomProgressDots(int current_index) {
        current_index--;
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 20;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shap_cricle);
            dots[i].setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);

        }
        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shap_cricle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }
//    protected void onActivityResult(int requestCode,
//                                    int resultCode,
//                                    Intent data) {
//
//        // Match the request 'pic id with requestCode
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == pic_id) {
//            haveImage=true;
//            // BitMap is data structure of image file
//            // which stor the image in memory
//            filePath =(Uri) data.getData();
//            Bitmap photo = (Bitmap) data.getExtras()
//                    .get("data");
//            // Set the image in imageview for display
//            imageView.setImageBitmap(photo);
//        }
//
//    }

    private void uploadImage(Uri filePath) {

        if(!isVideo)
            imagePath="images/"+ UUID.randomUUID().toString();
        else
            imagePath="videos/"+ UUID.randomUUID().toString();
        System.out.println("Start uploading 1");
        if (filePath != null) {
            System.out.println("Start uploading 2");
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child( imagePath);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(camera.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(camera.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        } else
            Toast.makeText(camera.this, "on image", Toast.LENGTH_LONG).show();
    }


    private static final int IMAGE_PICK_REQUEST = 12345;


    private void displayChoiceDialog() {
        String choiceString[] = new String[]{"Gallery Image" ,"Gallery Video", "Camera"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.shareklogo);
        dialog.setTitle("Select image from");
        dialog.setItems(choiceString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isVideo=false;
                        Intent intent = new Intent();
                        if (which == 0) {
                            if (ActivityCompat.checkSelfPermission(camera.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(camera.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_PICK_REQUEST);
                            }
                            //Use  MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);



                        } else  if (which == 1) {
                            if (ActivityCompat.checkSelfPermission(camera.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(camera.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_PICK_REQUEST);
                            }
                            isVideo=true;
                            intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);


                        } else {
                            if (ActivityCompat.checkSelfPermission(camera.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(camera.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_PICK_REQUEST);
                            }
                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        }


                        startActivityForResult(
                                Intent.createChooser(intent, "Select profile picture"), IMAGE_PICK_REQUEST);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST)
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                System.out.println("selectedImageUri = " + selectedImageUri);
                if (selectedImageUri != null)  {
                    uploadImage(selectedImageUri);

                    try {
                        System.out.println("imagePath = " + imagePath);
                        if(imagePath.contains("images")){
                            Bitmap bitmap = getThumbnail(selectedImageUri);
                            System.out.println("HERE WE ARE !");
                            System.out.println("photo = " + bitmap);
                            VideoView videoView = findViewById(R.id.videoView);
                            videoView.setVisibility(View.GONE);
                            imageView.setImageBitmap(bitmap);
                        }else {
                            System.out.println("Select A Video");
                            imageView.setVisibility(View.GONE);
                            VideoView videoView = findViewById(R.id.videoView);
                            videoView.setVisibility(View.VISIBLE);
                            videoView.setVideoPath("https://firebasestorage.googleapis.com/v0/b/sharek-80e70.appspot.com/o/"+imagePath.replace("/","%2F")+"?alt=media&token=4f4fcf03-e48c-4247-975e-96f6e8befcfe");
                            System.out.println("https://firebasestorage.googleapis.com/v0/b/sharek-80e70.appspot.com/o/"+imagePath.replace("/","%2F")+"?alt=media&token=4f4fcf03-e48c-4247-975e-96f6e8befcfe");
                            videoView.setZOrderOnTop(true);//this line solve the problem
                            videoView.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {    // image captured from camera

                    bitmap = (Bitmap) data.getExtras().get("data");
                    Uri imageUri = getImageUri(this, bitmap);
                    VideoView videoView = findViewById(R.id.videoView);
                    videoView.setVisibility(View.GONE);
                    uploadImage(imageUri);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmap);

                }
            } else {
                Log.d("==>", "Operation canceled!");
            }
    }

    /**
     * get actual path from uri
     *
     * @param context    context
     * @param contentUri uri
     * @return actual path
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 64) ? (originalSize / 64) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
}