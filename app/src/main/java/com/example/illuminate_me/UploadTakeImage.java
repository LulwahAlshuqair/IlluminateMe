package com.example.illuminate_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class UploadTakeImage extends AppCompatActivity {
    private float x1,x2,y1,y2;
    private ImageView image;
    private Uri selectedImage ;
    private Uri photoUri ;
    private Bitmap takenPicture ;
    private  String pathToFile ;


    // we should replace the selected and taken with only one attribute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make it cover the entire screen,
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // getSupportActionBar().hide();

        setContentView(R.layout.activity_upload_take_image);
        image= findViewById(R.id.imageToUpload);



        // Permissions :

        if (Build.VERSION.SDK_INT >= 23 )
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2) ;


        // repeatDescription =(Button) findViewById(R.id.repeatDescription);
        // TO PEN THE GALLARY
        if(MainActivity.chosenButton==MainActivity.UPLOAD_PIC)
            uploadPicture();
        else
            takePicture();
    }

    //to allow user select image from the gallary
    public void uploadPicture() {

        Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryIntent,MainActivity.chosenButton);

    }

    public void takePicture() {

        // intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null ;

                photoFile = createImageFile() ;


            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath() ;

                photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri) ;
                startActivityForResult(cameraIntent,MainActivity.chosenButton);

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //to upload picture

        if( requestCode==MainActivity.UPLOAD_PIC&&resultCode==RESULT_OK &&data!=null){
            selectedImage = data.getData();
            image.setImageURI(selectedImage);
        }

        // allow user to take a picture
        if( requestCode==MainActivity.TAKE_PIC && resultCode==RESULT_OK){
            takenPicture = BitmapFactory.decodeFile(pathToFile) ;
            image.setImageBitmap(takenPicture);
            Toast.makeText(this, pathToFile, Toast.LENGTH_SHORT).show();


        }

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                y1=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=event.getX();
                y2=event.getY();
                if(x1<x2){
                    // swipe right
                    // go to previous screen
                    Intent intent = new Intent (UploadTakeImage.this, MainActivity.class);
                    startActivity(intent);
                }


                if (x1>x2) {
                    // swipe left
                    // share menu

                    Intent intent = new Intent (UploadTakeImage.this, Share.class);
                    startActivity(intent);



                }
                break;

        }//end switch
        //if swipe left to right

        return super.onTouchEvent(event);
    }

    public Uri getSelectedImage() {
        return selectedImage ;
    }

    public Bitmap getTakenPicture() {
        return takenPicture;
    }

    public Drawable getImageView() {
        return image.getDrawable() ;

    }

    private File createImageFile() {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // getExternalFilesDir(Environment.DIRECTORY_PICTURES); < this will make the img available in our app only
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) ;

        File Fimage = null ;

        try {
             Fimage = File.createTempFile(imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */);

        } catch (Exception e) {
            Log.d("mylog", "ExceptionF : " + e.toString() ) ;
        }
        return Fimage;
    }

}
